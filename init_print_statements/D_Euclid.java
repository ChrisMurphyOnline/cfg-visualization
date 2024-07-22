public class D_Euclid {
 	public static int gcd(int x, int y) {
print("3"); 		int a, b;
print("4"); 		if (x > y) {
print("5"); 			a = x ;   
		}
		else {
print("8"); 			a = y;
		}
print("10"); 		if (x < y) {
print("11"); 			b = x ;   
		}
		else {
print("14"); 			b = y;
		}
print("16"); 		int r = b;
print("17"); 		while (a % b != 0) {
print("18"); 			r = a % b;  
print("19"); 			a = b;  
print("20"); 			b = r;  
		}  
print("22"); 		return r;  
	}
}
