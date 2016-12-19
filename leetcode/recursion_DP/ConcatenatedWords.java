import java.util.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TrieNode;

// LC472: https://leetcode.com/problems/concatenated-words
//
// Given a list of words, please write a program that returns all concatenated
// words in the given list of words.
public class ConcatenatedWords {
    // Recursion + Trie
    // beats N/A(167 ms for 43 tests)
    public List<String> findAllConcatenatedWordsInADict(String[] words) {
        Trie trie = new Trie();
        for (String word : words) {
            trie.insert(word);
        }
        List<String> res = new ArrayList<>();
        for (String word : words) {
            if (isConcatenated(word, trie)) {
                res.add(word);
            }
        }
        return res;
    }

    private boolean isConcatenated(String word, Trie trie) {
        int len = word.length();
        for (int i = 1; i < len; i++) {
            if (trie.search(word.substring(0, i))) {
                String subWord = word.substring(i);
                if (trie.search(subWord) || isConcatenated(subWord, trie)) return true;
            }
        }
        return false;
    }

    private static class Trie {
        private TrieNode root = new TrieNode();

        private void insert(String word) {
            TrieNode cur = root;
            for (char c : word.toCharArray()) {
                if (cur.getChild(c) == null) {
                    cur.setChild(c, new TrieNode());
                }
                cur = cur.getChild(c);
            }
            cur.setEnd();
        }

        private boolean search(String word) {
            TrieNode cur = root;
            for (char c : word.toCharArray()) {
                if (cur == null) return false;

                cur = cur.getChild(c);
            }
            return cur != null && cur.isEnd();
        }
    }

    // Recursion + Hash Table
    // beats N/A(95 ms for 43 tests)
    public List<String> findAllConcatenatedWordsInADict2(String[] words) {
        Set<String> dict = new HashSet<>();
        for (String word : words) {
            dict.add(word);
        }
        List<String> res = new ArrayList<>();
        for (String word : words) {
            if (isComposite(word, dict)) {
                res.add(word);
            }
        }
        return res;
    }

    private boolean isComposite(String word, Set<String> dict) {
        int len = word.length();
        for (int i = 1; i < len; i++) {
            if (dict.contains(word.substring(0, i))) {
                String subWord = word.substring(i);
                if (dict.contains(subWord) || isComposite(subWord, dict)) return true;
            }
        }
        return false;
    }

    // Dynamic Programming
    // beats N/A(477 ms for 43 tests)
    public List<String> findAllConcatenatedWordsInADict3(String[] words) {
        Arrays.sort(words, new Comparator<String>() {
            public int compare(String s1, String s2) {
                return s1.length() - s2.length();
            }
        });
        List<String> res = new ArrayList<>();
        Set<String> dict = new HashSet<>();
        for (String word : words) {
            if (!dict.isEmpty() && isCombined(word, dict)) {
                res.add(word);
            }
            dict.add(word);
        }
        return res;
    }

    private boolean isCombined(String word, Set<String> dict) {
        int len = word.length();
        boolean[] dp = new boolean[len + 1];
        dp[0] = true;
        for (int i = 1; i <= len; i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && dict.contains(word.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[len];
    }

    void test(String[] words, String ... expected) {
        ConcatenatedWords c = new ConcatenatedWords();
        test(c::findAllConcatenatedWordsInADict, words, expected);
        test(c::findAllConcatenatedWordsInADict2, words, expected);
        test(c::findAllConcatenatedWordsInADict3, words, expected);
    }

    void test(Function<String[], List<String> > findConcatenatedWords,
              String[] words, String ... expected) {
        List<String> res = findConcatenatedWords.apply(words);
        Collections.sort(res);
        assertArrayEquals(expected, res.toArray(new String[0]));
    }

    @Test
    public void test1() {
        test(new String[] {""});
        test(new String[] {"cat", "cats", "catsdogcats", "dog", "dogcatsdog",
                           "hippopotamuses", "rat", "ratcatdogcat"},
             "catsdogcats","dogcatsdog","ratcatdogcat");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ConcatenatedWords");
    }
}
