import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1013: https://leetcode.com/problems/partition-array-into-three-parts-with-equal-sum/
//
// Given an array A of integers, return true if and only if we can partition the array into three
// non-empty parts with equal sums.
public class ThreePartsEqualSum {
    // Note:
    // 3 <= A.length <= 50000
    // -10000 <= A[i] <= 10000
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(100.00%), 49.5 MB(100%) for 53 tests
    public boolean canThreePartsEqualSum(int[] A) {
        int sum = 0;
        for (int a : A) {
            sum += a;
        }
        if (sum % 3 != 0) {
            return false;
        }

        int target = sum / 3;
        int match = 0;
        int cur = 0;
        for (int a : A) {
            cur += a;
            if (cur != target) {
                continue;
            }
            if (++match == 3) {
                return true;
            }
            cur = 0;
        }
        return false;
    }

    void test(int[] A, boolean expected) {
        assertEquals(expected, canThreePartsEqualSum(A));
    }

    @Test
    public void test() {
        test(new int[]{0, 2, 1, -6, 6, -7, 9, 1, 2, 0, 1}, true);
        test(new int[]{0, 2, 1, -6, 6, 7, 9, -1, 2, 0, 1}, false);
        test(new int[]{3, 3, 6, 5, -2, 2, 5, 1, -9, 4}, true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
