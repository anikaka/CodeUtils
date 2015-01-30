package com.TY.TYBHMapForAndroid;

import java.util.List;
import java.util.Vector;

import com.TY.bhgis.Carto.IMap;
import com.TY.bhgis.Carto.Map;
import com.TY.bhgis.Controls.ICustomDraw;
import com.TY.bhgis.Controls.MapControl;
import com.TY.bhgis.Database.BHClass;
import com.TY.bhgis.Database.DataProvider;
import com.TY.bhgis.Database.IBH;
import com.TY.bhgis.Database.IBHClass;
import com.TY.bhgis.Display.IFillSymbol;
import com.TY.bhgis.Display.ILineFillSymbol;
import com.TY.bhgis.Display.IMarkFillSymbol;
import com.TY.bhgis.Display.IMarkSymbol;
import com.TY.bhgis.Display.ISymbol;
import com.TY.bhgis.Display.LineFillSymbol;
import com.TY.bhgis.Display.LineSymbol.DashStyle;
import com.TY.bhgis.Display.MarkFillSymbol;
import com.TY.bhgis.Display.MarkSymbol.MarkStyle;
import com.TY.bhgis.Display.ISimpleMarkSymbol;
import com.TY.bhgis.Display.PictureMarkerSymbol;
import com.TY.bhgis.Display.SimpleLineSymbol;
import com.TY.bhgis.Display.SymbolPID;
import com.TY.bhgis.Display.ILineSymbol;
import com.TY.bhgis.Geometry.IGeometry;
import com.TY.bhgis.Util.Image;
import com.TY.bhgis.Util.Type;
import android.graphics.Canvas;
import android.graphics.Color;

public class BHDisplay implements ICustomDraw {

	// 地图控制
	private MapControl mapControl;
	public float x, y;
	public IMap bhmap;
	private List<java.util.Map<String, Object>> bhImageMaps = null;// 病害的示意图
	
