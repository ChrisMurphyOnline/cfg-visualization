public class D_HyperbolicCosine {
print("2"); 	public static double cosh(double x) {
print("3"); 		if (x != x) {
print("4"); 			return x;
		}
print("6"); 		if (x > 20) {
print("7"); 			if (x >= LOG_MAX_VALUE) {
print("8"); 				final double t = Math.exp(0.5 * x);
print("9"); 				return (0.5 * t) * t;
			} 
			else {
print("12"); 				return 0.5 * Math.exp(x);
			}
		} 
		else {
print("16"); 			if (x < -20) {
print("17"); 				if (x <= -LOG_MAX_VALUE) {
print("18"); 					final double t = Math.exp(-0.5 * x);
print("19"); 					return (0.5 * t) * t;
				} 
				else {
print("22"); 					return 0.5 * Math.exp(-x);
				}
			}
		}
print("26"); 		final double hiPrec[] = new double[2];
print("27"); 		if (x < 0.0) {
print("28"); 			x = -x;
		}
print("30"); 		FastMath.exp(x, 0.0, hiPrec);
print("31"); 		double ya = hiPrec[0] + hiPrec[1];
print("32"); 		double yb = -(ya - hiPrec[0] - hiPrec[1]);
print("33"); 		double temp = ya * HEX_40000000;
print("34"); 		double yaa = ya + temp - temp;
print("35"); 		double yab = ya - yaa;
print("36"); 		double recip = 1.0/ya;
print("37"); 		temp = recip * HEX_40000000;
print("38"); 		double recipa = recip + temp - temp;
print("39"); 		double recipb = recip - recipa;
print("40"); 		recipb += (1.0 - yaa*recipa - yaa*recipb - yab*recipa - yab*recipb) * recip;
print("41"); 		recipb += -yb * recip * recip;
print("42"); 		temp = ya + recipa;
print("43"); 		yb += -(temp - ya - recipa);
print("44"); 		ya = temp;
print("45"); 		temp = ya + recipb;
print("46"); 		yb += -(temp - ya - recipb);
print("47"); 		ya = temp;
print("48"); 		double result = ya + yb;
print("49"); 		result *= 0.5;
print("50"); 		return result;
	}
}