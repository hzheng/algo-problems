import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC151: https://leetcode.com/problems/reverse-words-in-a-string/
//
// Given an input string, reverse the string word by word.
public class ReverseWords {
    // beats 82.21%(4 ms for 22 tests)
    public String reverseWords(String s) {
        if (s == null || s.length() == 0) return s;

        String[] words = s.split(" ");
        // String[] words = s.split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = words.length - 1; i >= 0; i--) {
            if (words[i].length() > 0) {
                sb.append(words[i]).append(" ");
            }
        }
        return sb.length() == 0 ? "" : sb.substring(0, sb.length() - 1);
    }

    // beats 38.90%(13 ms)
    public String reverseWords2(String s) {
        String[] words = s.trim().split(" +");
        Collections.reverse(Arrays.asList(words));
        return String.join(" ", words);
    }

    // Solution of Choice
    // beats 82.21%(4 ms for 22 tests)
    public String reverseWords3(String s) {
        char[] chars = s.toCharArray();
        int n = chars.length;
        for (int i = 0, j = 0; ; ) {
            for ( ; j < n && chars[j] == ' '; j++) {}
            while (j < n && chars[j] != ' ') {
                chars[i++] = chars[j++];
            }
            for ( ; j < n && chars[j] == ' '; j++) {}
            if (j < n) {
                chars[i++] = ' ';
            } else {
                n = i;
                break;
            }
        }
        reverse(chars, 0, n - 1);
        for (int i = 0, j = 0; j < n; j++) {
            for (i = j; i < n && chars[i] == ' '; i++) {}
            for (j = i + 1; j < n && chars[j] != ' '; j++) {}
            reverse(chars, i, j - 1);
        }
        return new String(chars, 0, n);
    }

    private void reverse(char[] chars, int start, int end) {
        for (int i = start, j = end; i < j; i++, j--) {
            char tmp = chars[i];
            chars[i] = chars[j];
            chars[j] = tmp;
        }
    }

    void test(String s, String expected) {
        assertEquals(expected, reverseWords(s));
        assertEquals(expected, reverseWords2(s));
        assertEquals(expected, reverseWords3(s));
    }

    @Test
    public void test1() {
        test(" the sky is blue ", "blue is sky the");
        test(" the   sky is blues", "blues is sky the");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ReverseWords");
    }
}
