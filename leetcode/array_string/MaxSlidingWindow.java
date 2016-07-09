import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/sliding-window-maximum/
//
// Given an array nums, there is a sliding window of size k which is moving from
// the very left of the array to the very right. You can only see the k numbers
// in the window. Each time the sliding window moves right by one position.
public class MaxSlidingWindow {
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

    void test(int[] nums, int k, int[] expected) {
        assertArrayEquals(expected, maxSlidingWindow(nums, k));
    }

    @Test
    public void test1() {
        test(new int[]{1, 3, 1, 2, 0, 5}, 3, new int[]{3, 3, 2, 5});
        test(new int[] {1, 3, -1, -3, 5, 3, 6, 7}, 3, new int[] {3, 3, 5, 5, 6, 7});
        test(new int[] {3, 2, 1, -3, 5, 3, 6, 7}, 3, new int[] {3, 2, 5, 5, 6, 7});
        test(new int[] {4, 5, 8, -1, 9, 10, 12, 18, 11, 0, -3, 6}, 4,
             new int[] {8, 9, 10, 12, 18, 18, 18, 18, 11});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxSlidingWindow");
    }
}
