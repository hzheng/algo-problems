import org.junit.Test;

import static org.junit.Assert.*;

// LC1395: https://leetcode.com/problems/count-number-of-teams/
//
// There are n soldiers standing in a line. Each soldier is assigned a unique rating value.
// You have to form a team of 3 soldiers amongst them under the following rules:
// Choose 3 soldiers with index (i, j, k) with rating (rating[i], rating[j], rating[k]).
// A team is valid if:  (rating[i] < rating[j] < rating[k]) or (rating[i] > rating[j] > rating[k])
// where (0 <= i < j < k < n). Return the number of teams you can form given the conditions.
// (soldiers can be part of multiple teams).
// Constraints:
// n == rating.length
// 1 <= n <= 200
// 1 <= rating[i] <= 10^5
public class CountTeams {
    // time complexity: O(N^2), space complexity: O(1)
    // 3 ms(82.39%), 36.9 MB(100%) for 53 tests
    public int numTeams(int[] rating) {
        return numTeams(rating, 1) + numTeams(rating, -1);
    }

    private int numTeams(int[] rating, int sign) {
        int n = rating.length;
        int res = 0;
        for (int i = 1; i < n - 1; i++) {
            int left = 0;
            int right = 0;
            for (int j = 0, cur = rating[i]; j < n; j++) {
                if ((rating[j] - cur) * sign * (j - i) > 0) {
                    if (j < i) {
                        left++;
                    } else {
                        right++;
                    }
                }
            }
            res += left * right;
        }
        return res;
    }

    // time complexity: O(N^2), space complexity: O(1)
    // 3 ms(82.39%), 36.9 MB(100%) for 53 tests
    public int numTeams2(int[] rating) {
        int res = 0;
        for (int i = 1, n = rating.length; i < n - 1; i++) {
            int[] less = new int[2];
            int[] greater = new int[2];
            for (int j = 0; j < n; j++) {
                if (rating[i] < rating[j]) {
                    less[j > i ? 1 : 0]++;
                } else if (rating[i] > rating[j]) {
                    greater[j > i ? 1 : 0]++;
                }
            }
            res += less[0] * greater[1] + greater[0] * less[1];
        }
        return res;
    }

    // Binary Indexed Tree
    // time complexity: O(N^log(MAX)), space complexity: O(1)
    // 1 ms(100.00%) for 53 tests
    public int numTeams3(int[] rating) {
        int max = 0;
        for (int r : rating) {
            max = Math.max(max, r);
        }
        int[] leftBit = new int[max + 1];
        int[] rightBit = new int[max + 1];
        for (int r : rating) {
            update(rightBit, r, 1);
        }
        int res = 0;
        for (int r : rating) {
            update(rightBit, r, -1);
            res += (lessTotal(leftBit, r - 1) * greaterTotal(rightBit, r + 1));
            res += (greaterTotal(leftBit, r + 1) * lessTotal(rightBit, r - 1));
            update(leftBit, r, 1);
        }
        return res;
    }

    private void update(int[] bit, int index, int val) {
        for (int i = index; i < bit.length; i += i & (-i)) {
            bit[i] += val;
        }
    }

    private int lessTotal(int[] bit, int index) {
        int sum = 0;
        for (int i = index; i > 0; i -= i & (-i)) {
            sum += bit[i];
        }
        return sum;
    }

    private int greaterTotal(int[] bit, int index) {
        return lessTotal(bit, bit.length - 1) - lessTotal(bit, index - 1);
    }

    // TODO: BST
    // TODO: Segment Tree

    private void test(int[] rating, int expected) {
        assertEquals(expected, numTeams(rating));
        assertEquals(expected, numTeams2(rating));
        assertEquals(expected, numTeams3(rating));
    }

    @Test public void test() {
        test(new int[] {1}, 0);
        test(new int[] {1, 3}, 0);
        test(new int[] {2, 1, 3}, 0);
        test(new int[] {1, 2, 3, 4}, 4);
        test(new int[] {4, 3, 2, 1}, 4);
        test(new int[] {2, 5, 3, 4, 1}, 3);
        test(new int[] {1, 3, 8, 7, 6, 5, 2, 12, 3, 9, 10, 1, 9, 4, 7, 4}, 144);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
