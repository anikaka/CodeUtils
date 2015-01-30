package com.TY.bhgis.Controls;

import java.util.Vector;

import com.TY.bhgis.Database.DataProvider;
import com.TY.bhgis.Database.IBH;
import com.TY.bhgis.Database.IBHClass;
import com.TY.bhgis.Display.IDisplayTransformation;
import com.TY.bhgis.Geometry.IEnvelope;
import com.TY.bhgis.Geometry.IGeometry;
import com.TY.bhgis.Geometry.ILinearRing;
import com.TY.bhgis.Geometry.IPoint;
import com.TY.bhgis.Geometry.LinearRing;
import com.TY.bhgis.Geometry.Point;
import com.TY.bhgis.Geometry.Polygon;
import com.TY.bhgis.MapFeature.HighWayLining;
import com.TY.bhgis.MapFeature.LiningPosition;
import com.TY.bhgis.Util.MathUtil;
import com.TY.bhgis.Util.Type;
import com.TY.bhgis.Util.mapUtil;
import com.TY.TYBHMapForAndroid.BHDisplay;

import com.tygeo.highwaytunnel.R;
import com.tygeo.highwaytunnel.common.InfoApplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class DrawPolygonTool implements IMapTool {

	private MapControl mapControl;
	private Vector<IPoint> points;

	private boolean flag = false;
	// 病害类型
	private int type;
	private int checkType;
	private IBHClass bhcClass;
	private  int checkItemId=1;
	
	
	public DrawPolygonTool(MapControl mapControl, int type,int checkType) {
		this.mapControl = mapControl;
		this.type = type;
		this.checkType=checkType;
		points = new Vector<IPoint>();
		if (checkType==1) {
			checkItemId=9;
		}
		bhcClass = ((BHDisplay) mapControl.getCustomDraw()).bhmap
				.getBHClass(this.type);
	}

	@Override
	public void draw(Canvas canvas) {

		if (points.size() > 1) {
			IDisplayTransformation displayTransformation = mapControl
					.getDisplay().getDisplayTransformation();

			IPoint pt = points.get(0);
			IPoint pt2 = new Point();
			displayTransformation.fromMapPoint(pt, pt2);
			Path path = new Path();
			path.moveTo(pt2.getX(), pt2.getY());
			Paint paint = new Paint();
			paint.setColor(Color.GREEN);
			paint.setStyle(Style.STROKE);
			for (int i = 1; i < points.size(); i++) {
				pt = points.get(i);
				displayTransformation.fromMapPoint(pt, pt2);
				path.lineTo(pt2.getX(), pt2.getY());
				canvas.drawPath(path, paint);
			}
		}

	}

	@Override
	public void pointerDragged(int x, int y) {
		if (flag) {
			
			IPoint point = new Point();
			mapControl.getDisplay().getDisplayTransformation()
					.toMapPoint(x, y, point);
			points.add(point);

			this.mapControl.repaint();
			
		}
	}

	@Override
	public void pointerPressed(int x, int y) {
		// TODO Auto-generated method stub
		if (!flag) {

			flag = true;

			IPoint point = new Point();
			mapControl.getDisplay().getDisplayTransformation()
					.toMapPoint(x, y, point);
			points.add(point);
			this.mapControl.noCustomDraw = true;
			this.mapControl.noSelectDraw = true;
		}
	}

	@Override
	public void pointerReleased(int x, int y) {
		// TODO Auto-generated method stub

		if (flag && points.size() > 2) {

			String title = "";
			if (type == Type.JGQC) {
				title = "起层";
			}
			if (type == Type.JGBL) {
				title = "剥落";

			}
			if (type == Type.MZGL) {
				title = "面状渗漏";

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
									IPoint[] mPoints = new Point[points.size() + 1];
									for (int i = 0; i < points.size(); i++) {
										mPoints[i] = points.get(i);
									}
									mPoints[points.size()] = points.get(0);
									ILinearRing linearRing = new LinearRing(
											mPoints, 0);
									Polygon polygon = new Polygon(linearRing);
									polygon.recalcEnvelope();
									IEnvelope envelope = polygon.getEnvelope();
									IPoint pt = envelope.getCenterPoint();
									HighWayLining[] highWayLinings = mapControl
											.getMap().getMapLayer()
											.getLinings();

									HighWayLining highWayLining = mapUtil
											.getHighWayLining(highWayLinings,
													pt, mapControl.getMap());
									LiningPosition liningPosition = mapUtil
											.getLiningPosition(highWayLining,
													pt, mapControl.getMap());
									String positionid = mapUtil
											.getPositionID(liningPosition
													.getPosition(),checkType);
									int checkid = DataProvider
											.getCheckIDFromBHType(bhcClass
													.getBHType());
									int position_id = Integer.valueOf(mapUtil
											.getPositionID(liningPosition
													.getPosition(),checkType));
									if (DataProvider
											.ValidationCheckContentForCILIV(
													DataProvider.PROJECTID,
													DataProvider.DIRECTION,
													checkid, position_id)) {
										mapUtil.DialogErro(
												(Context) InfoApplication
														.getInstance()
														.getUserdata(), mapUtil
														.getTJAddErro(checkid,
																position_id));

									} else {

										IBH bh = null;
										RadioGroup rg = (RadioGroup) textEntryView
												.findViewById(R.id.radiogroup_isPhoto);
										switch (rg.getCheckedRadioButtonId()) {
										case R.id.radioN:
											bh = bhcClass.createBH(false);
											break;
										default:
											bh = bhcClass.createBH(true);
											break;
										}
										// 备注
										EditText QS = (EditText) textEntryView
												.findViewById(R.id.editTextBZ);
										String input = QS.getText().toString();
										if (bhcClass.findField("BZ") != -1)
											bh.setValue(
													bhcClass.findField("BZ"),
													input);
										if (bhcClass.findField("judge_level") != -1) {
											RadioGroup radioGroup = (RadioGroup) textEntryView
													.findViewById(R.id.radiogroup_level);
											String level = "S";
											switch (radioGroup
													.getCheckedRadioButtonId()) {
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
										
										bh.setShape((IGeometry) polygon);
										if (bhcClass.findField("AREA") != -1)
											bh.setValue(bhcClass
													.findField("AREA"), String
													.valueOf(MathUtil.round(
															polygon.getArea(),
															3)));
										
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
										
										points.clear();
										mapControl.noCustomDraw = false;
										mapControl.noSelectDraw = false;
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
									points.clear();
									mapControl.noCustomDraw = false;
									mapControl.noSelectDraw = false;
									mapControl.repaint();
									mapControl.setPanTool();
								}
							}).create();

			try {
				dlg.show();
				flag = false;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
