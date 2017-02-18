import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC243: https://leetcode.com/problems/shortest-word-distance/?tab=Description
//
// Given a list of words and two words word1 and word2, return the shortest
// distance between these two words in the list.
public class ShortestWordDistance {
    // beats 57.94%(3 ms for 26 tests)
    public int shortestDistance(String[] words, String word1, String word2) {
        List<Integer> indices1 = new ArrayList<>();
        List<Integer> indices2 = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(word1)) {
                indices1.add(i);
            } else if (words[i].equals(word2)) {
                indices2.add(i);
            }
        }
        int min = Integer.MAX_VALUE;
        for (int i : indices1) {
            for (int j : indices2) {
                min = Math.min(min, Math.abs(i - j));
            }
        }
        return min;
    }

    // Binary Search
    // beats 57.94%(3 ms for 26 tests)
    public int shortestDistance2(String[] words, String word1, String word2) {
        List<Integer> indices1 = new ArrayList<>();
        List<Integer> indices2 = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(word1)) {
                indices1.add(i);
            } else if (words[i].equals(word2)) {
                indices2.add(i);
            }
        }
        int min = Integer.MAX_VALUE;
        for (int i : indices1) {
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

    // Two Pointers
    // beats 57.94%(3 ms for 26 tests)
    public int shortestDistance3(String[] words, String word1, String word2) {
        List<Integer> indices1 = new ArrayList<>();
        List<Integer> indices2 = new ArrayList<>();
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(word1)) {
                indices1.add(i);
            } else if (words[i].equals(word2)) {
                indices2.add(i);
            }
        }
        int min = Integer.MAX_VALUE;
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

    // beats 84.04%(2 ms for 26 tests)
    public int shortestDistance4(String[] words, String word1, String word2) {
        int min = Integer.MAX_VALUE;
        for (int i = 0, n = words.length, index1 = -n, index2 = -n; i < n; i++) {
            if (words[i].equals(word1)) {
                index1 = i;
                min = Math.min(min, i - index2);
            } else if (words[i].equals(word2)) {
                index2 = i;
                min = Math.min(min, i - index1);
            }
        }
        return min;
    }

    // beats 84.04%(2 ms for 26 tests)
    public int shortestDistance4_2(String[] words, String word1, String word2) {
        int min = Integer.MAX_VALUE;
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

    // beats 84.04%(2 ms for 26 tests)
    public int shortestDistance4_3(String[] words, String word1, String word2) {
        int min = Integer.MAX_VALUE;
        for (int i = 0, index = -1; i < words.length; i++) {
            if (words[i].equals(word1) || words[i].equals(word2)) {
                if (index >= 0 && !words[index].equals(words[i])) {
                    min = Math.min(min, i - index);
                }
                index = i;
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
            assertEquals(expected[i], shortestDistance4_2(words, pair[i * 2], pair[i * 2 + 1]));
            assertEquals(expected[i], shortestDistance4_3(words, pair[i * 2], pair[i * 2 + 1]));
        }
    }

    @Test
    public void test() {
        test(new String[] {"practice", "makes", "perfect", "coding", "makes"},
             new String[] {"coding", "practice", "makes", "coding"}, 3, 1);
        test(new String[] {"this", "is", "a", "long", "sentence", "is", "fun",
                           "day", "today", "sunny", "weather", "is", "a", "day",
                           "tuesday","this", "sentence", "run", "running", "rainy"},
             new String[] {"sentence", "a"}, 2);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ShortestWordDistance");
    }
}
