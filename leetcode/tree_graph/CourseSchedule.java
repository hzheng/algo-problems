import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC207: https://leetcode.com/problems/course-schedule/
//
// There are a total of n courses you have to take, labeled from 0 to n - 1.
// Some courses may have prerequisites. Given the total number of courses and a
// list of prerequisite pairs, is it possible for you to finish all courses?
public class CourseSchedule {
    // Hash Table + DFS + Recursion
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

    // Solution of Choice
    // Hash Table + DFS + Recursion + 3 Colors
    // reverse mapping: actually both will do
    // beats 71.49%(11 ms for 35 tests)
    public boolean canFinish2(int numCourses, int[][] prerequisites) {
        Map<Integer, List<Integer> > map = new HashMap<>();
        for (int[] prerequisite : prerequisites) {
            if (map.containsKey(prerequisite[1])) {
                map.get(prerequisite[1]).add(prerequisite[0]);
            } else {
                List<Integer> dependants = new ArrayList<>();
                dependants.add(prerequisite[0]);
                map.put(prerequisite[1], dependants);
            }
        }
        byte[] visited = new byte[numCourses];
        for (int i = 0; i < numCourses; i++) {
            if (!canFinish2(i, map, visited)) return false;
        }
        return true;
    }

    private boolean canFinish2(int course, Map<Integer, List<Integer> > map,
                               byte[] visited) {
        if (visited[course] != 0) return visited[course] > 0;

        visited[course] = -1; // mark for cycle detection
        if (map.containsKey(course)) {
            for (int dependant : map.get(course)) {
                if (!canFinish2(dependant, map, visited)) return false;
            }
        }
        visited[course] = 1;
        return true;
    }

    // BFS + Queue / Topological Sort
    // count down independents
    // beats 28.61%(37 ms)
    public boolean canFinish3(int numCourses, int[][] prerequisites) {
        int[] dependentCount = new int[numCourses];
        for (int[] prerequisite : prerequisites) {
            dependentCount[prerequisite[0]]++;
        }
        Queue<Integer> independents = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (dependentCount[i] == 0) {
                independents.offer(i);
            }
        }

        int independentCount = independents.size();
        while (!independents.isEmpty()) {
            int independent = independents.poll();
            for (int[] prerequisite : prerequisites) {
                if (prerequisite[1] == independent) {
                    if (--dependentCount[prerequisite[0]] == 0) {
                        independentCount++;
                        independents.offer(prerequisite[0]);
                    }
                }
            }
        }
        return independentCount == numCourses;
    }

    // Solution of Choice
    // BFS + Hash Table + Queue / Topological Sort
    // inverse mapping, count down independents
    // beats 51.24%(20 ms for 35 tests)
    public boolean canFinish4(int numCourses, int[][] prerequisites) {
        Map<Integer, Set<Integer> > adjacencyList = new HashMap<>();
        int[] dependentCounts = new int[numCourses];
        for (int[] prerequisite : prerequisites) {
            int dependant = prerequisite[0];
            int depended = prerequisite[1];
            adjacencyList.putIfAbsent(depended, new HashSet<>());
            Set<Integer> dependants = adjacencyList.get(depended);
            if (dependants.add(dependant)) { // avoid duplicate count
                dependentCounts[dependant]++;
            }
        }
        // can also use stack or list
        Queue<Integer> independents = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (dependentCounts[i] == 0) {
                independents.offer(i);
            }
        }
        int dependentCount = numCourses;
        for (; !independents.isEmpty(); dependentCount--) {
            int independent = independents.poll();
            if (adjacencyList.containsKey(independent)) {
                for (int dependant : adjacencyList.get(independent)) {
                    if (--dependentCounts[dependant] == 0) {
                        independents.offer(dependant);
                    }
                }
            }
        }
        return dependentCount == 0;
    }

    void test(int n, int[][] prerequisites, boolean expected) {
        assertEquals(expected, canFinish(n, prerequisites));
        assertEquals(expected, canFinish2(n, prerequisites));
        assertEquals(expected, canFinish3(n, prerequisites));
        assertEquals(expected, canFinish4(n, prerequisites));
    }

    @Test
    public void test1() {
        test(2, new int[][] {{1, 0}, {0, 1}}, false);
        test(2, new int[][] {{1, 0}}, true);
        test(5, new int[][] {{1, 2}, {1, 3}, {2, 4}, {3, 4}}, true);
        test(5, new int[][] {{1, 2}, {1, 3}, {2, 4}, {3, 4}, {4, 1}}, false);
        test(10, new int[][] {{5, 8}, {3, 5}, {1, 9}, {4, 5},
                              {0, 2}, {1, 9}, {7, 8}, {4, 9}}, true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CourseSchedule");
    }
}
