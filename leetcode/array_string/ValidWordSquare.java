import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC422: https://leetcode.com/problems/valid-word-square
//
// Given a sequence of words, check whether it forms a valid word square.
// A sequence of words forms a valid word square if the kth row and column read
// the exact same string, where 0 ≤ k < max(numRows, numColumns).
// Note:
// The number of words given is at least 1 and does not exceed 500.
// Word length will be at least 1 and does not exceed 500.
// Each word contains only lowercase English alphabet a-z.
public class ValidWordSquare {
    // beats N/A(17 ms for 33 tests)
    public boolean validWordSquare(List<String> words) {
        int row = words.size();
        for (int i = 0; i < row; i++) {
            String word = words.get(i);
            int col = word.length();
            if (col > row) return false;

            for (int j = 0; j < col; j++) {
                String word2 = words.get(j);
                if (word2.length() <= i || word.charAt(j) != word2.charAt(i)) {
                    return false;
                }
            }
        }
        return true;
    }

    void test(String[] words, boolean expected) {
        List<String> wordList = Arrays.asList(words);
        assertEquals(expected, validWordSquare(wordList));
    }

    @Test
    public void test() {
        test(new String[] {"abcd", "bnrt", "crmy", "dtye"}, true);
        test(new String[] {"abcd", "bnrt", "crm", "dt"}, true);
        test(new String[] {"ball", "area", "read", "lady"}, false);
        test(new String[] {"abc","bde","cefg"}, false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ValidWordSquare");
    }
}
