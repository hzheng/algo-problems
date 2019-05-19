import java.util.*;
import java.util.stream.Stream;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1048: https://leetcode.com/problems/longest-string-chain/
//
// Given a list of words, each word consists of English lowercase letters. Let's say word1 is a
// predecessor of word2 if and only if we can add exactly one letter anywhere in word1 to make it
// equal to word2. A word chain is a sequence of words [word_1, word_2, ..., word_k] with k >= 1,
// where word_1 is a predecessor of word_2, word_2 is a predecessor of word_3, and so on.
// Return the longest possible length of a word chain with words chosen from the given list.
// Note:
// 1 <= words.length <= 1000
// 1 <= words[i].length <= 16
//words[i] only consists of English lowercase letters.
public class LongestStringChain {
    // Dynamic Programming + Sort + Hash Table
    // time complexity: O(N * len(W)), space complexity: O(log(N))
    // 651 ms(%), 40.5 MB(100%) for 71 tests
    public int longestStrChain(String[] words) {
        Map<String, Integer> map = new HashMap<>();
        return Stream.of(words).sorted(Comparator.comparing(String::length).reversed())
                     .mapToInt(word -> {
                         if (map.containsKey(word)) {
                             return 0;
                         }

                         int maxLen = 0;
                         int len = word.length();
                         for (char c = 'a'; c <= 'z'; c++) {
                             for (int pos = 0; pos <= len; pos++) {
                                 String newWord = word.substring(0, pos) + c + word.substring(pos);
                                 maxLen = Math.max(maxLen, map.getOrDefault(newWord, 0));
                             }
                         }
                         map.put(word, ++maxLen);
                         return maxLen;
                     }).max().getAsInt();
    }

    // Dynamic Programming + Sort + Hash Table
    // time complexity: O(N * len(W)), space complexity: O(log(N))
    // 71 ms(%), 38.7 MB(100%) for 71 tests
    public int longestStrChain2(String[] words) {
        Map<String, Integer> map = new HashMap<>();
        return Stream.of(words).sorted(Comparator.comparing(String::length)).mapToInt(word -> {
            int maxLen = 0;
            int len = word.length();
            for (int pos = 0; pos < len; pos++) {
                String predecessor = word.substring(0, pos) + word.substring(pos + 1);
                maxLen = Math.max(maxLen, map.getOrDefault(predecessor, 0));
            }
            map.put(word, ++maxLen);
            return maxLen;
        }).max().getAsInt();
    }

    // Recursion + Dynamic Programming(Top-Down) + Set + Hash Table
    // time complexity: O(N * len(W)), space complexity: O(log(N))
    // 33 ms(%), 41.4 MB(100%) for 71 tests
    public int longestStrChain3(String[] words) {
        Set<String> wordSet = new HashSet<>();
        for (String word : words) {
            wordSet.add(word);
        }
        int res = 0;
        Map<String, Integer> map = new HashMap<>();
        for (String word : words) {
            res = Math.max(res, longestChain(word, wordSet, map));
        }
        return res;
    }

    private int longestChain(String word, Set<String> words, Map<String, Integer> map) {
        int max = map.getOrDefault(word, 0);
        if (max > 0) {
            return max;
        }
        int len = word.length();
        for (int i = 0; i < len; i++) {
            String predecessor = word.substring(0, i) + word.substring(i + 1);
            if (words.contains(predecessor)) {
                max = Math.max(max, longestChain(predecessor, words, map));
            }
        }
        map.put(word, ++max);
        return max;
    }

    void test(String[] words, int expected) {
        assertEquals(expected, longestStrChain(words));
        assertEquals(expected, longestStrChain2(words));
        assertEquals(expected, longestStrChain3(words));
    }

    @Test
    public void test() {
        test(new String[]{"a", "b", "ba", "bca", "bda", "bdca"}, 4);
        test(new String[]{"ksqvsyq", "ks", "kss", "czvh", "zczpzvdhx", "zczpzvh", "zczpzvhx",
                          "zcpzvh", "zczvh", "gr", "grukmj", "ksqvsq", "gruj", "kssq", "ksqsq",
                          "grukkmj", "grukj", "zczpzfvdhx", "gru"}, 7);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
