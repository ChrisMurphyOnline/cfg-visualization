public class D_HyperbolicSine {
 	public static double sinh(double x) {
print("3"); 		boolean negate = false;
print("4"); 		if (x != x) {
print("5"); 			return x;
		}
print("7"); 		if (x > 20) {
print("8"); 			if (x >= LOG_MAX_VALUE) {
print("9"); 				final double t = Math.exp(0.5 * x);
print("10"); 				return (0.5 * t) * t;
			} 
			else {
print("13"); 				return 0.5 * Math.exp(x);
			}
		} 
		else {
print("17"); 			if (x < -20) {
print("18"); 				if (x <= -LOG_MAX_VALUE) {
print("19"); 					final double t = Math.exp(-0.5 * x);
print("20"); 					return (-0.5 * t) * t;
				} 
				else {
print("23"); 					return -0.5 * Math.exp(-x);
				}
			}
		}
print("27"); 		if (x == 0) {
print("28"); 			return x;
		}
print("30"); 		if (x < 0.0) {
print("31"); 			x = -x;
print("32"); 			negate = true;
		}
print("34"); 		double result;
print("35"); 		if (x > 0.25) {
print("36"); 			double hiPrec[] = new double[2];
print("37"); 			FastMath.exp(x, 0.0, hiPrec);
print("38"); 			double ya = hiPrec[0] + hiPrec[1];
print("39"); 			double yb = -(ya - hiPrec[0] - hiPrec[1]);
print("40"); 			double temp = ya * HEX_40000000;
print("41"); 			double yaa = ya + temp - temp;
print("42"); 			double yab = ya - yaa;
print("43"); 			double recip = 1.0/ya;
print("44"); 			temp = recip * HEX_40000000;
print("45"); 			double recipa = recip + temp - temp;
print("46"); 			double recipb = recip - recipa;
print("47"); 			recipb += (1.0 - yaa*recipa - yaa*recipb - yab*recipa - yab*recipb) * recip;
print("48"); 			recipb += -yb * recip * recip;
print("49"); 			recipa = -recipa;
print("50"); 			recipb = -recipb;
print("51"); 			temp = ya + recipa;
print("52"); 			yb += -(temp - ya - recipa);
print("53"); 			ya = temp;
print("54"); 			temp = ya + recipb;
print("55"); 			yb += -(temp - ya - recipb);
print("56"); 			ya = temp;
print("57"); 			result = ya + yb;
print("58"); 			result *= 0.5;
		}
		else {
print("61"); 			double hiPrec[] = new double[2];
print("62"); 			FastMath.expm1(x, hiPrec);
print("63"); 			double ya = hiPrec[0] + hiPrec[1];
print("64"); 			double yb = -(ya - hiPrec[0] - hiPrec[1]);
print("65"); 			double denom = 1.0 + ya;
print("66"); 			double denomr = 1.0 / denom;
print("67"); 			double denomb = -(denom - 1.0 - ya) + yb;
print("68"); 			double ratio = ya * denomr;
print("69"); 			double temp = ratio * HEX_40000000;
print("70"); 			double ra = ratio + temp - temp;
print("71"); 			double rb = ratio - ra;
print("72"); 			temp = denom * HEX_40000000;
print("73"); 			double za = denom + temp - temp;
print("74"); 			double zb = denom - za;
print("75"); 			rb += (ya - za*ra - za*rb - zb*ra - zb*rb) * denomr;
print("76"); 			rb += yb*denomr;  
print("77"); 			rb += -ya * denomb * denomr * denomr;
print("78"); 			temp = ya + ra;
print("79"); 			yb += -(temp - ya - ra);
print("80"); 			ya = temp;
print("81"); 			temp = ya + rb;
print("82"); 			yb += -(temp - ya - rb);
print("83"); 			ya = temp;
print("84"); 			result = ya + yb;
print("85"); 			result *= 0.5;
		}
print("87"); 		if (negate) {
print("88"); 			result = -result;
		}
print("90"); 		return result;
	}
}
