import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.collection.IsIn.*;

// LC471: https://leetcode.com/problems/encode-string-with-shortest-length/
//
// Given a non-empty string, encode the string such that its encoded length is
// the shortest.
// The encoding rule is: k[encoded_string], where the encoded_string inside the
// square brackets is being repeated exactly k times.
// Note:
// k will be a positive integer and encoded string will not be empty or have extra space.
// You may assume that the input string contains only lowercase English letters.
// The string's length is at most 160.
// If an encoding process does not make the string shorter, then do not encode
// it. If there are several solutions, return any of them is fine.
public class ShortestEncoder {
    // Recursion + Hash Table
    // beats N/A(46 ms for 35 tests)
    public String encode(String s) {
        return encode(s, new HashMap<>());
    }

    private String encode(String s, Map<String, String> memo) {
        if (s.length() < 5) return s;

        if (memo.containsKey(s)) return memo.get(s);

        String minCode = s;
        for (int i = s.length() / 2; i > 0; i--) {
            String prefix = s.substring(0, i);
            String suffix = s.substring(i);
            String codedPrefix = encode(prefix, memo);
            String codedSuffix = encode(suffix, memo);
            if (codedPrefix.length() + codedSuffix.length() < minCode.length()) {
                minCode = codedPrefix + codedSuffix;
            }
            String multiPrefixes = codedPrefix;
            for (int count = 2; suffix.startsWith(prefix); count++) {
                multiPrefixes += codedPrefix;
                suffix = suffix.substring(i);
                codedSuffix = encode(suffix, memo);
                if (multiPrefixes.length() + codedSuffix.length() < minCode.length()) {
                    minCode = multiPrefixes + codedSuffix;
                }
                String code = count + "[" + codedPrefix + "]" + codedSuffix;
                // slow?
                // String code = String.format("%d[%s]%s", count, codedPrefix, codedSuffix);
                if (code.length() < minCode.length()) {
                    minCode = code;
                }
            }
        }
        memo.put(s, minCode);
        return minCode;
    }

    // Recursion + Hash Table
    // beats N/A(277 ms for 35 tests)
    public String encode2(String s) {
        return encode2(s, new HashMap<>());
    }

    private String encode2(String s, Map<String, String> memo) {
        if (s.length() < 5) return s;

        if (memo.containsKey(s)) return memo.get(s);

        int len = s.length();
        String minCode = s;
outer:
        for (int unit = len / 2; unit > 0; unit--) {
            if (len % unit != 0) continue;

            for (int i = 0, j = 0; i < len; i++, j = (j + 1) % unit) {
                if (s.charAt(i) != s.charAt(j)) continue outer;
            }
            String code = len / unit + "["
                          + encode2(s.substring(0, unit), memo) + "]";
            // String code = String.format("%d[%s]", len / unit,
            // encode2(s.substring(0, unit), memo));
            if (code.length() < minCode.length()) {
                minCode = code;
            }
        }
        for (int i = len - 1; i > 0; i--) {
            String code = encode2(s.substring(0, i), memo)
                          + encode2(s.substring(i, len), memo);
            if (code.length() < minCode.length()) {
                minCode = code;
            }
        }
        memo.put(s, minCode);
        return minCode;
    }

    // Dynamic Programming
    // time complexity: O(N ^ 3)
    // beats N/A(116 ms for 35 tests)
    public String encode3(String s) {
        int n = s.length();
        String[][] dp = new String[n][n];
        for (int len = 1; len <= n; len++) {
            for (int i = 0; i + len <= n; i++) {
                int j = i + len - 1;
                String slice = s.substring(i, j + 1);
                dp[i][j] = slice;
                if (j - i < 4) continue;

                for (int k = i; k < j; k++) {
                    if ((dp[i][k] + dp[k + 1][j]).length() < dp[i][j].length()) {
                        dp[i][j] = dp[i][k] + dp[k + 1][j];
                    }
                }
                for (int k = len / 2; k > 0; k--) {
                    if (len % k == 0 && isRepeated(slice, k)) {
                        String code = len / k + "[" + dp[i][i + k - 1] + "]";
                        if (code.length() < dp[i][j].length()) {
                            dp[i][j] = code;
                        }
                    }
                }
            }
        }
        return dp[0][n - 1];
    }

    private boolean isRepeated(String s, int unitLen) {
        for (int i = s.length() - unitLen - 1; i >= 0; i--) {
            if (s.charAt(i) != s.charAt(i + unitLen)) return false;
        }
        return true;
    }

    private boolean isRepeated0(String s, int unitLen) { // 283 ms if use this function
        return s.replaceAll(s.substring(0, unitLen), "").isEmpty();
    }

    // Recursion + Dynamic Programming
    // time complexity: O(N ^ 3)
    // beats N/A(72 ms for 35 tests)
    public String encode4(String s) {
        int n = s.length();
        return (n < 5) ? s : encode(s, 0, n - 1, new String[n][n]);
    }

    private String encode(String s, int start, int end, String[][] dp) {
        int len = end - start + 1;
        if (len < 5) {
            dp[start][end] = s.substring(start, end + 1);
        }
        if (dp[start][end] != null) return dp[start][end];

        String minCode = s;
        for (int i = start; i < end; i++) {
            String code1 = encode(s, start, i, dp);
            String code2 = encode(s, i + 1, end, dp);
            if (code1.length() + code2.length() < minCode.length()) {
                minCode = code1 + code2;
            }
        }
        for (int unit = len / 2; unit > 0; unit--) {
            if (len % unit == 0 && isRepeated(s, start, end, unit)) {
                String code = len / unit + "[" + encode(s, start, start + unit - 1, dp) + "]";
                if (code.length() < minCode.length()) {
                    minCode = code;
                }
            }
        }
        return dp[start][end] = minCode;
    }

    private boolean isRepeated(String s, int start, int end, int unitLen) {
        for (int i = end - unitLen; i >= start; i--) {
            if (s.charAt(i) != s.charAt(i + unitLen)) return false;
        }
        return true;
    }

    void test(String s, String ... expected) {
        assertThat(encode(s), in(expected));
        assertThat(encode2(s), in(expected));
        assertThat(encode3(s), in(expected));
        assertThat(encode4(s), in(expected));
    }

    @Test
    public void test1() {
        test("aaa", "aaa");
        test("aaaaa", "5[a]");
        test("aaaaaaaaaa", "10[a]", "a9[a]", "9[a]a", "5[aa]");
        test("aaaaaaaaaaa", "11[a]");
        test("aabcaabcd", "2[aabc]d");
        test("abbbabbbcabbbabbbc", "2[2[abbb]c]");
        test("aabcccccddd", "aab5[c]ddd");
        test("aabcccccddd", "aab5[c]ddd");
        test("abcdefghijklmxyzxyzxyzc", "abcdefghijklm3[xyz]c");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
