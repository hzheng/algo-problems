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

    // Trie + backtracking
    // beats 32.41%(172 ms)
    public List<List<Integer> > palindromePairs(String[] words) {
        int len = words.length;
        if (len == 0) return Collections.emptyList();

        String[] reversed = new String[len];
        for (int i = 0; i < len; i++) {
            reversed[i] = new StringBuilder(words[i]).reverse().toString();
        }
        Set<List<Integer> > res = new HashSet<>();
        palindromePairs(words, reversed, res, true);
        // TODO: avoid call twice
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
            TrieNode node = search(root, words[i]);
            if (node != null) {
                findPalindrome(node, i, new StringBuilder(), res, dir);
            }
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
        if (node.index >= 0 && node.index != index && isPalindrome(sb)) {
            res.add(dir ? Arrays.asList(index, node.index)
                        : Arrays.asList(node.index, index));
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
        for (int i = 0, j = sb.length() - 1; i < j; i++, j--) {
            if (sb.charAt(i) != sb.charAt(j)) return false;
        }
        return true;
    }

    // Hash Table
    // https://discuss.leetcode.com/topic/39631/accepted-short-java-solution-using-hashmap
    // time complexity: O(N * K ^ 2)
    // beats 16.69%(261 ms)
    public List<List<Integer>> palindromePairs2(String[] words) {
        List<List<Integer>> res = new LinkedList<>();
        int len = words.length;
        if (len == 0) return res;

        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < len; i++) {
            map.put(words[i], i);
        }

        for (int i = 0; i < len; i++) {
            String word = words[i];
            for (int left = 0, right = 0; left <= right; ) {
                String s = word.substring(left, right);
                Integer j = map.get(new StringBuilder(s).reverse().toString());
                if (j != null && i != j) {
                    if (left == 0 && isPalindrome(word.substring(right))) {
                        res.add(Arrays.asList(new Integer[]{i, j}));
                    } else if (left > 0 && isPalindrome(word.substring(0, left))) {
                        res.add(Arrays.asList(new Integer[]{j, i}));
                    }
                }
                if (right < word.length()) {
                    right++;
                } else {
                    left++;
                }
            }
        }
        return res;
    }

    private boolean isPalindrome(String s) {
        for (int i = 0, j = s.length() - 1; i < j; i++, j--) {
            if (s.charAt(i) != s.charAt(j)) return false;
        }
        return true;
    }

    // TODO: Manacher algorithm
    // https://discuss.leetcode.com/topic/39512/java-naive-154-ms-o-nk-2-r-and-126-ms-o-nk-r-manacher-suffixes-prefixes

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
        test(p::palindromePairs2, expected, words);
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
