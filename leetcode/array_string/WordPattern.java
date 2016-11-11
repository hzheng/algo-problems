import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC290: https://leetcode.com/problems/word-pattern/
//
// Given a pattern and a string str, find if str follows the same pattern.
// Here follow means a full match, such that there is a bijection between a
// letter in pattern and a non-empty word in str.
public class WordPattern {
    // Hash Table
    // beats 50.38%(2 ms for 29 tests)
    public boolean wordPattern(String pattern, String str) {
        // String[] words = str.split("\\s+"); // very slow
        String[] words = str.split(" ");
        if (pattern.length() != words.length) return false;

        Map<Character, String> map = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            char c = pattern.charAt(i);
            String word = words[i];
            if (map.containsValue(word)) { // or (map.values().contains(word))
                if (!word.equals(map.get(c))) return false;
            } else if (map.put(c, word) != null) return false;
        }
        return true;
    }

    // Hash Table + Set
    // beats 96.73%(1 ms for 29 tests)
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

    // Solution of Choice
    // Hash Table
    // beats 50.38%(2 ms for 29 tests)
    public boolean wordPattern3(String pattern, String str) {
        String[] words = str.split(" ");
        if (pattern.length() != words.length) return false;

        Map<Object, Integer> map = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            if (!Objects.equals(map.put(pattern.charAt(i), i),
                                map.put(words[i], i))) return false;
            // we cannot use object == instead of equals unless i less than 128
            // if (map.put(pattern.charAt(i), i) != map.put(words[i], i)) return false;
        }
        return true;
    }

    // Hash Table
    // beats 19.28%(3 ms for 29 tests)
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
        test("aaa", "aa aa aa", true);
        test("aaa", "aa aa aa aa", false);
        test("abba", "dog cat cat dog", true);
        test("abba", "dog cat cat fish", false);
        test("aaaa", "dog cat cat dog", false);
        test("abba", "dog dog dog dog", false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("WordPattern");
    }
}
