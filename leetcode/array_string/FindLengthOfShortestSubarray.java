import org.junit.Test;

import static org.junit.Assert.*;

// LC1574: https://leetcode.com/problems/shortest-subarray-to-be-removed-to-make-array-sorted/
//
// Given an integer array arr, remove a subarray (can be empty) from arr such that the remaining
// elements in arr are non-decreasing.
// A subarray is a contiguous subsequence of the array.
// Return the length of the shortest subarray to remove.
//
// Constraints:
// 1 <= arr.length <= 10^5
// 0 <= arr[i] <= 10^9
public class FindLengthOfShortestSubarray {
    // Binary Search
    // time complexity: O(N*log(N)), space complexity: O(1)
    // 2 ms(28.77%), 56.2 MB(99.45%) for 117 tests
    public int findLengthOfShortestSubarray(int[] arr) {
        int n = arr.length;
        int firstTurn = -1;
        for (int i = 0; i < n - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                firstTurn = i + 1;
                break;
            }
        }
        if (firstTurn < 0) { return 0; }

        int secondTurn = n - 1;
        for (int i = n - 2; i >= 0; i--) {
            if (arr[i] > arr[i + 1]) {
                secondTurn = i;
                break;
            }
        }
        int res = Math.min(n - firstTurn, secondTurn + 1);
        for (int i = 0; i < firstTurn; i++) {
            int index = binarySearch(arr, secondTurn + 1, n, arr[i]);
            res = Math.min(res, index - i - 1);
        }
        for (int i = n - 1; i > secondTurn; i--) {
            int index = binarySearch(arr, 0, firstTurn, arr[i] + 1);
            res = Math.min(res, i - index);
        }
        return res;
    }

    private int binarySearch(int[] arr, int start, int end, int target) {
        int low = start;
        for (int high = end; low < high; ) {
            int mid = (low + high) >>> 1;
            if (arr[mid] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    // Sliding Window
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(100.00%), 56.4 MB(86.30%) for 117 tests
    public int findLengthOfShortestSubarray2(int[] arr) {
        int n = arr.length;
        int left = 0;
        for (; left < n - 1 && arr[left] <= arr[left + 1]; left++) {}
        if (left == n - 1) { return 0; }

        int right = n - 1;
        for (; right > left && arr[right - 1] <= arr[right]; right--) {}
        int res = Math.min(n - left - 1, right);
        for (int i = 0, j = right; i <= left && j < n; ) {
            if (arr[j] >= arr[i]) {
                res = Math.min(res, j - ++i);
            } else {
                j++;
            }
        }
        return res;
    }

    // Solution of Choice
    // Sliding Window
    // time complexity: O(N), space complexity: O(1)
    // 2 ms(28.77%), 56.9 MB(27.12%) for 117 tests
    public int findLengthOfShortestSubarray3(int[] arr) {
        int n = arr.length;
        int right = n - 1;
        for (; right > 0 && arr[right - 1] <= arr[right]; right--) {}
        int res = right;
        for (int i = 0; i < right && (i == 0 || arr[i] >= arr[i - 1]); i++) {
            if (right == n || arr[i] <= arr[right]) {
                res = Math.min(res, right - i - 1);
            } else {
                right++;
            }
        }
        return res;
    }

    private void test(int[] arr, int expected) {
        assertEquals(expected, findLengthOfShortestSubarray(arr));
        assertEquals(expected, findLengthOfShortestSubarray2(arr));
        assertEquals(expected, findLengthOfShortestSubarray3(arr));
    }

    @Test public void test1() {
        test(new int[] {1, 2, 3, 10, 4, 2, 3, 5}, 3);
        test(new int[] {5, 4, 3, 2, 1}, 4);
        test(new int[] {1, 2, 3}, 0);
        test(new int[] {1}, 0);
        test(new int[] {1, 2, 3, 10, 4, 2, 2, 3, 3, 5}, 3);
        test(new int[] {6, 3, 10, 11, 15, 20, 13, 3, 18, 12}, 8);
        test(new int[] {100, 80, 70, 90, 95, 72, 63}, 6);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
