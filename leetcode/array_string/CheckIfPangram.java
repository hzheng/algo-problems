import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1832: https://leetcode.com/problems/check-if-the-sentence-is-pangram/
//
// A pangram is a sentence where every letter of the English alphabet appears at least once.
// Given a string sentence containing only lowercase English letters, return true if sentence is a
// pangram, or false otherwise.
//
// Constraints:
// 1 <= sentence.length <= 1000
// sentence consists of lowercase English letters.
public class CheckIfPangram {
    // Set
    // time complexity: O(N), space complexity: O(1)
    // 1 ms(100.00%), 37.5 MB(25.00%) for 79 tests
    public boolean checkIfPangram(String sentence) {
        boolean[] set = new boolean[26];
        for (int i = sentence.length() - 1; i >= 0; i--) {
            set[sentence.charAt(i) - 'a'] = true;
        }
        for (boolean b : set) {
            if (!b) { return false; }
        }
        return true;
    }

    // Set
    // time complexity: O(N), space complexity: O(1)
    // 4 ms(25.00%), 39.4 MB(25.00%) for 79 tests
    public boolean checkIfPangram2(String sentence) {
        Set<Character> set = new HashSet<>();
        for (int i = sentence.length() - 1; i >= 0; i--) {
            set.add(sentence.charAt(i));
        }
        return set.size() == 26;
    }

    // Bit Manipulation
    // time complexity: O(N), space complexity: O(1)
    // 2 ms(25.00%), 38.9 MB(25.00%) for 79 tests
    public boolean checkIfPangram3(String sentence) {
        int visited = 0;
        for (int i = sentence.length() - 1; i >= 0; i--) {
            visited |= (1 << (sentence.charAt(i) - 'a'));
        }
        return visited == ((1 << 26) - 1);
    }

    private void test(String sentence, boolean expected) {
        assertEquals(expected, checkIfPangram(sentence));
        assertEquals(expected, checkIfPangram2(sentence));
        assertEquals(expected, checkIfPangram3(sentence));
    }

    @Test public void test() {
        test("thequickbrownfoxjumpsoverthelazydog", true);
        test("leetcode", false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
