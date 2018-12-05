import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC239: https://leetcode.com/problems/sliding-window-maximum/
//
// Given an array nums, there is a sliding window of size k which is moving from
// the very left of the array to the very right. You can only see the k numbers
// in the window. Each time the sliding window moves right by one position.
public class MaxSlidingWindow {
    // Deque
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

    // Deque
    // just merge two for-loop's above
    // time complexity: O(N), space complexity: O(K)
    // beats 90.51%(19 ms for 18 tests)
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
            if (i < k - 1) continue;

            if (i >= k && maxQ.peekFirst() == nums[i - k]) {
                maxQ.pollFirst();
            }
            res[i - k + 1] = maxQ.peekFirst();
        }
        return res;
    }

    // Solution of Choice
    // Deque
    // almost same as above except for saving max index instead of value
    // time complexity: O(N), space complexity: O(K)
    // beats 74.33%(12 ms for 18 tests)
    public int[] maxSlidingWindow3(int[] nums, int k) {
        int n = nums.length;
        int[] res = new int[n == 0 ? 0 : n - k + 1];
        Deque<Integer> maxQ = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (!maxQ.isEmpty() && maxQ.peekFirst() <= i - k) {
                maxQ.pollFirst();
            }
            while (!maxQ.isEmpty() && nums[maxQ.peekLast()] <= nums[i]) {
                maxQ.pollLast();
            }
            maxQ.offerLast(i);
            if ((i + 1) >= k) {
                res[i + 1 - k] = nums[maxQ.peek()];
            }
        }
        return res;
    }

    // Heap
    // time complexity: O(N * K), space complexity: O(K)
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

    // Solution of Choice
    // Dynamic Programming
    // https://discuss.leetcode.com/topic/26480/o-n-solution-in-java-with-two-simple-pass-in-the-array/2
    // time complexity: O(N), space complexity: O(N)
    // beats 95.00%(13 ms for 18 tests)
    public int[] maxSlidingWindow5(int[] nums, int k) {
        int n = nums.length;
        if (n == 0) return new int[0];

        int[] maxLeft = new int[n];
        int[] maxRight = new int[n];
        maxLeft[0] = nums[0];
        maxRight[n - 1] = nums[n - 1];
        for (int i = 1, j = n - 2; i < n; i++, j--) {
            maxLeft[i] = (i % k == 0) ? nums[i] : Math.max(maxLeft[i - 1], nums[i]);
            maxRight[j] = (j % k == 0) ? nums[j] : Math.max(maxRight[j + 1], nums[j]);
        }
        int[] res = new int[n - k + 1];
        for (int i = 0; i + k <= n; i++) {
            res[i] = Math.max(maxRight[i], maxLeft[i + k - 1]);
        }
        return res;
    }

    // Solution of Choice
    // Circular Array
    // time complexity: O(N), space complexity: O(K)
    // beats 95.63%(8 ms for 18 tests)
    public int[] maxSlidingWindow6(int[] nums, int k) {
        int n = nums.length;
        if (n == 0) return new int[0];

        int[] maxQ = new int[++k];
        int[] res = new int[n - k + 2];
        for (int i = 0, head = 0, tail = 0; i < n; i++) {
            int num = nums[i];
            for (; head != tail && num > maxQ[(tail - 1 + k) % k]; tail--) {}
            maxQ[tail++ % k] = num;
            if (i < k - 2) continue;

            if (i >= k - 1 && maxQ[head % k] == nums[i - k + 1]) {
                head++;
            }
            res[i - k + 2] = maxQ[head % k];
        }
        return res;
    }

    void test(int[] nums, int k, int[] expected) {
        assertArrayEquals(expected, maxSlidingWindow(nums, k));
        assertArrayEquals(expected, maxSlidingWindow2(nums, k));
        assertArrayEquals(expected, maxSlidingWindow3(nums, k));
        assertArrayEquals(expected, maxSlidingWindow4(nums, k));
        assertArrayEquals(expected, maxSlidingWindow5(nums, k));
        assertArrayEquals(expected, maxSlidingWindow6(nums, k));
    }

    @Test
    public void test1() {
        test(new int[] {}, 0, new int[] {});
        test(new int[] {1, -1}, 1, new int[] {1, -1});
        test(new int[] {1, 3, 1, 2, 0, 5}, 3, new int[] {3, 3, 2, 5});
        test(new int[] {1, 3, -1, -3, 5, 3, 6, 7}, 3, new int[] {3, 3, 5, 5, 6, 7});
        test(new int[] {3, 2, 1, -3, 5, 3, 6, 7}, 3, new int[] {3, 2, 5, 5, 6, 7});
        test(new int[] {4, 5, 8, -1, 9, 10, 12, 18, 11, 0, -3, 6}, 4,
             new int[] {8, 9, 10, 12, 18, 18, 18, 18, 11});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
