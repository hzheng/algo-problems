import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1248: https://leetcode.com/problems/count-number-of-nice-subarrays/
//
// Given an array of integers nums and an integer k. A continuous subarray is called nice if there
// are k odd numbers on it.
// Return the number of nice sub-arrays.
//
// Constraints:
// 1 <= nums.length <= 50000
// 1 <= nums[i] <= 10^5
// 1 <= k <= nums.length
public class NumberOfSubarrays {
    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 38 ms(18.73%), 47.9 MB(49.14%) for 38 tests
    public int numberOfSubarrays(int[] nums, int k) {
        int res = 0;
        Map<Integer, Integer> countMap = new HashMap<>();
        countMap.put(0, 1);
        for (int i = 0, oddCount = 0; i < nums.length; i++) {
            oddCount += nums[i] % 2;
            countMap.put(oddCount, countMap.getOrDefault(oddCount, 0) + 1);
            res += countMap.getOrDefault(oddCount - k, 0);
        }
        return res;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 8 ms(90.84%), 47.6 MB(69.19%) for 38 tests
    public int numberOfSubarrays2(int[] nums, int k) {
        int res = 0;
        int n = nums.length;
        int[] count = new int[n + 1];
        count[0] = 1;
        for (int i = 0, oddCount = 0; i < n; i++) {
            oddCount += nums[i] % 2;
            count[oddCount]++;
            res += (oddCount >= k) ? count[oddCount - k] : 0;
        }
        return res;
    }

    // Deque
    // time complexity: O(N), space complexity: O(K)
    // 14 ms(50.46%), 47 MB(98.01%) for 38 tests
    public int numberOfSubarrays3(int[] nums, int k) {
        LinkedList<Integer> oddList = new LinkedList<>();
        oddList.add(-1);
        int res = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] % 2 == 1) {
                oddList.offerLast(i);
            }
            if (oddList.size() > k + 1) {
                oddList.pollFirst();
            }
            if (oddList.size() == k + 1) {
                res += oddList.get(1) - oddList.get(0);
            }
        }
        return res;
    }

    // Two Pointers
    // time complexity: O(N), space complexity: O(1)
    // 7 ms(95.22%), 47.5 MB(78.49%) for 38 tests
    public int numberOfSubarrays4(int[] nums, int k) {
        int res = 0;
        for (int left = 0, right = 0, oddCount = 0, niceCount = 0; right < nums.length; right++) {
            oddCount += nums[right] % 2;
            if (oddCount < k) { continue; }

            if (nums[right] % 2 == 1) {
                for (niceCount = 1; (nums[left++] % 2) == 0; niceCount++) {}
            }
            res += niceCount;
        }
        return res;
    }

    // Two Pointers
    // time complexity: O(N), space complexity: O(1)
    // 8 ms(90.84%), 47.9 MB(49.14%) for 38 tests
    public int numberOfSubarrays5(int[] nums, int k) {
        int res = 0;
        for (int left = 0, right = 0, oddNeeded = k, niceCount = 0; right < nums.length; right++) {
            if (nums[right] % 2 == 1) {
                oddNeeded--;
                niceCount = 0;
            }
            for (; oddNeeded == 0; niceCount++) {
                oddNeeded += nums[left++] & 1;
            }
            res += niceCount;
        }
        return res;
    }

    // Sliding Window
    // time complexity: O(N), space complexity: O(1)
    // 14 ms(50.46%), 47.5 MB(78.49%) for 38 tests
    public int numberOfSubarrays6(int[] nums, int k) {
        return oddAtMost(nums, k) - oddAtMost(nums, k - 1);
    }

    private int oddAtMost(int[] nums, int k) {
        int res = 0;
        for (int left = 0, right = 0, oddNeeded = k; right < nums.length; right++) {
            for (oddNeeded -= nums[right] % 2; oddNeeded < 0; oddNeeded += nums[left++] % 2) {}
            res += right - left + 1; // all new possibilities added by the new right endpoint
        }
        return res;
    }

    private void test(int[] nums, int k, int expected) {
        assertEquals(expected, numberOfSubarrays(nums, k));
        assertEquals(expected, numberOfSubarrays2(nums, k));
        assertEquals(expected, numberOfSubarrays3(nums, k));
        assertEquals(expected, numberOfSubarrays4(nums, k));
        assertEquals(expected, numberOfSubarrays5(nums, k));
        assertEquals(expected, numberOfSubarrays6(nums, k));
    }

    @Test public void test() {
        test(new int[] {1, 1, 2, 1, 1}, 3, 2);
        test(new int[] {2, 4, 6}, 1, 0);
        test(new int[] {2, 2, 2, 1, 2, 2, 1, 2, 2, 2}, 2, 16);
        test(new int[] {45627, 50891, 94884, 11286, 35337, 46414, 62029, 20247, 72789, 89158, 54203,
                        79628, 25920, 16832, 47469, 80909}, 1, 28);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
