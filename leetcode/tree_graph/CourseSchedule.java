import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/course-schedule/
//
// There are a total of n courses you have to take, labeled from 0 to n - 1.
// Some courses may have prerequisites. Given the total number of courses and a
// list of prerequisite pairs, is it possible for you to finish all courses?
public class CourseSchedule {
    // DFS
    // beats 49.07%(24 ms)
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        Map<Integer, Set<Integer> > adjacencyList = new HashMap<>();
        for (int[] prerequisite : prerequisites) {
            int course = prerequisite[0];
            if (!adjacencyList.containsKey(course)) {
                adjacencyList.put(course, new HashSet<>());
            }
            adjacencyList.get(course).add(prerequisite[1]);
        }

        // if use iterator, will beat 52.26%(21 ms)
        // Iterator<Integer> iter = adjacencyList.keySet().iterator();
        // while (iter.hasNext()) {
        //     if (canFinish(iter.next(), 0, numCourses, adjacencyList)) {
        //         iter.remove();
        //     } else return false;
        // }
        for (int course : adjacencyList.keySet()) {
            if (!canFinish(course, 0, numCourses, adjacencyList)) return false;
        }
        return true;
    }

    private boolean canFinish(int course, int depth, int numCourses,
                              Map<Integer, Set<Integer> > adjacencyList) {
        if (depth == numCourses) return false;

        if (!adjacencyList.containsKey(course)) return true;

        Set<Integer> prerequisites = adjacencyList.get(course);
        Iterator<Integer> iter = prerequisites.iterator();
        while (iter.hasNext()) {
            if (canFinish(iter.next(), depth + 1, numCourses, adjacencyList)) {
                iter.remove();
            } else return false;
        }
        return true;
    }

    void test(int n, int[][] prerequisites, boolean expected) {
        assertEquals(expected, canFinish(n, prerequisites));
    }

    @Test
    public void test1() {
        test(2, new int[][] {{1, 0}}, true);
        test(2, new int[][] {{1, 0}, {0, 1}}, false);
        test(5, new int[][] {{1, 2}, {1, 3}, {2, 4}, {3, 4}}, true);
        test(5, new int[][] {{1, 2}, {1, 3}, {2, 4}, {3, 4}, {4, 1}}, false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CourseSchedule");
    }
}
