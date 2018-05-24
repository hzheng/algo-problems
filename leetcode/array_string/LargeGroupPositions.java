import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Utils;

// LC830: https://leetcode.com/problems/positions-of-large-groups/
//
// In a string S of lowercase letters, these letters form consecutive groups of
// the same character. Call a group large if it has 3 or more characters.  We
// would like the starting and ending positions of every large group.
public class LargeGroupPositions {
    // beats %(18 ms for 202 tests)
    public List<List<Integer> > largeGroupPositions(String S) {
        S += " ";
        List<List<Integer> > res = new ArrayList<>();
        char prevChar = 0;
        int prev = -1;
        int i = 0;
        for (char c : S.toCharArray()) {
            if (c != prevChar) {
                if (i - prev >= 3) {
                    res.add(Arrays.asList(prev, i - 1));
                }
                prevChar = c;
                prev = i;
            }
            i++;
        }
        return res;
    }

    // beats %(20 ms for 202 tests)
    public List<List<Integer> > largeGroupPositions2(String S) {
        List<List<Integer> > res = new ArrayList<>();
        for (int i = 0, j = 0, n = S.length(); j < n; j++) {
            if (j == n - 1 || S.charAt(j) != S.charAt(j + 1)) {
                if (j - i >= 2) {
                    res.add(Arrays.asList(i, j));
                }
                i = j + 1;
            }
        }
        return res;
    }

    // beats %(18 ms for 202 tests)
    public List<List<Integer>> largeGroupPositions3(String S) {
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0, j = 0, n = S.length(); j < n; i = j) {
            for (; j < n && S.charAt(j) == S.charAt(i); j++) {}
            if (j - i >= 3) {
                res.add(Arrays.asList(i, j - 1));
            }
        }
        return res;
    }

    void test(String S, int[][] expected) {
        assertArrayEquals(expected, Utils.toInts(largeGroupPositions(S)));
        assertArrayEquals(expected, Utils.toInts(largeGroupPositions2(S)));
        assertArrayEquals(expected, Utils.toInts(largeGroupPositions3(S)));
    }

    @Test
    public void test() {
        test("abbxxxxzzy", new int[][] { { 3, 6 } });
        test("abcdddeeeeaabbbcd",
             new int[][] { { 3, 5 }, { 6, 9 }, { 12, 14 } });
        test("abbxxxxzyyy", new int[][] { { 3, 6 }, { 8, 10 } });
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
