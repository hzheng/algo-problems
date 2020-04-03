import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1394: https://leetcode.com/problems/find-lucky-integer-in-an-array/
//
// Given an array of integers arr, a lucky integer is an integer which has a frequency in the array equal to its value.
// Return a lucky integer in the array. If there are multiple lucky integers return the largest of them. If there is no
// lucky integer return -1.
// Constraints:
// 1 <= arr.length <= 500
// 1 <= arr[i] <= 500
public class FindLucky {
    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(99.90%), 37.9 MB(100%) for 100 tests
    public int findLucky(int[] arr) {
        int[] freq = new int[501];
        for (int a : arr) {
            freq[a]++;
        }
        for (int i = freq.length - 1; i > 0; i--) {
            if (freq[i] == i) return i;
        }
        return -1;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 3 ms(79.06%), 39.3 MB(100%) for 100 tests
    public int findLucky2(int[] arr) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int a : arr) {
            freq.put(a, freq.getOrDefault(a, 0) + 1);
        }
        int res = -1;
        for (int key : freq.keySet()) {
            if (key == freq.get(key)) {
                res = Math.max(res, key);
            }
        }
        return res;
    }

    private void test(int[] arr, int expected) {
        assertEquals(expected, findLucky(arr));
        assertEquals(expected, findLucky2(arr));
    }

    @Test
    public void test() {
        test(new int[]{2, 2, 3, 4}, 2);
        test(new int[]{1, 2, 2, 3, 3, 3}, 3);
        test(new int[]{2, 2, 2, 3, 3}, -1);
        test(new int[]{5}, -1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
