import org.junit.Test;

import static org.junit.Assert.*;

// LC1668: https://leetcode.com/problems/maximum-repeating-substring/
//
// For a string sequence, a string word is k-repeating if word concatenated k times is a substring
// of sequence. The word's maximum k-repeating value is the highest value k where word is
// k-repeating in sequence. If word is not a substring of sequence, word's maximum k-repeating value
// is 0.
// Given strings sequence and word, return the maximum k-repeating value of word in sequence.
//
// Constraints:
// 1 <= sequence.length <= 100
// 1 <= word.length <= 100
// sequence and word contains only lowercase English letters.
public class MaxRepeating {
    // time complexity: O(N^2), space complexity: O(n)
    // 0 ms(100.00%), 37.3 MB(56.65%) for 211 tests
    public int maxRepeating(String sequence, String word) {
        int k = 1;
        for (; sequence.contains(word.repeat(k)); k++) {}
        return k - 1;
    }

    // time complexity: O(N^2), space complexity: O(n)
    // 1 ms(67.22%), 37.1 MB(71.06%) for 211 tests
    public int maxRepeating2(String sequence, String word) {
        String target = "";
        for (; sequence.contains(target); target += word) {}
        return target.length() / word.length() - 1;
    }

    private void test(String sequence, String word, int expected) {
        assertEquals(expected, maxRepeating(sequence, word));
        assertEquals(expected, maxRepeating2(sequence, word));
    }

    @Test public void test() {
        test("abbaabababc", "ab", 3);
        test("ababc", "ab", 2);
        test("ababc", "ba", 1);
        test("ababc", "ac", 0);
        test("ababaabac", "aba", 2);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
