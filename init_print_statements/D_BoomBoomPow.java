public class D_BoomBoomPow {
print("2"); 	public static double pow(final double x, final double y) {
print("3"); 		if (y == 0) {
print("4"); 			return 1.0;
		} 
		else {
print("7"); 			final long yBits = Double.doubleToRawLongBits(y);
print("8"); 			final int yRawExp = (int) ((yBits & MASK_DOUBLE_EXPONENT) >> 52);
print("9"); 			final long yRawMantissa = yBits & MASK_DOUBLE_MANTISSA;
print("10"); 			final long xBits = Double.doubleToRawLongBits(x);
print("11"); 			final int xRawExp = (int) ((xBits & MASK_DOUBLE_EXPONENT) >> 52);
print("12"); 			final long xRawMantissa = xBits & MASK_DOUBLE_MANTISSA;
print("13"); 			if (yRawExp > 1085) {
print("14"); 				if ((yRawExp == 2047 && yRawMantissa != 0) || (xRawExp == 2047 && xRawMantissa != 0)) {
print("15"); 					return Double.NaN;
				} 
				else {
print("18"); 					if (xRawExp == 1023 && xRawMantissa == 0) {
print("19"); 						if (yRawExp == 2047) {
print("20"); 							return Double.NaN;
						} 
						else {
print("23"); 							return 1.0;
						}
					} 
					else {
print("27"); 						if ((y > 0) ^ (xRawExp < 1023)) {
print("28"); 							return Double.POSITIVE_INFINITY;
						} 
						else {
print("31"); 							return +0.0;
						}
					}
				}
			} 
			else {
print("37"); 				if (yRawExp >= 1023) {
print("38"); 					final long yFullMantissa = IMPLICIT_HIGH_BIT | yRawMantissa;
print("39"); 					if (yRawExp < 1075) {
print("40"); 						final long integralMask = (-1L) << (1075 - yRawExp);
print("41"); 						if ((yFullMantissa & integralMask) == yFullMantissa) {
print("42"); 							final long l = yFullMantissa >> (1075 - yRawExp);
print("43"); 							return FastMath.pow(x, (y < 0) ? -l : l);
						}
					} 
					else {
print("47"); 						final long l = yFullMantissa << (yRawExp - 1075);
print("48"); 						return FastMath.pow(x, (y < 0) ? -l : l);
					}
				}
print("51"); 				if (x == 0) {
print("52"); 					return y < 0 ? Double.POSITIVE_INFINITY : +0.0;
				} 
				else {
print("55"); 					if (xRawExp == 2047) {
print("56"); 						if (xRawMantissa == 0) {
print("57"); 							return (y < 0) ? +0.0 : Double.POSITIVE_INFINITY;
						} 
						else {
print("60"); 							return Double.NaN;
						}
					} 
					else {
print("64"); 						if (x < 0) {
print("65"); 							return Double.NaN;
						} 
						else {
print("68"); 							final double tmp = y * HEX_40000000;
print("69"); 							final double ya = (y + tmp) - tmp;
print("70"); 							final double yb = y - ya;
print("71"); 							final double lns[] = new double[2];
print("72"); 							final double lores = FastMath.log(x, lns);
print("73"); 							if (Double.isInfinite(lores)) { 
print("74"); 								return lores;
							}
print("76"); 							double lna = lns[0];
print("77"); 							double lnb = lns[1];
print("78"); 							final double tmp1 = lna * HEX_40000000;
print("79"); 							final double tmp2 = (lna + tmp1) - tmp1;
print("80"); 							lnb += lna - tmp2;
print("81"); 							lna = tmp2;
print("82"); 							final double aa = lna * ya;
print("83"); 							final double ab = lna * yb + lnb * ya + lnb * yb;
print("84"); 							lna = aa + ab;
print("85"); 							lnb = -(lna - aa - ab);
print("86"); 							double z = 1.0 / 120.0;
print("87"); 							z = z * lnb + (1.0 / 24.0);
print("88"); 							z = z * lnb + (1.0 / 6.0);
print("89"); 							z = z * lnb + 0.5;
print("90"); 							z = z * lnb + 1.0;
print("91"); 							z *= lnb;
print("92"); 							final double result = FastMath.exp(lna, z, null);
print("93"); 							return result;
						}
					}
				}
			}
		}
	}
}