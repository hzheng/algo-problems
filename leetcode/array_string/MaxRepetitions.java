import org.junit.Test;
import static org.junit.Assert.*;

// LC466: https://leetcode.com/problems/count-the-repetitions/
//
// Define S = [s,n] as the string S which consists of n connected strings s.
// On the other hand, we define that string s1 can be obtained from string s2 if
// we can remove some characters from s2 such that it becomes s1.
// You are given two non-empty strings s1 and s2 (each at most 100 characters
// long) and two integers 0 ≤ n1 ≤ 106 and 1 ≤ n2 ≤ 106. Now consider the
// strings S1 and S2, where S1=[s1,n1] and S2=[s2,n2]. Find the maximum integer
// M such that [S2,M] can be obtained from S1.
public class MaxRepetitions {
    // time complexity: O(|S1| * n1), space complexity: O(|S1| + |S2|)
    // beats N/A(279 ms for 34 tests)
    public int getMaxRepetitions(String s1, int n1, String s2, int n2) {
        if (s1.length() * n1 < s2.length() * n2) return 0;

        int factor = gcd(n1, n2);
        n1 /= factor;
        n2 /= factor;
        char[] cs1 = s1.toCharArray();
        char[] cs2 = s2.toCharArray();
        int len1 = cs1.length;
        int len2 = cs2.length;
        int loops1 = 0;
        int loops2 = 0;
        for (int i = 0, j = 0;; ) {
            if (cs1[i] == cs2[j]) {
                if (++j == len2) {
                    j = 0;
                    loops2++;
                }
            }
            if (++i == len1) {
                if (++loops1 == n1) break;
                i = 0;
            }
        }
        return loops2 / n2;
    }

    private int gcd(int a, int b) {
        if (a < b) return gcd(b, a);

        return (b == 0) ? a : gcd(a % b, b);
    }

    // time complexity: O(|S1| * n1), space complexity: O(1)
    // beats N/A(1357 ms for 34 tests)
    public int getMaxRepetitions1(String s1, int n1, String s2, int n2) {
        int loops1 = 0;
        int loops2 = 0;
        int len1 = s1.length();
        int len2 = s2.length();
        for (int i = 0; loops1 < n1; loops1++) {
            for (int j = 0; j < len1; j++) {
                if (s1.charAt(j) == s2.charAt(i)) {
                    if (++i == len2) {
                        i = 0;
                        loops2++;
                    }
                }
            }
        }
        return loops2 / n2;
    }

    // time complexity: O(|S1| * |S2| + n1), space complexity: O(|S1| + |S2|)
    // beats N/A(39 ms for 34 tests)
    public int getMaxRepetitions2(String s1, int n1, String s2, int n2) {
        char[] cs1 = s1.toCharArray();
        char[] cs2 = s2.toCharArray();
        int len1 = cs1.length;
        int len2 = cs2.length;
        int[] loops = new int[len2];
        int[] next = new int[len2];
        for (int i = 0; i < len2; i++) {
            int cur = i;
            for (int j = 0; j < len1; j++) {
                if (cs1[j] == cs2[cur % len2]) {
                    cur++;
                }
            }
            loops[i] = cur / len2;
            next[i] = cur % len2;
        }
        int res = 0;
        for (int i = 0, j = 0; i < n1; i++) {
            res += loops[j];
            j = next[j];
        }
        return res / n2;
    }

    // time complexity: O(|S1| * |S2|), space complexity: O(|S2|)
    // beats N/A(6 ms for 34 tests)
    public int getMaxRepetitions3(String s1, int n1, String s2, int n2) {
        if (n1 <= 0) return 0;

        int len1 = s1.length();
        int len2 = s2.length();
        int[] loops = new int[len2 + 2];
        int[] indices = new int[len2 + 2];
        int cycleBegin = 0;
        int cycleEnd = 1;
        for (int i = 0, repeats = 0;; cycleEnd++) {
            for (int j = 0; j < len1; j++) {
                if (s2.charAt(i) == s1.charAt(j)) {
                    if (++i == len2) {
                        repeats++;
                        i = 0;
                    }
                }
            }
            if (cycleEnd >= n1) return repeats / n2;

            loops[cycleEnd] = repeats;
            if (indices[i] > 0) { // cycle found
                cycleBegin = indices[i];
                break;
            }
            indices[i] = cycleEnd;
        }
        int cycle = cycleEnd - cycleBegin;
        return ((n1 - cycleBegin) / cycle * (loops[cycleEnd] - loops[cycleBegin])
                + loops[(n1 - cycleBegin) % cycle + cycleBegin]) / n2;
    }

    // TODO: remove useless characters from s1

    // TODO: Dynamic Programming

    void test(String s1, int n1, String s2, int n2, int expected) {
        assertEquals(expected, getMaxRepetitions(s1, n1, s2, n2));
        assertEquals(expected, getMaxRepetitions1(s1, n1, s2, n2));
        assertEquals(expected, getMaxRepetitions2(s1, n1, s2, n2));
        assertEquals(expected, getMaxRepetitions3(s1, n1, s2, n2));
    }

    @Test
    public void test1() {
        test("ba", 3, "ab", 1, 2);
        test("a", 0, "a", 1, 0);
        test("a", 0, "ab", 1, 0);
        test("acb", 4, "ad", 2, 0);
        test("acb", 4, "ab", 2, 2);
        test("abcabd", 5, "ab", 3, 3);
        test("abab", 5, "ab", 3, 3);
        test("bab", 8, "abab", 2, 2);
        test("baba", 2, "ab", 1, 3);
        test("baba", 5, "ab", 2, 4);
        test("abcabca", 5, "abc", 3, 3);
        test("niconiconi", 10, "nico", 3, 6);
        test("niconiconi", 99981, "nico", 81, 2468);
        test("nlhqgllunmelayl", 2, "lnl", 1, 3);
        test("phqghumeaylnlfdxfircvscxggbwkfnqduxwfnfozvsrtkjprepggxrpnrvystmwc"
             + "ysyycqpevikeffmznimkkasvwsrenzkycxf", 100, "xtlsgypsfa", 1, 49);
        test("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
             + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 1000000, "a", 1000000, 100);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxRepetitions");
    }
}
