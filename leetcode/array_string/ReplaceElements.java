import org.junit.Test;

import static org.junit.Assert.*;

// LC1299: https://leetcode.com/problems/replace-elements-with-greatest-element-on-right-side/
//
// Given an array arr, replace every element in that array with the greatest element among the
// elements to its right, and replace the last element with -1.
// After doing so, return the array.
//
// Constraints:
// 1 <= arr.length <= 10^4
// 1 <= arr[i] <= 10^5
public class ReplaceElements {
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(99.89%), 40.4 MB(52.63%) for 15 tests
    public int[] replaceElements(int[] arr) {
        int n = arr.length;
        int[] res = new int[n];
        res[n - 1] = -1;
        for (int i = n - 2; i >= 0; i--) {
            res[i] = Math.max(arr[i + 1], res[i + 1]);
        }
        return res;
    }

    private void test(int[] arr, int[] expected) {
        assertArrayEquals(expected, replaceElements(arr));
    }

    @Test public void test() {
        test(new int[] {17, 18, 5, 4, 6, 1}, new int[] {18, 6, 6, 6, 1, -1});
        test(new int[] {400}, new int[] {-1});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
