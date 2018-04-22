import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC821: https://leetcode.com/problems/shortest-distance-to-a-character/
//
// Given a string S and a character C, return an array of integers representing
// the shortest distance from the character C in the string.
public class ShortestToChar {
    // Sorted Set
    // time complexity: O(N * log(C)) (C: C's count)
    // beats %(19 ms for 76 tests)
    public int[] shortestToChar(String S, char C) {
        int n = S.length();
        NavigableSet<Integer> set = new TreeSet<>();
        for (int i = 0; i < n; i++) {
            if (S.charAt(i) == C) {
                set.add(i);
            }
        }
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            if (S.charAt(i) == C) continue;

            int dist = n;
            Integer hi = set.higher(i);
            if (hi != null) {
                dist = hi - i;
            }
            Integer low = set.lower(i);
            if (low != null) {
                dist = Math.min(dist, i - low);
            }
            res[i] = dist;
        }
        return res;
    }

    // time complexity: O(N)
    // beats %(9 ms for 76 tests)
    public int[] shortestToChar2(String S, char C) {
        int n = S.length();
        int[] res = new int[n];
        Arrays.fill(res, n);
        res[0] = (S.charAt(0) == C) ? 0 : n;
        for (int i = 1; i < n; i++) {
            if (S.charAt(i) == C) {
                res[i] = 0;
            } else {
                res[i] = Math.min(res[i], res[i - 1] + 1);
            }
        }
        for (int i = n - 2; i >= 0; i--) {
            res[i] = Math.min(res[i], res[i + 1] + 1);
        }
        return res;
    }

    // time complexity: O(N)
    // beats %(8 ms for 76 tests)
    public int[] shortestToChar3(String S, char C) {
        int n = S.length();
        int[] res = new int[n];
        int prev = -n;
        for (int i = 0; i < n; i++) {
            if (S.charAt(i) == C) {
                prev = i;
            }
            res[i] = i - prev;
        }
        prev = n * 2;
        System.out.println(Arrays.toString(res));
        for (int i = n - 1; i >= 0; i--) {
            if (S.charAt(i) == C) {
                prev = i;
            }
            res[i] = Math.min(res[i], prev - i);
        }
        return res;
    }

    void test(String S, char C, int[] expected) {
        assertArrayEquals(expected, shortestToChar(S, C));
        assertArrayEquals(expected, shortestToChar2(S, C));
        assertArrayEquals(expected, shortestToChar3(S, C));
    }

    @Test
    public void test() {
        test("aaba", 'b', new int[] {2, 1, 0, 1});
        test("abaa", 'b', new int[] {1, 0, 1, 2});
        test("loveleetcode", 'e',
             new int[] {3, 2, 1, 0, 1, 0, 0, 1, 2, 2, 1, 0});
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
