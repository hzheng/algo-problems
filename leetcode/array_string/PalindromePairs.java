import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/palindrome-pairs/
//
// Given a list of unique words. Find all pairs of distinct indices (i, j) in
// the given list, so that the concatenation of the two words, i.e.
// words[i] + words[j] is a palindrome.
public class PalindromePairs {
    static class TrieNode {
        public static final int SIZE = 26;

        int index = -1;

        TrieNode[] children;

        public TrieNode getChild(char c) {
            return children == null ? null : children[c - 'a'];
        }

        public void setChild(char c, TrieNode child) {
            if (children == null) {
                children = new TrieNode[SIZE];
            }
            children[c - 'a'] = child;
        }
    }

    // beats 26.15%(208 ms)
    public List<List<Integer> > palindromePairs(String[] words) {
        int len = words.length;
        if (len == 0) return Collections.emptyList();

        String[] reversed = new String[len];
        for (int i = 0; i < len; i++) {
            reversed[i] = new StringBuilder(words[i]).reverse().toString();
        }
        Set<List<Integer> > res = new HashSet<>();
        palindromePairs(words, reversed, res, true);
        palindromePairs(reversed, words, res, false);
        return new ArrayList<>(res);
    }

    private void palindromePairs(String[] words, String[] reversed,
                                 Set<List<Integer> > res, boolean dir) {
        int len = words.length;
        TrieNode root = new TrieNode();
        for (int i = 0; i < len; i++) {
            insert(root, reversed[i], i);
        }
        for (int i = 0; i < len; i++) {
            String word = words[i];
            TrieNode node = search(root, word);
            if (node == null) continue;

            findPalindrome(node, i, new StringBuilder(), res, dir);
        }
    }

    private void insert(TrieNode root, String word, int index) {
        TrieNode cur = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (cur.getChild(c) == null) {
                cur.setChild(c, new TrieNode());
            }
            cur = cur.getChild(c);
        }
        cur.index = index;
    }

    private TrieNode search(TrieNode root, String word) {
        TrieNode cur = root;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (cur == null || cur.children == null) return null;

            cur = cur.getChild(c);
        }
        return cur;
    }

    private void findPalindrome(TrieNode node, int index, StringBuilder sb,
                                Set<List<Integer> > res, boolean dir) {
        TrieNode[] children = node.children;
        if (node.index >= 0) {
            if (node.index != index && isPalindrome(sb)) {
                res.add(dir ? Arrays.asList(index, node.index)
                        : Arrays.asList(node.index, index));
            }
        }

        if (children == null) return;

        for (int i = 0; i < children.length; i++) {
            TrieNode child = children[i];
            if (child != null) {
                sb.append((char)('a' + i));
                findPalindrome(child, index, sb, res, dir);
                sb.deleteCharAt(sb.length() - 1);
            }
        }
    }

    private boolean isPalindrome(StringBuilder sb) {
        if (sb.length() == 0) return true;

        for (int i = 0, j = sb.length() - 1; i < j; i++, j--) {
            if (sb.charAt(i) != sb.charAt(j)) return false;
        }
        return true;
    }

    void test(Function<String[], List<List<Integer>>> pairs,
              Integer [][] expected, String ... words) {
        List<List<Integer> > res = pairs.apply(words);
        Integer[][] resArray = res.stream().map(
            a -> a.toArray(new Integer[0])).toArray(Integer[][]::new);
        sort(resArray);
        sort(expected);
        assertArrayEquals(expected, resArray);
    }

    Integer[][] sort(Integer[][] lists) {
        Arrays.sort(lists, (a, b) -> {
            int len = Math.min(a.length, b.length);
            int i = 0;
            for (; i < len && (a[i] == b[i]); i++);
            if (i == len) return a.length - b.length;
            return a[i] - b[i];
        });
        return lists;
    }

    void test(Integer[][] expected, String ... words) {
        PalindromePairs p = new PalindromePairs();
        test(p::palindromePairs, expected, words);
    }

    @Test
    public void test1() {
        test(new Integer[][] {{0, 1}, {1, 0}}, "bat", "tab", "cat");
        test(new Integer[][] {{0, 1}, {1, 0}, {3, 2}, {2, 4}},
             "abcd", "dcba", "lls", "s", "sssll");
        test(new Integer[][] {{0, 1}, {1, 0}, {3, 2}, {2, 4}, {2, 5}, {5, 3}, {5, 2}},
             "abcd", "dcba", "lls", "s", "sssll", "sll");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PalindromePairs");
    }
}
