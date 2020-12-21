import java.util.*;
import java.lang.reflect.Constructor;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1032: https://leetcode.com/problems/stream-of-characters/
//
// Implement the StreamChecker class as follows:
// StreamChecker(words): Constructor, init the data structure with the given words.
// query(letter): returns true if and only if for some k >= 1, the last k characters queried (in
// order from oldest to newest) spell one of the words in the given list.
//
// Note:
// 1 <= words.length <= 2000
// 1 <= words[i].length <= 2000
// Words will only consist of lowercase English letters.
// Queries will only consist of lowercase English letters.
// The number of queries is at most 40000.
public class StreamChecker {
    private static class TrieNode {
        private final TrieNode[] children = new TrieNode[26];
        private boolean end;

        public TrieNode query(char c) {
            return children[c - 'a'];
        }

        private boolean isEnd() {
            return end;
        }

        public void insert(String word) {
            TrieNode cur = this;
            for (char c : word.toCharArray()) {
                TrieNode node = cur.children[c - 'a'];
                if (node == null) {
                    node = cur.children[c - 'a'] = new TrieNode();
                }
                cur = node;
            }
            cur.end = true;
        }
    }

    // Trie + Queue
    // time complexity: O(W*Q), space complexity: O(W)
    // 658 ms(11.85%), 69.8 MB(86.59%) for 17 tests
    static class StreamChecker1 {
        private final TrieNode root = new TrieNode();
        private final Queue<TrieNode> checkers = new LinkedList<>();

        public StreamChecker1(String[] words) {
            for (String word : words) {
                root.insert(word);
            }
        }

        public boolean query(char letter) {
            boolean res = false;
            checkers.offer(root);
            for (int i = checkers.size(); i > 0; i--) {
                TrieNode cur = checkers.poll();
                cur = cur.query(letter);
                if (cur != null) {
                    checkers.offer(cur);
                    res |= cur.isEnd();
                }
            }
            return res;
        }
    }

    // Trie
    // time complexity: O(W*Q), space complexity: O(W)
    // 81 ms(91.12%), 71.6 MB(71.43%) for 17 tests
    static class StreamChecker2 {
        private final TrieNode root = new TrieNode();
        private final StringBuilder sb = new StringBuilder();

        public StreamChecker2(String[] words) {
            for (String word : words) {
                root.insert(new StringBuilder(word).reverse().toString());
            }
        }

        public boolean query(char letter) {
            sb.append(letter);
            TrieNode cur = root;
            for (int i = sb.length() - 1; i >= 0; i--) {
                cur = cur.query(sb.charAt(i));
                if (cur == null) { return false; }

                if (cur.isEnd()) { return true; }
            }
            return false;
        }
    }

    void test1(String className) throws Exception {
        test(new String[] {className, "query", "query", "query", "query", "query", "query", "query",
                           "query", "query", "query", "query", "query"},
             new Object[][] {{new String[] {"cd", "f", "kl"}}, {'a'}, {'b'}, {'c'}, {'d'}, {'e'},
                             {'f'}, {'g'}, {'h'}, {'i'}, {'j'}, {'k'}, {'l'}},
             new Object[] {null, false, false, false, true, false, true, false, false, false, false,
                           false, true});
        test(new String[] {className, "query", "query", "query", "query", "query", "query", "query",
                           "query", "query", "query", "query", "query"},
             new Object[][] {{new String[] {"leetcode", "code", "leet", "a", "this", "is"}}, {'t'},
                             {'h'}, {'i'}, {'s'}, {'a'}, {'l'}, {'e'}, {'e'}, {'t'}, {'c'}, {'o'},
                             {'d'}, {'e'}},
             new Object[] {null, false, false, false, true, true, false, false, false, true, false,
                           false, false, true});
    }

    void test(String[] methods, Object[][] args, Object[] expected) throws Exception {
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
            } else if (arg.length == 1) {
                res = clazz.getMethod(methods[i], char.class).invoke(obj, arg);
            } else if (arg.length == 2) {
                res = clazz.getMethod(methods[i], int.class, String.class).invoke(obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test public void test1() {
        try {
            test1("StreamChecker1");
            test1("StreamChecker2");
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
