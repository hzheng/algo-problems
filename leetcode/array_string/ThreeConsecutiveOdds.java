import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1550: https://leetcode.com/problems/three-consecutive-odds/
//
// Given an integer array arr, return true if there are three consecutive odd numbers in the array.
// Otherwise, return false.
// Constraints:
// 1 <= arr.length <= 1000
// 1 <= arr[i] <= 1000
public class ThreeConsecutiveOdds {
    // time complexity: O(N)), space complexity: O(1)
    // 0 ms(100%), 38.8 MB(12.51%) for 32 tests
    public boolean threeConsecutiveOdds(int[] arr) {
        int count = 0;
        for (int a : arr) {
            if (a % 2 == 0) {
                count = 0;
            } else if (++count >= 3) { return true; }
        }
        return false;
    }

    private void test(int[] arr, boolean expected) {
        assertEquals(expected, threeConsecutiveOdds(arr));
    }

    @Test public void test() {
        test(new int[] {2, 6, 4, 1}, false);
        test(new int[] {1, 2, 34, 3, 4, 5, 7, 23, 12}, true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
