import java.math.BigInteger;
public class D_Prime {
        public static boolean isPrime(int n) {
            if (n < 2) {
            	return false;
            }
            for (int p : PRIMES) {
            	if (0 == (n % p)) {
            		return n == p;
                }
            }
            final int nMinus1 = n - 1;
            final int s = Integer.numberOfTrailingZeros(nMinus1);
            final int r = nMinus1 >> s; 
            int t = 1;
            if (n >= 2047) {
            	t = 2;
            }
            if (n >= 1373653) {
            	t = 3;
            }
            if (n >= 25326001) {
            	t = 4;
            } 
            BigInteger br = BigInteger.valueOf(r); 
            BigInteger bn = BigInteger.valueOf(n);
            for (int i = 0; i < t; i++) {
            	BigInteger a = BigInteger.valueOf(PRIMES[i]);
            	BigInteger bPow = a.modPow(br, bn);
            	int y = bPow.intValue();
            	if ((1 != y) && (y != nMinus1)) {
            		int j = 1;
            		while ((j <= s - 1) && (nMinus1 != y)) {
            			long square = ((long) y) * y;
            			y = (int) (square % n);
            			if (1 == y) { 
            				return false;
                        } 
            			j++;
                    }
            		if (nMinus1 != y) { 
            			return false;
                    } 
                }
            } 
            return true; 
        }
} 