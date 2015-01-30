package com.TY.bhgis.Util;

import java.math.BigDecimal;

import android.R.bool;

import com.TY.bhgis.Geometry.IPoint;
import com.TY.bhgis.Geometry.Point;

public class MathUtil {
	public static double getDistance(float x1, float y1, float x2, float y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	// Àƒ…·ŒÂ»Î
	public static double round(float v, int scale) {
		BigDecimal value = new BigDecimal(v);
		float actualTax = value.setScale(scale, BigDecimal.ROUND_HALF_UP)
				.floatValue();
		return actualTax;
	}

	

	public static float getYFromCircle(IPoint pt1, IPoint pt2, IPoint pt3,
			float x, boolean isbig) {
		float x1 = pt1.getX();
		float y1 = pt1.getY();
		float x2 = pt2.getX();
		float y2 = pt2.getY();
		float x3 = pt3.getX();
		float y3 = pt3.getY();
		float x0 = ((y3 - y1) * (y2 * y2 - y1 * y1 + x2 * x2 - x1 * x1) + (y2 - y1)
				* (y1 * y1 - y3 * y3 + x1 * x1 - x3 * x3))
				/ (2 * (x2 - x1) * (y3 - y1) - 2 * (x3 - x1) * (y2 - y1));
		float y0 = ((x3 - x1) * (x2 * x2 - x1 * x1 + y2 * y2 - y1 * y1) + (x2 - x1)
				* (x1 * x1 - x3 * x3 + y1 * y1 - y3 * y3))
				/ (2 * (y2 - y1) * (x3 - x1) - 2 * (y3 - y1) * (x2 - x1));
		
		float  radius=(float) Math.sqrt((x1-x0)*(x1-x0)+(y1-y0)*(y1-y0));
		if (isbig) {
			return (float) (Math.sqrt(radius * radius - (x - x0)
					* (x - x0)) + y0);
		} else {
			return (float) (y0 - Math.sqrt(radius * radius
					- (x - x0) * (x - x0)));
		}
		
	}

	public static double tolerance = 0.01;
}
