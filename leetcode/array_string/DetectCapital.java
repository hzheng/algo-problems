import java.util.regex.Pattern;

import org.junit.Test;
import static org.junit.Assert.*;

// LC520: https://leetcode.com/problems/detect-capital/
//
// Given a word, you need to judge whether the usage of capitals in it is right or not.
// We define the usage of capitals in a word to be right when one of the following cases holds:
// All letters in this word are capitals, like "USA".
// All letters in this word are not capitals, like "leetcode".
// Only the first letter in this word is capital if it has more than one letter
public class DetectCapital {
    // beats 95.79%(27 ms for 550 tests)
    public boolean detectCapitalUse(String word) {
        int len = word.length();
        if (len == 1) return true;

        boolean firstCapital = Character.isUpperCase(word.charAt(0));
        boolean secondCapital = Character.isUpperCase(word.charAt(1));
        if (!firstCapital && secondCapital) return false;

        boolean isCapital = firstCapital && secondCapital;
        for (int i = 2; i < len; i++) {
            if (isCapital != Character.isUpperCase(word.charAt(i))) return false;
        }
        return true;
    }

    // beats 82.90%(29 ms for 550 tests)
    public boolean detectCapitalUse2(String word) {
        int len = word.length();
        for (int i = 2; i < len; i++) {
            if (Character.isUpperCase(word.charAt(i)) != Character.isUpperCase(word.charAt(i - 1))) return false;
        }
        return len < 2 || Character.isUpperCase(word.charAt(0)) || Character.isLowerCase(word.charAt(1));
    }

    // beats 89.42%(28 ms for 550 tests)
    public boolean detectCapitalUse2_2(String word) {
        int len = word.length();
        if (len == 1) return true;

        boolean secondCapital = Character.isUpperCase(word.charAt(1));
        for (int i = 2; i < len; i++) {
            if (Character.isUpperCase(word.charAt(i)) != secondCapital) return false;
        }
        return !secondCapital || Character.isUpperCase(word.charAt(0));
    }

    // beats 89.42%(28 ms for 550 tests)
    public boolean detectCapitalUse3(String word) {
        int capitals = 0;
        char[] cs = word.toCharArray();
        for (char c : cs) {
            if (c <= 'Z') {
                capitals++;
            }
        }
        return capitals == 0 || capitals == cs.length
               || (capitals == 1 && Character.isUpperCase(word.charAt(0)));
    }

    // Regex
    // beats 12.48%(46 ms for 550 tests)
    public boolean detectCapitalUse4(String word) {
        return word.matches("[A-Z]?[a-z]*|[A-Z]+");
    }

    private static final Pattern CAPITAL_PAT = Pattern.compile("[A-Z]?[a-z]*|[A-Z]+");

    // Regex
    // beats 32.02%(36 ms for 550 tests)
    public boolean detectCapitalUse5(String word) {
        return CAPITAL_PAT.matcher(word).matches();
    }

    void test(String word, boolean expected) {
        assertEquals(expected, detectCapitalUse(word));
        assertEquals(expected, detectCapitalUse2(word));
        assertEquals(expected, detectCapitalUse2_2(word));
        assertEquals(expected, detectCapitalUse3(word));
        assertEquals(expected, detectCapitalUse4(word));
        assertEquals(expected, detectCapitalUse5(word));
    }

    @Test
    public void test() {
        test("A", true);
        test("USA", true);
        test("Google", true);
        test("FlaG", false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("DetectCapital");
    }
}
