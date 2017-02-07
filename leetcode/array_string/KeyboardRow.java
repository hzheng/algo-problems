import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.junit.Test;
import static org.junit.Assert.*;

// LC500: https://leetcode.com/problems/keyboard-row/
//
// Given a List of words, return the words that can be typed using letters of
// alphabet on only one row's of American keyboard.
public class KeyboardRow {
    private static final char[][] KEYBOARD_ROWS = {
        "asdfghjkl".toCharArray(),
        "qwertyuiop".toCharArray(),
        "zxcvbnm".toCharArray()
    };

    // Hash Table
    // beats 72.20%(3 ms for 22 tests)
    public String[] findWords(String[] words) {
        int[] map = new int[26];
        for (int i = 0; i < 3; i++) {
            for (char c : KEYBOARD_ROWS[i]) {
                map[c - 'a'] = i;
            }
        }
        List<String> res = new ArrayList<>();
outer:
        for (String word : words) {
            int index = map[Character.toLowerCase(word.charAt(0)) - 'a'];
            for (int i = word.length() - 1; i > 0; i--) {
                if (map[Character.toLowerCase(word.charAt(i)) - 'a'] != index)
                    continue outer;
            }
            res.add(word);
        }
        return res.toArray(new String[0]);
    }

    private static final String KEYBOARD_PAT_STR = "[qwertyuiop]*|[asdfghjkl]*|[zxcvbnm]*";

    // Regex
    // beats 2.53%(92 ms for 22 tests)
    public String[] findWords2(String[] words) {
        return Stream.of(words).filter(s -> s.toLowerCase().matches(KEYBOARD_PAT_STR))
               .toArray(String[]::new);
    }

    private static final Pattern KEYBOARD_PAT = Pattern.compile(KEYBOARD_PAT_STR,
                                                                Pattern.CASE_INSENSITIVE);

    // Regex
    // beats 0.36%(106 ms for 22 tests)
    public String[] findWords3(String[] words) {
        return Stream.of(words).filter(s -> KEYBOARD_PAT.matcher(s).matches())
               .toArray(String[]::new);
    }

    // private static final String[] KEYBOARD_SORTED_ROWS = {
    // "adfghjkls", "eiopqrtuwy", "bcmnvxz"};
    void test(String[] words, String ... expected) {
        assertArrayEquals(expected, findWords(words));
        assertArrayEquals(expected, findWords2(words));
        assertArrayEquals(expected, findWords3(words));
    }

    @Test
    public void test() {
        test(new String[] {"Hello", "Alaska", "Dad", "Peace"}, "Alaska", "Dad");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("KeyboardRow");
    }
}
