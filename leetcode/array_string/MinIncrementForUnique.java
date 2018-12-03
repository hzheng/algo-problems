import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC945: https://leetcode.com/problems/minimum-increment-to-make-array-unique/
//
// Given an array of integers A, a move consists of choosing any A[i], and 
// incrementing it by 1.
// Return the least number of moves to make every value in A unique.
// Note:
// 0 <= A.length <= 40000
// 0 <= A[i] < 40000
public class MinIncrementForUnique {
    // Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 44.31%(33 ms for 59 tests)
    public int minIncrementForUnique(int[] A) {
        Arrays.sort(A);
        int repeat = 0;
        int res = 0;
        for (int i = 1, n = A.length; i < n; i++) {
            int diff = A[i] - A[i - 1];
            if (diff == 0) {
                repeat++;
            } else {
                int fill = Math.min(diff - 1, repeat);
                repeat -= fill;
                res += fill * (fill + 1) / 2 + repeat * diff;
            }
        }
        return res += repeat * (repeat + 1) / 2;
    }

    // Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 37.08%(37 ms for 59 tests)
    public int minIncrementForUnique_2(int[] A) {
        Arrays.sort(A);
        int res = 0;
        int taken = 0;
        for (int i = 1; i < A.length; ++i) {
            if (A[i - 1] == A[i]) {
                taken++;
                res -= A[i];
            } else {
                int fill = Math.min(taken, A[i] - A[i - 1] - 1);
                taken -= fill;
                res += fill * (fill + 1) / 2 + fill * A[i - 1];
            }
        }
        if (A.length > 0) {
            res += taken * (taken + 1) / 2 + taken * A[A.length - 1];
        }
        return res;
    }

    // Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 31.12%(41 ms for 59 tests)
    public int minIncrementForUnique2(int[] A) {
        Arrays.sort(A);
        int res = 0;
        int need = 0;
        for (int a : A) {
            res += Math.max(need - a, 0);
            need = Math.max(a, need) + 1;
        }
        return res;
    }

    // Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 51.32%(30 ms for 59 tests)
    public int minIncrementForUnique2_2(int[] A) {
        Arrays.sort(A);
        int res = 0;
        int need = -1;
        for (int a : A) {
            need = Math.max(need, a);
            res += need - a;
            need++;
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(N)
    // beats 80.71%(16 ms for 59 tests)
    public int minIncrementForUnique3(int[] A) {
        int SIZE = 100000;
        int[] count = new int[SIZE];
        for (int a : A) {
            count[a]++;
        }
        int res = 0;
        int taken = 0;
        for (int a = 0; a < SIZE; a++) {
            if (count[a] > 1) {
                taken += count[a] - 1;
                res -= a * (count[a] - 1);
            } else if (taken > 0 && count[a] == 0) {
                taken--;
                res += a;
            }
        }
        return res;
    }

    void test(int[] A, int expected) {
        assertEquals(expected, minIncrementForUnique(A));
        assertEquals(expected, minIncrementForUnique_2(A));
        assertEquals(expected, minIncrementForUnique2(A));
        assertEquals(expected, minIncrementForUnique2_2(A));
        assertEquals(expected, minIncrementForUnique3(A));
    }

    @Test
    public void test() {
        test(new int[] {1, 2, 2}, 1);
        test(new int[] {3, 2, 1, 2, 1, 7}, 6);
        test(new int[] {7, 2, 7, 2, 1, 4, 3, 1, 4, 8}, 16);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
