import java.math.BigInteger;
public class D_Prime {
         public static boolean isPrime(int n) {
print("4");             if (n < 2) {
print("5");             	return false;
            }
print("7");             for (int p : PRIMES) {
print("8");             	if (0 == (n % p)) {
print("9");             		return n == p;
                }
            }
print("12");             final int nMinus1 = n - 1;
print("13");             final int s = Integer.numberOfTrailingZeros(nMinus1);
print("14");             final int r = nMinus1 >> s; 
print("15");             int t = 1;
print("16");             if (n >= 2047) {
print("17");             	t = 2;
            }
print("19");             if (n >= 1373653) {
print("20");             	t = 3;
            }
print("22");             if (n >= 25326001) {
print("23");             	t = 4;
            } 
print("25");             BigInteger br = BigInteger.valueOf(r); 
print("26");             BigInteger bn = BigInteger.valueOf(n);
print("27");             for (int i = 0; i < t; i++) {
print("28");             	BigInteger a = BigInteger.valueOf(PRIMES[i]);
print("29");             	BigInteger bPow = a.modPow(br, bn);
print("30");             	int y = bPow.intValue();
print("31");             	if ((1 != y) && (y != nMinus1)) {
print("32");             		int j = 1;
print("33");             		while ((j <= s - 1) && (nMinus1 != y)) {
print("34");             			long square = ((long) y) * y;
print("35");             			y = (int) (square % n);
print("36");             			if (1 == y) { 
print("37");             				return false;
                        } 
print("39");             			j++;
                    }
print("41");             		if (nMinus1 != y) { 
print("42");             			return false;
                    } 
                }
            } 
print("46");             return true; 
        }
} 
