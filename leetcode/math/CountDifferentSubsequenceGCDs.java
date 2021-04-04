import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1819: https://leetcode.com/problems/number-of-different-subsequences-gcds/
//
// You are given an array nums that consists of positive integers. The GCD of a sequence of numbers
// is defined as the greatest integer that divides all the numbers in the sequence evenly.
// Return the number of different GCDs among all non-empty subsequences of nums.
//
// Constraints:
// 1 <= nums.length <= 10^5
// 1 <= nums[i] <= 2 * 10^5
public class CountDifferentSubsequenceGCDs {
    // Set
    // time complexity: O(MAX*log(MAX)), space complexity: O(N)
    // 573 ms(57.14%), 56.2 MB(57.14%) for 39 tests
    public int countDifferentSubsequenceGCDs(int[] nums) {
        int max = 0;
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
            max = Math.max(max, num);
        }
        int res = 0;
        for (int factor = 1; factor <= max; factor++) {
            int commonMultiple = 0;
            for (int k = 1, m = max / factor; k <= m && commonMultiple != 1; k++) {
                if (set.contains(factor * k)) {
                    commonMultiple = gcd(commonMultiple, k);
                }
            }
            res += (commonMultiple == 1) ? 1 : 0;
        }
        return res;
    }

    // time complexity: O(N*MAX^1/2+MAX), space complexity: O(MAX)
    // 729 ms(57.14%), 48.5 MB(57.14%) for 39 tests
    public int countDifferentSubsequenceGCDs2(int[] nums) {
        int max = Arrays.stream(nums).max().getAsInt();
        int[] factors = new int[max + 1];
        for (int num : nums) {
            for (int factor = 1; factor * factor <= num; factor++) {
                if (num % factor == 0) {
                    factors[factor] = gcd(factors[factor], num);
                    int factor2 = num / factor;
                    factors[factor2] = gcd(factors[factor2], num);
                }
            }
        }
        int res = 0;
        for (int i = 1; i <= max; i++) {
            res += (factors[i] == i) ? 1 : 0;
        }
        return res;
    }

    private int gcd(int a, int b) {
        return (a == 0) ? b : gcd(b % a, a);
    }

    private void test(int[] nums, int expected) {
        assertEquals(expected, countDifferentSubsequenceGCDs(nums));
        assertEquals(expected, countDifferentSubsequenceGCDs2(nums));
    }

    @Test public void test() {
        test(new int[] {6, 10, 3}, 5);
        test(new int[] {5, 15, 40, 5, 6}, 7);
        test(new int[] {2, 4, 5, 15, 10, 48, 39, 98, 32, 21, 26, 84, 40, 5, 6}, 22);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
