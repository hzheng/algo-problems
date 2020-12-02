import org.junit.Test;

import static org.junit.Assert.*;

// LC953: https://leetcode.com/problems/verifying-an-alien-dictionary/
//
// In an alien language, surprisingly they also use english lowercase letters, but possibly in a
// different order. The order of the alphabet is some permutation of lowercase letters.
// Given a sequence of words written in the alien language, and the order of the alphabet, return
// true if and only if the given words are sorted lexicographicaly in this alien language.
//
// Constraints:
// 1 <= words.length <= 100
// 1 <= words[i].length <= 20
// order.length == 26
// All characters in words[i] and order are English lowercase letters.
public class VerifyAlienDict {
    // time complexity: O(WORD_CHARS), space complexity: O(1)
    // 0 ms(100.00%), 37.6 MB(78.26%) for 119 tests
    public boolean isAlienSorted(String[] words, String order) {
        char[] orderChars = order.toCharArray();
        char[] map = new char[orderChars.length];
        for (int i = orderChars.length - 1; i >= 0; i--) {
            map[orderChars[i] - 'a'] = (char)('a' + i);
        }
        String prev = convert(words[0], map);
        for (int i = 1; i < words.length; i++) {
            String cur = convert(words[i], map);
            if (cur.compareTo(prev) < 0) { return false; }

            prev = cur;
        }
        return true;
    }

    private String convert(String word, char[] map) {
        char[] cs = word.toCharArray();
        for (int i = cs.length - 1; i >= 0; i--) {
            cs[i] = map[cs[i] - 'a'];
        }
        return String.valueOf(cs);
    }

    // time complexity: O(WORD_CHARS), space complexity: O(1)
    // 0 ms(100.00%), 37.6 MB(78.26%) for 119 tests
    public boolean isAlienSorted2(String[] words, String order) {
        int[] map = new int[order.length()];
        for (int i = order.length() - 1; i >= 0; i--) {
            map[order.charAt(i) - 'a'] = i;
        }
        outer:
        for (int i = 1; i < words.length; i++) {
            String word1 = words[i - 1];
            String word2 = words[i];
            for (int j = 0, len = Math.min(word1.length(), word2.length()); j < len; j++) {
                if (word1.charAt(j) != word2.charAt(j)) {
                    if (map[word1.charAt(j) - 'a'] > map[word2.charAt(j) - 'a']) { return false; }
                    continue outer;
                }
            }
            if (word1.length() > word2.length()) { return false; }
        }
        return true;
    }

    private void test(String[] words, String order, boolean expected) {
        assertEquals(expected, isAlienSorted(words, order));
        assertEquals(expected, isAlienSorted2(words, order));
    }

    @Test public void test() {
        test(new String[] {"hello", "leetcode"}, "hlabcdefgijkmnopqrstuvwxyz", true);
        test(new String[] {"word", "world", "row"}, "worldabcefghijkmnpqstuvxyz", false);
        test(new String[] {"apple", "app"}, "abcdefghijklmnopqrstuvwxyz", false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
