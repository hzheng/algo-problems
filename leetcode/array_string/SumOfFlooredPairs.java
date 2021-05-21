import java.util.*;
import java.util.function.Function;

import common.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

// LC1862: https://leetcode.com/problems/sum-of-floored-pairs/
//
// Given an integer array nums, return the sum of floor(nums[i] / nums[j]) for all pairs of indices
// 0 <= i, j < nums.length in the array. Since the answer may be too large, return it modulo
// 10^9 + 7.
// The floor() function returns the integer part of the division.
//
// Constraints:
// 1 <= nums.length <= 10^5
// 1 <= nums[i] <= 10^5
public class SumOfFlooredPairs {
    private static final int MOD = 1000_000_007;

    // Binary Indexed Tree
    // time complexity: O(MAX*log(MAX)*N), space complexity: O(N+MAX)
    // 316 ms(45.59%), 148.4 MB(5.17%) for 51 tests
    public int sumOfFlooredPairs(int[] nums) {
        int max = Arrays.stream(nums).max().getAsInt();
        int[] bit = new int[max + 1];
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) {
            for (int i = num; i <= max; i += (i & -i)) {
                bit[i]++;
            }
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }
        long res = 0;
        for (int num : freq.keySet()) {
            int sum = 0;
            for (int limit = num; limit <= max; limit += num) {
                int count = nums.length;
                for (int i = limit - 1; i > 0; i -= (i & -i)) {
                    count -= bit[i];
                }
                sum = (sum + count) % MOD;
            }
            res = (res + (long)sum * freq.get(num)) % MOD;
        }
        return (int)res;
    }

    // Binary Indexed Tree
    // time complexity: O(MAX*log(MAX)*N), space complexity: O(N+MAX)
    // 284 ms(50.96%), 54.6 MB(31.99%) for 51 tests
    public int sumOfFlooredPairs2(int[] nums) {
        int max = Arrays.stream(nums).max().getAsInt();
        int[] bit = new int[max + 2];
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) {
            for (int i = num + 1; i <= max + 1; i += (i & -i)) {
                bit[i]++;
            }
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }
        long res = 0;
        for (int num : freq.keySet()) {
            long cnt = freq.get(num);
            for (int k = max / num; k > 0; k--) {
                int s = sum(bit, Math.min(max, (k + 1) * num - 1)) - sum(bit, k * num - 1);
                res = (res + cnt * k * s) % MOD;
            }
        }
        return (int)res;
    }

    private int sum(int[] bit, int num) {
        int res = 0;
        for (int i = num + 1; i > 0; i -= (i & -i)) {
            res += bit[i];
        }
        return res;
    }

    // Cumulative Count
    // time complexity: O(MAX*log(MAX)*N), space complexity: O(N+MAX)
    // 287 ms(50.19%), 54.3 MB(33.33%) for 51 tests
    public int sumOfFlooredPairs3(int[] nums) {
        int max = Arrays.stream(nums).max().getAsInt();
        int[] count = new int[max + 1];
        Map<Integer, Integer> freq = new HashMap<>();
        for (int num : nums) {
            count[num]++;
            freq.put(num, freq.getOrDefault(num, 0) + 1);
        }
        for (int i = 1; i <= max; i++) {
            count[i] += count[i - 1];
        }
        long res = 0;
        for (int num : freq.keySet()) {
            long cnt = freq.get(num);
            for (int k = 1, l = num, r = l + num - 1; l <= max; k++, l += num, r += num) {
                res = (res + cnt * k * (count[Math.min(r, max)] - count[l - 1])) % MOD;
            }
        }
        return (int)res;
    }

    private void test(int[] nums, int expected) {
        assertEquals(expected, sumOfFlooredPairs(nums));
        assertEquals(expected, sumOfFlooredPairs2(nums));
        assertEquals(expected, sumOfFlooredPairs3(nums));
    }

    @Test public void test1() {
        test(new int[] {2, 5, 9}, 10);
        test(new int[] {7, 7, 7, 7, 7, 7, 7}, 49);
        test(new int[] {2, 5, 9, 16, 28, 5, 10}, 75);
    }

    private void test(Function<int[], Integer> sumOfFlooredPairs) {
        try {
            String clazz = new Object() {
            }.getClass().getEnclosingClass().getName();
            Scanner scanner = new Scanner(new java.io.File("data/" + clazz));
            while (scanner.hasNextLine()) {
                int[] pairs = Utils.readIntArray(scanner.nextLine());
                int res = sumOfFlooredPairs.apply(pairs);
                int expected = Integer.parseInt(scanner.nextLine());
                assertEquals(expected, res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test public void test2() {
        SumOfFlooredPairs s = new SumOfFlooredPairs();
        test(s::sumOfFlooredPairs);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
