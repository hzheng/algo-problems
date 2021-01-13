import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1718: https://leetcode.com/problems/construct-the-lexicographically-largest-valid-sequence/
//
// Given an integer n, find a sequence that satisfies all of the following:
// The integer 1 occurs once in the sequence.
// Each integer between 2 and n occurs twice in the sequence.
// For every integer i between 2 and n, the distance between the two occurrences of i is exactly i.
// The distance between two numbers on the sequence, a[i] and a[j], is the absolute difference of
// their indices, |j - i|.
// Return the lexicographically largest sequence. It is guaranteed that under the given constraints,
// there is always a solution.
// Constraints:
// 1 <= n <= 20
public class ConstructDistancedSequence {
    // DFS + Recursion + Backtracking + Set
    // 2 ms(32.11%), 36.9 MB(54.74%) for 20 tests
    public int[] constructDistancedSequence(int n) {
        int[] res = new int[2 * n - 1];
        dfs(res, 0, n, new HashSet<>());
        return res;
    }

    private boolean dfs(int[] res, int cur, int n, Set<Integer> visited) {
        if (cur == res.length) { return true; }

        if (res[cur] != 0) { return dfs(res, cur + 1, n, visited); }

        for (int i = n; i > 0; i--) {
            if (!visited.add(i)) { continue; }

            int second = cur + ((i == 1) ? 0 : i);
            if (second < res.length && res[second] == 0) {
                res[cur] = res[second] = i;
                if (dfs(res, cur + 1, n, visited)) { return true; }

                res[cur] = res[second] = 0;
            }
            visited.remove(i);
        }
        return false;
    }

    private void test(int n, int[] expected) {
        assertArrayEquals(expected, constructDistancedSequence(n));
    }

    @Test public void test() {
        test(3, new int[] {3, 1, 2, 3, 2});
        test(5, new int[] {5, 3, 1, 4, 3, 5, 2, 4, 2});
        test(1, new int[] {1});
        test(2, new int[] {2, 1, 2});
        test(4, new int[] {4, 2, 3, 2, 4, 3, 1});
        test(9, new int[] {9, 7, 5, 3, 8, 6, 3, 5, 7, 9, 4, 6, 8, 2, 4, 2, 1});
        test(10, new int[] {10, 8, 6, 9, 3, 1, 7, 3, 6, 8, 10, 5, 9, 7, 4, 2, 5, 2, 4});
        test(19,
             new int[] {19, 17, 18, 14, 12, 16, 9, 15, 6, 3, 13, 1, 3, 11, 6, 9, 12, 14, 17, 19, 18,
                        16, 15, 13, 11, 10, 8, 4, 5, 7, 2, 4, 2, 5, 8, 10, 7});
        test(20,
             new int[] {20, 18, 19, 15, 13, 17, 10, 16, 7, 5, 3, 14, 12, 3, 5, 7, 10, 13, 15, 18,
                        20, 19, 17, 16, 12, 14, 11, 9, 4, 6, 8, 2, 4, 2, 1, 6, 9, 11, 8});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
