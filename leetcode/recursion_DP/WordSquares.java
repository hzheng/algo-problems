import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.function.Function;

import common.Utils;

// LC425: https://leetcode.com/problems/word-squares
//
// Given a set of words (without duplicates), find all word squares you can build from them.
// Note:
// There are at least 1 and at most 1000 words.
// All words will have the exact same length.
// Word length is at least 1 and at most 5.
// Each word contains only lowercase English alphabet a-z.
public class WordSquares {
    // DFS(Backtracking) + Recursion
    // Time Limit Exceeded
    public List<List<String> > wordSquares(String[] words) {
        List<List<String> > res = new ArrayList<>();
        wordSquares(words, new ArrayList<>(), res);
        return res;
    }

    private void wordSquares(String[] words, List<String> buf,
                             List<List<String> > res) {
        if (buf.size() == words[0].length()) {
            res.add(new ArrayList<>(buf));
            return;
        }
        for (String word : words) {
            if (isValid(buf, word)) {
                buf.add(word);
                wordSquares(words, buf, res);
                buf.remove(buf.size() - 1);
            }
        }
    }

    private boolean isValid(List<String> words, String newWord) {
        for (int i = 0, size = words.size(); i < size; i++) {
            if (newWord.charAt(i) != words.get(i).charAt(size)) return false;
        }
        return true;
    }

    // Hash Table + DFS(Backtracking) + Recursion
    // beats 68.60%(124 ms for 16 tests)
    public List<List<String> > wordSquares2(String[] words) {
        List<List<String> > res = new ArrayList<>();
        Map<String, Set<String> > memo = new HashMap<>();
        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j < words[0].length(); j++) {
                String prefix = words[i].substring(0, j);
                if (!memo.containsKey(prefix)) {
                    memo.put(prefix, new HashSet<String>());
                }
                memo.get(prefix).add(words[i]);
            }
        }
        wordSquares2(res, new ArrayList<>(), 0, words[0].length(), memo);
        return res;
    }

    private void wordSquares2(List<List<String> > res, List<String> cur,
                              int matched, int total, Map<String, Set<String> > memo){
        if (matched == total) {
            res.add(new ArrayList<>(cur));
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matched; i++) {
            sb.append(cur.get(i).charAt(matched));
        }
        for (String str : memo.getOrDefault(sb.toString(), Collections.emptySet())) {
            cur.add(str);
            wordSquares2(res, cur, matched + 1, total, memo);
            cur.remove(cur.size() - 1);
        }
    }

    // Trie + DFS(Backtracking) + Recursion
    // beats 80.19%(99 ms for 16 tests)
    public List<List<String> > wordSquares3(String[] words) {
        Trie trie = new Trie(words);
        List<List<String> > res = new ArrayList<>();
        int len = words[0].length();
        for (String word : words) {
            List<String> cur = new ArrayList<>();
            cur.add(word);
            wordSquares3(res, cur, 1, len, trie);
        }
        return res;
    }

    private void wordSquares3(List<List<String> > res, List<String> cur,
                              int matched, int total, Trie trie){
        if (matched == total) {
            res.add(new ArrayList<>(cur));
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matched; i++) {
            sb.append(cur.get(i).charAt(matched));
        }
        for (String str : trie.startsWith(sb.toString())) {
            cur.add(str);
            wordSquares3(res, cur, matched + 1, total, trie);
            cur.remove(cur.size() - 1);
        }
    }

    private static class TrieNode {
        TrieNode[] children = new TrieNode[26];
        List<String> words = new ArrayList<>();
    }

    private static class Trie {
        private TrieNode root = new TrieNode();

        public Trie(String[] words) {
            for (String word : words) {
                TrieNode cur = root;
                for (char c : word.toCharArray()) {
                    int index = c - 'a';
                    if (cur.children[index] == null) {
                        cur.children[index] = new TrieNode();
                    }
                    cur = cur.children[index];
                    cur.words.add(word);
                }
            }
        }

        List<String> startsWith(String prefix) {
            TrieNode cur = root;
            for (char c : prefix.toCharArray()) {
                cur = cur.children[c - 'a'];
                if (cur == null) return Collections.emptyList();
            }
            return cur.words;
        }
    }

    void test(Function<String[], List<List<String>>> wordSquares,
              String[] words, String[][] expected) {
        List<List<String> > res = wordSquares.apply(words);
        Collections.sort(res, new Utils.StrListComparator());
        Arrays.sort(expected, new Utils.StrArrayComparator());
        String[][] resArray = Utils.toStrArray(res);
        assertArrayEquals(expected, resArray);
    }

    void test(String[] words, String[][] expected) {
        WordSquares w = new WordSquares();
        test(w::wordSquares, words, expected);
        test(w::wordSquares2, words, expected);
        test(w::wordSquares3, words, expected);
    }

    @Test
    public void test() {
        test(new String[] {"ab", "cd", "ac", "bd"}, new String[][] {{"ab", "bd"}, {"ac", "cd"}});
        test(new String[] {"abat","baba","atan","atal"},
             new String[][] {{"baba", "abat", "baba", "atan"},
                             {"baba", "abat", "baba", "atal"}});
        test(new String[] {"area","lead","wall","lady","ball"},
             new String[][] {{"wall", "area", "lead", "lady"},
                             {"ball", "area", "lead", "lady"}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("WordSquares");
    }
}
