import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC820: https://leetcode.com/problems/short-encoding-of-words/
//
// Given a list of words, we may encode it by writing a reference string S and a
// list of indexes A. e.g if the list of words is ["time", "me", "bell"], we can
// write it as S = "time#bell#" and indexes = [0, 2, 5].
// What is the length of the shortest reference string S possible that encodes
// the given words?
public class ShortEncodingOfWords {
    // Trie + DFS + Recursion
    // beats %(29 ms for 30 tests)
    public int minimumLengthEncoding(String[] words) {
        TrieNode root = new TrieNode();
        for (String word : words) {
            root.insert(word);
        }
        return dfs(root, 1);
    }

    private int dfs(TrieNode root, int depth) {
        int res = 0;
        for (TrieNode child : root.children) {
            if (child != null) {
                res += dfs(child, depth + 1);
            }
        }
        return (res == 0) ? depth : res;
    }

    private static class TrieNode {
        private TrieNode[] children = new TrieNode[26];

        public void insert(String word) {
            TrieNode cur = this;
            for (int i = word.length() - 1; i >= 0; i--) { // reversely insert
                char c = word.charAt(i);
                TrieNode child = cur.children[c - 'a'];
                if (child == null) {
                    child = cur.children[c - 'a'] = new TrieNode();
                }
                cur = child;
            }
        }
    }

    // Trie + Hash Table
    // beats %(39 ms for 30 tests)
    public int minimumLengthEncoding2(String[] words) {
        TrieNode2 root = new TrieNode2();
        Map<TrieNode2, String> nodes = new HashMap<>();
        for (String word : words) {
            TrieNode2 cur = root;
            for (int i = word.length() - 1; i >= 0; i--) {
                cur = cur.get(word.charAt(i));
            }
            nodes.put(cur, word);
        }
        int res = 0;
        for (TrieNode2 node : nodes.keySet()) {
            if (node.isLeaf) {
                res += nodes.get(node).length() + 1;
            }
        }
        return res;
    }

    class TrieNode2 {
        TrieNode2[] children = new TrieNode2[26];;
        boolean isLeaf = true;

        public TrieNode2 get(char c) {
            if (children[c - 'a'] == null) {
                children[c - 'a'] = new TrieNode2();
                isLeaf = false;
            }
            return children[c - 'a'];
        }
    }

    // Set
    // beats %(34 ms for 30 tests)
    public int minimumLengthEncoding3(String[] words) {
        Set<String> references = new HashSet(Arrays.asList(words));
        for (String word : words) {
            for (int i = word.length() - 1; i > 0; i--) {
                references.remove(word.substring(i));
            }
        }
        int res = 0;
        for (String word : references) {
            res += word.length() + 1;
        }
        return res;
    }

    void test(String[] words, int expected) {
        assertEquals(expected, minimumLengthEncoding(words));
        assertEquals(expected, minimumLengthEncoding2(words));
        assertEquals(expected, minimumLengthEncoding3(words));
    }

    @Test
    public void test() {
        test(new String[] {"time", "me", "bell"}, 10);
        test(new String[] {"abcd", "cd", "ab", "bcd"}, 8);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
