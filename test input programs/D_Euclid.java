public class D_Euclid {
	public static int gcd(int x, int y) {
		int a, b;
		if (x > y) {
			a = x ;   
		}
		else {
			a = y;
		}
		if (x < y) {
			b = x ;   
		}
		else {
			b = y;
		}
		int r = b;
		while (a % b != 0) {
			r = a % b;  
			a = b;  
			b = r;  
		}  
		return r;  
	}
}
