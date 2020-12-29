import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1703: https://leetcode.com/problems/minimum-adjacent-swaps-for-k-consecutive-ones/
//
// You are given an integer array, nums, and an integer k. nums comprises of only 0's and 1's. In
// one move, you can choose two adjacent indices and swap their values.
// Return the minimum number of moves required so that nums has k consecutive 1's.
//
// Constraints:
// 1 <= nums.length <= 10^5
// nums[i] is 0 or 1.
// 1 <= k <= sum(nums)
public class MinSwapsForKOnes {
    // Sliding Window
    // time complexity: O(N), space complexity: O(N)
    // 11 ms(85.71%), 56.4 MB(39.10%) for 115 tests
    public int minMoves(int[] nums, int k) {
        List<Integer> ones = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 1) {
                ones.add(i);
            }
        }
        int n = ones.size();
        int[] shifts = new int[n];
        for (int i = 0; i < n; i++) {
            shifts[i] = ones.get(i) - i;
        }
        int mid = k / 2;
        int leftSize = mid;
        int rightSize = k - 1 - leftSize;
        int leftShift = 0;
        for (int i = 0; i < mid; i++) {
            leftShift += shifts[mid] - shifts[i];
        }
        int rightShift = 0;
        for (int i = mid + 1; i < k; i++) {
            rightShift += shifts[i] - shifts[mid];
        }
        int res = leftShift + rightShift;
        for (int i = 1; i <= n - k; i++) {
            leftShift -= shifts[i + mid - 1] - shifts[i - 1];
            int shiftDiff = shifts[i + mid] - shifts[i + mid - 1];
            leftShift += shiftDiff * leftSize;
            rightShift -= shiftDiff * rightSize;
            rightShift += shifts[i + k - 1] - shifts[i + mid];
            res = Math.min(leftShift + rightShift, res);
        }
        return res;
    }

    // Sliding Window
    // time complexity: O(N), space complexity: O(N)
    // 11 ms(85.71%), 56.4 MB(42.86%) for 115 tests
    public int minMoves2(int[] nums, int k) {
        List<Integer> ones = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 1) {
                ones.add(i);
            }
        }
        int res = Integer.MAX_VALUE;
        int n = ones.size();
        int[] sum = new int[n + 1];
        for (int i = 0; i < n; i++) {
            sum[i + 1] = sum[i] + ones.get(i);
        }
        for (int i = 0; i < n - k + 1; i++) {
            res = Math.min(res, sum[i + k] - sum[i + k / 2] - sum[i + (k + 1) / 2] + sum[i]);
        }
        return res - (k / 2) * ((k + 1) / 2);
    }

    // Sliding Window + Deque
    // time complexity: O(N), space complexity: O(N)
    // 28 ms(36.09%), 51.1 MB(77.44%) for 115 tests
    public int minMoves3(int[] nums, int k) {
        // the count of 0's that are closer to the left (with number of 1's as the distance)
        Deque<Integer> leftQueue = new LinkedList<>();
        for (int i = k / 2; i > 0; i--) {
            leftQueue.offerLast(0);
        }
        // the count of 0's that are closer to the right (with number of 1's as the distance)
        Deque<Integer> rightQueue = new LinkedList<>();
        for (int i = (k - 1) / 2; i > 0; i--) {
            rightQueue.offerLast(0);
        }
        int res = Integer.MAX_VALUE;
        for (int i = 0, j = 0, running0s = 0, ones = 0, left0s = 0, right0s = 0, sum = 0;
             j < nums.length; j++) {
            if (nums[j] == 0) {
                running0s++;
            } else {
                ones++;
                rightQueue.offerLast(running0s);
                right0s += running0s;
                running0s = 0;
                int shifting0s = rightQueue.pollFirst();
                leftQueue.offerLast(shifting0s);
                left0s += shifting0s;
                sum += shifting0s * (k / 2) - left0s; // update left sum
                sum += right0s - shifting0s * ((k - 1) / 2); // update right sum
                left0s -= leftQueue.pollFirst();
                right0s -= shifting0s;
            }
            for (; ones > k; ones -= nums[i++]) {}
            for (; nums[i] == 0; i++) {}
            if (ones == k) {
                res = Math.min(res, sum);
            }
        }
        return res;
    }

    private void test(int[] nums, int k, int expected) {
        assertEquals(expected, minMoves(nums, k));
        assertEquals(expected, minMoves2(nums, k));
        assertEquals(expected, minMoves3(nums, k));
    }

    @Test public void test() {
        test(new int[] {1, 0, 0, 1, 0, 1}, 2, 1);
        test(new int[] {1, 0, 0, 0, 0, 0, 1, 1}, 3, 5);
        test(new int[] {1, 1, 0, 1}, 2, 0);
        test(new int[] {1, 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0, 1}, 4, 4);
        test(new int[] {1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1}, 5, 2);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
