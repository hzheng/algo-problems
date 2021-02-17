import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1156: https://leetcode.com/problems/swap-for-longest-repeated-character-substring/
//
// Given a string text, we are allowed to swap two of the characters in the string. Find the length
// of the longest substring with repeated characters.
//
// Constraints:
// 1 <= text.length <= 20000
// text consist of lowercase English characters only.
public class MaxRepeatedCharacterSubstr {
    // time complexity: O(N), space complexity: O(N)
    // 4 ms(77.19%), 38.7 MB(64.91%) for 53 tests
    public int maxRepOpt1(String text) {
        int res = 1;
        char[] s = text.toCharArray();
        int[] freq = new int[26];
        for (char c : s) {
            freq[c - 'a']++;
        }
        List<int[]> repeats = new ArrayList<>();
        for (int i = 1, prev = 0, n = s.length; i <= n; i++) {
            if (i == n || s[i] != s[i - 1]) {
                repeats.add(new int[] {s[prev], i - prev});
                prev = i;
            }
        }
        for (int i = 0, m = repeats.size(); i < m; i++) {
            int[] repeat = repeats.get(i);
            int count = repeat[1];
            int index = i;
            if (repeat[1] == 1 && i > 0 && i < m - 1 && repeats.get(i - 1)[0] == repeats
                    .get(i + 1)[0]) {
                count = repeats.get(i - 1)[1] + repeats.get(i + 1)[1];
                index--;
            }
            res = Math.max(res, count + (freq[repeats.get(index)[0] - 'a'] > count ? 1 : 0));
        }
        return res;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 7 ms(53.80%), 38.9 MB(52.63%) for 53 tests
    public int maxRepOpt2(String text) {
        Map<Character, List<Integer>> indexMap = new HashMap<>();
        for (int i = 0; i < text.length(); i++) {
            indexMap.computeIfAbsent(text.charAt(i), x -> new ArrayList<>()).add(i);
        }
        int res = 0;
        for (List<Integer> pos : indexMap.values()) {
            int count = 1;
            int n = pos.size();
            for (int i = 1, prev = 0, repeat = 1; i < n; i++) {
                int dist = pos.get(i) - pos.get(i - 1);
                if (dist == 1) {
                    repeat++;
                } else {
                    prev = (dist == 2) ? repeat : 0;
                    repeat = 1;
                }
                count = Math.max(count, repeat + prev);
            }
            res = Math.max(res, count + (count < n ? 1 : 0));
        }
        return res;
    }

    // Sliding Window
    // time complexity: O(N), space complexity: O(N)
    // 5 ms(70.18%), 37.3 MB(96.20%) for 53 tests
    public int maxRepOpt3(String text) {
        int res = 0;
        char[] s = text.toCharArray();
        int[] freq = new int[26];
        for (char c : s) {
            freq[c - 'a']++;
        }
        for (char c = 'a'; c <= 'z'; c++) {
            int i = 0;
            int j = 0;
            for (int gap = 0, n = s.length; i < n; ) {
                gap += (s[i++] != c) ? 1 : 0;
                if (gap > 1) {
                    gap -= (s[j++] != c) ? 1 : 0;
                }
            }
            res = Math.max(res, Math.min(i - j, freq[c - 'a']));
        }
        return res;
    }

    private void test(String text, int expected) {
        assertEquals(expected, maxRepOpt1(text));
        assertEquals(expected, maxRepOpt2(text));
        assertEquals(expected, maxRepOpt3(text));
    }

    @Test public void test() {
        test("ababa", 3);
        test("aaabaaa", 6);
        test("aaabbaaa", 4);
        test("aaaaa", 5);
        test("abcdef", 1);
        test("bbababaaaa", 6);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
