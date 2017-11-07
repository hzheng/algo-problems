import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC630: https://leetcode.com/problems/course-schedule-iii/
//
// There are n different courses numbered from 1 to n. Each course has some
// duration t and closed on dth day. A course should be taken continuously for t
// days and must be finished before or on the dth day. You will start at the 1st
// day. Given n courses represented by pairs (t,d), your task is to find the
// maximal number of courses that can be taken.
// Note:
// The integer 1 <= d, t, n <= 10,000.
// You can't take two courses simultaneously.
public class CourseSchedule3 {
    // Greedy + Sort + Heap
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 60.14%(170 ms for 95 tests)
    public int scheduleCourse(int[][] courses) {
        Arrays.sort(courses, (a, b) -> a[1] - b[1]);
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> b - a);
        int t = 0;
        for (int[] course : courses) {
            t += course[0];
            pq.offer(course[0]);
            if (t > course[1]) {
                t -= pq.poll();
            }
        }
        return pq.size();
    }

    // Greedy + Sort
    // time complexity: O(N * count), space complexity: O(log(N))
    // beats 2.03%(436 ms for 95 tests)
    public int scheduleCourse2(int[][] courses) {
        Arrays.sort(courses, (a, b) -> a[1] - b[1]);
        int count = 0;
        for (int i = 0, t = 0; i < courses.length; i++) {
            int[] course = courses[i];
            if (t + course[0] <= course[1]) {
                t += course[0];
                courses[count++] = courses[i];
            } else {
                int maxIndex = i;
                for (int j = 0; j < count; j++) {
                    if (courses[j][0] > courses[maxIndex][0]) {
                        maxIndex = j;
                    }
                }
                if (courses[maxIndex][0] > course[0]) {
                    t += course[0] - courses[maxIndex][0];
                    courses[maxIndex] = course;
                }
            }
        }
        return count;
    }

    void test(int[][] courses, int expected) {
        assertEquals(expected, scheduleCourse(courses));
        assertEquals(expected, scheduleCourse2(courses));
    }

    @Test
    public void test() {
        test(new int[][] {{7, 17}, {3, 12}, {10, 20}, {9, 10}, {5, 20},
                          {10, 19}, {4, 18}}, 4);
        test(new int[][] {{7, 16}, {2, 3}, {3, 12}, {3, 14}, {10, 19}, {10, 16},
                          {6, 8}, {6, 11}, {3, 13}, {6, 16}}, 4);
        test(new int[][] {{5, 15}, {3, 19}, {6, 7}, {2, 10}, {5, 16}, {8, 14},
                          {10, 11}, {2, 19}}, 5);
        test(new int[][] {{5, 11}, {3, 5}, {10, 20}, {4, 20}, {10, 16}}, 3);
        test(new int[][] {{5, 5}, {4, 6}, {2, 6}}, 2);
        test(new int[][] {{100, 200}, {200, 1300}, {1000, 1250}, {2000, 3200}},
             3);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
