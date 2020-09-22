import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1592: https://leetcode.com/problems/rearrange-spaces-between-words/
//
// You are given a string text of words that are placed among some number of spaces. Each word
// consists of one or more lowercase English letters and are separated by at least one space. It's
// guaranteed that text contains at least one word. Rearrange the spaces so that there is an equal
// number of spaces between every pair of adjacent words and that number is maximized. If you cannot redistribute all the spaces equally, place the extra spaces at the end, meaning the returned string should be the same length as text.
// Return the string after rearranging the spaces.
public class ReorderSpaces {
    // time complexity: O(N), space complexity: O(N)
    // 3 ms(65.89%), 37.8 MB(89.50%) for 88 tests
    public String reorderSpaces(String text) {
        int spaces = 0;
        for (char c : text.toCharArray()) {
            if (c == ' ') {
                spaces++;
            }
        }
        String[] words = text.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        int n = words.length;
        int gap = (n < 2) ? 0 : spaces / (n - 1);
        for (int j = 0; j < n; j++) {
            String w = words[j];
            sb.append(w);
            if (j < n - 1) {
                sb.append(" ".repeat(Math.max(0, gap)));
            }
        }
        int extraSpaces = (gap == 0) ? spaces : spaces % (n - 1);
        for (int i = 0; i < extraSpaces; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }

    // time complexity: O(N), space complexity: O(N)
    // 5 ms(43.25%), 38.2 MB(78.14%) for 88 tests
    public String reorderSpaces2(String text) {
        int spaces = (int)text.chars().filter(c -> c == ' ').count();
        String[] words = text.trim().split("\\s+");
        int n = words.length;
        int gap = (n < 2) ? 0 : spaces / (n - 1);
        int extraSpaces = (gap == 0) ? spaces : spaces % (n - 1);
        return String.join(" ".repeat(gap), words) + " ".repeat(extraSpaces);
    }

    void test(String text, String expected) {
        assertEquals(expected, reorderSpaces(text));
        assertEquals(expected, reorderSpaces2(text));
    }

    @Test public void test() {
        test("  this   is  a sentence ", "this   is   a   sentence");
        test(" practice   makes   perfect", "practice   makes   perfect ");
        test("hello   world","hello   world");
        test("  walks  udp package   into  bar a","walks  udp  package  into  bar  a ");
        test("a", "a");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
