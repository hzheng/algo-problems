import org.junit.Test;

import static org.junit.Assert.*;

// LC1768: https://leetcode.com/problems/merge-strings-alternately/
//
// You are given two strings word1 and word2. Merge the strings by adding letters in alternating
// order, starting with word1. If a string is longer than the other, append the additional letters
// onto the end of the merged string. Return the merged string.
//
// Constraints:
// 1 <= word1.length, word2.length <= 100
// word1 and word2 consist of lowercase English letters.
public class MergeAlternately {
    // time complexity: O(M+N), space complexity: O(1)
    // 0 ms(100.00%), 37.5 MB(40.00%) for 108 tests
    public String mergeAlternately(String word1, String word2) {
        StringBuilder sb = new StringBuilder();
        int n1 = word1.length();
        int n2 = word2.length();
        int n = Math.min(n1, n2);
        for (int i = 0; i < n; i++) {
            sb.append(word1.charAt(i));
            sb.append(word2.charAt(i));
        }
        for (int i = n; i < n1; i++) {
            sb.append(word1.charAt(i));
        }
        for (int i = n; i < n2; i++) {
            sb.append(word2.charAt(i));
        }
        return sb.toString();
    }

    // time complexity: O(M+N), space complexity: O(1)
    // 0 ms(100.00%), 37.3 MB(40.00%) for 108 tests
    public String mergeAlternately2(String word1, String word2) {
        StringBuilder sb = new StringBuilder();
        int n1 = word1.length();
        int n2 = word2.length();
        int n = Math.max(n1, n2);
        for (int i = 0; i < n; i++) {
            if (i < n1) {
                sb.append(word1.charAt(i));
            }
            if (i < n2) {
                sb.append(word2.charAt(i));
            }
        }
        return sb.toString();
    }

    private void test(String word1, String word2, String expected) {
        assertEquals(expected, mergeAlternately(word1, word2));
        assertEquals(expected, mergeAlternately2(word1, word2));
    }

    @Test public void test() {
        test("ab", "pqrs", "apbqrs");
        test("abcd", "pq", "apbqcd");
        test("abcde", "a", "aabcde");
        test("abcdef", "uvwxyz", "aubvcwdxeyfz");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
