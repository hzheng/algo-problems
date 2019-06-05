import java.util.regex.Pattern;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1071: https://leetcode.com/problems/greatest-common-divisor-of-strings/
//
// For strings S and T, we say "T divides S" if and only if S = T + ... + T  (T concatenated with
// itself 1 or more times)
// Return the largest string X such that X divides str1 and X divides str2.
public class GcdOfStrings {
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(76.11%), 36.3 MB(100%) for 102 tests
    public String gcdOfStrings(String str1, String str2) {
        int len1 = str1.length();
        int len2 = str2.length();
        int max = 0;
        for (int i = Math.min(len1, len2); i > 0; i--) {
            if ((len1 % i == 0) && (len2 % i == 0)) {
                if (divide(str1, str2, i) && divide(str2, str2, i)) {
                    max = i;
                    break;
                }
            }
        }
        return str1.substring(0, max);
    }

    private boolean divide(String str, String pattern, int len) {
        for (int i = 0; i < str.length(); i += len) {
            for (int j = 0; j < len; j++) {
                if (str.charAt(i + j) != pattern.charAt(j)) {
                    return false;
                }
            }
        }
        return true;
    }

    // time complexity: O(N), space complexity: O(1)
    // 3 ms(38.40%), 36.1 MB(100%) for 102 tests
    public String gcdOfStrings2(String str1, String str2) {
        int len1 = str1.length();
        int len2 = str2.length();
        int max = 0;
        outer:
        for (int i = 1; i <= len1 && i <= len2 && str1.charAt(i - 1) == str2.charAt(i - 1); i++) {
            if (len1 % i != 0 || len2 % i != 0) {
                continue;
            }
            String pattern = str1.substring(0, i);
            for (int j = 2 * i; j <= len1; j += i) {
                if (!pattern.equals(str1.substring(j - i, j))) {
                    continue outer;
                }
            }
            for (int j = 2 * i; j <= len2; j += i) {
                if (!pattern.equals(str2.substring(j - i, j))) {
                    continue outer;
                }
            }
            max = i;
        }
        return str1.substring(0, max);
    }

    // Recursion
    // time complexity: O(N ^ 2), space complexity: O(N)
    // 0 ms(100.00%), 36.1 MB(100%) for 102 tests
    public String gcdOfStrings3(String str1, String str2) {
        if (str1.length() < str2.length()) { return gcdOfStrings3(str2, str1); }
        if (!str1.startsWith(str2)) { return ""; }
        if (str2.isEmpty()) { return str1; }
        return gcdOfStrings3(str1.substring(str2.length()), str2);
    }

    // RegEx
    // time complexity: O(N ^ 2), space complexity: O(1)
    // 68 ms(6.42%), 38.9 MB(100%) for 102 tests
    public String gcdOfStrings4(String str1, String str2) {
        for (int i = str1.length(); i > 0; i--) {
            String gcd = str1.substring(0, i);
            Pattern pattern = Pattern.compile("(" + gcd + ")+");
            if (pattern.matcher(str1).matches() && pattern.matcher(str2).matches()) {
                return gcd;
            }
        }
        return "";
    }

    // time complexity: O(N ^ 2), space complexity: O(1)
    // 20 ms(11.25%), 38.4 MB(100%) for 102 tests
    public String gcdOfStrings5(String str1, String str2) {
        String str = (str1.length() < str2.length()) ? str1 : str2;
        int n = str.length();
        for (int i = 1; i <= n; i++) {
            if (n % i != 0) {
                continue;
            }
            String pattern = str.substring(0, n / i);
            if (str1.replaceAll(pattern, "").isEmpty()
                && str2.replaceAll(pattern, "").isEmpty()) {
                return pattern;
            }
        }
        return "";
    }

    void test(String str1, String str2, String expected) {
        assertEquals(expected, gcdOfStrings(str1, str2));
        assertEquals(expected, gcdOfStrings2(str1, str2));
        assertEquals(expected, gcdOfStrings3(str1, str2));
        assertEquals(expected, gcdOfStrings4(str1, str2));
        assertEquals(expected, gcdOfStrings5(str1, str2));
    }

    @Test
    public void test() {
        test("ABCABC", "ABC", "ABC");
        test("ABABAB", "ABAB", "AB");
        test("LEET", "CODE", "");
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
