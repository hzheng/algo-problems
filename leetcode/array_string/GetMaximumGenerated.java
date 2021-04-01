import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1646: https://leetcode.com/problems/get-maximum-in-generated-array/
//
// You are given an integer n. An array nums of length n + 1 is generated in the following way:
// nums[0] = 0
// nums[1] = 1
// nums[2 * i] = nums[i] when 2 <= 2 * i <= n
// nums[2 * i + 1] = nums[i] + nums[i + 1] when 2 <= 2 * i + 1 <= n
// Return the maximum integer in the array nums.
//
// Constraints:
// 0 <= n <= 100
public class GetMaximumGenerated {
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 35.8 MB(45.63%) for 101 tests
    public int getMaximumGenerated(int n) {
        if (n == 0) { return 0; }

        int[] arr = new int[n + 1];
        arr[1] = 1;
        int res = 1;
        for (int i = 2; i <= n; i++) {
            arr[i] = arr[i / 2];
            if (i % 2 == 1) {
                arr[i] += arr[i / 2 + 1];
                res = Math.max(res, arr[i]);
            }
        }
        return res;
    }

    private void test(int n, int expected) {
        assertEquals(expected, getMaximumGenerated(n));
    }

    @Test public void test() {
        test(7, 3);
        test(2, 1);
        test(3, 2);
        test(0, 0);
        test(1, 1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
