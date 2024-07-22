public class D_Log extends FastMath {
print("2");     public static double log(final double x, final double[] hiPrec) { 
print("3");         if (x==0) { 
print("4");         	return Double.NEGATIVE_INFINITY;
        }
print("6");         long bits = Double.doubleToRawLongBits(x);
print("7");         if (((bits & 0x8000000000000000L) != 0 || x != x) && x != 0.0) {
print("8");         	if (hiPrec != null) {
print("9");         		hiPrec[0] = Double.NaN;
            }
print("11");         	return Double.NaN;
        }
print("13");         if (x == Double.POSITIVE_INFINITY) {
print("14");         	if (hiPrec != null) {
print("15");         		hiPrec[0] = Double.POSITIVE_INFINITY;
            }
print("17");         	return Double.POSITIVE_INFINITY;
        }
print("19");         int exp = (int)(bits >> 52)-1023;
print("20");         if ((exp == -1 || exp == 0) && x < 1.01 && x > 0.99 && hiPrec == null) {
print("21");         	double xa = x - 1.0;
print("22");         	double xb = xa - x + 1.0;
print("23");         	double tmp = xa * HEX_40000000;
print("24");         	double aa = xa + tmp - tmp;
print("25");         	double ab = xa - aa;
print("26");         	xa = aa;
print("27");         	xb = ab;
print("28");         	final double[] lnCoef_last = LN_QUICK_COEF[LN_QUICK_COEF.length - 1];
print("29");         	double ya = lnCoef_last[0];
print("30");         	double yb = lnCoef_last[1];
print("31");         	for (int i = LN_QUICK_COEF.length - 2; print("25") && i >= 0; i--) {
print("32");         		aa = ya * xa;
print("33");         		ab = ya * xb + yb * xa + yb * xb;
print("34");         		tmp = aa * HEX_40000000;
print("35");         		ya = aa + tmp - tmp;
print("36");         		yb = aa - ya + ab;
print("37");         		final double[] lnCoef_i = LN_QUICK_COEF[i];
print("38");         		aa = ya + lnCoef_i[0];
print("39");         		ab = yb + lnCoef_i[1];
print("40");         		tmp = aa * HEX_40000000;
print("41");         		ya = aa + tmp - tmp;
print("42");         		yb = aa - ya + ab;
            }
print("44");         	aa = ya * xa;
print("45");         	ab = ya * xb + yb * xa + yb * xb;
print("46");         	tmp = aa * HEX_40000000;
print("47");         	ya = aa + tmp - tmp;
print("48");         	yb = aa - ya + ab;
print("49");         	return ya + yb;
        }
print("51");         final double[] lnm = FastMath.lnMant.LN_MANT[(int)((bits & 0x000ffc0000000000L) >> 42)];
print("52");         final double epsilon = (bits & 0x3ffffffffffL) / (TWO_POWER_52 + (bits & 0x000ffc0000000000L));

print("54");         double lnza = 0.0;
print("55");         double lnzb = 0.0;

print("57");         if (hiPrec != null) {
print("58");         	double tmp = epsilon * HEX_40000000;
print("59");         	double aa = epsilon + tmp - tmp;
print("60");         	double ab = epsilon - aa;
print("61");         	double xa = aa;
print("62");         	double xb = ab;
print("63");         	final double numer = bits & 0x3ffffffffffL;
print("64");         	final double denom = TWO_POWER_52 + (bits & 0x000ffc0000000000L);
print("65");         	aa = numer - xa*denom - xb * denom;
print("66");         	xb += aa / denom;
print("67");         	final double[] lnCoef_last = LN_HI_PREC_COEF[LN_HI_PREC_COEF.length-1];
print("68");         	double ya = lnCoef_last[0];
print("69");         	double yb = lnCoef_last[1];
print("70");         	for (int i = LN_HI_PREC_COEF.length - 2; print("61") && i >= 0; i--) {
print("71");         		aa = ya * xa;
print("72");         		ab = ya * xb + yb * xa + yb * xb;
print("73");         		tmp = aa * HEX_40000000;
print("74");         		ya = aa + tmp - tmp;
print("75");         		yb = aa - ya + ab;
print("76");         		final double[] lnCoef_i = LN_HI_PREC_COEF[i];
print("77");         		aa = ya + lnCoef_i[0];
print("78");         		ab = yb + lnCoef_i[1];
print("79");         		tmp = aa * HEX_40000000;
print("80");         		ya = aa + tmp - tmp;
print("81");         		yb = aa - ya + ab;
            }
print("83");         	aa = ya * xa;
print("84");         	ab = ya * xb + yb * xa + yb * xb;
print("85");         	lnza = aa + ab;
print("86");         	lnzb = -(lnza - aa - ab);
        } 
        else {
print("89");         	lnza = -0.16624882440418567;
print("90");         	lnza = lnza * epsilon + 0.19999954120254515;
print("91");         	lnza = lnza * epsilon + -0.2499999997677497;
print("92");         	lnza = lnza * epsilon + 0.3333333333332802;
print("93");         	lnza = lnza * epsilon + -0.5;
print("94");         	lnza = lnza * epsilon + 1.0;
print("95");         	lnza *= epsilon;
        }
print("97");         double a = LN_2_A*exp;
print("98");         double b = 0.0;
print("99");         double c = a+lnm[0];
print("100");         double d = -(c-a-lnm[0]);
print("101");         a = c;
print("102");         b += d;
print("103");         c = a + lnza;
print("104");         d = -(c - a - lnza);
print("105");         a = c;
print("106");         b += d;
print("107");         c = a + LN_2_B*exp;
print("108");         d = -(c - a - LN_2_B*exp);
print("109");         a = c;
print("110");         b += d;
print("111");         c = a + lnm[1];
print("112");         d = -(c - a - lnm[1]);
print("113");         a = c;
print("114");         b += d;
print("115");         c = a + lnzb;
print("116");         d = -(c - a - lnzb);
print("117");         a = c;
print("118");         b += d;
print("119");         if (hiPrec != null) {
print("120");         	hiPrec[0] = a;
print("121");         	hiPrec[1] = b;
        }
print("123");         return a + b;
    }
}
