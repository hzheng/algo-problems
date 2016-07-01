import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/course-schedule-ii/
//
// There are a total of n courses you have to take, labeled from 0 to n - 1.
// Some courses may have prerequisites, for example to take course 0 you have to
// first take course 1, which is expressed as a pair: [0,1]
// Given the total number of courses and a list of prerequisite pairs, return
// the ordering of courses you should take to finish all courses.
public class CourseSchedule2 {
    // BFS/Topological-sorting
    // beats 35.40%(23 ms)
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        int[] order = new int[numCourses];
        Map<Integer, Set<Integer> > adjacencyList = new HashMap<>();
        int[] dependentCounts = new int[numCourses];
        for (int[] prerequisite : prerequisites) {
            int dependant = prerequisite[0];
            int depended = prerequisite[1];
            if (!adjacencyList.containsKey(depended)) {
                adjacencyList.put(depended, new HashSet<>());
            }
            Set<Integer> dependants = adjacencyList.get(depended);
            if (!dependants.contains(dependant)) { // avoid duplicate count
                dependants.add(dependant);
                dependentCounts[dependant]++;
            }
        }

        Queue<Integer> independents = new LinkedList<>();
        for (int i = 0; i < numCourses; i++) {
            if (dependentCounts[i] == 0) {
                independents.offer(i);
            }
        }

        int dependentCount = numCourses;
        for (int index = 0; !independents.isEmpty(); dependentCount--) {
            int independent = independents.poll();
            order[index++] = independent;

            if (adjacencyList.containsKey(independent)) {
                for (int dependant : adjacencyList.get(independent)) {
                    if (--dependentCounts[dependant] == 0) {
                        independents.offer(dependant);
                    }
                }
            }
        }
        return (dependentCount == 0) ? order : new int[0];
    }

    // DFS
    // beats 50.28%(15 ms)
    public int[] findOrder2(int numCourses, int[][] prerequisites) {
        int[] order = new int[numCourses];
        Map<Integer, List<Integer> > map = new HashMap<>();
        for (int[] prerequisite : prerequisites) {
            if (map.containsKey(prerequisite[0])) {
                map.get(prerequisite[0]).add(prerequisite[1]);
            } else {
                List<Integer> depended = new ArrayList<>();
                depended.add(prerequisite[1]);
                map.put(prerequisite[0], depended);
            }
        }

        Boolean[] visited = new Boolean[numCourses];
        int[] index = new int[1];
        for (int i = 0; i < numCourses; i++) {
            if (!canFinish(i, map, order, index, visited)) return new int[0];
        }
        return order;
    }

    private boolean canFinish(int course, Map<Integer, List<Integer> > map,
                              int[] order, int[] index, Boolean[] visited) {
        if (visited[course] != null) return visited[course];

        visited[course] = false;
        if (map.containsKey(course)) {
            for (int depended : map.get(course)) {
                if (!canFinish(depended, map, order, index, visited)) return false;
            }
        }
        order[index[0]++] = course;
        visited[course] = true;
        return true;
    }

    void test(int n, int[][] prerequisites, int ... expected) {
        assertArrayEquals(expected, findOrder(n, prerequisites));
        assertArrayEquals(expected, findOrder2(n, prerequisites));
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<Integer, int[][], int[]> findOrder,
              int n, int[][] prerequisites, int[] ... expected) {
        int[] res = findOrder.apply(n, prerequisites);
        for (int[] expectedRes : expected) {
            int i = 0;
            for (; i < n; i++) {
                if (res[i] != expectedRes[i]) break;
            }
            if (i == n) return;
        }

        String msg = Arrays.toString(res) + " doesn't match one of : \n";
        for (int[] expectedRes : expected) {
             msg += Arrays.toString(expectedRes) + "\n";
         }
        fail(msg);
    }

    void test(int n, int[][] prerequisites, int[] ... expected) {
        CourseSchedule2 c = new CourseSchedule2();
        test(c::findOrder, n, prerequisites, expected);
        test(c::findOrder2, n, prerequisites, expected);
    }

    @Test
    public void test1() {
        test(2, new int[][] {{1, 0}, {0, 1}}, new int[0]);
        test(2, new int[][] {{1, 0}}, 0, 1);
        test(5, new int[][] {{1, 2}, {1, 3}, {2, 4}, {3, 4}}, 0, 4, 2, 3, 1);
        test(5, new int[][] {{1, 2}, {1, 3}, {2, 4}, {3, 4}, {4, 1}}, new int[0]);
        test(10, new int[][] {{5, 8}, {3, 5}, {1, 9}, {4, 5},
                              {0, 2}, {1, 9}, {7, 8}, {4, 9}},
             new int[] {2, 6, 8, 9, 0, 5, 7, 1, 3, 4},
             new int[] {2, 0, 9, 1, 8, 5, 3, 4, 6, 7});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CourseSchedule2");
    }
}
