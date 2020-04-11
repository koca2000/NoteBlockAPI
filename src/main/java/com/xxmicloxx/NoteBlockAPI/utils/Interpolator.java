package com.xxmicloxx.NoteBlockAPI.utils;

import java.util.Arrays;

/**
 * <p>Static methods for doing useful math</p><hr>
 *
 * @author : $Author: brian $
 * @version : $Revision: 1.1 $
 *          <hr><p><a href="http://www.mbari.org">
 *          The Monterey Bay Aquarium Research Institute (MBARI)</a> provides this
 *          documentation and code &quot;as is&quot;, with no warranty, express or
 *          implied, of its quality or consistency. It is provided without support and
 *          without obligation on the part of MBARI to assist in its use, correction,
 *          modification, or enhancement. This information should not be published or
 *          distributed to third parties without specific written permission from
 *          MBARI.</p><br>
 *          Copyright 2002 MBARI.<br>
 *          MBARI Proprietary Information. All rights reserved.<br><hr><br>
 */
public class Interpolator {

	public static double[] interpLinear(double[] x, double[] y, double[] xi) throws IllegalArgumentException {
		if (x.length != y.length) {
			throw new IllegalArgumentException("X and Y must be the same length");
		}
		if (x.length == 1) {
			throw new IllegalArgumentException("X must contain more than one value");
		}

		double[] dx = new double[x.length - 1];
		double[] dy = new double[x.length - 1];
		double[] slope = new double[x.length - 1];
		double[] intercept = new double[x.length - 1];

		// Calculate the line equation (i.e. slope and intercept) between each point
		for (int i = 0; i < x.length - 1; i++) {
			dx[i] = x[i + 1] - x[i];
			if (dx[i] == 0) {
				throw new IllegalArgumentException("X must be montotonic. A duplicate " + "x-value was found");
			}
			if (dx[i] < 0) {
				throw new IllegalArgumentException("X must be sorted");
			}
			dy[i] = y[i + 1] - y[i];
			slope[i] = dy[i] / dx[i];
			intercept[i] = y[i] - x[i] * slope[i];
		}

		// Perform the interpolation here
		double[] yi = new double[xi.length];
		for (int i = 0; i < xi.length; i++) {
			if ((xi[i] > x[x.length - 1]) || (xi[i] < x[0])) {
				yi[i] = Double.NaN;
			} else {
				int loc = Arrays.binarySearch(x, xi[i]);
				if (loc < -1) {
					loc = -loc - 2;
					yi[i] = slope[loc] * xi[i] + intercept[loc];
				} else {
					yi[i] = y[loc];
				}
			}
		}

		return yi;
	}

	public static double[] interpLinear(long[] x, double[] y, long[] xi) throws IllegalArgumentException {
		double[] xd = new double[x.length];
		for (int i = 0; i < x.length; i++) {
			xd[i] = (double) x[i];
		}

		double[] xid = new double[xi.length];
		for (int i = 0; i < xi.length; i++) {
			xid[i] = (double) xi[i];
		}

		return interpLinear(xd, y, xid);
	}

	public static double interpLinear(double[] xy, double xx) {
		if (xy.length % 2 != 0) {
			throw new IllegalArgumentException("XY must be divisible by two.");
		}

		double[] x = new double[xy.length/2];
		double[] y = new double[x.length];

		for (int i = 0; i < xy.length; i++) {
			if (i % 2 == 0) {
				x[i/2] = xy[i];
			} else {
				y[i/2] = xy[i];
			}
		}
		return interpLinear(x, y, new double[] {xx})[0];
	}
	
}