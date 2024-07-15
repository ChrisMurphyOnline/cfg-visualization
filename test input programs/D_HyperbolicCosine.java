public class D_HyperbolicCosine {
	public static double cosh(double x) {
		print("1"); if (x != x) {
			print("2"); return x;
		}
		print("4"); if (x > 20) {
			print("5"); if (x >= LOG_MAX_VALUE) {
				print("6"); final double t = Math.exp(0.5 * x);
				print("7"); return (0.5 * t) * t;
			} 
			else {
				print("10"); return 0.5 * Math.exp(x);
			}
		} 
		else {
			print("14"); if (x < -20) {
				print("15"); if (x <= -LOG_MAX_VALUE) {
					print("16"); final double t = Math.exp(-0.5 * x);
					print("17"); return (0.5 * t) * t;
				} 
				else {
					print("20"); return 0.5 * Math.exp(-x);
				}
			}
		}
		print("24"); final double hiPrec[] = new double[2];
		print("25"); if (x < 0.0) {
			print("26"); x = -x;
		}
		print("28"); FastMath.exp(x, 0.0, hiPrec);
		print("29"); double ya = hiPrec[0] + hiPrec[1];
		print("30"); double yb = -(ya - hiPrec[0] - hiPrec[1]);
		print("31"); double temp = ya * HEX_40000000;
		print("32"); double yaa = ya + temp - temp;
		print("33"); double yab = ya - yaa;
		print("34"); double recip = 1.0/ya;
		print("35"); temp = recip * HEX_40000000;
		print("36"); double recipa = recip + temp - temp;
		print("37"); double recipb = recip - recipa;
		print("38"); recipb += (1.0 - yaa*recipa - yaa*recipb - yab*recipa - yab*recipb) * recip;
		print("39"); recipb += -yb * recip * recip;
		print("40"); temp = ya + recipa;
		print("41"); yb += -(temp - ya - recipa);
		print("42"); ya = temp;
		print("43"); temp = ya + recipb;
		print("44"); yb += -(temp - ya - recipb);
		print("45"); ya = temp;
		print("46"); double result = ya + yb;
		print("47"); result *= 0.5;
		print("48"); return result;
	}
}
