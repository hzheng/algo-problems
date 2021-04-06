import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1648: https://leetcode.com/problems/sell-diminishing-valued-colored-balls/
//
// You have an inventory of different colored balls, and there is a customer that wants orders balls
// of any color. The customer weirdly values the colored balls. Each colored ball's value is the
// number of balls of that color you currently have in your inventory. For example, if you own 6
// yellow balls, the customer would pay 6 for the first yellow ball. After the transaction, there
// are only 5 yellow balls left, so the next yellow ball is then valued at 5 (i.e., the value of the
// balls decreases as you sell more to the customer). You are given an integer array, inventory,
// where inventory[i] represents the number of balls of the ith color that you initially own. You
// are also given an integer orders, which represents the total number of balls that the customer
// wants. You can sell the balls in any order.
// Return the maximum total value that you can attain after selling orders colored balls. As the
// answer may be too large, return it modulo 10^9 + 7.
//
// Constraints:
// 1 <= inventory.length <= 10^5
// 1 <= inventory[i] <= 10^9
// 1 <= orders <= min(sum(inventory[i]), 10^9)
public class DiminishingValuedColoredBalls {
    private static final int MOD = 1_000_000_007;

    // Greedy + SortedMap
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 139 ms(5.05%), 51.1 MB(94.68%) for 94 tests
    public int maxProfit(int[] inventory, int orders) {
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (int i : inventory) {
            map.put(i, map.getOrDefault(i, 0) + 1);
        }
        long res = 0;
        for (int need = orders, offer; need > 0; need -= offer) {
            var cur = map.pollLastEntry();
            int count = cur.getValue();
            int num = cur.getKey();
            int nextNum = map.isEmpty() ? 0 : map.lastKey();
            offer = Math.min(need, (num - nextNum) * count);
            long share = offer / count;
            res += (num + num - share + 1) * share / 2 * count;
            num -= share;
            res += num * (offer - share * count);
            map.put(num, map.getOrDefault(num, 0) + count);
        }
        return (int)(res % MOD);
    }

    // Greedy + Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 23 ms(77.51%), 53.4 MB(51.72%) for 94 tests
    public int maxProfit2(int[] inventory, int orders) {
        Arrays.sort(inventory);
        long res = 0;
        for (int i = inventory.length - 1, count = 1, need = orders; need > 0; i--, count++) {
            int cur = inventory[i];
            int prev = (i > 0) ? inventory[i - 1] : 0;
            if (i > 0 && cur > prev && need >= count * (cur - prev)) {
                res += count * sum(cur, prev);
                need -= count * (cur - prev);
            } else if (i == 0 || cur > prev) {
                long last = cur - need / count;
                res += count * sum(cur, last) + need % count * last;
                break;
            }
        }
        return (int)(res % MOD);
    }

    private long sum(long start, long end) {
        return start * (start + 1) / 2 - end * (end + 1) / 2;
    }

    // Greedy + Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 26 ms(55.82%), 53.2 MB(74.07%) for 94 tests
    public int maxProfit3(int[] inventory, int orders) {
        Arrays.sort(inventory);
        long res = 0;
        for (int i = inventory.length - 1, colors = 1, need = orders; need > 0; i--, colors++) {
            int cur = inventory[i];
            int prev = (i > 0) ? inventory[i - 1] : 0;
            long rounds = Math.min(need / colors, cur - prev);
            long nextVal = cur - rounds;
            need -= rounds * colors;
            res += (cur * (cur + 1L) - nextVal * (nextVal + 1)) / 2 * colors;
            if (nextVal > prev) {
                res += need * nextVal;
                break;
            }
        }
        return (int)(res % MOD);
    }

    private void test(int[] inventory, int orders, int expected) {
        assertEquals(expected, maxProfit(inventory, orders));
        assertEquals(expected, maxProfit2(inventory, orders));
        assertEquals(expected, maxProfit3(inventory, orders));
    }

    @Test public void test() {
        test(new int[] {2, 5}, 4, 14);
        test(new int[] {3, 5}, 6, 19);
        test(new int[] {2, 8, 4, 10, 6}, 20, 110);
        test(new int[] {1000000000}, 1000000000, 21);
        test(new int[] {497978859, 167261111, 483575207, 591815159}, 836556809, 373219333);
        test(new int[] {565259708, 715164401, 716563713, 958255469, 844600740, 823949511, 180479359,
                        287829385, 164248818, 73361150, 230686692, 322986846, 598720034, 338241127,
                        748922260, 181241085, 833659853, 509571179, 250093451, 690995620, 703292727,
                        595636202}, 650114768, 997286992);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
