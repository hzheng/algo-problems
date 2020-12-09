import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC961: https://leetcode.com/problems/n-repeated-element-in-size-2n-array/
//
// In a array A of size 2N, there are N+1 unique elements, and exactly one of these elements is
// repeated N times. Return the element repeated N times.
//
// Note:
// 4 <= A.length <= 10000
// 0 <= A[i] < 10000
// A.length is even
public class RepeatedNTimes {
    // Set
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 40.4 MB(14.95%) for 102 tests
    public int repeatedNTimes(int[] A) {
        Set<Integer> set = new HashSet<>();
        for (int a : A) {
            if (!set.add(a)) { return a; }
        }
        return -1;
    }

    // Boyer-Moore Majority Vote algorithm
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 40.4 MB(14.95%) for 102 tests
    public int repeatedNTimes2(int[] A) {
        int count1 = 0;
        int cand1 = 0;
        int count2 = 0;
        int cand2 = 0;
        for (int a : A) {
            if (a == cand1) {
                if (++count1 > 1) { return a; }
            } else if (a == cand2) {
                if (++count2 > 1) { return a; }
            } else if (count1 == 0) {
                cand1 = a;
                count1 = 1;
            } else if (count2 == 0) {
                cand2 = a;
                count2 = 1;
            } else {
                count1--;
                count2--;
            }
        }
        count1 = count2 = 0;
        for (int a : A) {
            if (a == cand1) {
                if (++count1 > 1) { return a; }
            } else if (a == cand2) {
                if (++count2 > 1) { return a; }
            }
        }
        return -1;
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 40.2 MB(28.22%) for 102 tests
    public int repeatedNTimes3(int[] A) {
        for (int n = A.length, k = 1; k <= 3; k++) {
            for (int i = k; i < n; i++) {
                if (A[i] == A[i - k]) { return A[i]; }
            }
        }
        return -1;
    }

    // Solution of Choice
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 40.2 MB(28.22%) for 102 tests
    public int repeatedNTimes4(int[] A) {
        for (int n = A.length, i = 2; i < n; i++) {
            if (A[i] == A[i - 1] || A[i] == A[i - 2]) { return A[i]; }
        }
        return A[0];
    }

    // Probability
    // time complexity: Average O(1), space complexity: O(1)
    // 1 ms(53.22%), 40.5 MB(9.82%) for 102 tests
    public int repeatedNTimes5(int[] A) {
        Random r = new Random();
        for (int n = A.length, i = 0, j = 0; ; i = r.nextInt(n), j = r.nextInt(n)) {
            if (i != j && A[i] == A[j]) { return A[i]; }
        }
    }

    private void test(int[] A, int expected) {
        assertEquals(expected, repeatedNTimes(A));
        assertEquals(expected, repeatedNTimes2(A));
        assertEquals(expected, repeatedNTimes3(A));
        assertEquals(expected, repeatedNTimes4(A));
        assertEquals(expected, repeatedNTimes5(A));
    }

    @Test public void test() {
        test(new int[] {1, 2, 3, 3}, 3);
        test(new int[] {1, 2, 3, 1}, 1);
        test(new int[] {2, 1, 2, 5, 3, 2}, 2);
        test(new int[] {5, 1, 5, 2, 5, 3, 5, 4}, 5);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
