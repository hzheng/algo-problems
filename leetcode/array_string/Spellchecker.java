import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC966: https://leetcode.com/problems/vowel-spellchecker/
//
// Given a wordlist, we want to implement a spellchecker that converts a query
// word into a correct word. For a given query word, the spell checker handles 
// two categories of spelling mistakes:
// Capitalization: If the query matches a word in the list(case-insensitive),
// then the query word is returned with the same case as the case in the list.
// Vowel Errors: If after replacing the vowels of the query word with any vowel 
// individually, it matches a word in the wordlist (case-insensitive), then the 
// query word is returned with the same case as the match in the wordlist.
// In addition, the spell checker operates under the following precedence rules:
// When the query exactly matches a word in the wordlist (case-sensitive), you
// should return the same word back.
// When the query matches a word up to capitlization, you should return the
// first such match in the wordlist.
// When the query matches a word up to vowel errors, you should return the first
// such match in the wordlist.
// If the query has no matches in the wordlist, return the empty string.
public class Spellchecker {
    // beats 96.30%(22 ms for 53 tests)
    public String[] spellchecker(String[] wordlist, String[] queries) {
        String[] res = new String[queries.length];
        Set<String> words = new HashSet<>();
        Map<String, String> capMap = new HashMap<>();
        Map<String, String> vowelMap = new HashMap<>();
        for (String word : wordlist) {
            words.add(word);
        }
        int n = wordlist.length;
        for (int i = n - 1; i >= 0; i--) {
            String word = wordlist[i];
            capMap.put(word.toUpperCase(), word);
        }
        for (int i = n - 1; i >= 0; i--) {
            String word = wordlist[i];
            vowelMap.put(macro(word), word);
        }
        int i = 0;
        for (String query : queries) {
            res[i++] = check(words, capMap, vowelMap, query);
        }
        return res;
    }

    private String macro(String w) {
        char[] cs = w.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            char c = Character.toUpperCase(cs[i]);
            switch (c) {
            case 'A':
            case 'E':
            case 'I':
            case 'O':
            case 'U':
                cs[i] = '*';
                break;
            default:
                cs[i] = c;
            }
        }
        return String.valueOf(cs);
    }

    private String check(Set<String> words, Map<String, String> capMap,
                         Map<String, String> vowelMap, String query) {
        if (words.contains(query)) return query;

        String match = capMap.get(query.toUpperCase());
        if (match != null) return match;

        match = vowelMap.get(macro(query));
        return (match != null) ? match : "";
    }

    // beats 96.30%(23 ms for 53 tests)
    public String[] spellchecker_2(String[] wordlist, String[] queries) {
        String[] res = new String[queries.length];
        Set<String> words = new HashSet<>();
        Map<String, String> capMap = new HashMap<>();
        Map<String, String> vowelMap = new HashMap<>();
        for (String word : wordlist) {
            words.add(word);
            capMap.putIfAbsent(word.toUpperCase(), word);
            vowelMap.putIfAbsent(macro(word), word);
        }
        int i = 0;
        for (String query : queries) {
            res[i++] = check(words, capMap, vowelMap, query);
        }
        return res;
    }

    // beats 46.30%(63 ms for 53 tests)
    public String[] spellchecker_3(String[] wordlist, String[] queries) {
        String[] res = new String[queries.length];
        Set<String> words = new HashSet<>();
        Map<String, String> capMap = new HashMap<>();
        Map<String, String> vowelMap = new HashMap<>();
        for (String word : wordlist) {
            words.add(word);
            String capWord = word.toUpperCase();
            capMap.putIfAbsent(capWord, word);
            vowelMap.putIfAbsent(capWord.replaceAll("[AEIOU]", "*"), word);
        }
        int i = 0;
        for (String query : queries) {
            if (words.contains(query)) {
                res[i++] = query;
            } else {
                String capQuery = query.toUpperCase();
                String match = capMap.get(capQuery);
                if (match != null) {
                    res[i++] = match;
                } else {
                    match = vowelMap.get(capQuery.replaceAll("[AEIOU]", "*"));
                    res[i++] = (match != null) ? match : "";
                }
            }
        }
        return res;
    }

    void test(String[] wordlist, String[] queries, String[] expected) {
        assertArrayEquals(expected, spellchecker(wordlist, queries));
        assertArrayEquals(expected, spellchecker_2(wordlist, queries));
        assertArrayEquals(expected, spellchecker_3(wordlist, queries));
    }

    @Test
    public void test() {
        test(new String[] {"KiTe", "kite", "hare", "Hare"},
             new String[] {"kite", "Kite", "KiTe", "Hare", "HARE", "Hear", "hear", "keti", "keet",
                           "keto"},
             new String[] {"kite", "KiTe", "KiTe", "Hare", "hare", "", "", "KiTe", "", "KiTe"});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
