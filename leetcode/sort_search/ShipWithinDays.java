import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1011: https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/
//
// A conveyor belt has packages that must be shipped from one port to another within D days.
// The i-th package on the conveyor belt has a weight of weights[i].  Each day, we load the ship
// with packages on the conveyor belt (in the order given by weights). We may not load more weight
// than the maximum weight capacity of the ship. Return the least weight capacity of the ship that
// will result in all the packages on the conveyor belt being shipped within D days.
// Note:
// 1 <= D <= weights.length <= 50000
// 1 <= weights[i] <= 500
public class ShipWithinDays {
    // Binary Search
    // time complexity: O(N * log(sum(W)), space complexity: O(1)
    // 6 ms(97.88%), 42 MB(96.95%) for 82 tests
    public int shipWithinDays(int[] weights, int D) {
        if (D == 1) {
            int res = 0;
            for (int w : weights) {
                res += w;
            }
            return res;
        }
        int low = 1;
        for (int high = 500 * 50000; low < high; ) {
            int mid = (low + high) >>> 1;
            if (check(weights, D, mid)) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }
        return low;
    }

    private boolean check(int[] weights, int D, int max) {
        int days = 0;
        for (int i = 0, sum = 0; i < weights.length; i++) {
            if (weights[i] > max) {
                return false;
            }

            sum += weights[i];
            if (sum < max) {
                if (i < weights.length - 1) {
                    continue;
                }
                return (++days <= D);
            }
            if (++days > D || (i == weights.length - 1 && ++days > D)) {
                return false;
            }

            sum = (sum == max) ? 0 : weights[i];
        }
        return true;
    }

    // Binary Search
    // time complexity: O(N * log(sum(W)), space complexity: O(1)
    // 8 ms(78.25%), 42 MB(96.91%) for 82 tests
    public int shipWithinDays2(int[] weights, int D) {
        int low = 0;
        int high = 0;
        for (int w : weights) {
            low = Math.max(low, w);
            high += w;
        }
        while (low < high) {
            int mid = (low + high) >>> 1;
            int days = 1;
            int cur = 0;
            for (int w : weights) {
                if (cur + w > mid) {
                    days++;
                    cur = 0;
                }
                cur += w;
            }
            if (days > D) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    void test(int[] weights, int D, int expected) {
        assertEquals(expected, shipWithinDays(weights, D));
        assertEquals(expected, shipWithinDays2(weights, D));
    }

    @Test
    public void test() {
        test(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 5, 15);
        test(new int[]{3, 2, 2, 4, 1, 4}, 3, 6);
        test(new int[]{1, 2, 3, 1, 1}, 4, 3);
        test(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 1, 55);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
