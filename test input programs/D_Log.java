public class D_Log extends FastMath {
    public static double log(final double x, final double[] hiPrec) { 
        if (x==0) { 
        	return Double.NEGATIVE_INFINITY;
        }
        long bits = Double.doubleToRawLongBits(x);
        if (((bits & 0x8000000000000000L) != 0 || x != x) && x != 0.0) {
        	if (hiPrec != null) {
        		hiPrec[0] = Double.NaN;
            }
        	return Double.NaN;
        }
        if (x == Double.POSITIVE_INFINITY) {
        	if (hiPrec != null) {
        		hiPrec[0] = Double.POSITIVE_INFINITY;
            }
        	return Double.POSITIVE_INFINITY;
        }
        int exp = (int)(bits >> 52)-1023;
        if ((exp == -1 || exp == 0) && x < 1.01 && x > 0.99 && hiPrec == null) {
        	double xa = x - 1.0;
        	double xb = xa - x + 1.0;
        	double tmp = xa * HEX_40000000;
        	double aa = xa + tmp - tmp;
        	double ab = xa - aa;
        	xa = aa;
        	xb = ab;
        	final double[] lnCoef_last = LN_QUICK_COEF[LN_QUICK_COEF.length - 1];
        	double ya = lnCoef_last[0];
        	double yb = lnCoef_last[1];
        	for (int i = LN_QUICK_COEF.length - 2; print("25") && i >= 0; i--) {
        		aa = ya * xa;
        		ab = ya * xb + yb * xa + yb * xb;
        		tmp = aa * HEX_40000000;
        		ya = aa + tmp - tmp;
        		yb = aa - ya + ab;
        		final double[] lnCoef_i = LN_QUICK_COEF[i];
        		aa = ya + lnCoef_i[0];
        		ab = yb + lnCoef_i[1];
        		tmp = aa * HEX_40000000;
        		ya = aa + tmp - tmp;
        		yb = aa - ya + ab;
            }
        	aa = ya * xa;
        	ab = ya * xb + yb * xa + yb * xb;
        	tmp = aa * HEX_40000000;
        	ya = aa + tmp - tmp;
        	yb = aa - ya + ab;
        	return ya + yb;
        }
        final double[] lnm = FastMath.lnMant.LN_MANT[(int)((bits & 0x000ffc0000000000L) >> 42)];
        final double epsilon = (bits & 0x3ffffffffffL) / (TWO_POWER_52 + (bits & 0x000ffc0000000000L));

        double lnza = 0.0;
        double lnzb = 0.0;

        if (hiPrec != null) {
        	double tmp = epsilon * HEX_40000000;
        	double aa = epsilon + tmp - tmp;
        	double ab = epsilon - aa;
        	double xa = aa;
        	double xb = ab;
        	final double numer = bits & 0x3ffffffffffL;
        	final double denom = TWO_POWER_52 + (bits & 0x000ffc0000000000L);
        	aa = numer - xa*denom - xb * denom;
        	xb += aa / denom;
        	final double[] lnCoef_last = LN_HI_PREC_COEF[LN_HI_PREC_COEF.length-1];
        	double ya = lnCoef_last[0];
        	double yb = lnCoef_last[1];
        	for (int i = LN_HI_PREC_COEF.length - 2; print("61") && i >= 0; i--) {
        		aa = ya * xa;
        		ab = ya * xb + yb * xa + yb * xb;
        		tmp = aa * HEX_40000000;
        		ya = aa + tmp - tmp;
        		yb = aa - ya + ab;
        		final double[] lnCoef_i = LN_HI_PREC_COEF[i];
        		aa = ya + lnCoef_i[0];
        		ab = yb + lnCoef_i[1];
        		tmp = aa * HEX_40000000;
        		ya = aa + tmp - tmp;
        		yb = aa - ya + ab;
            }
        	aa = ya * xa;
        	ab = ya * xb + yb * xa + yb * xb;
        	lnza = aa + ab;
        	lnzb = -(lnza - aa - ab);
        } 
        else {
        	lnza = -0.16624882440418567;
        	lnza = lnza * epsilon + 0.19999954120254515;
        	lnza = lnza * epsilon + -0.2499999997677497;
        	lnza = lnza * epsilon + 0.3333333333332802;
        	lnza = lnza * epsilon + -0.5;
        	lnza = lnza * epsilon + 1.0;
        	lnza *= epsilon;
        }
        double a = LN_2_A*exp;
        double b = 0.0;
        double c = a+lnm[0];
        double d = -(c-a-lnm[0]);
        a = c;
        b += d;
        c = a + lnza;
        d = -(c - a - lnza);
        a = c;
        b += d;
        c = a + LN_2_B*exp;
        d = -(c - a - LN_2_B*exp);
        a = c;
        b += d;
        c = a + lnm[1];
        d = -(c - a - lnm[1]);
        a = c;
        b += d;
        c = a + lnzb;
        d = -(c - a - lnzb);
        a = c;
        b += d;
        if (hiPrec != null) {
        	hiPrec[0] = a;
        	hiPrec[1] = b;
        }
        return a + b;
    }
}
