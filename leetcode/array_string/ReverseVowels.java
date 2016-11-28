import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC345: https://leetcode.com/problems/reverse-vowels-of-a-string/
//
// Write a function that reverses only the vowels of a string.
public class ReverseVowels {
    private static final char[] vowels
        = new char[] {'a', 'A', 'o', 'O', 'e', 'E', 'i', 'I', 'u', 'U'};

    // beats 72.05%(6 ms)
    public String reverseVowels(String s) {
        boolean[] isVowel = new boolean[256];
        for (char c : vowels) {
            isVowel[c] = true;
        }
        int len = s.length();
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0, j = len - 1;; i++, j--) {
            while (i < j && !isVowel[s.charAt(i)]) {
                i++;
            }
            while (i < j && !isVowel[s.charAt(j)]) {
                j--;
            }
            if (i >= j) break;

            char tmp = s.charAt(j);
            sb.setCharAt(j, s.charAt(i));
            sb.setCharAt(i, tmp);
        }
        return sb.toString();
    }

    // beats 97.83%(4 ms)
    public String reverseVowels2(String s) {
        boolean[] isVowel = new boolean[256];
        for (char c : vowels) {
            isVowel[c] = true;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, j = chars.length - 1;; i++, j--) {
            while (i < j && !isVowel[chars[i]]) {
                i++;
            }
            while (i < j && !isVowel[chars[j]]) {
                j--;
            }
            if (i >= j) break;

            char tmp = chars[j];
            chars[j] = chars[i];
            chars[i] = tmp;
        }
        return new String(chars);
    }

    // Solution of Choice
    // beats 97.83%(4 ms)
    public String reverseVowels3(String s) {
        boolean[] isVowel = new boolean[256];
        for (char c : vowels) {
            isVowel[c] = true;
        }
        char[] chars = s.toCharArray();
        for (int i = 0, j = chars.length - 1; i < j; ) {
            if (!isVowel[chars[i]]) {
                i++;
            } else if (!isVowel[chars[j]]) {
                j--;
            } else {
                char tmp = chars[j];
                chars[j--] = chars[i];
                chars[i++] = tmp;
            }
        }
        return new String(chars);
    }

    // beats 27.99%(16 ms)
    public String reverseVowels4(String s) {
        Set<Character> vowels = new HashSet<>(Arrays.asList(new Character[] {
            'a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U'
        }));

        char[] chars = s.toCharArray();
        for (int i = 0, j = chars.length - 1; i < j; ) {
            if (!vowels.contains(chars[i])) {
                i++;
            } else if (!vowels.contains(chars[j])) {
                j--;
            } else {
                char tmp = chars[j];
                chars[j] = chars[i];
                chars[i] = tmp;
                i++;
                j--;
            }
        }
        return new String(chars);
    }

    void test(String s, String expected) {
        assertEquals(expected, reverseVowels(s));
        assertEquals(expected, reverseVowels2(s));
        assertEquals(expected, reverseVowels3(s));
        assertEquals(expected, reverseVowels4(s));
    }

    @Test
    public void test1() {
        test("", "");
        test("a", "a");
        test("aA", "Aa");
        test("b", "b");
        test("ab", "ab");
        test("abo", "oba");
        test("hello", "holle");
        test("leetcode", "leotcede");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ReverseVowels");
    }
}
