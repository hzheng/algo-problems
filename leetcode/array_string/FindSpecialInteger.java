import org.junit.Test;

import static org.junit.Assert.*;

// LC1287: https://leetcode.com/problems/element-appearing-more-than-25-in-sorted-array/
//
// Given an integer array sorted in non-decreasing order, there is exactly one integer in the array
// that occurs more than 25% of the time.
// Return that integer.
//
// Constraints:
// 1 <= arr.length <= 10^4
// 0 <= arr[i] <= 10^5
public class FindSpecialInteger {
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 39.2 MB(69.59%) for 18 tests
    public int findSpecialInteger(int[] arr) {
        int n = arr.length;
        int target = (n + 1) / 4;
        for (int i = 1, repeat = 1; i < n; i++) {
            if (arr[i] != arr[i - 1]) {
                repeat = 1;
            } else if (++repeat > target) { return arr[i]; }
        }
        return arr[0];
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 44.2 MB(12.54%) for 18 tests
    public int findSpecialInteger2(int[] arr) {
        int target = arr.length / 4;
        for (int i = arr.length - target - 1; ; i--) {
            if (arr[i] == arr[i + target]) { return arr[i]; }
        }
    }

    // Binary Search
    // time complexity: O(log(N)), space complexity: O(1)
    // 0 ms(100.00%), 38.6 MB(99.91%) for 18 tests
    public int findSpecialInteger3(int[] arr) {
        int target = (arr.length + 1) / 4;
        for (int i = 0; ; i += target + 1) {
            int left = findLeftmost(arr, arr[i]);
            int right = findRightmost(arr, arr[i]);
            if (right - left + 1 > target) {return arr[i]; }
        }
    }

    private int findLeftmost(int[] arr, int key) {
        int low = 0;
        for (int high = arr.length - 1; low < high; ) {
            int mid = (low + high) >>> 1;
            if (arr[mid] < key) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    private int findRightmost(int[] arr, int key) {
        int low = 0;
        for (int high = arr.length - 1; low < high; ) {
            int mid = (low + high + 1) >>> 1;
            if (arr[mid] > key) {
                high = mid - 1;
            } else {
                low = mid;
            }
        }
        return low;
    }

    // Binary Search
    // time complexity: O(log(N)), space complexity: O(1)
    // 0 ms(100.00%), 38.6 MB(99.91%) for 18 tests
    public int findSpecialInteger4(int[] arr) {
        int n = arr.length;
        int target = n / 4 + 1;
        for (int i = 0; ; i += target) {
            int left = findLeftmost(arr, arr[i]);
            if (arr[left] == arr[Math.min(left + target - 1, n - 1)]) {return arr[i]; }
        }
    }

    private void test(int[] arr, int expected) {
        assertEquals(expected, findSpecialInteger(arr));
        assertEquals(expected, findSpecialInteger2(arr));
        assertEquals(expected, findSpecialInteger3(arr));
        assertEquals(expected, findSpecialInteger4(arr));
    }

    @Test public void test() {
        test(new int[] {1}, 1);
        test(new int[] {2, 2}, 2);
        test(new int[] {1, 2, 2, 6, 6, 6, 6, 7, 10}, 6);
        test(new int[] {1, 1, 2, 2, 5, 6, 6, 6, 7,8, 8, 8, 8, 10}, 8);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
