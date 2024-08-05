//import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class FriendFinderTest {
	
	private static int which = 1;
	
	@Before
	public void printTestNumber() {
		System.out.print("test" + which++ + ": ");		
	}
	
	@Test
	public void testNoCourses() {		
		Map<String, List<String>> rosters = new HashMap<>();
		rosters.put("21", List.of("A", "B", "C"));
		rosters.put("31", List.of("A", "C", "D"));
		rosters.put("35", List.of("A", "B", "E"));
		
		Set<String> result = FriendFinder.findClassmates("K", rosters);
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void testNoFriends() {
		Map<String, List<String>> rosters = new HashMap<>();
		rosters.put("21", List.of("A", "B", "C"));
		rosters.put("31", List.of("A", "C", "D"));
		rosters.put("35", List.of("A", "B", "E"));
		
		Set<String> result = FriendFinder.findClassmates("A", rosters);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testOneFriend() {
		Map<String, List<String>> rosters = new HashMap<>();
		rosters.put("21", List.of("A", "B", "C"));
		rosters.put("31", List.of("A", "C", "D"));
		rosters.put("35", List.of("A", "B", "C"));
		
		Set<String> result = FriendFinder.findClassmates("A", rosters);
		assertEquals(Set.of("C"), result);
	}

	@Test
	public void testTwoFriends() {
		Map<String, List<String>> rosters = new HashMap<>();
		rosters.put("21", List.of("A", "B", "C"));
		rosters.put("31", List.of("A", "B", "C", "D"));
		rosters.put("35", List.of("A", "B", "C"));
		
		Set<String> result = FriendFinder.findClassmates("A", rosters);
		assertEquals(Set.of("B", "C"), result);
	}
	
	@Test//(expected=IllegalArgumentException.class)
	public void testNullName() {
		Map<String, List<String>> rosters = new HashMap<>();
		rosters.put("21", List.of("A", "B", "C"));
		try {
			FriendFinder.findClassmates(null, rosters);
		}
		catch (IllegalArgumentException e) {
			System.out.println("Pass");
			return;
		}
		System.out.println("Fail");
	}
	
	@Test//(expected=IllegalArgumentException.class)
	public void testNullRosters() {
		try {
			FriendFinder.findClassmates("A", null);
		}
		catch (IllegalArgumentException e) {
			System.out.println("Pass");
			return;
		}
		System.out.println("Fail");
	}
	
	@Test
	public void testRostersContainsNullList() {
		Map<String, List<String>> rosters = new HashMap<>();
		rosters.put("21", List.of("A", "B", "C"));
		rosters.put("31", List.of("A", "B", "C", "D"));
		rosters.put("35", null);
		
		Set<String> result = FriendFinder.findClassmates("A", rosters);
		assertEquals(Set.of("B", "C"), result);
	}
	
	@Test
	public void testCourseListContainsNull() {
		Map<String, List<String>> rosters = new HashMap<>();
		rosters.put("21", List.of("A", "B", "C"));
		rosters.put("31", List.of("A", "B", "C", "D"));
		List<String> students = new LinkedList<>(List.of("A", "B"));
		students.add(null);
		rosters.put("35", students);
		
		Set<String> result = FriendFinder.findClassmates("A", rosters);
		assertEquals(Set.of("B"), result);
	}
	
	public void assertEquals(Object o1, Object o2) {
		if (o1.equals(o2)) System.out.println("Pass");
		else System.out.println("Fail");
	}
	
	public void assertTrue(boolean b) {
		if (b) System.out.println("Pass");
		else System.out.println("Fail");
	}
}
