import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC408: https://leetcode.com/problems/valid-word-abbreviation
//
// Given a non-empty string s and an abbreviation abbr, return whether the
// string matches with the given abbreviation.
public class ValidWordAbbreviation {
    // beats 32.60%(24 ms for 315 tests)
    public boolean validWordAbbreviation(String word, String abbr) {
        int len1 = word.length();
        int len2 = abbr.length();
        if (len1 == 0) return len2 == 0;

        int i = 0;
        int j = 0;
        while (i < len1 && j < len2) {
            char c1 = word.charAt(i);
            char c2 = abbr.charAt(j);
            if (c1 == c2) {
                i++;
                j++;
                continue;
            }
            if (abbr.charAt(j) <= '0' || abbr.charAt(j) > '9') return false;

            int count = 0;
            for (int power = 1; j < len2 && Character.isDigit(abbr.charAt(j)); j++) {
                count *= 10;
                count += (abbr.charAt(j) - '0');
            }
            i += count;
        }
        return i == len1 && j == len2;
    }

    // beats 3.63%(44 ms for 315 tests)
    public boolean validWordAbbreviation2(String word, String abbr) {
        return word.matches(abbr.replaceAll("[1-9]\\d*", ".{$0}"));
    }

    void test(String word, String abbr, boolean expected) {
        assertEquals(expected, validWordAbbreviation(word, abbr));
        assertEquals(expected, validWordAbbreviation2(word, abbr));
    }

    @Test
    public void test1() {
        test("internationalization", "i12iz4n", true);
        test("apple", "a2e", false);
        test("word", "1ord", true);
        test("word", "1rd", false);
        test("a", "01", false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ValidWordAbbreviation");
    }
}
