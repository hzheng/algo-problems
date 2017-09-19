import org.junit.Test;
import static org.junit.Assert.*;

// LC467: https://leetcode.com/problems/unique-substrings-in-wraparound-string/
//
// String s is the infinite wraparound string of "abcdefghijklmnopqrstuvwxyz".
// Now we have another string p. Your job is to find out how many unique non-empty
// substrings of p are present in s.
public class FindSubstringInWraproundString {
    // beats N/A(491 ms for 81 tests)
    public int findSubstringInWraproundString(String p) {
        char[] chars = p.toCharArray();
        int[] lengths = new int[26];
        int maxLen = 0;
        for (int i = 1, start = 0, len = chars.length; i <= len; i++) {
            if (i == len || (chars[i] - chars[i - 1] + 26) % 26 != 1) {
                int index = chars[start] - 'a';
                lengths[index] = Math.max(lengths[index], i - start);
                maxLen = Math.max(maxLen, lengths[index]);
                start = i;
            }
        }
        int total = 0;
        for (int len = 1; len <= maxLen; len++) {
            boolean[] starts = new boolean[26];
            for (int i = 0; i < 26; i++) {
                int startCh = i;
                for (int j = 0; j <= lengths[i] - len; j++) {
                    if (!starts[(startCh + j) % 26]) {
                        total++;
                        starts[(startCh + j) % 26] = true;
                    }
                }
            }
        }
        return total;
    }

    // beats N/A(13 ms for 81 tests)
    public int findSubstringInWraproundString2(String p) {
        char[] s = p.toCharArray();
        int len = s.length;
        boolean[] hasNext = new boolean[len];
        for (int i = 0; i + 1 < len; i++) {
            hasNext[i] = (s[i] + 1 == s[i + 1] || (s[i] == 'z' && s[i + 1] == 'a'));
        }
        int[] maxLen = new int[26];
        for (int i = 0, j; i < len; ) {
            for (j = i + 1; j < len && hasNext[j - 1]; j++) {}
            for (; i < j; i++) {
                maxLen[s[i] - 'a'] = Math.max(maxLen[s[i] - 'a'], j - i);
            }
        }
        int total = 0;
        for (int i = 0; i < 26; i++) {
            total += maxLen[i];
        }
        return total;
    }

    // beats N/A(14 ms for 81 tests)
    public int findSubstringInWraproundString3(String p) {
        char[] s = p.toCharArray();
        int[] maxLen = new int[26];
        for (int i = 1, start = 0, len = s.length; i <= len; i++) {
            if (i == len || (s[i] - s[i - 1] + 26) % 26 != 1) {
                int index = s[start] - 'a';
                maxLen[index] = Math.max(maxLen[index], i - start);
                start = i;
            }
        }
        int total = 0;
        for (int i = 0; i < 26; i++) {
            int max = 0;
            for (int j = 0; j < 26; j++) {
                max = Math.max(max, maxLen[j] + j - i - ((j > i) ? 26 : 0));
            }
            total += max;
        }
        return total;
    }

    // Solution of Choice
    // beats N/A(16 ms for 81 tests)
    public int findSubstringInWraproundString4(String p) {
        int[] maxLen = new int[26];
        char[] s = p.toCharArray();
        for (int i = s.length - 1, streak = 0; i >= 0; i--) {
            if (i + 1 < s.length && (s[i + 1] - s[i] + 26) % 26 == 1) {
                streak++;
            } else {
                streak = 1;
            }
            maxLen[s[i] - 'a'] = Math.max(maxLen[s[i] - 'a'], streak);
        }
        int total = 0;
        for (int i = 0; i < 26; i++) {
            total += maxLen[i];
        }
        return total;
    }

    // Dynamic Programming
    // beats N/A(21 ms for 81 tests)
    public int findSubstringInWraproundString5(String p) {
        int[] maxLen = new int[26]; // max length of ending letter
        char[] s = p.toCharArray();
        int[] dp = new int[s.length];
        for (int i = 0; i < s.length; i++) {
            dp[i] = (i > 0 && (s[i] - s[i - 1] + 26) % 26 == 1) ? dp[i - 1] + 1 : 1;
            maxLen[s[i] - 'a'] = Math.max(maxLen[s[i] - 'a'], dp[i]);
        }
        int total = 0;
        for (int i = 0; i < 26; i++) {
            total += maxLen[i];
        }
        return total;
    }

    // beats N/A(17 ms for 81 tests)
    public int findSubstringInWraproundString6(String p) {
        char[] s = p.toCharArray();
        int[] maxLen = new int[26];
        int total = 0;
        for (int i = 0, len = 0; i < s.length; i++) {
            if (i > 0 && (s[i] - s[i - 1] + 26) % 26 != 1) {
                len = 0;
            }
            int index = s[i] - 'a';
            if (++len > maxLen[index]) {
                total += len - maxLen[index];
                maxLen[index] = len;
            }
        }
        return total;
    }

    void test(String p, int expected) {
        assertEquals(expected, findSubstringInWraproundString(p));
        assertEquals(expected, findSubstringInWraproundString2(p));
        assertEquals(expected, findSubstringInWraproundString3(p));
        assertEquals(expected, findSubstringInWraproundString4(p));
        assertEquals(expected, findSubstringInWraproundString5(p));
        assertEquals(expected, findSubstringInWraproundString6(p));
    }

    @Test
    public void test() {
        test("", 0);
        test("a", 1);
        test("cac", 2);
        test("zab", 6);
        test("zabdefghzabc", 25);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FindSubstringInWraproundString");
    }
}
