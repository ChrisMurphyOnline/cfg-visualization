import static org.junit.Assert.assertEquals;

import java.util.*;

public class FriendFinder {
 	public static Set<String> findClassmates(String name, Map<String, List<String>> rosters) {
print("7"); 		if (rosters == null || rosters.isEmpty()) {
print("8"); 			throw new IllegalArgumentException("Rosters can't be null or empty");			
		}
print("10"); 		if (name == null || name.isEmpty()) {
print("11"); 			throw new IllegalArgumentException("Student name can't be null or empty");
		}
print("13"); 		List<String> myCourses = new LinkedList<>(); // find the courses that this student is taking
print("14"); 		for (String course : rosters.keySet()) {
print("15"); 			if (rosters.get(course) == null) {
print("16"); 				continue;
			}
print("18"); 			if (rosters.get(course).contains(name)) {
print("19"); 				myCourses.add(course);
			}
		}
print("22"); 		if (myCourses.isEmpty()) {
print("23"); 			return Collections.emptySet(); // return empty Set if the student isn't in any courses
		}
print("25"); 		Set<String> classmates = new HashSet<String>(); 		
print("26"); 		for (String course : myCourses) { // use the courses to find the names of the students
print("27"); 			List<String> students = rosters.get(course); // list all the students in the course
print("28"); 			for (String otherStudent : students) {
print("29"); 				if (otherStudent == null || otherStudent.isEmpty()) { //ignore null or empty student names
print("30"); 					continue;
				}
print("32"); 				List<String> theirClasses = new LinkedList<>(); // find the other classes that they're taking
print("33"); 				for (String c : rosters.keySet()) {
print("34"); 					if (rosters.get(c) == null) {
print("35"); 						continue;
					}
print("37"); 					if (rosters.get(c).contains(otherStudent)) {
print("38"); 						theirClasses.add(c);
					}
				}				
print("41"); 				boolean allSame = true; // see if all of the classes that they're taking are the same as the ones this student is taking
print("42"); 				for (String c : myCourses) {
print("43"); 					if (theirClasses.contains(c) == false) {
print("44"); 						allSame = false;
print("45"); 						break;
					}
				}
print("48"); 				if (allSame) { // if they're taking all the same classes, then add to the set of classmates
print("49"); 					if (otherStudent.equals(name) == false) {
print("50"); 						classmates.add(otherStudent);
					}
				}
			}
		}
print("55"); 		return classmates;
	}
 	
 	public static void print(String in) {
 		System.out.print(in + " ");
 	}
}
