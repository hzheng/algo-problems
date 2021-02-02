import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1128: https://leetcode.com/problems/number-of-equivalent-domino-pairs/
//
// Given a list of dominoes, dominoes[i] = [a, b] is equivalent to dominoes[j] = [c, d] if and only
// if either (a==c and b==d), or (a==d and b==c) - that is, one domino can be rotated to be equal to
// another domino. Return the number of pairs (i, j) for which 0 <= i < j < dominoes.length, and
// dominoes[i] is equivalent to dominoes[j].
//
// Constraints:
// 1 <= dominoes.length <= 40000
// 1 <= dominoes[i][j] <= 9
public class NumEquivDominoPairs {
    // Hash Table
    // time complexity: O(N), space complexity: O(1)
    // 8 ms(50.83%), 48.1 MB(79.11%) for 19 tests
    public int numEquivDominoPairs(int[][] dominoes) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int[] domino : dominoes) {
            int a = domino[0];
            int b = domino[1];
            int key = (Math.max(a, b) * 10) + Math.min(a, b);
            count.put(key, count.getOrDefault(key, 0) + 1);
        }
        int res = 0;
        for (int cnt : count.values()) {
            res += cnt * (cnt - 1) / 2;
        }
        return res;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(1)
    // 2 ms(98.85%), 48.6 MB(42.68%) for 19 tests
    public int numEquivDominoPairs2(int[][] dominoes) {
        int[] count = new int[100];
        int res = 0;
        for (int[] domino : dominoes) {
            int a = domino[0];
            int b = domino[1];
            int key = (Math.max(a, b) * 10) + Math.min(a, b);
            res += count[key]++;
        }
        return res;
    }

    private void test(int[][] dominoes, int expected) {
        assertEquals(expected, numEquivDominoPairs(dominoes));
        assertEquals(expected, numEquivDominoPairs2(dominoes));
    }

    @Test public void test() {
        test(new int[][] {{1, 2}, {2, 1}, {3, 4}, {5, 6}}, 1);
        test(new int[][] {{1, 2}, {2, 1}, {3, 4}, {3, 4}, {6, 5}, {5, 6}, {5, 6}}, 5);
        test(new int[][] {{1, 1}, {1, 1}, {1, 2}, {2, 1}, {3, 3}, {3, 4}, {3, 4}, {6, 5}, {5, 6},
                          {5, 6}}, 6);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
