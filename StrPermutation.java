import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 1.3:
 * Given two strings,write a method to decide if one is a permutation of the
 * other.
 */
public class StrPermutation {
    // time complexity: O(nlogn), space complexity: O(n)
    public boolean isPermutationBySort(String s1, String s2) {
        if (s1 == null || s2 == null) return false;

        char[] c1 = s1.toCharArray();
        Arrays.sort(c1);
        char[] c2 = s2.toCharArray();
        Arrays.sort(c2);
        return Arrays.equals(c1, c2);
    }

    // time complexity: O(n), space complexity: O(n)
    public boolean isPermutationByMapCount(String s1, String s2) {
        if (s1 == null || s2 == null) return false;

        int len = s1.length();
        if (s2.length() != len) return false;

        Map<Character, Integer> counts = new HashMap<Character, Integer>();
        for (int i = 0; i < len; ++i) {
            char c = s1.charAt(i);
            int count = counts.getOrDefault(c, 0);
            counts.put(c, count + 1);
        }
        for (int i = 0; i < len; ++i) {
            char c = s2.charAt(i);
            int count = counts.getOrDefault(c, 0);
            if (count <= 0) return false;
            counts.put(c, count - 1);
        }

        // notice that s1 and s2 have the same length
        return true;
    }

    // time complexity: O(n), space complexity: O(1)
    public boolean isPermutationByArrayCount(String s1, String s2) {
        if (s1 == null || s2 == null) return false;

        int len = s1.length();
        if (s2.length() != len) return false;

        int[] counts = new int[256]; // ***Assumption
        for (int i = 0; i < len; ++i) {
            char c = s1.charAt(i);
            counts[c]++;
        }
        for (int i = 0; i < len; ++i) {
            char c = s2.charAt(i);
            if (--counts[c] < 0) {
                return false;
            }
        }

        // notice that s1 and s2 have the same length
        return true;
    }

    void test(String s1, String s2, boolean expected) {
        assertEquals(expected, isPermutationBySort(s1, s2));
        assertEquals(expected, isPermutationByMapCount(s1, s2));
        assertEquals(expected, isPermutationByArrayCount(s1, s2));
    }

    @Test
    public void test1() {
        test(null, null, false);
    }

    @Test
    public void test2() {
        test("", "", true);
    }

    @Test
    public void test3() {
        test("abc", "cba", true);
    }

    @Test
    public void test4() {
        test("aba", "ab", false);
    }

    @Test
    public void test5() {
        test("abc", "d", false);
    }

    @Test
    public void test6() {
        test("abcccbb", "bcbcacb", true);
    }

    @Test
    public void test7() {
        test("abcccbb", "bcbcac", false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("StrPermutation");
    }
}
