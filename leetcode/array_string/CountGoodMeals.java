import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1711: https://leetcode.com/problems/count-good-meals/
//
// A good meal is a meal that contains exactly two different food items with a sum of deliciousness
// equal to a power of two. You can pick any two different foods to make a good meal.
// Given an array of integers deliciousness where deliciousness[i] is the deliciousness of the
// ith item of food, return the number of different good meals you can make from this list modulo
// 10^9 + 7.
// Note that items with different indices are considered different even if they have the same
// deliciousness value.
//
// Constraints:
// 1 <= deliciousness.length <= 10^5
// 0 <= deliciousness[i] <= 2^20
public class CountGoodMeals {
    private static final int MOD = 1_000_000_007;

    // Hash Table
    // time complexity: O(N*log(MAX)), space complexity: O(N)
    // 108 ms(82.91%), 48.9 MB(92.59%) for 69 tests
    public int countPairs(int[] deliciousness) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int d : deliciousness) {
            count.put(d, count.getOrDefault(d, 0) + 1);
        }
        long res = 0; // res cannot be int type
        for (int powerOf2 = 1, k = 22; k > 0; k--, powerOf2 <<= 1) {
            for (int d : count.keySet()) {
                long other = count.getOrDefault(powerOf2 - d, 0);
                if (other == 0 || d > powerOf2 - d) { continue; }

                if (d < powerOf2 - d) {
                    res += other * count.get(d);
                } else {
                    res += other * (other - 1) / 2;
                }
                res %= MOD;
            }
        }
        return (int)res;
    }

    // Hash Table
    // time complexity: O(N*log(MAX)), space complexity: O(N)
    // 214 ms(49.64%), 50.7 MB(64.56%) for 69 tests
    public int countPairs2(int[] deliciousness) {
        Map<Integer, Integer> count = new HashMap<>();
        long res = 0;
        for (int num : deliciousness) {
            for (int k = 22, powerOf2 = 1; k > 0; k--, powerOf2 <<= 1) {
                res = (res + count.getOrDefault(powerOf2 - num, 0)) % MOD;
            }
            count.put(num, count.getOrDefault(num, 0) + 1);
        }
        return (int)res;
    }

    // Sort + Two Pointers
    // time complexity: O(N*log(MAX)), space complexity: O(N)
    // 27 ms(100.00%), 53.7 MB(58.59%) for 69 tests
    public int countPairs3(int[] deliciousness) {
        Arrays.sort(deliciousness);
        long res = 0; // res cannot be int type
        for (int powerOf2 = 1, k = 22; k > 0; k--, powerOf2 <<= 1) {
            for (int i = 0, j = deliciousness.length - 1; i < j; ) {
                int diff = deliciousness[i] + deliciousness[j] - powerOf2;
                if (diff < 0) {
                    i++;
                    continue;
                }
                if (diff > 0) {
                    j--;
                    continue;
                }
                long d = deliciousness[i];
                if (d == deliciousness[j]) {
                    res = (res + (j - i + 1L) * (j - i) / 2) % MOD;
                    break;
                }
                int oldI = i;
                for (i++; i < j && deliciousness[i] == d; i++) {}
                int oldJ = j;
                for (j--; oldI < j && deliciousness[j] == deliciousness[oldJ]; j--) {}
                res = (res + ((long)i - oldI) * (oldJ - j)) % MOD;
            }
        }
        return (int)res;
    }

    private void test(int[] deliciousness, int expected) {
        assertEquals(expected, countPairs(deliciousness));
        assertEquals(expected, countPairs2(deliciousness));
        assertEquals(expected, countPairs3(deliciousness));
    }

    @Test public void test() {
        test(new int[] {1, 3, 5, 7, 9}, 4);
        test(new int[] {1, 1, 1, 3, 3, 3, 7}, 15);
        test(new int[] {1048576, 1048576}, 1);
        int[] input = new int[100000];
        Arrays.fill(input, 32);
        test(input, 999949972);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
