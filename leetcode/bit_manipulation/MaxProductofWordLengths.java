import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/maximum-product-of-word-lengths/
//
// Given a string array words, find the maximum value of
// length(word[i]) * length(word[j]) where the two words do not share common
// letters. You may assume that each word will contain only lower case letters.
// If no such two words exist, return 0.
public class MaxProductofWordLengths {
    // beats 94.90%(19 ms)
    // time complexity: O(N ^ 2), space complexity: O(N)
    public int maxProduct(String[] words) {
        int n = words.length;
        int[] codes = new int[n];
        int[] lens = new int[n]; // cache length (otherwise runtime is 33 ms)
        for (int i = n - 1; i >= 0; i--) {
            String word = words[i];
            int v = 0;
            char[] chars = word.toCharArray();
            for (char c : chars) {
                v |= 1 << (c - 'a');
            }
            codes[i] = v;
            lens[i] = chars.length;
        }
        int max = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if ((codes[i] & codes[j]) == 0) {
                    max = Math.max(max, lens[i] * lens[j]);
                }
            }
        }
        return max;
    }

    // beats 21.65%(70 ms)
    public int maxProduct2(String[] words) {
        Map<Integer, Integer> maxLen = new HashMap<>();;
        int max = 0;
        for (String word : words) {
            int v = 0;
            char[] chars = word.toCharArray();
            for (char c : chars) {
                v |= 1 << (c - 'a');
            }
            maxLen.put(v, Math.max(maxLen.getOrDefault(v, 0), chars.length));
            for (int key : maxLen.keySet()) {
                if ((v & key) == 0) {
                    max = Math.max(max, chars.length * maxLen.get(key));
                }
            }
        }
        return max;
    }

    void test(int expected, String... words) {
        assertEquals(expected, maxProduct(words));
        assertEquals(expected, maxProduct2(words));
    }

    @Test
    public void test1() {
        test(16, "abcw", "baz", "foo", "bar", "xtfn", "abcdef");
        test(4, "a", "ab", "abc", "d", "cd", "bcd", "abcd");
        test(0, "a", "aa", "aaa", "aaaa");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxProductofWordLengths");
    }
}
