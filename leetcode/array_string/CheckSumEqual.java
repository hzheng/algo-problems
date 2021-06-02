import org.junit.Test;

import static org.junit.Assert.*;

// LC1880: https://leetcode.com/problems/check-if-word-equals-summation-of-two-words/
//
// The letter value of a letter is its position in the alphabet starting from 0 (i.e. 'a' -> 0,
// 'b' -> 1, 'c' -> 2, etc.).
// The numerical value of some string of lowercase English letters s is the concatenation of the
// letter values of each letter in s, which is then converted into an integer.
// For example, if s = "acb", we concatenate each letter's letter value, resulting in "021". After
// converting it, we get 21.
// You are given three strings firstWord, secondWord, and targetWord, each consisting of lowercase
// English letters 'a' through 'j' inclusive.
// Return true if the summation of the numerical values of firstWord and secondWord equals the
// numerical value of targetWord, or false otherwise.
//
// Constraints:
// 1 <= firstWord.length, secondWord.length, targetWord.length <= 8
// firstWord, secondWord, and targetWord consist of lowercase English letters from 'a' to 'j'
// inclusive.
public class CheckSumEqual {
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 38.8 MB(40.61%) for 100 tests
    public boolean isSumEqual(String firstWord, String secondWord, String targetWord) {
        return convert(firstWord) + convert(secondWord) == convert(targetWord);
    }

    private int convert(String s) {
        int res = 0;
        for (int i = 0, n = s.length(); i < n; i++) {
            res *= 10;
            res += s.charAt(i) - 'a';
        }
        return res;
    }

    private void test(String firstWord, String secondWord, String targetWord, boolean expected) {
        assertEquals(expected, isSumEqual(firstWord, secondWord, targetWord));
    }

    @Test public void test1() {
        test("acb", "cba", "cdb", true);
        test("aaa", "a", "aab", false);
        test("aaa", "a", "aaaa", true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
