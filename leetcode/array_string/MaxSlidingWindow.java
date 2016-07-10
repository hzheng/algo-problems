import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/sliding-window-maximum/
//
// Given an array nums, there is a sliding window of size k which is moving from
// the very left of the array to the very right. You can only see the k numbers
// in the window. Each time the sliding window moves right by one position.
public class MaxSlidingWindow {
    // time complexity: O(N), space complexity: O(K)
    // beats 70.46%(28 ms)
    public int[] maxSlidingWindow(int[] nums, int k) {
        int n = nums.length;
        if (n == 0) return new int[0];

        Deque<Integer> maxQ = new LinkedList<>();
        int[] res = new int[n - k + 1];
        maxQ.offer(nums[k - 1]);
        for (int i = k - 2; i >= 0; i--) {
            if (nums[i] >= maxQ.peekFirst()) {
                maxQ.offerFirst(nums[i]);
            }
        }
        res[0] = maxQ.peekFirst();
        for (int i = k; i < n; i++) {
            int cur = nums[i];
            while (!maxQ.isEmpty() && cur > maxQ.peekLast()) {
                maxQ.pollLast();
            }
            maxQ.offerLast(cur);
            if (nums[i - k] == maxQ.peekFirst()) {
                maxQ.pollFirst();
            }
            res[i - k + 1] = maxQ.peekFirst();
        }
        return res;
    }

    // just merge two for-loop's above
    // time complexity: O(N), space complexity: O(K)
    // beats 61.38%(29 ms)
    public int[] maxSlidingWindow2(int[] nums, int k) {
        int n = nums.length;
        if (n == 0) return new int[0];

        int[] res = new int[n - k + 1];
        Deque<Integer> maxQ = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            while ((!maxQ.isEmpty() && num > maxQ.peekLast())) {
                maxQ.pollLast();
            }
            maxQ.offerLast(num);
            if (i + 1 > k && maxQ.peekFirst() == nums[i - k]) {
                maxQ.pollFirst();
            }
            if (i + 1 >= k) {
                res[i - k + 1] = maxQ.peekFirst();
            }
        }
        return res;
    }

    // almost same, except save max index instead of value
    // time complexity: O(N), space complexity: O(K)
    // beats 42.32%(31 ms)
    public int[] maxSlidingWindow3(int[] nums, int k) {
        int n = nums.length;
        if (n == 0) return new int[0];

        int[] res = new int[n - k + 1];
        Deque<Integer> maxQ = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (!maxQ.isEmpty() && maxQ.peekFirst() == i - k) {
                maxQ.poll();
            }
            while (!maxQ.isEmpty() && nums[maxQ.peekLast()] < nums[i]) {
                maxQ.removeLast();
            }
            maxQ.offerLast(i);
            if ((i + 1) >= k) {
                res[i + 1 - k] = nums[maxQ.peek()];
            }
        }
        return res;
    }

    // time complexity: O(N * log(K)), space complexity: O(K)
    // beats 19.56%(71 ms)
    public int[] maxSlidingWindow4(int[] nums, int k) {
        int n = nums.length;
        if (n == 0) return new int[0];

        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        int[] res = new int[n - k + 1];
        for (int i = 0; i < n; i++) {
            if (i >= k) {
                pq.remove(nums[i - k]);
            }
            pq.offer(nums[i]);
            if (i + 1 >= k) {
                res[i + 1 - k] = pq.peek();
            }
        }
        return res;
    }

    // https://discuss.leetcode.com/topic/26480/o-n-solution-in-java-with-two-simple-pass-in-the-array/2
    // time complexity: O(N), space complexity: O(N)
    // beats 92.61%(11 ms)
    public int[] maxSlidingWindow5(int[] nums, int k) {
        int n = nums.length;
        if (n == 0) return new int[0];

        int[] maxLeft = new int[n];
        int[] maxRight = new int[n];
        maxLeft[0] = nums[0];
        maxRight[n - 1] = nums[n - 1];
        for (int i = 1; i < n; i++) {
            maxLeft[i] = (i % k == 0) ? nums[i] : Math.max(maxLeft[i - 1], nums[i]);

            int j = n - i - 1;
            maxRight[j] = (j % k == 0) ? nums[j] : Math.max(maxRight[j + 1], nums[j]);
        }

        int[] res = new int[n - k + 1];
        for (int i = 0; i + k <= n; i++) {
            res[i] = Math.max(maxRight[i], maxLeft[i + k - 1]);
        }
        return res;
    }

    void test(int[] nums, int k, int[] expected) {
        assertArrayEquals(expected, maxSlidingWindow(nums, k));
        assertArrayEquals(expected, maxSlidingWindow2(nums, k));
        assertArrayEquals(expected, maxSlidingWindow3(nums, k));
        assertArrayEquals(expected, maxSlidingWindow4(nums, k));
        assertArrayEquals(expected, maxSlidingWindow5(nums, k));
    }

    @Test
    public void test1() {
        test(new int[] {1, 3, 1, 2, 0, 5}, 3, new int[] {3, 3, 2, 5});
        test(new int[] {1, 3, -1, -3, 5, 3, 6, 7}, 3, new int[] {3, 3, 5, 5, 6, 7});
        test(new int[] {3, 2, 1, -3, 5, 3, 6, 7}, 3, new int[] {3, 2, 5, 5, 6, 7});
        test(new int[] {4, 5, 8, -1, 9, 10, 12, 18, 11, 0, -3, 6}, 4,
             new int[] {8, 9, 10, 12, 18, 18, 18, 18, 11});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxSlidingWindow");
    }
}
