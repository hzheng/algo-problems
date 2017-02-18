import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC244: https://leetcode.com/problems/shortest-word-distance-ii/
//
// This is a follow up of Shortest Word Distance(LC243). The only difference is
// now you are given the list of words and your method will be called repeatedly
// many times with different parameters. How would you optimize it?
public class ShortestWordDistance2 {
    static interface IWordDistance {
        public int shortest(String word1, String word2);
    }

    // beats 57.67%(149 ms for 12 tests)
    static class WordDistance implements IWordDistance {
        private Map<String, List<Integer>> map = new HashMap<>();

        public WordDistance(String[] words) {
            for (int i = 0; i < words.length; i++) {
                List<Integer> list = map.get(words[i]);
                if (list == null) {
                    map.put(words[i], list = new ArrayList<>());
                }
                list.add(i);
            }
        }

        public int shortest(String word1, String word2) {
            int min = Integer.MAX_VALUE;
            for (int i : map.get(word1)) {
                for (int j : map.get(word2)) {
                    min = Math.min(min, Math.abs(i - j));
                }
            }
            return min;
        }
    }

    // Binary Search
    // beats 53.76%(154 ms for 12 tests)
    static class WordDistance2 implements IWordDistance {
        private Map<String, List<Integer>> map = new HashMap<>();

        public WordDistance2(String[] words) {
            for (int i = 0; i < words.length; i++) {
                List<Integer> list = map.get(words[i]);
                if (list == null) {
                    map.put(words[i], list = new ArrayList<>());
                }
                list.add(i);
            }
        }

        public int shortest(String word1, String word2) {
            int min = Integer.MAX_VALUE;
            List<Integer> indices2 = map.get(word2);
            for (int i : map.get(word1)) {
                int j = -Collections.binarySearch(indices2, i) - 1;
                if (j < indices2.size()) {
                    min = Math.min(min, Math.abs(indices2.get(j) - i));
                }
                if (j > 0) {
                    min = Math.min(min, Math.abs(indices2.get(j - 1) - i));
                }
            }
            return min;
        }
    }

    // beats 45.94%(160 ms for 12 tests)
    static class WordDistance3 implements IWordDistance {
        private Map<String, List<Integer>> map = new HashMap<>();

        public WordDistance3(String[] words) {
            for (int i = 0; i < words.length; i++) {
                List<Integer> list = map.get(words[i]);
                if (list == null) {
                    map.put(words[i], list = new ArrayList<>());
                }
                list.add(i);
            }
        }

        public int shortest(String word1, String word2) {
            int min = Integer.MAX_VALUE;
            List<Integer> indices1 = map.get(word1);
            List<Integer> indices2 = map.get(word2);
            for (int i = 0, j = 0, m = indices1.size(), n = indices2.size(); i < m && j < n; ) {
                int diff = indices1.get(i) - indices2.get(j);
                if (diff < 0) {
                    min = Math.min(-diff, min);
                    i++;
                } else {
                    min = Math.min(diff, min);
                    j++;
                }
            }
            return min;
        }
    }

    void test1(IWordDistance obj) {
        assertEquals(2, obj.shortest("sentence", "a"));
        assertEquals(2, obj.shortest("weather", "a"));
        assertEquals(4, obj.shortest("today", "a"));
    }

    @Test
    public void test() {
        String[] words = {"this", "is", "a", "long", "sentence", "is", "fun",
                           "day", "today", "sunny", "weather", "is", "a", "day",
                           "tuesday","this", "sentence", "run", "running", "rainy"};
        test1(new WordDistance(words));
        test1(new WordDistance2(words));
        test1(new WordDistance3(words));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ShortestWordDistance2");
    }
}
