import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a string, s, and a list of words, words, all of the same length. Find
// all starting indices of substring(s) in s that is a concatenation of each
// word in words exactly once and without any intervening characters.
public class ConcatenationSubstring {
    // Time Limit Exceeded
    public List<Integer> findSubstring(String s, String[] words) {
        if (s.isEmpty() || words.length == 0) return Collections.emptyList();

        List<Integer> res = new ArrayList<>();
        String word = words[0];
        int wordLen = word.length();
        int wordsLen = wordLen * words.length;
        WordSet wordSet = new WordSet(words);

        for (int i = 0; i < s.length(); ) {
            int index = s.indexOf(word, i);
            if (index < 0) break;

            if (words.length == 1) {
                res.add(index);
            } else {
                findAdjacency(s, index, 0, word, wordSet, res);
            }
            i = index + 1; // can be optimized
        }
        return res;
    }

    static class WordSet {
        private String[] words;
        private Map<String, Integer> wordMap;
        private int len;

        public WordSet(String[] words) {
            this.words = words;
            reset();
            len = words[0].length();
        }

        public void reset() {
            wordMap = new HashMap<>();
            for (String word : words) {
                if (wordMap.containsKey(word)) {
                    wordMap.put(word, wordMap.get(word) + 1);
                } else {
                    wordMap.put(word, 1);
                }
            }
        }

        public int length() {
            return len;
        }

        public int totalLen() {
            return len * words.length;
        }

        public String check(String s, int offset) {
            if (offset + len > s.length()) return null;
            return check(s.substring(offset, offset + len));
        }

        public String check(String word) {
            if (!wordMap.containsKey(word)) return null;

            int count = wordMap.get(word);
            if (--count == 0) {
                wordMap.remove(word);
            } else {
                wordMap.put(word, count);
            }
            return word;
        }

        public boolean done() {
            return wordMap.isEmpty();
        }
    }

    private void add(List<Integer> res, int index) {
        if (res.size() == 0 || res.get(res.size() - 1) < index) {
            res.add(index);
        }
    }

    private void findAdjacency(String s, int index, int leftmost, String word,
                               WordSet wordSet, List<Integer> res) {
        wordSet.reset();
        wordSet.check(word);
        int wordLen = wordSet.length();
        int i = index - wordLen;
        int resIndex = index;
        for (; i >= leftmost; i -= wordLen) {
            String found = wordSet.check(s, i);
            if (found == null) break;
            resIndex = i;
        }
        if (wordSet.done()) {
            add(res, resIndex);
            wordSet.reset();
            wordSet.check(word);
        }

        for (i = index + wordLen; i < s.length(); i += wordLen) {
            String found = wordSet.check(s, i);
            if (found == null) break;

            if (wordSet.done()) {
                resIndex = i - wordSet.totalLen() + wordLen;
                add(res, resIndex);
                if (resIndex < index) {
                    leftmost = resIndex + wordLen;
                    findAdjacency(s, index, leftmost, word, wordSet, res);
                }
                return;
            }
        }
    }

    void test(Integer[] expected, String s, String ... words) {
        List<Integer> indexes = findSubstring(s, words);
        // System.out.println(indexes);
        assertArrayEquals(expected, indexes.toArray(new Integer[0]));
    }

    @Test
    public void test1() {
        test(new Integer[] {0, 9}, "barfoothefoobarman", "foo", "bar");
        test(new Integer[] {3, 9, 15}, "barfoothefoobarfooman", "foo");
        test(new Integer[] {1}, "yarewas", "was", "are");
        test(new Integer[] {5, 15}, "12345isaareiam6areisaiamthisamareis",
             "isa", "are", "iam");
        test(new Integer[] {0, 2}, "abcdab", "ab", "cd");
        test(new Integer[] {0, 2}, "abcdab", "cd", "ab");
        test(new Integer[] {0, 2}, "abcdefab", "cd", "ab", "ef");
        test(new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14},
             "aaaaaaaaaaaaaaaa", "a", "a");
        test(new Integer[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13},
             "aaaaaaaaaaaaaaaa", "a", "a", "a");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ConcatenationSubstring");
    }
}
