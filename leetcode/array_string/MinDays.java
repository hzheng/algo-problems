import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1482: https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/
//
// Given an integer array bloomDay, an integer m and an integer k.
// We need to make m bouquets. To make a bouquet, you need to use k adjacent flowers from the garden.
// The garden consists of n flowers, the ith flower will bloom in the bloomDay[i] and then can be
// used in exactly one bouquet.
// Return the minimum number of days you need to wait to be able to make m bouquets from the garden.
// If it is impossible to make m bouquets return -1.
// Constraints:
// bloomDay.length == n
// 1 <= n <= 10^5
// 1 <= bloomDay[i] <= 10^9
// 1 <= m <= 10^6
// 1 <= k <= n
public class MinDays {
    // Binary Search + Set
    // time complexity: O(N * log(N)), space complexity: O(1)
    // 279 ms(33.33%), 121.1 MB(33.33%) for 91 tests
    public int minDays(int[] bloomDay, int m, int k) {
        int n = bloomDay.length;
        if (m * k > n) { return -1; }

        Set<Integer> set = new TreeSet<>();
        for (int d : bloomDay) {
            set.add(d);
        }
        List<Integer> days = new ArrayList<>(set);
        int low = 0;
        for (int high = days.size(); low < high; ) {
            int mid = (low + high) >>> 1;
            if (ok(bloomDay, m, k, days.get(mid))) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return days.get(low);
    }

    private boolean ok(int[] bloomDay, int m, int k, int d) {
        int n = bloomDay.length;
        for (int i = 0, needStreak = k, needBouquets = m; i < n; i++) {
            if (bloomDay[i] > d) {
                needStreak = k;
            } else if (--needStreak == 0) {
                needStreak = k;
                if (--needBouquets == 0) { return true; }
            }
        }
        return false;
    }

    void test(int[] bloomDay, int m, int k, int expected) {
        assertEquals(expected, minDays(bloomDay, m, k));
    }

    @Test public void test() {
        test(new int[] {1, 10, 3, 10, 2}, 3, 1, 3);
        test(new int[] {1, 10, 3, 10, 2}, 3, 2, -1);
        test(new int[] {7, 7, 7, 7, 12, 7, 7}, 2, 3, 12);
        test(new int[] {1000000000, 1000000000}, 1, 1, 1000000000);
        test(new int[] {1, 10, 2, 9, 3, 8, 4, 7, 5, 6}, 4, 2, 9);
        test(new int[] {97,83}, 2, 1,97);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
