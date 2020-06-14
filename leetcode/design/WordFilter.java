import java.util.*;
import java.lang.reflect.Constructor;

import org.junit.Test;

import static org.junit.Assert.*;

// LC745: https://leetcode.com/problems/prefix-and-suffix-search/
//
// Given many words, words[i] has weight i.
//
// Design a class WordFilter that supports one function, WordFilter.f(String prefix, String suffix).
// It will return the word with given prefix and suffix with maximum weight. If no word exists,
// return -1.
// Note:
// words has length in range [1, 15000].
// For each test case, up to words.length queries WordFilter.f may be made.
// words[i] has length in range [1, 10].
// prefix, suffix have lengths in range [0, 10].
// words[i] and prefix, suffix queries consist of lowercase letters only.
public class WordFilter {
    // Trie
    // 178 ms(53.57%), 81.6 MB(100.00%) for 12 tests
    static class WordFilter1 {
        private final Trie trie = new Trie();
        private static final String DELIM = String.valueOf((char)('z' + 1));

        // time complexity: O(N * L), space complexity: O(N * L)
        public WordFilter1(String[] words) {
            int weight = 0;
            for (String word : words) {
                String w = DELIM + word;
                trie.insert(w, weight);
                for (int j = word.length() - 1; j >= 0; j--) {
                    w = word.charAt(j) + w;
                    trie.insert(w, weight);
                }
                weight++;
            }
        }

        // time complexity: O(P+S), space complexity: O(P+S)
        public int f(String prefix, String suffix) {
            return trie.search(suffix + DELIM + prefix);
        }

        static class Trie {
            static class TrieNode {
                TrieNode[] children = new TrieNode[27];
                int weight;
            }

            private TrieNode root = new TrieNode();

            public void insert(String word, int weight) {
                TrieNode cur = root;
                for (char c : word.toCharArray()) {
                    int i = c - 'a';
                    if (cur.children[i] == null) {
                        cur.children[i] = new TrieNode();
                    }
                    cur = cur.children[i];
                    cur.weight = weight;
                }
            }

            public int search(String word) {
                TrieNode cur = root;
                for (char c : word.toCharArray()) {
                    int i = c - 'a';
                    if (cur.children[i] == null) { return -1; }

                    cur = cur.children[i];
                }
                return cur.weight;
            }
        }
    }

    // Time Limited Exceeded
    static class WordFilter2 {
        private String[] input;

        // time complexity: O(N * L), space complexity: O(N * L)
        public WordFilter2(String[] words) {
            input = words.clone();
        }

        // time complexity: O(N*(P+S)), space complexity: O(P+S)
        public int f(String prefix, String suffix) {
            for (int i = input.length - 1; i >= 0; i--) {
                if (input[i].startsWith(prefix) && input[i].endsWith(suffix)) {
                    return i;
                }
            }
            return -1;
        }
    }

    // 227 ms(41.30%), 70.6 MB(100.00%) for 12 tests
    static class WordFilter3 {
        private Map<String, Integer> map = new HashMap<>();

        // time complexity: O(N * L ^ 2), space complexity: O(N * L ^ 2)
        public WordFilter3(String[] words) {
            int w = 0;
            for (String word : words) {
                for (int i = 0, len = word.length(); i <= len; i++) {
                    for (int j = 0; j <= len; j++) {
                        map.put(word.substring(0, i) + "#" + word.substring(len - j), w);
                    }
                }
                w++;
            }
        }

        // time complexity: O(P+S), space complexity: O(P+S)
        public int f(String prefix, String suffix) {
            Integer w = map.get(prefix + "#" + suffix);
            return (w == null) ? -1 : w;
        }
    }

    void test1(String className) throws Exception {
        test(new String[] {className, "f"},
             new Object[][] {new Object[] {new String[] {"apple"}}, {"a", "e"}},
             new Integer[] {null, 0});
        test(new String[] {className, "f", "f", "f", "f", "f", "f", "f", "f", "f", "f"},
             new Object[][] {new Object[] {
                     new String[] {"abbbababbb", "baaabbabbb", "abababbaaa", "abbbbbbbba",
                                   "bbbaabbbaa", "ababbaabaa", "baaaaabbbb", "babbabbabb",
                                   "ababaababb", "bbabbababa"}}, {"", "abaa"}, {"babbab", ""},
                             {"ab", "baaa"}, {"baaabba", "b"}, {"abab", "abbaabaa"}, {"", "aa"},
                             {"", "bba"}, {"", "baaaaabbbb"}, {"ba", "aabbbb"},
                             {"baaa", "aabbabbb"}},
             new Integer[] {null, 5, 7, 2, 1, 5, 5, 3, 6, 6, 1});
    }

    void test(String[] methods, Object[][] args, Integer[] expected) throws Exception {
        final String name = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        Class<?> clazz = Class.forName(name + "$" + methods[0]);
        Constructor<?> ctor = clazz.getConstructors()[0];
        Object obj = ctor.newInstance(args[0]);
        for (int i = 1; i < methods.length; i++) {
            Object[] arg = args[i];
            Object res = null;
            if (arg.length == 0) {
                res = clazz.getMethod(methods[i]).invoke(obj);
            } else if (arg.length == 2) {
                res = clazz.getMethod(methods[i], String.class, String.class).invoke(obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test public void test1() {
        try {
            test1("WordFilter1");
            test1("WordFilter2");
            test1("WordFilter3");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
