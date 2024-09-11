
public class Parity {
	
	public static int parity(String message, String type) {
		for (char c : message.toCharArray()) {
			if (c != '0' && c != '1') {
				return -1;
			}
		}
		
		if (type.equals("even") == false && type.equals("odd") == false) {
			return -1;
		}
		
		int count = 0;
		for (char c : message.toCharArray()) { 
			if (c == '1') {
				count ++;
			}
		}
		
		if (type.equals("even")) {
			if (count % 2 == 0) {
				return 0;
			}
			else {
				return 1;
			}
		}
		else {
			if (count % 2 == 1) { // bug here
				return 1;
			}
			else {
				return 0;
			}
		}			
	}
}
