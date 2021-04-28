import java.util.*;

import common.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

// LC1838: https://leetcode.com/problems/frequency-of-the-most-frequent-element/
//
// The frequency of an element is the number of times it occurs in an array.
// You are given an integer array nums and an integer k. In one operation, you can choose an index
// of nums and increment the element at that index by 1.
// Return the maximum possible frequency of an element after performing at most k operations.
//
// Constraints:
// 1 <= nums.length <= 10^5
// 1 <= nums[i] <= 10^5
// 1 <= k <= 10^5
public class MaxFrequency {
    // Sort + Two Pointers
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 26 ms(77.28%), 53.6 MB(61.13%) for 69 tests
    public int maxFrequency(int[] nums, int k) {
        Arrays.sort(nums);
        int n = nums.length;
        int res = 1;
        for (int i = 0, j = 1, cost = 0; j < n; ) {
            int nextCost = cost + (nums[j] - nums[j - 1]) * (j - i);
            if (nextCost <= k) {
                res = Math.max(res, ++j - i);
                cost = nextCost;
            } else {
                cost -= nums[j - 1] - nums[i++];
            }
        }
        return res;
    }

    // Sort + Sliding Window
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 25 ms(95.43%), 53.6 MB(61.13%) for 69 tests
    public int maxFrequency2(int[] nums, int k) {
        Arrays.sort(nums);
        int i = 0;
        int j = 0;
        long sum = k;
        for (int n = nums.length; j < n; j++) {
            sum += nums[j];
            if (sum < (long)nums[j] * (j - i + 1)) {
                sum -= nums[i++];
            }
        }
        return j - i;
    }

    @FunctionalInterface interface Function<A, B, C> {
        C apply(A a, B b);
    }

    private void test(Function<int[], Integer, Integer> maxFrequency) {
        try {
            String clazz = new Object() {
            }.getClass().getEnclosingClass().getName();
            Scanner scanner = new Scanner(new java.io.File("data/" + clazz));
            while (scanner.hasNextLine()) {
                int[] nums = Utils.readIntArray(scanner.nextLine());
                int k = Integer.parseInt(scanner.nextLine());
                int res = maxFrequency.apply(nums, k);
                int expected = Integer.parseInt(scanner.nextLine());
                assertEquals(expected, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test public void test2() {
        MaxFrequency c = new MaxFrequency();
        test(c::maxFrequency);
        test(c::maxFrequency2);
    }

    private void test(int[] nums, int k, int expected) {
        assertEquals(expected, maxFrequency(nums, k));
        assertEquals(expected, maxFrequency2(nums, k));
    }

    @Test public void test1() {
        test(new int[] {1, 2, 4}, 5, 3);
        test(new int[] {1, 4, 8, 13}, 5, 2);
        test(new int[] {3, 9, 6}, 2, 1);
        test(new int[] {3, 9, 41, 2, 18, 7, 21, 26, 13}, 5, 2);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