	// 初始化的时候把地图控制权传过来
	public BHDisplay(MapControl mapControl) {
		this.mapControl = mapControl;
		this.bhmap = this.mapControl.getMap();
		// 初始化十种病害类
		IBHClass[] bhcClasses = new BHClass[10];
		for (int i = 1; i <= 10; i++) {
			String[] fields = DataProvider.getFields(i);
			IBHClass bhclass = new BHClass(fields, i);
			Vector bhs = DataProvider.BHSelect(i);
			bhclass.setBHs(bhs);
			bhcClasses[i - 1] = bhclass;
		}
		bhImageMaps = DataProvider.getBHImageMap();
		bhmap.setBhClasses(bhcClasses);
	}

	
	@Override
	public void draw(Canvas g) {

		int visvibleCount = this.mapControl.getMap().getMapLayer().getVisibleCount();
		int startLczh = this.mapControl.getMap().getMapLayer().getVisibleStartLczh();

		ISymbol symbol = null;
		for (int i = 0; i < this.bhmap.getBHClasses().length; i++) {
			IBHClass bhClass = (this.bhmap.getBHClasses())[i];

			int Lczhindex = bhClass.findField("mileage");
			int Pidindex = bhClass.findField("pic_id");

			int postionindex = bhClass.findField("check_position");

			Vector<IBH> bhs = bhClass.getBHs();
			for (int j = 0; j < bhs.size(); j++) {
				IBH bh = bhs.get(j);
				if (bh.getShape() == null) {
					// bhClass.delete(bh);
					// continue;
					if (postionindex != -1 && Lczhindex != -1) {
//						int lczh = Integer.parseInt(bh.getValue(Lczhindex));
//						String position = bh.getValue(postionindex);
//						HighWayLining highWayLining = this.mapControl.getMap()
//								.getMapLayer().getHighWayLining(lczh);
//						bh.setShape(mapUtil.getShape(highWayLining, position,
//								DataProvider.getGeoType(bh.getBHType())));
					}
					if (bh.getShape() == null) {
						bhClass.delete(bh);
						continue;
					}
				}

				if (Lczhindex != -1) {
					String value=bh.getValue(Lczhindex);
//					if(value.contains("K")){
//						value=value.replaceAll("K", "");
//					}
//					if(value.contains("+")){
//						value=value.replace("+", "");
//					}
//					if(value.contains("Z")){
//						value=value.replace("Z", "");
//					}
					int id = Integer.valueOf(bh.getValue(Lczhindex));
	//				int id = Integer.valueOf(value.trim()); //ChenLang modify
					if (Map.flag) {
						if (id > startLczh - 1
								&& id < startLczh + visvibleCount)
							bh.setVisible(true);

						else
							bh.setVisible(false);
					} else {
						if (id < startLczh + 1
								&& id > startLczh - visvibleCount)
							bh.setVisible(true);

						else
							bh.setVisible(false);
					}

				}
				if (bh.getVisible()) {
					IGeometry shape = bh.getShape();

					switch (bhClass.getBHType()) {
					case Type.JGLF: // 结构裂缝
						symbol = new SimpleLineSymbol();
						((ILineSymbol) symbol).setColor(Color.BLUE);
						((ILineSymbol) symbol).setWidth(1);
						break;
					case Type.JGCT:// 结构错台
						symbol = new PictureMarkerSymbol(getBHImage(Type.JGCT));
						break;
					case Type.JGQC:// 结构起层
						symbol = new MarkFillSymbol();
						((IFillSymbol) symbol).getOutline().setColor(Color.BLUE);
						((IMarkSymbol) ((IMarkFillSymbol) symbol).getMarkSymbol()).setSize(5);
						((ISimpleMarkSymbol) ((IMarkFillSymbol) symbol).getMarkSymbol()).setStyle(MarkStyle.SolidTriangle);
						break;
					case Type.JGBL:// 结构剥落
						symbol = new MarkFillSymbol();
						((IFillSymbol) symbol).getOutline()
								.setColor(Color.BLUE);
						((ISimpleMarkSymbol) ((IMarkFillSymbol) symbol)
								.getMarkSymbol())
								.setStyle(MarkStyle.SolidCircle);
						break;
					case Type.SFDSL:// 三缝点渗漏
						symbol = new PictureMarkerSymbol(getBHImage(Type.SFDSL));
						break;
					case Type.SFXSL:// 三缝线渗漏
						symbol = new PictureMarkerSymbol(getBHImage(Type.SFXSL));
						break;
					case Type.LFDSL:// 裂缝点渗漏
						symbol = new PictureMarkerSymbol(getBHImage(Type.LFDSL));
						break;
					case Type.LFXSL:// 裂缝线渗漏
						symbol = new PictureMarkerSymbol(getBHImage(Type.LFXSL));
						break;
					case Type.MZGL:// 面状渗漏
						symbol = new LineFillSymbol();
						((IFillSymbol) symbol).getOutline().setColor(Color.BLUE);
						((ILineSymbol)((ILineFillSymbol) symbol).getInline()).setColor(Color.BLUE);
						((ILineFillSymbol) symbol).setStyle(DashStyle.Dot);
						((ILineFillSymbol) symbol).setAngle(0);
						break;
					case Type.GB:// 挂冰
						symbol = new PictureMarkerSymbol(getBHImage(Type.GB));
						break;
					}

					if (symbol != null) {
						String pid = bh.getValue(Pidindex);
						SymbolPID symbolPID = new SymbolPID(pid);
						if (shape != null) {
							symbol.draw(g, mapControl.getDisplay().getDisplayTransformation(), shape);
							symbolPID.draw(g, mapControl.getDisplay()
									.getDisplayTransformation(), shape);
						}
					}
				}
			}
		}
	}
	
	private Image getBHImage(int bhtype) {
		if (this.bhImageMaps != null && this.bhImageMaps.size() > 0) {
			for (int i = 0; i < this.bhImageMaps.size(); i++) {
				java.util.Map<String, Object> map = this.bhImageMaps.get(i);
				if (bhtype == Integer.parseInt(map.get("bhtype").toString())) {
					return (Image) map.get("image");
				}
			}
		}
		return null;
	}
}
