import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1816: https://leetcode.com/problems/truncate-sentence/
//
// A sentence is a list of words that are separated by a single space with no leading or trailing
// spaces. Each of the words consists of only uppercase and lowercase English letters.
// For example, "Hello World", "HELLO", and "hello world hello world" are all sentences.
// You are given a sentence s and an integer k. You want to truncate s such that it contains only
// the first k words. Return s after truncating it.
//
// Constraints:
// 1 <= s.length <= 500
// k is in the range [1, the number of words in s].
// s consist of only lowercase and uppercase English letters and spaces.
// The words in s are separated by a single space.
// There are no leading or trailing spaces.
public class TruncateSentence {
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 37 MB(%) for 72 tests
    public String truncateSentence(String s, int k) {
        int i = 0;
        int n = s.length();
        for (; i < n && k > 0; i++) {
            if (s.charAt(i) == ' ') {
                k--;
            }
        }
        return s.substring(0, (i == n ? n : i - 1));
    }

    // time complexity: O(N), space complexity: O(1)
    // 1 ms(%), 37.5 MB(%) for 72 tests
    public String truncateSentence2(String s, int k) {
        String[] words = s.split(" ");
        StringBuilder sb = new StringBuilder();
        sb.append(words[0]);
        for (int i = 1; i < k; i++) {
            sb.append(' ');
            sb.append(words[i]);
        }
        return sb.toString();
    }

    private void test(String s, int k, String expected) {
        assertEquals(expected, truncateSentence(s, k));
        assertEquals(expected, truncateSentence2(s, k));
    }

    @Test public void test() {
        test("Hello how are you Contestant", 4, "Hello how are you");
        test("What is the solution to this problem", 4, "What is the solution");
        test("chopper is not a tanuki", 5, "chopper is not a tanuki");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
