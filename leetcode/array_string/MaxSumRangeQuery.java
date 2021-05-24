import java.util.*;

import common.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

// LC1589: https://leetcode.com/problems/maximum-sum-obtained-of-any-permutation/
//
// We have an array of integers, nums, and an array of requests where requests[i] = [starti, endi].
// The ith request asks for the sum of nums[starti] + nums[starti + 1] + ... + nums[endi - 1]
// + nums[endi]. Both starti and endi are 0-indexed.
// Return the maximum total sum of all requests among all permutations of nums.
// Since the answer may be too large, return it modulo 10^9 + 7.
//
// Constraints:
// n == nums.length
// 1 <= n <= 10^5
// 0 <= nums[i] <= 10^5
// 1 <= requests.length <= 10^5
// requests[i].length == 2
// 0 <= starti <= endi < n
public class MaxSumRangeQuery {
    private static final int MOD = 1000_000_007;

    // Sort + Cumulative Count
    // time complexity: O(N^log(N)), space complexity: O(N)
    // 1 ms(69.92%), 36.5 MB(60.16%) for 96 tests
    public int maxSumRangeQuery(int[] nums, int[][] requests) {
        int n = nums.length;
        int[] count = new int[n + 1];
        for (int[] req : requests) {
            count[req[0]]++;
            count[req[1] + 1]--;
        }
        for (int i = 1; i < n; i++) {
            count[i] += count[i - 1];
        }
        Arrays.sort(nums);
        Arrays.sort(count);
        long res = 0;
        for (int i = 0; i < n; i++) {
            res += (long)nums[i] * count[i + 1];
        }
        return (int)(res % MOD);
    }

    private void test(int[] nums, int[][] requests, int expected) {
        assertEquals(expected, maxSumRangeQuery(nums, requests));
    }

    @Test public void test1() {
        test(new int[] {1, 2, 3, 4, 5}, new int[][] {{1, 3,}, {0, 1}}, 19);
        test(new int[] {1, 2, 3, 4, 5, 6}, new int[][] {{0, 1}}, 11);
        test(new int[] {1, 2, 3, 4, 5, 10}, new int[][] {{0, 2}, {1, 3}, {1, 1}}, 47);
        test(new int[] {1, 8, 5, 5, 2}, new int[][] {{4, 4}, {3, 4}, {4, 4}, {2, 4}, {0, 0}}, 49);
    }

    @FunctionalInterface interface Function<A, B, C> {
        C apply(A a, B b);
    }

    private void test(Function<int[], int[][], Integer> maxSumRangeQuery) {
        try {
            String clazz = new Object() {
            }.getClass().getEnclosingClass().getName();
            Scanner scanner = new Scanner(new java.io.File("data/" + clazz));
            while (scanner.hasNextLine()) {
                int[] nums = Utils.readIntArray(scanner.nextLine());
                int[][] query = Utils.readInt2Array(scanner.nextLine());
                int res = maxSumRangeQuery.apply(nums, query);
                int expected = Integer.parseInt(scanner.nextLine());
                assertEquals(expected, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test public void test2() {
        MaxSumRangeQuery m = new MaxSumRangeQuery();
        test(m::maxSumRangeQuery);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
