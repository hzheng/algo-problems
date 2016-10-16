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
    // beats N/A
    public List<List<String> > wordSquares(String[] words) {
        List<List<String>> res = new ArrayList<>();
        wordSquares(words, new ArrayList<>(), res);
        return res;
    }

    private void wordSquares(String[] words, List<String> buf,
                             List<List<String>> res) {
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

    // TODO: Trie

    void test(Function<String[], List<List<String>>> wordSquares,
              String[] words, String[][] expected) {
        List<List<String>> res = wordSquares.apply(words);
        Collections.sort(res, new Utils.StrListComparator());
        Arrays.sort(expected, new Utils.StrArrayComparator());
        String[][] resArray = Utils.toStrArray(res);
        assertArrayEquals(expected, resArray);
    }

    void test(String[] words, String[][] expected) {
        WordSquares w = new WordSquares();
        test(w::wordSquares, words, expected);
    }

    @Test
    public void test() {
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
