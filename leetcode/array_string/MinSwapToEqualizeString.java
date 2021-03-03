import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1247: https://leetcode.com/problems/minimum-swaps-to-make-strings-equal/
//
// You are given two strings s1 and s2 of equal length consisting of letters "x" and "y" only. Your
// task is to make these two strings equal to each other. You can swap any two characters that
// belong to different strings, which means: swap s1[i] and s2[j].
// Return the minimum number of swaps required to make s1 and s2 equal, or return -1 if it is
// impossible to do so.
//
// Constraints:
// 1 <= s1.length, s2.length <= 1000
// s1, s2 only contain 'x' or 'y'.
public class MinSwapToEqualizeString {
    // Greedy
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 36.7 MB(95.69%) for 70 tests
    public int minimumSwap(String s1, String s2) {
        int x1 = 0;
        int x2 = 0;
        for (int i = 0, n = s1.length(); i < n; i++) {
            char c = s1.charAt(i);
            if (c == s2.charAt(i)) { continue; }

            if (c == 'x') {
                x1++;
            } else {
                x2++;
            }
        }
        return ((x1 + x2) % 2 != 0) ? -1 : (x1 / 2 + x2 / 2 + x1 % 2 * 2);
    }

    private void test(String s1, String s2, int expected) {
        assertEquals(expected, minimumSwap(s1, s2));
    }

    @Test public void test() {
        test("xx", "yy", 1);
        test("xy", "yx", 2);
        test("xx", "xy", -1);
        test("xxyyxyxyxx", "xyyxyxxxyx", 4);
        test("xxyyxyxyxxyxyxxyy", "xyyxyxxxyxyyxyyxx", 6);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
