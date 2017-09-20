import org.junit.Test;
import static org.junit.Assert.*;

// LC555: https://leetcode.com/problems/split-concatenated-strings/
//
// Given a list of strings, you could concatenate these strings together into a loop,
// where for each string you could choose to reverse it or not. Among all the possible
// loops, you need to find the lexicographically biggest string after cutting the loop,
// which will make the looped string into a regular one.
// Specifically, to find the lexicographically biggest string, you need to experience two phases:
// Concatenate all the strings into a loop, where you can reverse some strings or not and connect
// them in the same order as given. Cut and make one breakpoint in any place of the loop, which
// will make the looped string into a regular one starting from the character at the cutpoint.
// Find the lexicographically biggest one among all the possible regular strings.
public class SplitLoopedString {
    // time complexity: O(N ^ 2) (N: total number of characters)
    // beats 96.74%(57 ms for 50 tests)
    public String splitLoopedString(String[] strs) {
        int n = strs.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(reverse(strs[i]));
        }
        String s = sb.toString();
        String res = "a";
        for (int i = 0, j = 0, len; i < n; i++, j += len) {
            String s1 = strs[i];
            String s2 = new StringBuilder(s1).reverse().toString();
            len = s1.length();
            String middle = s.substring(j + len) + s.substring(0, j);
            for (int k = 0; k < len; k++) {
                if (s1.charAt(k) >= res.charAt(0)) {
                    String t = s1.substring(k) + middle + s1.substring(0, k);
                    if (res.compareTo(t) < 0) {
                        res = t;
                    }
                }
                if (s2.charAt(k) >= res.charAt(0)) {
                    String t = s2.substring(k) + middle + s2.substring(0, k);
                    if (res.compareTo(t) < 0) {
                        res = t;
                    }
                }
            }
        }
        return res;
    }

    private String reverse(String str) {
        for (int i = 0, j = str.length() - 1; i < j; i++, j--) {
            int diff = str.charAt(i) - str.charAt(j);
            if (diff > 0) return str;
            if (diff < 0) return new StringBuilder(str).reverse().toString();
        }
        return str;
    }

    // time complexity: O(N ^ 2) (N: total number of characters)
    // beats 40.95%(246 ms for 50 tests)
    public String splitLoopedString2(String[] strs) {
        for (int i = 0; i < strs.length; i++) {
            String rev = new StringBuilder(strs[i]).reverse().toString();
            if (strs[i].compareTo(rev) < 0) {
                strs[i] = rev;
            }
        }
        String res = "";
        for (int i = 0; i < strs.length; i++) {
            String rev = new StringBuilder(strs[i]).reverse().toString();
            for (String s : new String[] {strs[i], rev}) {
                for (int k = 0; k < s.length(); k++) {
                    StringBuilder t = new StringBuilder(s.substring(k));
                    for (int j = i + 1; j < strs.length; j++) {
                        t.append(strs[j]);
                    }
                    for (int j = 0; j < i; j++) {
                        t.append(strs[j]);
                    }
                    t.append(s.substring(0, k));
                    if (t.toString().compareTo(res) > 0) {
                        res = t.toString();
                    }
                }
            }
        }
        return res;
    }

    void test(String[] strs, String expected) {
        assertEquals(expected, splitLoopedString(strs));
    }

    @Test
    public void test() {
        test(new String[] {"abc"}, "cba");
        test(new String[] {"yzb", "abc"}, "zycbab");
        test(new String[] {"yzy", "aba"}, "zyabay");
        test(new String[] {"a", "b", "c"}, "cab");
        test(new String[] {"abc", "xyz"}, "zyxcba");
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
