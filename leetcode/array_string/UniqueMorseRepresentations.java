import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC804: https://leetcode.com/problems/unique-morse-code-words/
//
// International Morse Code defines a standard encoding where each letter is
// mapped to a series of dots and dashes, as follows: "a" maps to ".-", "b" maps
// to "-...", "c" maps to "-.-.", and so on.
// Given a list of words, each word can be written as a concatenation of the
// Morse code of each letter.
// Return the number of different transformations among all words we have.
// Note:
// The length of words will be at most 100.
// Each words[i] will have length in range [1, 12].
// words[i] will only consist of lowercase letters.
public class UniqueMorseRepresentations {
    static final String[] CODE = new String[] {
        ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....",
        "..", ".---", "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.",
        "...", "-", "..-", "...-", ".--", "-..-", "-.--", "--.."
    };

    // beats %(9 ms for 83 tests)
    public int uniqueMorseRepresentations(String[] words) {
        Set<String> repr = new HashSet<>();
        for (String word : words) {
            StringBuilder code = new StringBuilder();
            for (char c : word.toCharArray()) {
                code.append(CODE[c - 'a']);
            }
            repr.add(code.toString());
        }
        return repr.size();
    }

    void test(String[] words, int expected) {
        assertEquals(expected, uniqueMorseRepresentations(words));
    }

    @Test
    public void test() {
        test(new String[] {"gin", "zen", "gig", "msg"}, 2);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
