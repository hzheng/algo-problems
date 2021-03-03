import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * A panalphabetic window is a stretch of text that contains all the letters of the alphabet in
 * order.
 * Find length of shortest substring of panalphabetic in a string.
 */
public class ShortestPanalphabetic {
    public boolean isPanalphabetic(String s) {
        char cur = 'a';
        for (char c : s.toCharArray()) {
            if (c == cur && cur++ == 'z') { return true; }
        }
        return false;
    }

    public String shortestPanalphabetic(String s) {
        char[] cs = s.toCharArray();
        int n = cs.length;
        int[] dp = new int[26];
        Arrays.fill(dp, -1);
        int res = n + 1;
        int end = 0;
        for (int i = 0; i < n; i++) {
            int c = cs[i] - 'a';
            if (c < 0 || c >= dp.length) { continue; }

            if (c == 0) {
                dp[0] = i;
            } else if (dp[c - 1] >= 0) {
                dp[c] = dp[c - 1];
                if (c == dp.length - 1) {
                    int len = i - dp[c] + 1;
                    if (len < res) {
                        res = len;
                        end = i;
                    }
                }
            }
        }
        return res > n ? "" : s.substring(end + 1 - res, end + 1);
    }

    private void test(String s, String expected) {
        assertEquals(!expected.isEmpty(), isPanalphabetic(s));
        assertEquals(expected, shortestPanalphabetic(s));
    }

    @Test public void test() {
        test("ababacbdcefagbhcid3@#jeklfmnoapbgcqrds13tu!v%wxyzefabcdefghijklmnopqrs!tazuvwxyz",
             "abcdefghijklmnopqrs!tazuvwxyz");
        test("ababacdefghijklmnopqrstuvwxyz", "abacdefghijklmnopqrstuvwxyz");
        test("abaacdefghijklmnopqrstuvwxyzaz", "abaacdefghijklmnopqrstuvwxyz");
        test("abacdefghijklmnopqrstuvwxyzaz", "abacdefghijklmnopqrstuvwxyz");
        test("zaabcdefghijklmnopqrstuvwxyzb", "abcdefghijklmnopqrstuvwxyz");
        test("bzaacdefghijklmnopqrstuvwxyzb", "");
        test("ababacdefghijklmnopqrstuvwxzy", "");
        test("ababacdefghijk13lmnopqrstuvwxyazbycdefghijklmnopqrstuvwxyazabcdefg",
             "azbycdefghijklmnopqrstuvwxyaz");
        test("ababacdefghijk13lmnopqrstuvwxyazbycdefghijklmnopqrstuvwxyazabcdefghijkzlmnopqrstuvwxyz",
             "abcdefghijkzlmnopqrstuvwxyz");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
