import java.util.*;

import javax.naming.directory.InvalidSearchFilterException;

import org.junit.Test;
import static org.junit.Assert.*;

// LC896: https://leetcode.com/problems/monotonic-array/
//
// Return true if and only if the given array A is monotonic.
public class MonotonicArray {
    // time complexity: O(N), space complexity: O(1)
    // beats %(18 ms for 366 tests)
    public boolean isMonotonic(int[] A) {
        Boolean increasing = null;
        for (int i = 1; i < A.length; i++) {
            if (A[i] > A[i - 1]) {
                if (increasing == null) {
                    increasing = true;
                } else if (!increasing) return false;
            } else if (A[i] < A[i - 1]) {
                if (increasing == null) {
                    increasing = false;
                } else if (increasing) return false;
            }
        }
        return true;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats %(19 ms for 366 tests)
    public boolean isMonotonic2(int[] A) {
        int monotone = 0;
        for (int i = 1; i < A.length; ++i) {
            int c = Integer.compare(A[i], A[i - 1]);
            if (c != 0) {
                if (c != monotone && monotone != 0) return false;

                monotone = c;
            }
        }
        return true;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats %(20 ms for 366 tests)
    public boolean isMonotonic3(int[] A) {
        boolean increase = true;
        boolean decrease = true;
        for (int i = 1; i < A.length; i++) {
            increase &= (A[i - 1] <= A[i]);
            decrease &= (A[i - 1] >= A[i]);
            // or:
            /*
            if (A[i] > A[i - 1]) {
                decrease = false;
            } else if (A[i] < A[i - 1]) {
                increase = false;
            }*/
        }
        return increase || decrease;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats %(15 ms for 366 tests)
    public boolean isMonotonic4(int[] A) {
        boolean increase = true;
        for (int i = 1; i < A.length; i++) {
            if (A[i] < A[i - 1]) {
                increase = false;
                break;
            }
        }
        if (increase) return true;

        for (int i = 1; i < A.length; i++) {
            if (A[i] > A[i - 1]) return false;
        }
        return true;
    }

    void test(int[] A, boolean expected) {
        assertEquals(expected, isMonotonic(A));
        assertEquals(expected, isMonotonic2(A));
        assertEquals(expected, isMonotonic3(A));
        assertEquals(expected, isMonotonic4(A));
    }

    @Test
    public void test() {
        test(new int[] { 1, 2, 2, 3 }, true);
        test(new int[] { 6, 5, 4, 4 }, true);
        test(new int[] { 1, 3, 2 }, false);
        test(new int[] { 1, 2, 4, 5 }, true);
        test(new int[] { 1, 1, 1 }, true);
        test(new int[] { 1 }, true);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
