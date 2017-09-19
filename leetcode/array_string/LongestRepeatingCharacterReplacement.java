import org.junit.Test;
import static org.junit.Assert.*;

// LC424: https://leetcode.com/problems/longest-repeating-character-replacement/
//
// Given a string that consists of only uppercase English letters, you can
// replace any letter in the string with another letter at most k times. Find
// the length of a longest substring containing all repeating letters you can
// get after performing the above operations.
// Note:
// Both the string's length and k will not exceed 10 ^ 4.
public class LongestRepeatingCharacterReplacement {
    // Two Pointers
    // beats N/A(12 ms for 34 tests)
    public int characterReplacement(String s, int k) {
        char[] chars = s.toCharArray();
        int[] counts = new int[26];
        int maxCount = 0;
        int maxLen = 0;
        for (int i = 0, j = 0; j < chars.length; j++) {
            char c = s.charAt(j);
            maxCount = Math.max(maxCount, ++counts[c - 'A']);
            // invariant: (length of substring - maxCount in the substring) <= k
            for (; j - i - maxCount + 1 > k; i++) {
                counts[chars[i] - 'A']--;
            }
            maxLen = Math.max(maxLen, j - i + 1);
        }
        return maxLen;
    }

    // Binary Search
    // beats N/A(38 ms for 34 tests)
    public int characterReplacement2(String s, int k) {
        char[] chars = s.toCharArray();
        int low = 1;
        for (int high = s.length() + 1; low < high - 1; ) {
            int mid = (low + high) >>> 1;
            if (isValid(chars, k, mid)) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return low;
    }

    private boolean isValid(char[] chars, int k, int len) {
        int[] cnt = new int[26];
        for (int i = 0; i < chars.length; i++) {
            if (i >= len) {
                cnt[chars[i - len] - 'A']--;
            }
            cnt[chars[i] - 'A']++;
            if (i >= len - 1) {
                int max = 0;
                for (int j : cnt) {
                    max = Math.max(max, j);
                }
                if (len - max <= k) return true;
            }
        }
        return false;
    }

    void test(String s, int k, int expected) {
        assertEquals(expected, characterReplacement(s, k));
        assertEquals(expected, characterReplacement2(s, k));
    }

    @Test
    public void test() {
        test("AABA", 0, 2);
        test("ABBB", 2, 4);
        test("ABAB", 2, 4);
        test("BAAAB", 2, 5);
        test("AABABBA", 1, 4);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LongestRepeatingCharacterReplacement");
    }
}
