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

    // Time Limit Exceeded
    public List<Integer> findSubstring2(String s, String[] words) {
        if (s.isEmpty() || words.length == 0) return Collections.emptyList();

        List<Integer> indices = new ArrayList<>();
        WordMap wordMap = new WordMap(words);
        for (int offset = 0;; offset++) {
            int res = wordMap.scan(s, offset);
            if (res < 0) break;

            if (res > 0) {
                indices.add(offset);
            }
        }
        return indices;
    }

    static class WordMap {
        private String[] words;
        private Map<String, Integer> map;
        private int len;

        public WordMap(String[] words) {
            this.words = words;
            len = words[0].length();
        }

        public void reset() {
            map = new HashMap<>();
            for (String word : words) {
                if (map.containsKey(word)) {
                    map.put(word, map.get(word) + 1);
                } else {
                    map.put(word, 1);
                }
            }
        }

        public boolean check(String word) {
            if (!map.containsKey(word)) return false;

            int count = map.get(word);
            if (--count == 0) {
                map.remove(word);
            } else {
                map.put(word, count);
            }
            return true;
        }

        public int scan(String s, int offset) {
            int sLen = s.length();
            int end = offset + len * words.length;
            if (sLen < end) return -1;

            reset();
            for (int i = offset; i <= end - len; i += len) {
                if (!check(s.substring(i, i + len))) return 0;
            }
            return 1;
        }
    }

    // beats 91.26%
    public List<Integer> findSubstring3(String s, String[] words) {
        if (s.isEmpty() || words.length == 0) return Collections.emptyList();

        List<Integer> res = new ArrayList<>();
        WordIndices indices = new WordIndices(words);
        int sLen = s.length();
        int slice = words[0].length();
        int maxDiff = (words.length - 1) * slice;
        for (int shift = 0; shift < slice; shift++) {
            int left = shift;
            indices.reset();
            for (int cur = shift; cur <= sLen - slice; cur += slice) {
                int index = indices.check(s, cur, left);
                if (index < 0) {
                    left = cur + slice;
                    continue;
                }

                if (index > 0) {
                    left = index;
                    continue;
                }

                if (left + maxDiff == cur) { // found one
                    res.add(left);
                    indices.decrease(s.substring(left, left + slice));
                    left += slice;
                }
            }
        }
        return res;
    }

    static class WordIndices {
        private Map<String, int[]> map = new HashMap<>();
        private int len;
        private boolean modified;

        public WordIndices(String[] words) {
            for (String word : words) {
                if (map.containsKey(word)) {
                    map.get(word)[0]++;
                } else {
                    map.put(word, new int[] {1, 0});
                }
            }
            len = words[0].length();
        }

        public void reset() {
            if (!modified) return;

            for (int[] index : map.values()) {
                index[1] = 0;
            }
            modified = false;
        }

        public int check(String s, int cur, int left) {
            String word = s.substring(cur, cur + len);
            if (!map.containsKey(word)) {
                reset();
                return -1;
            }

            modified = true;
            int[] indices = map.get(word);
            if (++indices[1] <= indices[0]) return 0;

            --indices[1];
            for (int index = left;; ) {
                String w = s.substring(index, index += len);
                if (w.equals(word)) return index;

                decrease(w);
            }
        }

        public void decrease(String word) {
            map.get(word)[1]--;
        }
    } // WordIndices

    // http://www.jiuzhang.com/solutions/substring-with-concatenation-of-all-words/
    // Time Limit Exceeded
    public List<Integer> findSubstring4(String s, String[] words) {
        List<Integer> result = new ArrayList<Integer>();
        Map<String, Integer> toFind = new HashMap<>();
        Map<String, Integer> found = new HashMap<>();
        int m = words.length;
        int n = words[0].length();
        for (int i = 0; i < m; i++) {
            if (!toFind.containsKey(words[i])) {
                toFind.put(words[i], 1);
            }
            else {
                toFind.put(words[i], toFind.get(words[i]) + 1);
            }
        }
        for (int i = 0; i <= s.length() - n * m; i++) {
            found.clear();
            int j;
            for (j = 0; j < m; j++) {
                int k = i + j * n;
                String stub = s.substring(k, k + n);
                if (!toFind.containsKey(stub)) break;
                if(!found.containsKey(stub)) {
                    found.put(stub, 1);
                }
                else {
                    found.put(stub, found.get(stub) + 1);
                }
                if (found.get(stub) > toFind.get(stub)) break;
            }
            if (j == m) {
                result.add(i);
            }
        }
        return result;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<String, String[], List<Integer> > find,
              Integer[] expected, String s, String ... words) {
        List<Integer> indexes = find.apply(s, words);
        // System.out.println(indexes);
        Collections.sort(indexes);
        assertArrayEquals(expected, indexes.toArray(new Integer[0]));
    }

    void test(Integer[] expected, String s, String ... words) {
        ConcatenationSubstring c = new ConcatenationSubstring();
        test(c::findSubstring, expected, s, words);
        test(c::findSubstring2, expected, s, words);
        test(c::findSubstring3, expected, s, words);
        test(c::findSubstring4, expected, s, words);
    }

    @Test
    public void test1() {
        test(new Integer[] {0, 1, 2}, "aaaaaaaa", "aa", "aa", "aa");
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
        test(new Integer[] {},
             "ababababababababababababababababababababababababa",
             "ab","ba","ab","ba","ab","ba","ab","ba","ab","ba","ab","ba");
        test(new Integer[] {2},
             "abababababababbababababababababababababababababa",
             "ab","ba","ab","ba","ab","ba","ab","ba","ab","ba","ab","ba");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ConcatenationSubstring");
    }
}
