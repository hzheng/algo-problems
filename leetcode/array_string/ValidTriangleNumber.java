import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC611: https://leetcode.com/problems/valid-triangle-number/
//
// Given an array consists of non-negative integers, your task is to count the
// number of triplets chosen from the array that can make triangles if we take
// them as side lengths of a triangle.
public class ValidTriangleNumber {
    // Sort + Binary Search
    // time complexity: O(N ^ 2 * log(N)), space complexity: O(log(N))
    // beats 24.93%(63 ms for 220 tests)
    public int triangleNumber(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        int res = 0;
        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                int a = nums[i];
                int b = nums[j];
                if (a == 0 || b == 0) continue;

                int max = a + b;
                int maxPos = Arrays.binarySearch(nums, j + 1, n, max);
                if (maxPos < 0) {
                    maxPos = -maxPos - 1;
                }
                for (; maxPos < n && nums[maxPos] >= max; maxPos--) {}
                res += Math.min(n - 1, maxPos) - j;
            }
        }
        return res;
    }

    // Sort + Binary Search
    // time complexity: O(N ^ 2 * log(N)), space complexity: O(N)
    // beats 9.62%(141 ms for 220 tests)
    public int triangleNumber_2(int[] nums) {
        double[] numArray = Arrays.stream(nums).asDoubleStream().toArray();
        Arrays.sort(numArray);
        int n = nums.length;
        int res = 0;
        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                int a = (int)numArray[i];
                int b = (int)numArray[j];
                if (a == 0 || b == 0) continue;

                int pos =
                    -Arrays.binarySearch(numArray, j + 1, n, a + b - 0.5) - 1;
                res += Math.max(0, pos - j - 1);
            }
        }
        return res;
    }

    // Sort + Binary Search
    // time complexity: O(N ^ 2 * log(N)), space complexity: O(log(N))
    // beats 34.19%(33 ms for 220 tests)
    public int triangleNumber2(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        int res = 0;
        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1 && nums[i] != 0; j++) {
                int k = binarySearch(nums, i + 2, n - 1, nums[i] + nums[j]);
                res += k - j - 1;
            }
        }
        return res;
    }

    private int binarySearch(int[] nums, int low, int high, int x) {
        while (low <= high && high < nums.length) {
            int mid = (low + high) >>> 1;
            if (nums[mid] >= x) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    // Sort + Linear Scan
    // time complexity: O(N ^ 2), space complexity: O(log(N))
    // beats 39.96%(30 ms for 220 tests)
    public int triangleNumber3(int[] nums) {
        Arrays.sort(nums);
        int n = nums.length;
        int res = 0;
        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1, k = j + 1; j < n - 1 && nums[i] != 0; j++) {
                for (; k < n && nums[i] + nums[j] > nums[k]; k++) {}
                res += k - j - 1;
            }
        }
        return res;
    }

    // Sort + Three Pointers
    // time complexity: O(N ^ 2), space complexity: O(log(N))
    // beats 39.96%(30 ms for 220 tests)
    public int triangleNumber4(int[] nums) {
        int res = 0;
        Arrays.sort(nums);
        for (int i = 2; i < nums.length; i++) {
            for (int left = 0, right = i - 1; left < right; ) {
                if (nums[left] + nums[right] > nums[i]) {
                    res += (right - left);
                    right--;
                }
                else {
                    left++;
                }
            }
        }
        return res;
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, triangleNumber(nums));
        assertEquals(expected, triangleNumber_2(nums));
        assertEquals(expected, triangleNumber2(nums));
        assertEquals(expected, triangleNumber3(nums));
        assertEquals(expected, triangleNumber4(nums));
    }

    @Test
    public void test() {
        test(new int[] {1, 2, 3, 4, 5, 6}, 7);
        test(new int[] {2, 2, 3, 4}, 3);
        test(new int[] {66, 99, 36, 44, 26, 99, 32, 64, 19, 69}, 65);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
