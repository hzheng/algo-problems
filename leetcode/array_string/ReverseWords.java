import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/reverse-words-in-a-string/
//
// Given an input string, reverse the string word by word.
public class ReverseWords {
    // beats 71.19%
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

    void test(String s, String expected) {
        assertEquals(expected, reverseWords(s));
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
