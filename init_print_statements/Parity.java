
public class Parity {
	
	public static int parity(String message, String type) {
System.out.print("2 ");		for (char c : message.toCharArray()) {
System.out.print("3 ");			if (c != '0' && c != '1') {
System.out.print("4 ");				return -1;
	}
}
		
System.out.print("6 ");		if (type.equals("even") == false && type.equals("odd") == false) {
	System.out.print("7 ");			return -1;
		}
		
System.out.print("9 ");		int count = 0;
System.out.print("10 ");		for (char c : message.toCharArray()) { 
	System.out.print("11 ");			if (c == '1') {
		System.out.print("12 ");				count ++;
			}
		}
		
System.out.print("14 ");		if (type.equals("even")) {
	System.out.print("15 ");			if (count % 2 == 0) {
		System.out.print("16 ");				return 0;
			}
			else {
				System.out.print("18 ");				return 1;
			}
		}
		else {
			System.out.print("20 ");			if (count % 2 == 1) { // bug here
				System.out.print("21 ");				return 1;
			}
			else {
				System.out.print("23 ");				return 0;
			}
		}			
		
	}

}
