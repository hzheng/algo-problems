import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1754: https://leetcode.com/problems/largest-merge-of-two-strings/
//
// You are given two strings word1 and word2. You want to construct a string merge in the following
// way: while either word1 or word2 are non-empty, choose one of the following options:
// If word1 is non-empty, append the first character in word1 to merge and delete it from word1.
// For example, if word1 = "abc" and merge = "dv", then after choosing this operation, word1 = "bc"
// and merge = "dva".
// If word2 is non-empty, append the first character in word2 to merge and delete it from word2. For
// example, if word2 = "abc" and merge = "", then after choosing this operation, word2 = "bc" and
// merge = "a".
// Return the lexicographically largest merge you can construct. A string a is lexicographically
// larger than a string b (of the same length) if in the first position where a and b differ, a has
// a character strictly larger than the corresponding character in b. For example, "abcd" is
// lexicographically larger than "abcc" because the first position they differ is at the fourth
// character, and d is greater than c.
//
// Constraints:
// 1 <= word1.length, word2.length <= 3000
// word1 and word2 consist only of lowercase English letters.
public class LargestMerge {
    // time complexity: O(W1*W2), space complexity: O(W1+W2)
    // 19 ms(78.57%), 39.5 MB(71.43%) for 96 tests
    public String largestMerge(String word1, String word2) {
        char[] s1 = word1.toCharArray();
        char[] s2 = word2.toCharArray();
        int n1 = s1.length;
        int n2 = s2.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0, j = 0; i < n1 || j < n2; ) {
            if (i >= n1) {
                sb.append(s2[j++]);
                continue;
            }
            if (j >= n2 || s1[i] > s2[j]) {
                sb.append(s1[i++]);
                continue;
            }
            if (s1[i] < s2[j]) {
                sb.append(s2[j++]);
                continue;
            }
            for (int k = 0; ; k++) {
                if (i + k >= n1) {
                    sb.append(s2[j++]);
                    break;
                }
                if (j + k >= n2 || s1[i + k] > s2[j + k]) {
                    sb.append(s1[i++]);
                    break;
                }
                if (s1[i + k] < s2[j + k]) {
                    sb.append(s2[j++]);
                    break;
                }
            }
        }
        return sb.toString();
    }

    // time complexity: O(W1*W2), space complexity: O(W1+W2)
    // 24 ms(78.57%), 39.5 MB(71.43%) for 96 tests
    public String largestMerge2(String word1, String word2) {
        int n1 = word1.length();
        int n2 = word2.length();
        char[] s1 = Arrays.copyOf(word1.toCharArray(), n1 + 1);
        char[] s2 = Arrays.copyOf(word2.toCharArray(), n2 + 1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0, j = 0, k = 0; i < n1 || j < n2 || k > 0; ) {
            if (s1[i] == s2[j] && i < n1) {
                i++;
                j++;
                k++;
                continue;
            }
            i -= k;
            j -= k;
            if (i >= n1 || s1[i + k] < s2[j + k]) {
                sb.append(s2[j++]);
            } else {
                sb.append(s1[i++]);
            }
            k = 0; // reset
        }
        return sb.toString();
    }

    // Recursion
    // time complexity: O(W1*W2), space complexity: O(W1+W2)
    // 95 ms(50.00%), 42.5 MB(14.29%) for 96 tests
    public String largestMerge3(String word1, String word2) {
        if (word1.isEmpty() || word2.isEmpty()) { return word1 + word2; }

        if (word1.compareTo(word2) > 0) {
            return word1.charAt(0) + largestMerge3(word1.substring(1), word2);
        }
        return word2.charAt(0) + largestMerge3(word1, word2.substring(1));
    }

    // time complexity: O(W1*W2), space complexity: O(W1+W2)
    // 46 ms(71.43%), 39.9 MB(35.71%) for 96 tests
    public String largestMerge4(String word1, String word2) {
        StringBuilder sb = new StringBuilder();
        int n1 = word1.length();
        int n2 = word2.length();
        int i = 0;
        int j = 0;
        while (i < n1 && j < n2) {
            if (word1.substring(i).compareTo(word2.substring(j)) > 0) {
                sb.append(word1.charAt(i++));
            } else {
                sb.append(word2.charAt(j++));
            }
        }
        for (; i < n1; i++) {
            sb.append(word1.charAt(i));
        }
        for (; j < n2; j++) {
            sb.append(word2.charAt(j));
        }
        return sb.toString();
    }

    private void test(String word1, String word2, String expected) {
        assertEquals(expected, largestMerge(word1, word2));
        assertEquals(expected, largestMerge2(word1, word2));
        assertEquals(expected, largestMerge3(word1, word2));
        assertEquals(expected, largestMerge4(word1, word2));
    }

    @Test public void test() {
        test("cabaa", "bcaaa", "cbcabaaaaa");
        test("abcabc", "abdcaba", "abdcabcabcaba");
        test("abc", "babc", "babcabc");
        test("jbjjjjjjjjbjbbjjbjbbbjbbjbjjbjjjjbbbjjjjjjbjjbjbbjjbbbbbjjjbbbjjbjjbbbbjbjjjjbjbjbjjbbjjbbjjjbbbbjbjjbbjjjjjjbjjbbbjbbjjjjjjjjbbbbjjbjjbjbbjbjjjjbjbjjbjbjbbjbjjbjjbbjjjjjjjjbjbbbbjjjbjbbbbbbjbjjbbjbbbjjjbbbjbbbjjbjjbbjjjjjbjbbjbbbbbbjjjbjbbjjjjbbjbjbbjbjjjjjbjbbbbjbjbjjjjjjjjbbjjjjbjjbbjbjbbjjbjjbbbbjbbjbbjjjjjbbjbbbjjbbjjbbjjbjbjjbjjbjjbbbjbbjjjjjjjbbbbbbjbbjjbjbbbbjjbbbjjbjjjbjbbbjjbjbjjbjjjjbbbjbjjbbbjjjjbjbjbjjjbjbjjbbjbbjjbjjbjjjbjbbjbbjjjjbjbbjbjbbjjbbjjjbjbjbjjjjjbbjjbbbjbbbbbbbjbjjjbbjjjbbjbbbbjjjjjjjbbbjjbjjjjbbbbjjjjbjjbjbbjjjbbbbjbbjjjjjjbjjjjjjbjjbjbbbbbbbbjbjbbbbjjjjjjbjbjjjbjbjjjjjbbbjbbjbbjjbbjbjjjjjjjjbbjbjbjbjjjjjjbbjjjbbjbbjjbbjjbjjbjbbjjbjbbjjjjbjjbbjbjbjjbjjbjbbjbbbjbjjbjbbbbjjjbjjjbjjbjjbbjbbjjbbbbjjjjjbjbbjbbbjjbjjjbjbbjjjbbbjjbjbbbjbbbjbjbjjbbjjjbbjjbbbbbjjjjbbbjbbjjjbbbjjbbbbbbjbbjbbjjbjjjjjbbjbjbjbjjbjjbjjjjbbjjjjbjbjjjjjbbjjjbbjbjjjbjjjjjjjjbjbbjbjbjjjbjbjjbjjjjbbbbbjbbbbjbbbjjbjbbjjbbbjjbbbbjbjjjbbjbjjjbjjjjjbjjbbbbbjjjbjbjbbbjbjjjbjjjjjjbjjbbbbjjbbbbjjjjbbbbbbbjbbjbjbbjjbbjbjjjbjjjjbbbjbjjbjbjjbjbbjjjjjbbbjbjjbbbbjjjbbjjjjjbjjbjjbbjbjjjbjjjjbbbjjjbjbbjbjbbjjbbbjbjjjbbbjjjbjjbbjbbbjjbjbjjbjbjbbbbbbbjjbjjbbjjbjbjjbbjbjbjbjjjjjjjbjjjjbjbjjjbjjjbjbjjbbjjbjjbbjjjjjbbjjjjbjbbbbbjjbbbbjbbbbbbjjjjjbjjbjbjjjbbbbjjjbjjbjbjjbbbjjjbjbjbjbjjbbbjbbjbbbbbjjjbjbbjjbjbbjbbjbjjbbbbjbbbjbjjbjjbjbjjbjjjjjbjbbbbjbjbbjjjjbjjbjbjjbjjbjbbbbjbjbbjbbbbbjjjbbjbbjjjjjbbbjjjbjbjjbjbjbbbjjbbbjjbjbbbbjbjbjbbbjbbbjbbjbbjjjbbjbbjbbbbjjjbbjjbbbbjjbbjjbbbbbbjbjbjjjbbbjbbjjbjbbbjbbjjjjjjjjjjjbbjbbjbjbjjjbjbbjjjjbbjjbjjbbjjjbbbjjjbbjbjbjjbjjjjjbbjbbbbjjjbbjjjjjbbbbbjbbjjjjbbjjjjbbbbjjjbjjbjbbbjjbbbjjbjjjbjbbbjjjjbbbbbbjjbbbbbbjjbbjjbjjbbjbjbjjbbjbbjbbbbjjjjjbjjjjbjjbbjjjjbjjbjjbbjjjjjbjjjjbjbbbjjbbbjjjbjbjbbjjjjbjjjbjbjbjbbjjjbjbjbjbjjbjbbbbjbbjbbjjbbbjbjjbjbbbbjjjjjjbjbbjbbbbbjbbbbbbbjbjbbbbbbjjjbjjjbbbjbjbbjbbjjbbbbbjjjjjbjbjbjbjjjbbjbjjbbbbjbbbbbjbjjjjjjjbbbbjjjbjbbbbjbjjjbjjbjjbbjbjjbjbbjbbjbbbbbbbbjbjjbbjbbjbjjjjjbjjjjjbbjjbjjbbjbjjjjjjjbbbbbbjbbbbjbjbbjjbjbbjbbbbjbjbbjjbjbjjjbjjbbbbbbbbjjbjjjjjbjbbjjjjjbbjjbbbbjjjjbbbbbjjbjbbbjjjjjjbbbbbjjbjbjbjjbbbjbbjbjjbbbbjjbjbbjjjjbbjbbbjjbbbjjbbjjjjjbjjbjjbjbbjbbjbbjbjjbbbjjbjjbbbjbbbjjjbbjjbjbjbbjjjbbbjjbjbbjjbbbjbbjbbbbjjbjjbjjjjjjjjbbbjjjbjjbbbbjbjjbjbbjbjjjbjbjbjjbbjjbjjjbbbjbbjbjjbjbjbbbbjjbbbbjjbjbbbbjjjbbbbbbbbbbbbbjbjbbjbbjbbbbjjbjjbjbbbjbjbjbjbbbjjbbjbbjbbjbjbbjbjjjbbbbbbjbbbjbjjbjbjjjjjbjjjbjjbbbbbjjjbjjbbbbjjbbbjjjjjbbbjjjbjjbbbjbjbbjbbbbbjbjjjbjbbjjbjbbjjbbjbjbbjjjjbjbbjjjbjbjjbbjjbjbbjjbjjjjbjjjbbbbbbbjjbbbjbjbjbbjjjjjbbbbbbbjjjjjjjbbjbjjjbjbbjbjbbjjjjbbjjjbbjjbjbbjjjjbbbbbjbjbjbbjjbjjbbbjjbbbjbjjjjbbbbjbjbbjbjbbbbjbjjjjbjjjbjjjjjjjbjjbbjbbbjjjjbbbjjjbjjjbbjjjjjbbbjjjbjbjjbjbbjbjjbbjjbjbbjbbjbbbbbbbbjbjbbbbbjbbbjjbbbbjjjbjjjbbbjjjjjbbjjbbjjbbbjjjjjbbjbjbjjjbjjjjjjbjjbjbbbjbbjjbjbbjjjjjbbbbjbbbjjjjjjjjjbbbjbjbjjbbjjjjbjbjjjjjbjjjbjbjbjbbbbjjjjjbbbjbjjjjbjbjjjjbjjbbjbjbjbjbbjbjbjbjjbbjbjjbbjjbbbbbjjjjjbbjjbjbbbbjbbbjbbbbjbbjbjbbbbjbbbjbjjjbbbjbbjjjjbbjbjbjjjjbjbbjbbbjjbjjjbjjbbbjbjbbbbjjbbbjbbjjbbjj",
             "jbjjjjjjjjbjbbjjbjbbbjbbjbjjbjjjjbbbjjjjjjbjjbjbbjjbbbbbjjjbbbjjbjjbbbbjbjjjjbjbjbjjbbjjbbjjjbbbbjbjjbbjjjjjjbjjbbbjbbjjjjjjjjbbbbjjbjjbjbbjbjjjjbjbjjbjbjbbjbjjbjjbbjjjjjjjjbjbbbbjjjbjbbbbbbjbjjbbjbbbjjjbbbjbbbjjbjjbbjjjjjbjbbjbbbbbbjjjbjbbjjjjbbjbjbbjbjjjjjbjbbbbjbjbjjjjjjjjbbjjjjbjjbbjbjbbjjbjjbbbbjbbjbbjjjjjbbjbbbjjbbjjbbjjbjbjjbjjbjjbbbjbbjjjjjjjbbbbbbjbbjjbjbbbbjjbbbjjbjjjbjbbbjjbjbjjbjjjjbbbjbjjbbbjjjjbjbjbjjjbjbjjbbjbbjjbjjbjjjbjbbjbbjjjjbjbbjbjbbjjbbjjjbjbjbjjjjjbbjjbbbjbbbbbbbjbjjjbbjjjbbjbbbbjjjjjjjbbbjjbjjjjbbbbjjjjbjjbjbbjjjbbbbjbbjjjjjjbjjjjjjbjjbjbbbbbbbbjbjbbbbjjjjjjbjbjjjbjbjjjjjbbbjbbjbbjjbbjbjjjjjjjjbbjbjbjbjjjjjjbbjjjbbjbbjjbbjjbjjbjbbjjbjbbjjjjbjjbbjbjbjjbjjbjbbjbbbjbjjbjbbbbjjjbjjjbjjbjjbbjbbjjbbbbjjjjjbjbbjbbbjjbjjjbjbbjjjbbbjjbjbbbjbbbjbjbjjbbjjjbbjjbbbbbjjjjbbbjbbjjjbbbjjbbbbbbjbbjbbjjbjjjjjbbjbjbjbjjbjjbjjjjbbjjjjbjbjjjjjbbjjjbbjbjjjbjjjjjjjjbjbbjbjbjjjbjbjjbjjjjbbbbbjbbbbjbbbjjbjbbjjbbbjjbbbbjbjjjbbjbjjjbjjjjjbjjbbbbbjjjbjbjbbbjbjjjbjjjjjjbjjbbbbjjbbbbjjjjbbbbbbbjbbjbjbbjjbbjbjjjbjjjjbbbjbjjbjbjjbjbbjjjjjbbbjbjjbbbbjjjbbjjjjjbjjbjjbbjbjjjbjjjjbbbjjjbjbbjbjbbjjbbbjbjjjbbbjjjbjjbbjbbbjjbjbjjbjbjbbbbbbbjjbjjbbjjbjbjjbbjbjbjbjjjjjjjbjjjjbjbjjjbjjjbjbjjbbjjbjjbbjjjjjbbjjjjbjbbbbbjjbbbbjbbbbbbjjjjjbjjbjbjjjbbbbjjjbjjbjbjjbbbjjjbjbjbjbjjbbbjbbjbbbbbjjjbjbbjjbjbbjbbjbjjbbbbjbbbjbjjbjjbjbjjbjjjjjbjbbbbjbjbbjjjjbjjbjbjjbjjbjbbbbjbjbbjbbbbbjjjbbjbbjjjjjbbbjjjbjbjjbjbjbbbjjbbbjjbjbbbbjbjbjbbbjbbbjbbjbbjjjbbjbbjbbbbjjjbbjjbbbbjjbbjjbbbbbbjbjbjjjbbbjbbjjbjbbbjbbjjjjjjjjjjjbbjbbjbjbjjjbjbbjjjjbbjjbjjbbjjjbbbjjjbbjbjbjjbjjjjjbbjbbbbjjjbbjjjjjbbbbbjbbjjjjbbjjjjbbbbjjjbjjbjbbbjjbbbjjbjjjbjbbbjjjjbbbbbbjjbbbbbbjjbbjjbjjbbjbjbjjbbjbbjbbbbjjjjjbjjjjbjjbbjjjjbjjbjjbbjjjjjbjjjjbjbbbjjbbbjjjbjbjbbjjjjbjjjbjbjbjbbjjjbjbjbjbjjbjbbbbjbbjbbjjbbbjbjjbjbbbbjjjjjjbjbbjbbbbbjbbbbbbbjbjbbbbbbjjjbjjjbbbjbjbbjbbjjbbbbbjjjjjbjbjbjbjjjbbjbjjbbbbjbbbbbjbjjjjjjjbbbbjjjbjbbbbjbjjjbjjbjjbbjbjjbjbbjbbjbbbbbbbbjbjjbbjbbjbjjjjjbjjjjjbbjjbjjbbjbjjjjjjjbbbbbbjbbbbjbjbbjjbjbbjbbbbjbjbbjjbjbjjjbjjbbbbbbbbjjbjjjjjbjbbjjjjjbbjjbbbbjjjjbbbbbjjbjbbbjjjjjjbbbbbjjbjbjbjjbbbjbbjbjjbbbbjjbjbbjjjjbbjbbbjjbbbjjbbjjjjjbjjbjjbjbbjbbjbbjbjjbbbjjbjjbbbjbbbjjjbbjjbjbjbbjjjbbbjjbjbbjjbbbjbbjbbbbjjbjjbjjjjjjjjbbbjjjbjjbbbbjbjjbjbbjbjjjbjbjbjjbbjjbjjjbbbjbbjbjjbjbjbbbbjjbbbbjjbjbbbbjjjbbbbbbbbbbbbbjbjbbjbbjbbbbjjbjjbjbbbjbjbjbjbbbjjbbjbbjbbjbjbbjbjjjbbbbbbjbbbjbjjbjbjjjjjbjjjbjjbbbbbjjjbjjbbbbjjbbbjjjjjbbbjjjbjjbbbjbjbbjbbbbbjbjjjbjbbjjbjbbjjbbjbjbbjjjjbjbbjjjbjbjjbbjjbjbbjjbjjjjbjjjbbbbbbbjjbbbjbjbjbbjjjjjbbbbbbbjjjjjjjbbjbjjjbjbbjbjbbjjjjbbjjjbbjjbjbbjjjjbbbbbjbjbjbbjjbjjbbbjjbbbjbjjjjbbbbjbjbbjbjbbbbjbjjjjbjjjbjjjjjjjbjjbbjbbbjjjjbbbjjjbjjjbbjjjjjbbbjjjbjbjjbjbbjbjjbbjjbjbbjbbjbbbbbbbbjbjbbbbbjbbbjjbbbbjjjbjjjbbbjjjjjbbjjbbjjbbbjjjjjbbjbjbjjjbjjjjjjbjjbjbbbjbbjjbjbbjjjjjbbbbjbbbjjjjjjjjjbbbjbjbjjbbjjjjbjbjjjjjbjjjbjbjbjbbbbjjjjjbbbjbjjjjbjbjjjjbjjbbjbjbjbjbbjbjbjbjjbbjbjjbbjjbbbbbjjjjjbbjjbjbbbbjbbbjbbbbjbbjbjbbbbjbbbjbjjjbbbjbbjjjjbbjbjbjjjjbjbbjbbbjjbjjjbjjbbbjbjbbbbjjbbbjbbjjbbjj",
             "jjbjjjjjjjjbjjjjjjjjbjbjbbjjbjbbjjbjbbbjbbjbjjbjjjjbbbjjjjjjbjjbjbbjjbbbjbbjbjjbjjjjbbbjjjjjjbjjbjbbjjbbbbbjjjbbbjjbjjbbbbjbjjjjbjbjbjjbbjjbbjjjbbbbjbjjbbjjjjjjbjjbbbjbbjjjjjjjjbbbbjjbjjbjbbjbjjjjbjbjjbjbjbbjbjjbjjbbjjjjjjjjbjbbbbjjjbjbbbbbjjjbbbjjbjjbbbbjbjjjjbjbjbjjbbjjbbjjjbbbbjbjjbbjjjjjjbjjbbbjbbjjjjjjjjbbbbjjbjjbjbbjbjjjjbjbjjbjbjbbjbjjbjjbbjjjjjjjjbjbbbbjjjbjbbbbbbjbjjbbjbbbjjjbbbjbbbjjbjjbbjjjjjbjbbjbbbbbbjjjbjbbjjjjbbjbjbbjbjjjjjbjbbbbjbjbjjjjjjjjbbjjjjbjjbbjbjbbjjbjjbbbbjbbjbbjjjjjbbjbbbjjbbjjbbjjbjbjjbjjbjjbbbjbbjjjjjjjbbbbbbjbjjbbjbbbjjjbbbjbbbjjbjjbbjjjjjbjbbjbbbbbbjjjbjbbjjjjbbjbjbbjbjjjjjbjbbbbjbjbjjjjjjjjbbjjjjbjjbbjbjbbjjbjjbbbbjbbjbbjjjjjbbjbbbjjbbjjbbjjbjbjjbjjbjjbbbjbbjjjjjjjbbbbbbjbbjjbjbbbbjjbbbjjbjjjbjbbbjjbjbjjbjjjjbbbjbjjbbbjjjjbjbjbjjjbjbjjbbjbbjjbjjbjjjbjbbjbbjjjjbjbbjbjbbjjbbjjjbjbjbjjjjjbbjjbbbjbbbbbbjbbjjbjbbbbjjbbbjjbjjjbjbbbjjbjbjjbjjjjbbbjbjjbbbjjjjbjbjbjjjbjbjjbbjbbjjbjjbjjjbjbbjbbjjjjbjbbjbjbbjjbbjjjbjbjbjjjjjbbjjbbbjbbbbbbbjbjjjbbjjjbbjbbbbjjjjjjjbbbjjbjjjjbbbbjjjjbjjbjbbjjjbbbbjbbjjjjjjbjjjjjjbjjbjbbbbbbbjbjjjbbjjjbbjbbbbjjjjjjjbbbjjbjjjjbbbbjjjjbjjbjbbjjjbbbbjbbjjjjjjbjjjjjjbjjbjbbbbbbbbjbjbbbbjjjjjjbjbjjjbjbjjjjjbbbjbbjbbjjbbjbjjjjjjjjbbjbjbjbjjjjjjbbjjjbbjbbjjbbjjbjjbjbbjjbjbbjjjjbjjbbjbjbjjbjjbjbbjbbbjbjjbjbbbbjjjbjjjbjjbjjbbjbbjjbbbbjjjjjbjbbjbbbjjbjjjbjbbjjjbbbjjbjbbbjbbbjbjbjjbbjjjbbjjbbbbbjjjjbbbjbbjjjbbbjjbbbbbbjbbjbbjjbjjjjjbbjbjbjbjjbjjbjjjjbbjjjjbjbjjjjjbbjjjbbjbjjjbjjjjjjjjbjbbjbjbjjjbjbjjbjjjjbbbbbjbbbbjbbbjjbjbbjjbbbjjbbbbjbjjjbbjbjjjbjjjjjbjjbbbbbjjjbjbjbbbjbjjjbjjjjjjbjjbbbbjjbbbbjjjjbbbbbbbjbbjbjbbjjbbjbjjjbjjjjbbbjbjjbjbjjbjbbjjjjjbbbjbjjbbbbjjjbbjjjjjbjjbjjbbjbjjjbjjjjbbbjjjbjbbjbjbbjjbbbjbjjjbbbjjjbjjbbjbbbjjbjbjjbjbjbbbbbbbjjbjjbbjjbjbjjbbjbjbjbjjjjjjjbjjjjbjbjjjbjjjbjbjjbbjjbjjbbjjjjjbbjjjjbjbbbbbjjbbbbjbbbbbbjjjjjbjjbjbjjjbbbbjjjbjjbjbjjbbbjjjbjbjbjbjjbbbjbbjbbbbbjjjbjbbjjbjbbjbbjbjjbbbbjbbbjbjjbjjbjbjjbjjjjjbjbbbbjbjbbjjjjbjjbjbjjbjjbjbbbbjbjbbjbbbbbjjjbbjbbjjjjjbbbjjjbjbjjbjbjbbbjjbbbjjbjbbbbjbjbjbbbjbbbjbbjbbjjjbbjbbjbbbbjjjbbjjbbbbjjbbjjbbbbbbjbjbjjjbbbjbbjjbjbbbjbbjjjjjjjjjjjbbjbbjbjbjjjbjbbjjjjbbjjbjjbbjjjbbbjjjbbjbjbjjbjjjjjbbjbbbbjjjbbjjjjjbbbbbjbbjjjjbbjjjjbbbbjjjbjjbjbbbjjbbbjjbjjjbjbbbjjjjbbbbbbjjbbbbbbjjbbjjbjjbbjbjbjjbbjbbjbbbbjjjjjbjjjjbjjbbjjjjbjjbjjbbjjjjjbjjjjbjbbbjjbbbjjjbjbjbbjjjjbjjjbjbjbjbbjjjbjbjbjbjjbjbbbbjbbjbbjjbbbjbjjbjbbbbjjjjjjbjbbjbbbbbjbbbbbbbjbjbbbbbbjjjbjjjbbbjbjbbjbbjjbbbbbjjjjjbjbjbjbjjjbbjbjjbbbbjbbbbbjbjjjjjjjbbbbjjjbjbbbbjbjjjbjjbjjbbjbjjbjbbjbbjbbbbbbbbjbjjbbjbbjbjjjjjbjjjjjbbjjbjjbbjbjjjjjjjbbbbbbjbbbbjbjbbjjbjbbjbbbbjbjbbjjbjbjjjbjjbbbbbbbbjjbjjjjjbjbbjjjjjbbjjbbbbjjjjbbbbbjjbjbbbjjjjjjbbbbbjjbjbjbjjbbbjbbjbjjbbbbjjbjbbjjjjbbjbbbjjbbbjjbbjjjjjbjjbjjbjbbjbbjbbjbjjbbbjjbjjbbbjbbbjjjbbjjbjbjbbjjjbbbjjbjbbjjbbbjbbjbbbbjjbjjbjjjjjjjjbbbjjjbjjbbbbjbjjbjbbjbjjjbjbjbjjbbjjbjjjbbbjbbjbjjbjbjbbbbjjbbbbjjbjbbbbjjjbbbbbbbbjbjbbbbjjjjjjbjbjjjbjbjjjjjbbbjbbjbbjjbbjbjjjjjjjjbbjbjbjbjjjjjjbbjjjbbjbbjjbbjjbjjbjbbjjbjbbjjjjbjjbbjbjbjjbjjbjbbjbbbjbjjbjbbbbjjjbjjjbjjbjjbbjbbjjbbbbjjjjjbjbbjbbbjjbjjjbjbbjjjbbbjjbjbbbjbbbjbjbjjbbjjjbbjjbbbbbjjjjbbbjbbjjjbbbjjbbbbbbjbbjbbjjbjjjjjbbjbjbjbjjbjjbjjjjbbjjjjbjbjjjjjbbjjjbbjbjjjbjjjjjjjjbjbbjbjbjjjbjbjjbjjjjbbbbbjbbbbjbbbjjbjbbjjbbbjjbbbbjbjjjbbjbjjjbjjjjjbjjbbbbbjjjbjbjbbbjbjjjbjjjjjjbjjbbbbjjbbbbjjjjbbbbbbbjbbjbjbbjjbbjbjjjbjjjjbbbjbjjbjbjjbjbbjjjjjbbbjbjjbbbbjjjbbjjjjjbjjbjjbbjbjjjbjjjjbbbjjjbjbbjbjbbjjbbbjbjjjbbbjjjbjjbbjbbbjjbjbjjbjbjbbbbbbbjjbjjbbjjbjbjjbbjbjbjbjjjjjjjbjjjjbjbjjjbjjjbjbjjbbjjbjjbbjjjjjbbjjjjbjbbbbbjjbbbbjbbbbbbjjjjjbjjbjbjjjbbbbjjjbjjbjbjjbbbjjjbjbjbjbjjbbbjbbjbbbbbjjjbjbbjjbjbbjbbjbjjbbbbjbbbjbjjbjjbjbjjbjjjjjbjbbbbjbjbbjjjjbjjbjbjjbjjbjbbbbjbjbbjbbbbbjjjbbjbbjjjjjbbbjjjbjbjjbjbjbbbjjbbbjjbjbbbbjbjbjbbbjbbbjbbjbbjjjbbjbbjbbbbjjjbbjjbbbbjjbbjjbbbbbbjbjbjjjbbbjbbjjbjbbbjbbjjjjjjjjjjjbbjbbjbjbjjjbjbbjjjjbbjjbjjbbjjjbbbjjjbbjbjbjjbjjjjjbbjbbbbjjjbbjjjjjbbbbbjbbjjjjbbjjjjbbbbjjjbjjbjbbbjjbbbjjbjjjbjbbbjjjjbbbbbbjjbbbbbbjjbbjjbjjbbjbjbjjbbjbbjbbbbjjjjjbjjjjbjjbbjjjjbjjbjjbbjjjjjbjjjjbjbbbjjbbbjjjbjbjbbjjjjbjjjbjbjbjbbjjjbjbjbjbjjbjbbbbjbbjbbjjbbbjbjjbjbbbbjjjjjjbjbbjbbbbbjbbbbbbbjbjbbbbbbjjjbjjjbbbjbjbbjbbjjbbbbbjjjjjbjbjbjbjjjbbjbjjbbbbjbbbbbjbjjjjjjjbbbbjjjbjbbbbjbjjjbjjbjjbbjbjjbjbbjbbjbbbbbbbbjbjjbbjbbjbjjjjjbjjjjjbbjjbjjbbjbjjjjjjjbbbbbbjbbbbjbjbbjjbjbbjbbbbjbjbbjjbjbjjjbjjbbbbbbbbjjbjjjjjbjbbjjjjjbbjjbbbbjjjjbbbbbjjbjbbbjjjjjjbbbbbjjbjbjbjjbbbjbbjbjjbbbbjjbjbbjjjjbbjbbbjjbbbjjbbjjjjjbjjbjjbjbbjbbjbbjbjjbbbjjbjjbbbjbbbjjjbbjjbjbjbbjjjbbbjjbjbbjjbbbjbbjbbbbjjbjjbjjjjjjjjbbbjjjbjjbbbbjbjjbjbbjbjjjbjbjbjjbbjjbjjjbbbjbbjbjjbjbjbbbbjjbbbbjjbjbbbbjjjbbbbbbbbbbbbbjbjbbjbbjbbbbjjbjjbjbbbjbjbjbjbbbjjbbjbbjbbjbjbbjbjjjbbbbbbjbbbjbjjbjbjjjjjbjjjbjjbbbbbjjjbjjbbbbjjbbbjjjjjbbbjjjbjjbbbjbjbbjbbbbbjbjjjbjbbjjbjbbjjbbjbjbbjjjjbjbbjjjbjbjjbbjjbjbbjjbjjjjbjjjbbbbbbbjjbbbjbjbjbbjjjjjbbbbbbbjjjjjjjbbjbjjjbjbbjbjbbjjjjbbjjjbbjjbjbbjjjjbbbbbjbjbjbbjjbjjbbbjjbbbjbjjjjbbbbjbjbbjbjbbbbjbjjjjbjjjbjjjjjjjbjjbbjbbbjjjjbbbjjjbjjjbbjjjjjbbbjjjbjbjjbjbbjbjjbbjjbjbbjbbjbbbbbbbbjbjbbbbbjbbbjjbbbbjjjbjjjbbbjjjjjbbjjbbjjbbbjjjjjbbjbjbjjjbjjjjjjbjjbjbbbjbbjjbjbbjjjjjbbbbjbbbjjjjjjjjjbbbjbjbjjbbjjjjbjbjjjjjbjjjbjbjbjbbbbjjjjjbbbjbjjjjbjbjjjjbjjbbjbjbjbjbbjbjbjbjjbbjbjjbbjjbbbbbjjjjjbbjjbjbbbbjbbbjbbbbjbbjbjbbbbjbbbjbjjjbbbjbbjjjjbbjbjbjjjjbjbbjbbbjjbjjjbjjbbbjbjbbbbjjbbbjbbjjbbjjbbbbbbbbbbbbbjbjbbjbbjbbbbjjbjjbjbbbjbjbjbjbbbjjbbjbbjbbjbjbbjbjjjbbbbbbjbbbjbjjbjbjjjjjbjjjbjjbbbbbjjjbjjbbbbjjbbbjjjjjbbbjjjbjjbbbjbjbbjbbbbbjbjjjbjbbjjbjbbjjbbjbjbbjjjjbjbbjjjbjbjjbbjjbjbbjjbjjjjbjjjbbbbbbbjjbbbjbjbjbbjjjjjbbbbbbbjjjjjjjbbjbjjjbjbbjbjbbjjjjbbjjjbbjjbjbbjjjjbbbbbjbjbjbbjjbjjbbbjjbbbjbjjjjbbbbjbjbbjbjbbbbjbjjjjbjjjbjjjjjjjbjjbbjbbbjjjjbbbjjjbjjjbbjjjjjbbbjjjbjbjjbjbbjbjjbbjjbjbbjbbjbbbbbbbbjbjbbbbbjbbbjjbbbbjjjbjjjbbbjjjjjbbjjbbjjbbbjjjjjbbjbjbjjjbjjjjjjbjjbjbbbjbbjjbjbbjjjjjbbbbjbbbjjjjjjjjjbbbjbjbjjbbjjjjbjbjjjjjbjjjbjbjbjbbbbjjjjjbbbjbjjjjbjbjjjjbjjbbjbjbjbjbbjbjbjbjjbbjbjjbbjjbbbbbjjjjjbbjjbjbbbbjbbbjbbbbjbbjbjbbbbjbbbjbjjjbbbjbbjjjjbbjbjbjjjjbjbbjbbbjjbjjjbjjbbbjbjbbbbjjbbbjbbjjbbjj");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
