import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1935: https://leetcode.com/problems/maximum-number-of-words-you-can-type/
//
// There is a malfunctioning keyboard where some letter keys do not work. All other keys on the
// keyboard work properly.
// Given a string text of words separated by a single space (no leading or trailing spaces) and a
// string brokenLetters of all distinct letter keys that are broken, return the number of words in
// text you can fully type using this keyboard.
//
// Constraints:
// 1 <= text.length <= 10^4
// 0 <= brokenLetters.length <= 26
// text consists of words separated by a single space without any leading or trailing spaces.
// Each word only consists of lowercase English letters.
// brokenLetters consists of distinct lowercase English letters.
public class MaximumTypedWords {
    // Set
    // time complexity: O(N+M), space complexity: O(M)
    // 2 ms(80.00%), 38.9 MB(100%) for 20 tests
    public int canBeTypedWords(String text, String brokenLetters) {
        int res = 0;
        Set<Character> bad = new HashSet<>();
        for (char c : brokenLetters.toCharArray()) {
            bad.add(c);
        }
        outer:
        for (String w : text.split(" ")) {
            for (char c : w.toCharArray()) {
                if (bad.contains(c)) { continue outer; }
            }
            res++;
        }
        return res;
    }

    // time complexity: O(N+M), space complexity: O(M)
    // 1 ms(100.00%), 37.5 MB(100%) for 20 tests
    public int canBeTypedWords2(String text, String brokenLetters) {
        boolean[] broken = new boolean[26];
        for (char c : brokenLetters.toCharArray()) {
            broken[c - 'a'] = true;
        }
        int res = 0;
        int cnt = 0;
        for (char c : text.toCharArray())
            if (c == ' ') {
                res += (cnt == 0) ? 1 : 0;
                cnt = 0;
            } else {
                cnt += broken[c - 'a'] ? 1 : 0;
            }
        return res + ((cnt == 0) ? 1 : 0);
    }

    // Bit Manipulation
    // time complexity: O(N+M), space complexity: O(M)
    // 1 ms(100.00%), 37.4 MB(100%) for 20 tests
    public int canBeTypedWords3(String text, String brokenLetters) {
        int mask = 0;
        for (int i = brokenLetters.length() - 1; i >= 0; i--) {
            mask |= 1 << ((brokenLetters.charAt(i) - 'a'));
        }
        int res = 0;
        int cur = 0;
        for (char c : text.toCharArray()) {
            if (c == ' ') {
                res += ((cur & mask) == 0) ? 1 : 0;
                cur = 0;
            } else {
                cur |= 1 << (c - 'a');
            }
        }
        return res + ((cur & mask) == 0 ? 1 : 0);
    }

    void test(String text, String brokenLetters, int expected) {
        assertEquals(expected, canBeTypedWords(text, brokenLetters));
        assertEquals(expected, canBeTypedWords2(text, brokenLetters));
        assertEquals(expected, canBeTypedWords3(text, brokenLetters));
    }

    @Test public void test() {
        test("hello world", "ad", 1);
        test("leet code", "lt", 1);
        test("hello world this is a pretty long test", "ade", 3);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
