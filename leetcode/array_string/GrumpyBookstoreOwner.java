import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1052: https://leetcode.com/problems/grumpy-bookstore-owner/
//
// Today, the bookstore owner has a store open for customers.length minutes.  Every minute, some
// number of customers enter the store, and all those customers leave after the end of that minute.
// On some minutes, the bookstore owner is grumpy.  If the bookstore owner is grumpy on the i-th
// minute, grumpy[i] = 1, otherwise grumpy[i] = 0.  When the bookstore owner is grumpy, the
// customers of that minute are not satisfied, otherwise they are satisfied. The bookstore owner
// knows a secret technique to keep themselves not grumpy for X minutes straight, but can only use
// it once. Return the maximum number of customers that can be satisfied throughout the day.
public class GrumpyBookstoreOwner {
    // Sliding Window
    // time complexity: O(N), space complexity: O(1)
    // 2 ms(100%), 41.7 MB(100%) for 78 tests
    public int maxSatisfied(int[] customers, int[] grumpy, int X) {
        int res = 0;
        for (int i = 0; i < X; i++) {
            res += customers[i] * grumpy[i];
        }
        int n = customers.length;
        for (int i = 0, j = X, cur = res; j < n; i++, j++) {
            if (grumpy[i] == 1) {
                cur -= customers[i];
            }
            if (grumpy[j] == 1) {
                cur += customers[j];
            }
            res = Math.max(res, cur);
        }
        for (int i = 0; i < n; i++) {
            res += customers[i] * (1 - grumpy[i]);
        }
        return res;
    }

    // Sliding Window
    // time complexity: O(N), space complexity: O(1)
    // 3 ms(88.40%), 40.7 MB(100%) for 78 tests
    public int maxSatisfied2(int[] customers, int[] grumpy, int X) {
        int satisfied = 0;
        int max = 0;
        for (int i = 0, cur = 0; i < grumpy.length; i++) {
            satisfied += customers[i] * (1 - grumpy[i]);
            cur += grumpy[i] * customers[i];
            if (i >= X) {
                cur -= customers[i - X] * grumpy[i - X];
            }
            max = Math.max(cur, max);
        }
        return satisfied + max;
    }

    void test(int[] customers, int[] grumpy, int X, int expected) {
        assertEquals(expected, maxSatisfied(customers, grumpy, X));
        assertEquals(expected, maxSatisfied2(customers, grumpy, X));
    }

    @Test
    public void test() {
        test(new int[]{1, 0, 1, 2, 1, 1, 7, 5}, new int[]{0, 1, 0, 1, 0, 1, 0, 1}, 3, 16);
        test(new int[]{1, 0, 1, 2, 12, 3, 9, 1, 23, 1, 1, 7, 5, 11, 8},
             new int[]{0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 0, 1, 1}, 4, 77);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
