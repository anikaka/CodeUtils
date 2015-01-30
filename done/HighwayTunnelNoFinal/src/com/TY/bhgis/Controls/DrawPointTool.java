package com.TY.bhgis.Controls;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.TY.TYBHMapForAndroid.BHDisplay;
import com.TY.bhgis.Database.DataProvider;
import com.TY.bhgis.Database.IBH;
import com.TY.bhgis.Database.IBHClass;
import com.TY.bhgis.Geometry.IGeometry;
import com.TY.bhgis.Geometry.IPoint;
import com.TY.bhgis.Geometry.Point;
import com.TY.bhgis.MapFeature.HighWayLining;
import com.TY.bhgis.MapFeature.LiningPosition;
import com.TY.bhgis.Util.Type;
import com.TY.bhgis.Util.mapUtil;
import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.common.InfoApplication;

public class DrawPointTool implements IMapTool {
	private MapControl mapControl;
	// 病害类型
	private int type;
	private int checkType;
	private IBHClass bhcClass;
	private int checkItemId=1;
	public DrawPointTool(MapControl mapControl, int type, int checkType) {
		this.mapControl = mapControl;
		this.type = type;
		this.checkType=checkType;
		if (checkType==1) {
			checkItemId=9;
		}

		bhcClass = ((BHDisplay) mapControl.getCustomDraw()).bhmap
				.getBHClass(type);

	}

	@Override
	public void draw(Canvas arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pointerDragged(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pointerPressed(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pointerReleased(int arg0, int arg1) {
		// TODO Auto-generated method stub

		final IPoint point = new Point();
		mapControl.getDisplay().getDisplayTransformation()
				.toMapPoint(arg0, arg1, point);
		if (type == Type.JGCT || type == Type.SFDSL || type == Type.SFXSL
				|| type == Type.LFDSL || type == Type.LFXSL || type == Type.GB) {
			String title = "";
			if (type == Type.JGCT) {
				title = "错台";
			}
			if (type == Type.SFDSL) {
				title = "三缝点渗漏";
			}
			if (type == Type.SFXSL) {
				title = "三缝线渗漏";
			}
			if (type == Type.LFDSL) {
				title = "裂缝点渗漏";
			}
			if (type == Type.LFXSL) {
				title = "裂缝线渗漏";
			}
			if (type == Type.GB) {
				title = "挂冰";
			}
			LayoutInflater factory = LayoutInflater.from(InfoApplication
					.getInstance());
			final View textEntryView = factory.inflate(R.layout.dialogbh, null);
			((TextView) textEntryView.findViewById(R.id.tv_level))
					.setVisibility(View.VISIBLE);
			((RadioGroup) textEntryView.findViewById(R.id.radiogroup_level))
					.setVisibility(View.VISIBLE);
			((RadioButton) textEntryView.findViewById(R.id.radioS))
					.setText(DataProvider.getLevelContent("S",
							bhcClass.getBHType()));
			((RadioButton) textEntryView.findViewById(R.id.radioB))
					.setText(DataProvider.getLevelContent("B",
							bhcClass.getBHType()));
			((RadioButton) textEntryView.findViewById(R.id.radioA))
					.setText(DataProvider.getLevelContent("A",
							bhcClass.getBHType()));
			AlertDialog dlg = new AlertDialog.Builder((Context) InfoApplication
					.getInstance().getUserdata())
					.setTitle(title)
					.setView(textEntryView)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									HighWayLining[] highWayLinings = mapControl.getMap().getMapLayer().getLinings();
									HighWayLining highWayLining = mapUtil.getHighWayLining(highWayLinings,point, mapControl.getMap());
									LiningPosition liningPosition = mapUtil.getLiningPosition(highWayLining,point, mapControl.getMap());
									String positionid = mapUtil.getPositionID(liningPosition.getPosition(),checkType);
									int checkid = DataProvider.getCheckIDFromBHType(bhcClass.getBHType());
									int position_id = Integer.valueOf(mapUtil.getPositionID(liningPosition.getPosition(),checkType));
									if (DataProvider.ValidationCheckContentForCILIV(DataProvider.PROJECTID,DataProvider.DIRECTION,checkid, position_id)) {
										mapUtil.DialogErro((Context) InfoApplication.getInstance().getUserdata(), mapUtil.getTJAddErro(checkid,position_id));
									} else {
										IBH bh = null;
										RadioGroup rg = (RadioGroup) textEntryView.findViewById(R.id.radiogroup_isPhoto);
										switch (rg.getCheckedRadioButtonId()) {
										case R.id.radioN:
											bh = bhcClass.createBH(false);
											break;
										default:
											bh = bhcClass.createBH(true);
											break;
										}
										// 备注
										EditText QS = (EditText) textEntryView.findViewById(R.id.editTextBZ);
										String input = QS.getText().toString();
										if (bhcClass.findField("BZ") != -1)
											bh.setValue(bhcClass.findField("BZ"),input);
										if (bhcClass.findField("judge_level") != -1) {
											RadioGroup radioGroup = (RadioGroup) textEntryView.findViewById(R.id.radiogroup_level);
											String level = "S";
											switch (radioGroup.getCheckedRadioButtonId()) {
											case R.id.radioS:
												level = "S";
												break;
											case R.id.radioB:
												level = "B";
												break;
											case R.id.radioA:
												level = "A";
												break;
											}
											RadioButton rbtn = (RadioButton) textEntryView.findViewById(radioGroup
													.getCheckedRadioButtonId());
											String level_content = rbtn
													.getText().toString();

											bh.setValue(bhcClass
													.findField("judge_level"),
													level);
											if (bhcClass
													.findField("level_content") != -1)
												bh.setValue(
														bhcClass.findField("level_content"),
														level_content);
										}
										bh.setShape((IGeometry) point);

										int lczh = highWayLining.getLczh();
										mapUtil.InsertCurrentZh(lczh);
										if (bhcClass.findField("mileage") != -1)
											bh.setValue(bhcClass
													.findField("mileage"),
													String.valueOf(lczh));

										if (bhcClass
												.findField("check_position") != -1) {

											bh.setValue(
													bhcClass.findField("check_position"),
													DataProvider
															.getPositionName(
																	checkItemId,
																	Integer.valueOf(positionid)));
											if (bhcClass
													.findField("POSITIONID") != -1) {
												bh.setValue(
														bhcClass.findField("POSITIONID"),
														positionid);

											}
										}
										bh.setValue(bhcClass
												.findField("BHTYPE"), String.valueOf(DataProvider.getCheckIDFromBHType(bh.getBHType(), checkType)));
										bh.setValue(bhcClass
												.findField("CHECKID"), String.valueOf(DataProvider.getCheckIDFromBHType(bh.getBHType(), checkType)));
										bh.store();
										mapControl.repaint();
										mapControl.setPanTool();
									}
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									// TODO Auto-generated method stub

									mapControl.repaint();
									mapControl.setPanTool();
								}
							}).create();
			try {
				dlg.show();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			IBH bh = bhcClass.createBH(false);
			// bhDataProvider.PointStore((IGeometry) point, type);
			bh.setShape((IGeometry) point);
			bh.store();
			this.mapControl.repaint();
		}

	}
}
