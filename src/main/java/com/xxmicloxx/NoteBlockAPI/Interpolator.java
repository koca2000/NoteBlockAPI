package com.xxmicloxx.NoteBlockAPI;

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
 * @deprecated {@link com.xxmicloxx.NoteBlockAPI.utils.Interpolator}
 */
@Deprecated
public class Interpolator {

	public static double[] interpLinear(double[] x, double[] y, double[] xi) throws IllegalArgumentException {
		return com.xxmicloxx.NoteBlockAPI.utils.Interpolator.interpLinear(x, y, xi);
	}

	public static double[] interpLinear(long[] x, double[] y, long[] xi) throws IllegalArgumentException {
		return com.xxmicloxx.NoteBlockAPI.utils.Interpolator.interpLinear(x, y, xi);
	}

	public static double interpLinear(double[] xy, double xx) {
		return com.xxmicloxx.NoteBlockAPI.utils.Interpolator.interpLinear(xy, xx);
	}

}