import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/word-pattern/
//
// Given a pattern and a string str, find if str follows the same pattern.
// Here follow means a full match, such that there is a bijection between a
// letter in pattern and a non-empty word in str.
public class WordPattern {
    // beats 21.21%(3 ms)
    public boolean wordPattern(String pattern, String str) {
        // String[] words = str.split("\\s+"); // very slow
        String[] words = str.split(" ");
        if (pattern.length() != words.length) return false;

        Map<Character, String> map = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            char c = pattern.charAt(i);
            String word = words[i];
            // if (map.containsValue(word)) { // slower?
            if (map.values().contains(word)) {
                if (!word.equals(map.get(c))) return false;
            } else {
                if (map.containsKey(c)) return false;

                map.put(c, word);
            }
        }
        return true;
    }

    // beats 84.02%(2 ms)
    public boolean wordPattern2(String pattern, String str) {
        String[] words = str.split(" ");
        if (pattern.length() != words.length) return false;

        String[] map = new String[256];
        Set<String> matchedWord = new HashSet<>();
        for (int i = 0; i < words.length; i++) {
            char c = pattern.charAt(i);
            String word = words[i];

            if (matchedWord.contains(word)) {
                if (!word.equals(map[c])) return false;
            } else {
                if (map[c] != null) return false;

                map[c] = word;
                matchedWord.add(word);
            }
        }
        return true;
    }

    // beats 21.21%(3 ms)
    public boolean wordPattern3(String pattern, String str) {
        String[] words = str.split(" ");
        if (pattern.length() != words.length) return false;

        Map<Object, Integer> map = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            char c = pattern.charAt(i);
            String word = words[i];
            if (!Objects.equals(map.put(c, i), map.put(word, i))) return false;
            // we cannot use object == instead of equals unless i less than 128
            // if (map.put(c, i) != map.put(word, i)) return false;
        }
        return true;
    }

    // beats 21.21%(3 ms)
    public boolean wordPattern4(String pattern, String str) {
        String[] words = str.split(" ");
        if (pattern.length() != words.length) return false;

        Map<Character, String> map = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            char c = pattern.charAt(i);
            String word = words[i];
            if (map.containsKey(c)) {
                if (!word.equals(map.get(c))) return false;
            } else {
                if (map.containsValue(word)) return false;

                map.put(c, word);
            }
        }
        return true;
    }

    void test(String pattern, String str, boolean expected) {
        assertEquals(expected, wordPattern(pattern, str));
        assertEquals(expected, wordPattern2(pattern, str));
        assertEquals(expected, wordPattern3(pattern, str));
        assertEquals(expected, wordPattern4(pattern, str));
    }

    @Test
    public void test1() {
        test("abba", "dog cat cat dog", true);
        test("abba", "dog cat cat fish", false);
        test("aaaa", "dog cat cat dog", false);
        test("abba", "dog dog dog dog", false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("WordPattern");
    }
}
