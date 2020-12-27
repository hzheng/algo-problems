import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1700: https://leetcode.com/problems/number-of-students-unable-to-eat-lunch/
//
// The school cafeteria offers circular and square sandwiches at lunch break, referred to by numbers
// 0 and 1 respectively. All students stand in a queue. Each student either prefers square or
// circular sandwiches. The number of sandwiches in the cafeteria is equal to the number of
// students. The sandwiches are placed in a stack. At each step:
// If the student at the front of the queue prefers the sandwich on the top of the stack, they will
// take it and leave the queue.
// Otherwise, they will leave it and go to the queue's end.
// This continues until none of the queue students want to take the top sandwich and are thus unable
// to eat.
// You are given two integer arrays students and sandwiches where sandwiches[i] is the type of the
// ith sandwich in the stack (i = 0 is the top of the stack) and students[j] is the preference of
// the jth student in the initial queue (j = 0 is the front of the queue). Return the number of
// students that are unable to eat.
//
// Constraints:
// 1 <= students.length, sandwiches.length <= 100
// students.length == sandwiches.length
// sandwiches[i] is 0 or 1.
// students[i] is 0 or 1.
public class CountStudents {
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 36.9 MB(100.00%) for 54 tests
    public int countStudents(int[] students, int[] sandwiches) {
        int ones = 0;
        for (int s : students) {
            ones += s;
        }
        int zeros = students.length - ones;
        for (int s : sandwiches) {
            if (s == 0) {
                if (zeros > 0) {
                    zeros--;
                } else { break; }
            } else if (ones > 0) {
                ones--;
            } else { break; }
        }
        return zeros + ones;
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 36.6 MB(100.00%) for 54 tests
    public int countStudents2(int[] students, int[] sandwiches) {
        int[] count = new int[2];
        for (int s : students) {
            count[s]++;
        }
        int n = students.length;
        for (int i = 0; i < n; i++) {
            if (--count[sandwiches[i]] < 0) { return n - i; }
        }
        return 0;
    }

    // Deque
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(100.00%), 36.8 MB(100.00%) for 54 tests
    public int countStudents3(int[] students, int[] sandwiches) {
        Deque<Integer> queue = new LinkedList<>();
        for (int s : students) {
            queue.offerLast(s);
        }
        outer:
        for (int sandwich : sandwiches) {
            for (int i = queue.size(); i > 0; i--) {
                int front = queue.pollFirst();
                if (front == sandwich) { continue outer; }

                queue.offerLast(front);
            }
            return queue.size();
        }
        return 0;
    }

    private void test(int[] students, int[] sandwiches, int expected) {
        assertEquals(expected, countStudents(students, sandwiches));
        assertEquals(expected, countStudents2(students, sandwiches));
        assertEquals(expected, countStudents3(students, sandwiches));
    }

    @Test public void test() {
        test(new int[] {1, 1, 0, 0}, new int[] {0, 1, 0, 1}, 0);
        test(new int[] {1, 1, 1, 0, 0, 1}, new int[] {1, 0, 0, 0, 1, 1}, 3);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
