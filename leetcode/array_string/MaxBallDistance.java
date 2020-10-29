import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1552: https://leetcode.com/problems/magnetic-force-between-two-balls/
//
// In universe Earth C-137, Rick discovered a special form of magnetic force between two balls if
// they are put in his new invented basket. Rick has n empty baskets, the ith basket is at
// position[i], Morty has m balls and needs to distribute the balls into the baskets such that the
// minimum magnetic force between any two balls is maximum. Rick stated that magnetic force between
// two different balls at positions x and y is |x - y|.
// Given the integer array position and the integer m. Return the required force.
// Constraints:
// n == position.length
// 2 <= n <= 10^5
// 1 <= position[i] <= 10^9
// All integers in position are distinct.
// 2 <= m <= position.length
public class MaxBallDistance {
    // Sort + Binary Search
    // time complexity: O(N^log(MAX_DISTANCE)), space complexity: O(1)
    // 42 ms(72.93%), 54.3 MB(6.32%) for 98 tests
    public int maxDistance(int[] position, int m) {
        Arrays.sort(position);
        int low = 0;
        for (int high = position[position.length - 1] - position[0]; low < high; ) {
            int mid = (low + high + 1) >>> 1;
            if (ok(position, m, mid)) {
                low = mid;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }

    private boolean ok(int[] position, int m, int distance) {
        for (int n = position.length, i = 1, left = m - 1, prev = position[0]; i < n; i++) {
            if (position[i] - prev >= distance) {
                if (--left == 0) { return true; }

                prev = position[i--];
            }
        }
        return false;
    }

    private void test(int[] position, int m, int expected) {
        assertEquals(expected, maxDistance(position, m));
    }

    @Test public void test() {
        test(new int[] {1, 2, 3, 4, 7}, 3, 3);
        test(new int[] {5, 4, 3, 2, 1, 1000000000}, 2, 999999999);
        test(new int[] {23, 67, 97, 15, 4, 23, 29, 33, 71, 213, 800}, 5, 30);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
