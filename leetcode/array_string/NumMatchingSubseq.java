import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC792: https://leetcode.com/problems/number-of-matching-subsequences/
//
// Given string S and a dictionary of words words, find the number of words[i]
// that is a subsequence of S.
// Note:
// All words in words and S will only consists of lowercase letters.
// The length of S will be in the range of [1, 50000].
// The length of words will be in the range of [1, 5000].
// The length of words[i] will be in the range of [1, 50].
public class NumMatchingSubseq {
    // beats %(950 ms for 49 tests)
    // time complexity: O(words.length*S.length+sum(words[i].length))
    public int numMatchingSubseq(String S, String[] words) {
        int res = 0;
        char[] cs = S.toCharArray();
        for (String word : words) {
            if (isSubseq(cs, word)) {
                res++;
            }
        }
        return res;
    }

    private boolean isSubseq(char[] cs, String word) {
        outer : for (int i = 0, j = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            while (j < cs.length) {
                if (cs[j++] == c) continue outer;
            }
            return false;
        }
        return true;
    }

    // Time Limit Exceeded
    // time complexity: O(words.length*S.length+sum(words[i].length))
    public int numMatchingSubseq2(String S, String[] words) {
        int res = 0;
        char[] cs = S.toCharArray();
        for (String word : words) {
            if (isSubseq2(cs, word)) {
                res++;
            }
        }
        return res;
    }

    private boolean isSubseq2(char[] cs, String word) {
        int i = 0;
        int len = word.length();
        for (char c : cs) {
            if (i < len && c == word.charAt(i)) {
                i++;
            }
        }
        return i == len;
    }

    // Next Letter Pointers
    // time complexity: O(S.length+sum(words[i].length))
    // beats %(104 ms for 49 tests)
    public int numMatchingSubseq3(String S, String[] words) {
        int res = 0;
        @SuppressWarnings("unchecked")
        List<Node>[] heads = new List[26];
        for (int i = 0; i < 26; i++) {
            heads[i] = new ArrayList<>();
        }
        for (String word : words) {
            heads[word.charAt(0) - 'a'].add(new Node(word, 0));
        }
        for (char c : S.toCharArray()) {
            List<Node> oldBucket = heads[c - 'a'];
            heads[c - 'a'] = new ArrayList<>();
            for (Node node : oldBucket) {
                if (++node.index == node.word.length()) {
                    res++;
                } else {
                    heads[node.word.charAt(node.index) - 'a'].add(node);
                }
            }
        }
        return res;
    }

    class Node {
        String word;
        int index;
        public Node(String word, int index) {
            this.word = word;
            this.index = index;
        }
    }

    // Map + Queue
    // time complexity: O(S.length+sum(words[i].length))
    // beats %(143 ms for 49 tests)
    public int numMatchingSubseq4(String S, String[] words) {
        Map<Character, Queue<String> > map = new HashMap<>();
        for (char c = 'a'; c <= 'z'; c++) {
            map.put(c, new LinkedList<>());
        }
        for (String word : words) {
            map.get(word.charAt(0)).offer(word);
        }
        int res = 0;
        for (char c : S.toCharArray()) {
            Queue<String> queue = map.get(c);
            for (int i = 0, size = queue.size(); i < size; i++) {
                String word = queue.poll();
                if (word.length() == 1) {
                    res++;
                } else {
                    map.get(word.charAt(1)).offer(word.substring(1));
                }
            }
        }
        return res;
    }

    // Preprocessing Table
    // time complexity: O(S.length+sum(words[i].length))
    // beats %(91 ms for 49 tests)
    public int numMatchingSubseq5(String S, String[] words) {
        int[][] next = nextTable(S.toCharArray(), 'a', 'z');
        int res = 0;
        outer : for (String w : words) {
            int pos = 0;
            for (char c : w.toCharArray()) {
                pos = next[c - 'a'][pos];
                if (pos < 0) continue outer;
            }
            res++;
        }
        return res;
    }

    private int[][] nextTable(char[] cs, char low, char high) {
        int n = cs.length;
        int[][] next = new int[high - low + 1][n + 1];
        for (int i = 0; i < high - low + 1; i++) {
            next[i][n] = -1;
        }
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < high - low + 1; j++) {
                next[j][i] = next[j][i + 1];
            }
            next[cs[i] - low][i] = i + 1;
        }
        return next;
    }

    void test(String S, String[] words, int expected) {
        assertEquals(expected, numMatchingSubseq(S, words));
        assertEquals(expected, numMatchingSubseq2(S, words));
        assertEquals(expected, numMatchingSubseq3(S, words));
        assertEquals(expected, numMatchingSubseq4(S, words));
        assertEquals(expected, numMatchingSubseq5(S, words));
    }

    @Test
    public void test() {
        test("abcde", new String[] {"a", "bb", "acd", "ace"}, 3);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
