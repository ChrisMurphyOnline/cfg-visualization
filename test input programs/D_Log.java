public class D_Log extends FastMath {
    public static double log(final double x, final double[] hiPrec) { 
        if (x==0) { 
        	return Double.NEGATIVE_INFINITY;
        }
        print("3");long bits = Double.doubleToRawLongBits(x);

        /* Handle special cases of negative input, and NaN */
        print("4");if (((bits & 0x8000000000000000L) != 0 || x != x) && x != 0.0) {
        	print("4.1");
        	print("5");if (hiPrec != null) {
        		print("5.1");
        		print("6");hiPrec[0] = Double.NaN;
            }
            else {
            	print("5.2");
            }

        	print("7");return Double.NaN;
        }
        else {
        	print("4.2");
        }

        /* Handle special cases of Positive infinity. */
        print("8"); if (x == Double.POSITIVE_INFINITY) {
        	print("8.1");
        	print("9");if (hiPrec != null) {
        		print("9.1");
        		print("10");hiPrec[0] = Double.POSITIVE_INFINITY;
            }
            else {
            	print("9.2");
            }

        	print("11");return Double.POSITIVE_INFINITY;
        }
        else {
        	print("8.2");
        }

        /* Extract the exponent */
        print("12");int exp = (int)(bits >> 52)-1023;


        print("13");if ((exp == -1 || exp == 0) && x < 1.01 && x > 0.99 && hiPrec == null) {
        	print("13.1");
            /* The normal method doesn't work well in the range [0.99, 1.01], so call do a straight
           polynomial expansion in higer precision. */

            /* Compute x - 1.0 and split it */
        	print("14");double xa = x - 1.0;
        	print("15");double xb = xa - x + 1.0;
        	print("16");double tmp = xa * HEX_40000000;
        	print("17");double aa = xa + tmp - tmp;
        	print("18");double ab = xa - aa;
        	print("19");xa = aa;
        	print("20");xb = ab;

        	print("21");final double[] lnCoef_last = LN_QUICK_COEF[LN_QUICK_COEF.length - 1];
        	print("22");double ya = lnCoef_last[0];
        	print("23");double yb = lnCoef_last[1];

        	print("24");for (int i = LN_QUICK_COEF.length - 2; print("25") && i >= 0; i--) {
        		print("25.1");
                /* Multiply a = y * x */
        		print("26");aa = ya * xa;
        		print("27");ab = ya * xb + yb * xa + yb * xb;
                /* split, so now y = a */
        		print("28");tmp = aa * HEX_40000000;
        		print("29");ya = aa + tmp - tmp;
        		print("30");yb = aa - ya + ab;

                /* Add  a = y + lnQuickCoef */
        		print("31");final double[] lnCoef_i = LN_QUICK_COEF[i];
        		print("32");aa = ya + lnCoef_i[0];
        		print("33");ab = yb + lnCoef_i[1];
                /* Split y = a */
        		print("34");tmp = aa * HEX_40000000;
        		print("35");ya = aa + tmp - tmp;
        		print("36");yb = aa - ya + ab;
            }

            /* Multiply a = y * x */
        	print("37");aa = ya * xa;
        	print("38");ab = ya * xb + yb * xa + yb * xb;
            /* split, so now y = a */
        	print("39");tmp = aa * HEX_40000000;
        	print("40");ya = aa + tmp - tmp;
        	print("41");yb = aa - ya + ab;

        	print("42");return ya + yb;
        }
        else {
        	print("13.2");
        }

        // lnm is a log of a number in the range of 1.0 - 2.0, so 0 <= lnm < ln(2)
        print("43");final double[] lnm = FastMath.lnMant.LN_MANT[(int)((bits & 0x000ffc0000000000L) >> 42)];

        /*
    double epsilon = x / Double.longBitsToDouble(bits & 0xfffffc0000000000L);

    epsilon -= 1.0;
         */

        // y is the most significant 10 bits of the mantissa
        //double y = Double.longBitsToDouble(bits & 0xfffffc0000000000L);
        //double epsilon = (x - y) / y;
        print("44");final double epsilon = (bits & 0x3ffffffffffL) / (TWO_POWER_52 + (bits & 0x000ffc0000000000L));

        print("45");double lnza = 0.0;
        print("46");double lnzb = 0.0;

        print("47");if (hiPrec != null) {
        	print("47.1");
            /* split epsilon -> x */
        	print("48");double tmp = epsilon * HEX_40000000;
        	print("49");double aa = epsilon + tmp - tmp;
        	print("50");double ab = epsilon - aa;
        	print("51");double xa = aa;
        	print("52");double xb = ab;

            /* Need a more accurate epsilon, so adjust the division. */
        	print("53");final double numer = bits & 0x3ffffffffffL;
        	print("54");final double denom = TWO_POWER_52 + (bits & 0x000ffc0000000000L);
        	print("55");aa = numer - xa*denom - xb * denom;
        	print("56");xb += aa / denom;

            /* Remez polynomial evaluation */
        	print("57");final double[] lnCoef_last = LN_HI_PREC_COEF[LN_HI_PREC_COEF.length-1];
        	print("58");double ya = lnCoef_last[0];
        	print("59");double yb = lnCoef_last[1];

        	print("60");for (int i = LN_HI_PREC_COEF.length - 2; print("61") && i >= 0; i--) {
        		print("61.1");
                /* Multiply a = y * x */
        		print("62");aa = ya * xa;
        		print("63");ab = ya * xb + yb * xa + yb * xb;
                /* split, so now y = a */
        		print("64");tmp = aa * HEX_40000000;
        		print("65");ya = aa + tmp - tmp;
        		print("66");yb = aa - ya + ab;

                /* Add  a = y + lnHiPrecCoef */
        		print("67");final double[] lnCoef_i = LN_HI_PREC_COEF[i];
        		print("68");aa = ya + lnCoef_i[0];
        		print("69");ab = yb + lnCoef_i[1];
                /* Split y = a */
        		print("70");tmp = aa * HEX_40000000;
        		print("71");ya = aa + tmp - tmp;
        		print("72");yb = aa - ya + ab;
            }

            /* Multiply a = y * x */
        	print("73");aa = ya * xa;
        	print("74");ab = ya * xb + yb * xa + yb * xb;

            /* split, so now lnz = a */
            /*
      tmp = aa * 1073741824.0;
      lnza = aa + tmp - tmp;
      lnzb = aa - lnza + ab;
             */
        	print("75");lnza = aa + ab;
        	print("76");lnzb = -(lnza - aa - ab);
        } 
        else {
        	print("47.2");
            /* High precision not required.  Eval Remez polynomial
         using standard double precision */
        	print("77");lnza = -0.16624882440418567;
        	print("78");lnza = lnza * epsilon + 0.19999954120254515;
        	print("79");lnza = lnza * epsilon + -0.2499999997677497;
        	print("80");lnza = lnza * epsilon + 0.3333333333332802;
        	print("81");lnza = lnza * epsilon + -0.5;
        	print("82");lnza = lnza * epsilon + 1.0;
        	print("83");lnza *= epsilon;
        }

        /* Relative sizes:
         * lnzb     [0, 2.33E-10]
         * lnm[1]   [0, 1.17E-7]
         * ln2B*exp [0, 1.12E-4]
         * lnza      [0, 9.7E-4]
         * lnm[0]   [0, 0.692]
         * ln2A*exp [0, 709]
         */

        /* Compute the following sum:
         * lnzb + lnm[1] + ln2B*exp + lnza + lnm[0] + ln2A*exp;
         */

        //return lnzb + lnm[1] + ln2B*exp + lnza + lnm[0] + ln2A*exp;
        print("84");double a = LN_2_A*exp;
        print("85");double b = 0.0;
        print("86");double c = a+lnm[0];
        print("87");double d = -(c-a-lnm[0]);
        print("88");a = c;
        print("89");b += d;

        print("90");c = a + lnza;
        print("91");d = -(c - a - lnza);
        print("92");a = c;
        print("93");b += d;

        print("94");c = a + LN_2_B*exp;
        print("95");d = -(c - a - LN_2_B*exp);
        print("96");a = c;
        print("97"); b += d;

        print("98");c = a + lnm[1];
        print("99");d = -(c - a - lnm[1]);
        print("100");a = c;
        print("101");b += d;

        print("102");c = a + lnzb;
        print("103");d = -(c - a - lnzb);
        print("104");a = c;
        print("105");b += d;

        print("106");if (hiPrec != null) {
        	print("106.1");
        	print("107");hiPrec[0] = a;
        	print("108");hiPrec[1] = b;
        }
        else {
        	print("106.2");
        }

        print("109");return a + b;
    }

	private static boolean print(String string) {
		System.out.print(" " + string);
		return true;

	}
}
