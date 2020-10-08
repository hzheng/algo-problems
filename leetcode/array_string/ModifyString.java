import org.junit.Test;

import static org.junit.Assert.*;

// LC1576: https://leetcode.com/problems/replace-all-s-to-avoid-consecutive-repeating-characters/
//
// Given a string s containing only lower case English letters and the '?' character, convert all
// the '?' characters into lower case letters such that the final string does not contain any
// consecutive repeating characters. You cannot modify the non '?' characters.
// It is guaranteed that there are no consecutive repeating characters in the given string except
// for '?'. Return the final string after all the conversions (possibly zero) have been made.  If
// there is more than one solution, return any of them.
public class ModifyString {
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(59.67%), 38.6 MB(57.98%) for 776 tests
    public String modifyString(String s) {
        char[] cs = s.toCharArray();
        for (int n = cs.length, i = 0; i < n; i++) {
            if (s.charAt(i) != '?') { continue; }

            char prev = (i == 0) ? '$' : cs[i - 1];
            char next = (i == n - 1) ? '$' : cs[i + 1];
            for (char c = 'a'; ; c++) {
                if ((c != prev) && (c != next)) {
                    cs[i] = c;
                    break;
                }
            }
        }
        return String.valueOf(cs);
    }

    private void test(String s, String expected) {
        assertEquals(expected, modifyString(s));
        assertTrue(check(modifyString(s)));
    }

    private boolean check(String s) {
        for (int i = s.length() - 1; i > 0; i--) {
            char c = s.charAt(i);
            if (c == '?' || c == s.charAt(i - 1)) { return false; }
        }
        return true;
    }

    @Test public void test() {
        test("?zs", "azs");
        test("ubv?w", "ubvaw");
        test("j?qg??b", "jaqgacb");
        test("??yw?ipkj?", "abywaipkja");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
