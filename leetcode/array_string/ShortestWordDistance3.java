import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC245: https://leetcode.com/problems/shortest-word-distance-iii/
//
// This is a follow up of Shortest Word Distance(LC243). The only difference is
// now word1 could be the same as word2.
// word1 and word2 may be the same and they represent two individual words in the list.
public class ShortestWordDistance3 {
    // Brute Force
    // beats 49.17%(3 ms for 39 tests)
    public int shortestDistance(String[] words, String word1, String word2) {
        List<Integer> indices1 = new ArrayList<>();
        List<Integer> indices2 = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(word1)) {
                indices1.add(i);
            }
            if (words[i].equals(word2)) {
                indices2.add(i);
            }
        }
        int min = Integer.MAX_VALUE;
        for (int i : indices1) {
            for (int j : indices2) {
                if (i != j) {
                    min = Math.min(min, Math.abs(i - j));
                }
            }
        }
        return min;
    }

    // Two Pointers
    // beats 49.17%(3 ms for 39 tests)
    public int shortestDistance2(String[] words, String word1, String word2) {
        List<Integer> indices1 = new ArrayList<>();
        List<Integer> indices2 = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(word1)) {
                indices1.add(i);
            }
            if (words[i].equals(word2)) {
                indices2.add(i);
            }
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0, j = 0, m = indices1.size(), n = indices2.size(); i < m && j < n; ) {
            int diff = indices1.get(i) - indices2.get(j);
            if (diff <= 0) {
                if (diff < 0) {
                    min = Math.min(-diff, min);
                }
                i++;
            } else {
                min = Math.min(diff, min);
                j++;
            }
        }
        return min;
    }

    // beats 96.45%(1 ms for 39 tests)
    public int shortestDistance3(String[] words, String word1, String word2) {
        int min = Integer.MAX_VALUE;
        if (word1.equals(word2)) {
            for (int i = 0, n = words.length, last = -n; i < n; i++) {
                if (words[i].equals(word1)) {
                    min = Math.min(min, i - last);
                    last = i;
                }
            }
            return min;
        }
        for (int i = 0, n = words.length, index1 = -n, index2 = -n; i < n; i++) {
            if (words[i].equals(word1)) {
                index1 = i;
            } else if (words[i].equals(word2)) {
                index2 = i;
            } else continue;
            min = Math.min(min, Math.abs(index1 - index2));
        }
        return min;
    }

    // beats 24.17%(4 ms for 39 tests)
    public int shortestDistance4(String[] words, String word1, String word2) {
        int min = Integer.MAX_VALUE;
        boolean sameWord = word1.equals(word2);
        for (int i = 0, index1 = -1, index2 = -1; i < words.length; i++) {
            if (words[i].equals(word1)) {
                if (sameWord) {
                    index2 = index1;
                }
                index1 = i;
            } else if (words[i].equals(word2)) {
                index2 = i;
            }
            if (index1 >= 0 && index2 >= 0) {
                min = Math.min(min, Math.abs(index1 - index2));
            }
        }
        return min;
    }

    void test(String[] words, String[] pair, int ... expected) {
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], shortestDistance(words, pair[i * 2], pair[i * 2 + 1]));
            assertEquals(expected[i], shortestDistance2(words, pair[i * 2], pair[i * 2 + 1]));
            assertEquals(expected[i], shortestDistance3(words, pair[i * 2], pair[i * 2 + 1]));
            assertEquals(expected[i], shortestDistance4(words, pair[i * 2], pair[i * 2 + 1]));
        }
    }

    @Test
    public void test() {
        test(new String[] {"practice", "makes", "perfect", "coding", "makes"},
             new String[] {"makes", "coding", "makes", "makes"}, 1, 3);
        test(new String[] {"this", "is", "a", "long", "sentence", "is", "fun",
                           "day", "today", "sunny", "weather", "is", "a", "day",
                           "tuesday","this", "sentence", "run", "running", "rainy"},
             new String[] {"sentence", "a", "is", "is"}, 2, 4);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ShortestWordDistance3");
    }
}
