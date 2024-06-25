public class D_HyperbolicSine {
	public static double sinh(double x) {
		boolean negate = false;
		if (x != x) {
			return x;
		}
		if (x > 20) {
			if (x >= LOG_MAX_VALUE) {
				final double t = Math.exp(0.5 * x);
				return (0.5 * t) * t;
			} 
			else {
				return 0.5 * Math.exp(x);
			}
		} 
		else {
			if (x < -20) {
				if (x <= -LOG_MAX_VALUE) {
					final double t = Math.exp(-0.5 * x);
					return (-0.5 * t) * t;
				} 
				else {
					return -0.5 * Math.exp(-x);
				}
			}
		}
		if (x == 0) {
			return x;
		}
		if (x < 0.0) {
			x = -x;
			negate = true;
		}
		double result;
		if (x > 0.25) {
			double hiPrec[] = new double[2];
			FastMath.exp(x, 0.0, hiPrec);
			double ya = hiPrec[0] + hiPrec[1];
			double yb = -(ya - hiPrec[0] - hiPrec[1]);
			double temp = ya * HEX_40000000;
			double yaa = ya + temp - temp;
			double yab = ya - yaa;
			double recip = 1.0/ya;
			temp = recip * HEX_40000000;
			double recipa = recip + temp - temp;
			double recipb = recip - recipa;
			recipb += (1.0 - yaa*recipa - yaa*recipb - yab*recipa - yab*recipb) * recip;
			recipb += -yb * recip * recip;
			recipa = -recipa;
			recipb = -recipb;
			temp = ya + recipa;
			yb += -(temp - ya - recipa);
			ya = temp;
			temp = ya + recipb;
			yb += -(temp - ya - recipb);
			ya = temp;
			result = ya + yb;
			result *= 0.5;
		}
		else {
			double hiPrec[] = new double[2];
			FastMath.expm1(x, hiPrec);
			double ya = hiPrec[0] + hiPrec[1];
			double yb = -(ya - hiPrec[0] - hiPrec[1]);
			double denom = 1.0 + ya;
			double denomr = 1.0 / denom;
			double denomb = -(denom - 1.0 - ya) + yb;
			double ratio = ya * denomr;
			double temp = ratio * HEX_40000000;
			double ra = ratio + temp - temp;
			double rb = ratio - ra;
			temp = denom * HEX_40000000;
			double za = denom + temp - temp;
			double zb = denom - za;
			rb += (ya - za*ra - za*rb - zb*ra - zb*rb) * denomr;
			rb += yb*denomr;  
			rb += -ya * denomb * denomr * denomr;
			temp = ya + ra;
			yb += -(temp - ya - ra);
			ya = temp;
			temp = ya + rb;
			yb += -(temp - ya - rb);
			ya = temp;
			result = ya + yb;
			result *= 0.5;
		}
		if (negate) {
			result = -result;
		}
		return result;
	}
}
