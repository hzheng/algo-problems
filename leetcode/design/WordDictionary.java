import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TrieNode;

// LC211: https://leetcode.com/problems/add-and-search-word-data-structure-design/
//
// Design a data structure that supports the following two operations:
// void addWord(word) and bool search(word)
// search(word) can search a literal word or a regular expression string
// containing only letters a-z or .. A . means it can represent any one letter.
public class WordDictionary {
    static abstract class AbstractWordDictionary {
        public void addWords(String ... words) {
            for (String word : words) {
                addWord(word);
            }
        }

        // Adds a word into the data structure.
        public abstract void addWord(String word);

        // Returns if the word is in the data structure. A word could
        // contain the dot character '.' to represent any one letter.
        public abstract boolean search(String word);
    }

    // Solution of Choice
    // Trie + Backtracking + Recursion
    // beats 51.36%(35 ms for 13 tests)
    static class WordDictionary1 extends AbstractWordDictionary {
        private TrieNode root;

        public WordDictionary1() {
            root = new TrieNode();
        }

        public void addWord(String word) {
            TrieNode cur = root;
            for (char c : word.toCharArray()) {
                if (cur.getChild(c) == null) {
                    cur.setChild(c, new TrieNode());
                }
                cur = cur.getChild(c);
            }
            cur.setEnd();
        }

        public boolean search(String word) {
            return search(word, root, 0);
        }

        private boolean search(String word, TrieNode cur, int start) {
            if (start == word.length()) return cur.isEnd();

            char c = word.charAt(start);
            if (c != '.') {
                TrieNode next = cur.getChild(c);
                return (next == null) ? false : search(word, next, start + 1);
            }

            for (c = 'a'; c <= 'z'; c++) {
                TrieNode next = cur.getChild(c);
                if (next != null && search(word, next, start + 1)) return true;
            }
            return false;
        }
    }

    // Trie
    // Time Limit Exceeded(slow addWord, quick search)
    static class WordDictionary2 extends AbstractWordDictionary {
        private TrieNode root;

        public WordDictionary2() {
            root = new TrieNode();
        }

        public void addWord(String word) {
            char[] chars = word.toCharArray();
            for (int i = (1 << chars.length) - 1; i >= 0; i--) {
                addWord(chars, i);
            }
        }

        private void addWord(char[] word, int mask) {
            TrieNode cur = root;
            for (int i = 0; i < word.length; i++) {
                char c = (mask & (1 << i)) == 0 ? word[i] : '.';
                if (cur.get(c) == null) {
                    cur.set(c, new TrieNode());
                }
                cur = cur.get(c);
            }
            cur.setEnd();
        }

        public boolean search(String word) {
            TrieNode cur = root;
            for (char c : word.toCharArray()) {
                cur = cur.get(c);
                if (cur == null) return false;
            }
            return cur.isEnd();
        }
    }

    void testSearch(AbstractWordDictionary dict, String[] words, boolean[] expected) {
        for (int i = 0; i < words.length; i++) {
            assertEquals(expected[i], dict.search(words[i]));
        }
    }

    void test1(AbstractWordDictionary dict) {
        dict.addWords("bad", "dad", "mad");
        testSearch(dict,
                   new String[] {"pad", "bad", ".ad", "b..", "...", "b...", "...."},
                   new boolean[] {false, true, true, true, true, false, false});
    }

    void test2(AbstractWordDictionary dict) {
        dict.addWords("at", "and", "an", "add");
        testSearch(dict, new String[] {"a", ".at"}, new boolean[2]);
        dict.addWord("bat");
        testSearch(dict, new String[] {".at", "an.", "a.d.", "b.", "a.d", "."},
                         new boolean[]{true, true, false, false, true, false});
    }

    @Test
    public void test1() {
        test1(new WordDictionary1());
        test1(new WordDictionary2());
    }

    @Test
    public void test2() {
        test2(new WordDictionary1());
        test2(new WordDictionary2());
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("WordDictionary");
    }
}
