import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1662: https://leetcode.com/problems/check-if-two-string-arrays-are-equivalent/
//
// Given two string arrays word1 and word2, return true if the two arrays represent the same string,
// and false otherwise. A string is represented by an array if the array elements concatenated in
// order forms the string.
//
// Constraints:
// 1 <= word1.length, word2.length <= 10^3
// 1 <= word1[i].length, word2[i].length <= 10^3
// 1 <= sum(word1[i].length), sum(word2[i].length) <= 10^3
// word1[i] and word2[i] consist of lowercase letters.
public class ArrayStringsAreEqual {
    // time complexity: O(N+M), space complexity: O(N+M)
    // 0 ms(100.00%), 36.9 MB(87.26%) for 104 tests
    public boolean arrayStringsAreEqual(String[] word1, String[] word2) {
        StringBuilder sb1 = new StringBuilder();
        for (String w : word1) {
            sb1.append(w);
        }
        StringBuilder sb2 = new StringBuilder();
        for (String w : word2) {
            sb2.append(w);
        }
        return sb1.toString().equals(sb2.toString());
    }

    // time complexity: O(N+M), space complexity: O(1)
    // 1 ms(68.95%), 37.1 MB(80.83%) for 104 tests
    public boolean arrayStringsAreEqual2(String[] word1, String[] word2) {
        return String.join("",  word1).equals(String.join("",  word2));
    }

    // time complexity: O(N+M), space complexity: O(1)
    // 1 ms(68.95%), 37.2 MB(70.85%) for 104 tests
    public boolean arrayStringsAreEqual3(String[] word1, String[] word2) {
        int n1 = word1.length;
        int n2 = word2.length;
        int i1 = 0;
        int i2 = 0;
        for (int c1 = 0, c2 = 0; i1 < n1 && i2 < n2; ) {
            String w1 = word1[i1];
            String w2 = word2[i2];
            if (w1.charAt(c1) != w2.charAt(c2)) { return false; }

            if (++c1 >= w1.length()) {
                c1 = 0;
                i1++;
            }
            if (++c2 >= w2.length()) {
                c2 = 0;
                i2++;
            }
        }
        return i1 >= n1 && i2 >= n2;
    }

    private void test(String[] word1, String[] word2, boolean expected) {
        assertEquals(expected, arrayStringsAreEqual(word1, word2));
        assertEquals(expected, arrayStringsAreEqual2(word1, word2));
        assertEquals(expected, arrayStringsAreEqual3(word1, word2));
    }

    @Test public void test() {
        test(new String[] {"ab", "c"}, new String[] {"a", "bc"}, true);
        test(new String[] {"a", "cb"}, new String[] {"ab", "c"}, false);
        test(new String[] {"abc", "d", "defg"}, new String[] {"abcddefg"}, true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
