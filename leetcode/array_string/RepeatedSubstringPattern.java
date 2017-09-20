import org.junit.Test;
import static org.junit.Assert.*;

// LC459: https://leetcode.com/problems/repeated-substring-pattern/
//
// Given a non-empty string check if it can be constructed by taking a substring
// of it and appending multiple copies of the substring together. You may assume
// the given string consists of lowercase English letters only and its length
// will not exceed 10000.
public class RepeatedSubstringPattern {
    // beats N/A(27 ms for 100 tests)
    public boolean repeatedSubstringPattern(String str) {
        int len = str.length();
        char[] chars = str.toCharArray();
        for (int size = 1; size <= len / 2; size++) {
            if (len % size != 0) continue;

            int j = 0;
middleLoop:
            for (j = 0; j < size; j++) {
                char c = chars[j];
                for (int k = j + size; k < len; k += size) {
                    if (c != chars[k]) break middleLoop;
                }
            }
            if (j == size) return true;
        }
        return false;
    }

    // Recursion
    // beats N/A(126 ms for 100 tests)
    public boolean repeatedSubstringPattern2(String str) {
        int len = str.length();
        for (int i = len / 2; i > 0; i--) {
            if (len % i == 0 && canConstruct(str, str.substring(0, i))) return true;
        }
        return false;
    }

    private boolean canConstruct(String str, String repeated) {
        if (!str.startsWith(repeated)) return false;

        if (str.length() == repeated.length()) return true;

        return canConstruct(str.substring(repeated.length(), str.length()), repeated);
    }

    // KMP algorithm
    // beats N/A(24 ms for 100 tests)
    public boolean repeatedSubstringPattern3(String str) {
        int len = str.length();
        int[] kmp = new int[len];
        for (int i = 0, j = 1; i < len && j < len; j++) {
            if (str.charAt(i) == str.charAt(j)) {
                kmp[j] = ++i;
            } else if (i > 0) {
                i = kmp[i - 1];
                j--;
            }
        }
        int prefixLen = kmp[len - 1];
        return prefixLen > 0 && len % (len - prefixLen) == 0;
    }

    void test(String str, boolean expected) {
        assertEquals(expected, repeatedSubstringPattern(str));
        assertEquals(expected, repeatedSubstringPattern2(str));
        assertEquals(expected, repeatedSubstringPattern3(str));
    }

    @Test
    public void test() {
        test("abab", true);
        test("aba", false);
        test("abac", false);
        test("abcabcabcabc", true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RepeatedSubstringPattern");
    }
}
