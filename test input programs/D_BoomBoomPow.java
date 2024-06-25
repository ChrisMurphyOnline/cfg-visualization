public class D_BoomBoomPow {
	public static double pow(final double x, final double y) {
		if (y == 0) {
			return 1.0;
		} 
		else {
			final long yBits = Double.doubleToRawLongBits(y);
			final int yRawExp = (int) ((yBits & MASK_DOUBLE_EXPONENT) >> 52);
			final long yRawMantissa = yBits & MASK_DOUBLE_MANTISSA;
			final long xBits = Double.doubleToRawLongBits(x);
			final int xRawExp = (int) ((xBits & MASK_DOUBLE_EXPONENT) >> 52);
			final long xRawMantissa = xBits & MASK_DOUBLE_MANTISSA;
			if (yRawExp > 1085) {
				if ((yRawExp == 2047 && yRawMantissa != 0) || (xRawExp == 2047 && xRawMantissa != 0)) {
					return Double.NaN;
				} 
				else {
					if (xRawExp == 1023 && xRawMantissa == 0) {
						if (yRawExp == 2047) {
							return Double.NaN;
						} 
						else {
							return 1.0;
						}
					} 
					else {
						if ((y > 0) ^ (xRawExp < 1023)) {
							return Double.POSITIVE_INFINITY;
						} 
						else {
							return +0.0;
						}
					}
				}
			} 
			else {
				if (yRawExp >= 1023) {
					final long yFullMantissa = IMPLICIT_HIGH_BIT | yRawMantissa;
					if (yRawExp < 1075) {
						final long integralMask = (-1L) << (1075 - yRawExp);
						if ((yFullMantissa & integralMask) == yFullMantissa) {
							final long l = yFullMantissa >> (1075 - yRawExp);
							return FastMath.pow(x, (y < 0) ? -l : l);
						}
					} 
					else {
						final long l = yFullMantissa << (yRawExp - 1075);
						return FastMath.pow(x, (y < 0) ? -l : l);
					}
				}
				if (x == 0) {
					return y < 0 ? Double.POSITIVE_INFINITY : +0.0;
				} 
				else {
					if (xRawExp == 2047) {
						if (xRawMantissa == 0) {
							return (y < 0) ? +0.0 : Double.POSITIVE_INFINITY;
						} 
						else {
							return Double.NaN;
						}
					} 
					else {
						if (x < 0) {
							return Double.NaN;
						} 
						else {
							final double tmp = y * HEX_40000000;
							final double ya = (y + tmp) - tmp;
							final double yb = y - ya;
							final double lns[] = new double[2];
							final double lores = FastMath.log(x, lns);
							if (Double.isInfinite(lores)) { 
								return lores;
							}
							double lna = lns[0];
							double lnb = lns[1];
							final double tmp1 = lna * HEX_40000000;
							final double tmp2 = (lna + tmp1) - tmp1;
							lnb += lna - tmp2;
							lna = tmp2;
							final double aa = lna * ya;
							final double ab = lna * yb + lnb * ya + lnb * yb;
							lna = aa + ab;
							lnb = -(lna - aa - ab);
							double z = 1.0 / 120.0;
							z = z * lnb + (1.0 / 24.0);
							z = z * lnb + (1.0 / 6.0);
							z = z * lnb + 0.5;
							z = z * lnb + 1.0;
							z *= lnb;
							final double result = FastMath.exp(lna, z, null);
							return result;
						}
					}
				}
			}
		}
	}
}
