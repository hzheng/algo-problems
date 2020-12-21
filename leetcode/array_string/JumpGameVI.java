import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1696: https://leetcode.com/problems/jump-game-vi/
//
// You are given a 0-indexed integer array nums and an integer k. You are initially standing at
// index 0. In one move, you can jump at most k steps forward without going outside the boundaries
// of the array. That is, you can jump from index i to any index in the range
// [i + 1, min(n - 1, i + k)] inclusive. You want to reach the last index of the array(index n - 1).
// Your score is the sum of all nums[j] for each index j you visited in the array.
// Return the maximum score you can get.
//
// Constraints:
// 1 <= nums.length, k <= 10^5
// -10^4 <= nums[i] <= 10^4
public class JumpGameVI {
    // Deque (monoqueue) + Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // 15 ms(100.00%), 53.2 MB(60.00%) for 58 tests
    public int maxResult(int[] nums, int k) {
        int n = nums.length;
        int[] dp = new int[n];
        dp[0] = nums[0];
        LinkedList<Integer> maxQ = new LinkedList<>();
        maxQ.offer(0);
        for (int i = 1; i < n; i++) {
            dp[i] = nums[i] + dp[maxQ.peekFirst()];
            while (!maxQ.isEmpty() && dp[maxQ.peekLast()] <= dp[i]) {
                maxQ.pollLast();
            }
            maxQ.offerLast(i);
            if (i - maxQ.peekFirst() + 1 > k) {
                maxQ.pollFirst();
            }
        }
        return dp[n - 1];
    }

    // Heap
    // time complexity: O(N*log(K)), space complexity: O(K)
    // 46 ms(60.00%), 52.4 MB(80.00%) for 58 tests
    public int maxResult2(int[] nums, int k) {
        if (nums.length == 1) { return nums[0]; }

        int res = Integer.MIN_VALUE;
        int n = nums.length;
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[1] - a[1]);
        pq.offer(new int[] {n - 1, nums[n - 1]});
        for (int i = n - 2; i >= 0; i--) {
            while (pq.peek()[0] > i + k) {
                pq.poll();
            }
            res = nums[i] + pq.peek()[1];
            pq.offer(new int[] {i, res});
        }
        return res;
    }

    private void test(int[] nums, int k, int expected) {
        assertEquals(expected, maxResult(nums, k));
        assertEquals(expected, maxResult2(nums, k));
    }

    @Test public void test() {
        test(new int[] {1, -1, -2, 4, -7, 3}, 2, 7);
        test(new int[] {10, -5, -2, 4, 0, 3}, 3, 17);
        test(new int[] {1, -5, -20, 4, -1, 3, -6, -3}, 2, 0);
        test(new int[] {1, -1, -2, -3, -7, 3}, 3, 2);
        test(new int[] {1}, 3, 1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
