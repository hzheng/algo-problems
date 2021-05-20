import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1859: https://leetcode.com/problems/sorting-the-sentence/
//
// A sentence is a list of words that are separated by a single space with no leading or trailing
// spaces. Each word consists of lowercase and uppercase English letters.
// A sentence can be shuffled by appending the 1-indexed word position to each word then rearranging
// the words in the sentence.
// For example, the sentence "This is a sentence" can be shuffled as "sentence4 a3 is2 This1" or
// "is2 sentence4 This1 a3".
// Given a shuffled sentence s containing no more than 9 words, reconstruct and return the original
// sentence.
//
// Constraints:
// 2 <= s.length <= 200
// s consists of lowercase and uppercase English letters, spaces, and digits from 1 to 9.
// The number of words in s is between 1 and 9.
// The words in s are separated by a single space.
// s contains no leading or trailing spaces.
public class SortSentence {
    // Sort
    // time complexity: O(W*log(W)+N), space complexity: O(N)
    // 2 ms(57.57%), 37.2 MB(75.01%) for 45 tests
    public String sortSentence(String s) {
        String[] words = s.split(" ");
        Arrays.sort(words, Comparator.comparingInt(a -> a.charAt(a.length() - 1)));
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(word, 0, word.length() - 1);
            sb.append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    // time complexity: O(N), space complexity: O(N)
    // 2 ms(57.57%), 37.2 MB(75.01%) for 45 tests
    public String sortSentence2(String s) {
        String[] words = s.split(" ");
        String[] res = new String[words.length];
        for (String word : words) {
            res[word.charAt(word.length() - 1) - '1'] = word.substring(0, word.length() - 1);
        }
        return String.join(" ", res);
    }

    private void test(String s, String expected) {
        assertEquals(expected, sortSentence(s));
        assertEquals(expected, sortSentence2(s));
    }

    @Test public void test1() {
        test("is2 sentence4 This1 a3", "This is a sentence");
        test("Myself2 Me1 I4 and3", "Me Myself and I");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
