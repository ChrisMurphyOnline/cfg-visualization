import static org.junit.Assert.assertEquals;

import java.util.*;

public class FriendFinder {
	public static Set<String> findClassmates(String name, Map<String, List<String>> rosters) {
		if (rosters == null || rosters.isEmpty()) {
			throw new IllegalArgumentException("Rosters can't be null or empty");			
		}
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("Student name can't be null or empty");
		}
		List<String> myCourses = new LinkedList<>(); // find the courses that this student is taking
		for (String course : rosters.keySet()) {
			if (rosters.get(course) == null) {
				continue;
			}
			if (rosters.get(course).contains(name)) {
				myCourses.add(course);
			}
		}
		if (myCourses.isEmpty()) {
			return Collections.emptySet(); // return empty Set if the student isn't in any courses
		}
		Set<String> classmates = new HashSet<String>(); 		
		for (String course : myCourses) { // use the courses to find the names of the students
			List<String> students = rosters.get(course); // list all the students in the course
			for (String otherStudent : students) {
				if (otherStudent == null || otherStudent.isEmpty()) { //ignore null or empty student names
					continue;
				}
				List<String> theirClasses = new LinkedList<>(); // find the other classes that they're taking
				for (String c : rosters.keySet()) {
					if (rosters.get(c) == null) {
						continue;
					}
					if (rosters.get(c).contains(otherStudent)) {
						theirClasses.add(c);
					}
				}				
				boolean allSame = true; // see if all of the classes that they're taking are the same as the ones this student is taking
				for (String c : myCourses) {
					if (theirClasses.contains(c) == false) {
						allSame = false;
						break;
					}
				}
				if (allSame) { // if they're taking all the same classes, then add to the set of classmates
					if (otherStudent.equals(name) == false) {
						classmates.add(otherStudent);
					}
				}
			}
		}
		return classmates;
	}
}