import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1647: https://leetcode.com/problems/minimum-deletions-to-make-character-frequencies-unique/
//
// A string s is called good if there are no two different characters in s that have the same
// frequency.
// Given a string s, return the minimum number of characters you need to delete to make s good.
// The frequency of a character in a string is the number of times it appears in the string.
// For example, in the string "aab", the frequency of 'a' is 2, while the frequency of 'b' is 1.
//
// Constraints:
// 1 <= s.length <= 10^5
// s contains only lowercase English letters.
public class MinDelToMakeCharFrequencyUnique {
    // Heap
    // time complexity: O(N), space complexity: O(N)
    // 11 ms(66.28%), 39.5 MB(85.88%) for 103 tests
    public int minDeletions(String s) {
        int[] freq = new int[26];
        for (char c : s.toCharArray()) {
            freq[c - 'a']++;
        }
        PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> b - a);
        for (int f : freq) {
            if (f != 0) {
                pq.offer(f);
            }
        }
        int res = 0;
        for (int prev = Integer.MAX_VALUE; !pq.isEmpty(); ) {
            int cur = pq.poll();
            if (cur < prev) {
                prev = cur;
            } else {
                res++;
                if (cur > 1) {
                    pq.offer(cur - 1);
                }
            }
        }
        return res;
    }

    // Sort
    // time complexity: O(N), space complexity: O(N)
    // 10 ms(93.34%), 39.7 MB(67.31%) for 103 tests
    public int minDeletions2(String s) {
        int[] freq = new int[26];
        for (char c : s.toCharArray()) {
            freq[c - 'a']++;
        }
        Arrays.sort(freq);
        int res = 0;
        for (int i = freq.length - 1, expected = freq[i]; i >= 0 && freq[i] > 0; i--) {
            if (freq[i] > expected) {
                res += freq[i] - expected;
            } else {
                expected = freq[i];
            }
            if (expected > 0) {
                expected--;
            }
        }
        return res;
    }

    // Greedy + Set
    // time complexity: O(N), space complexity: O(1)
    // 15 ms(69.48%), 39.8 MB(67.31%) for 103 tests
    public int minDeletions3(String s) {
        int[] freq = new int[26];
        for (int i = s.length() - 1; i >= 0; i--) {
            freq[s.charAt(i) - 'a']++;
        }
        int res = 0;
        Set<Integer> used = new HashSet<>();
        for (int i = freq.length - 1; i >= 0; i--) {
            for (int f = freq[i]; f > 0 && !used.add(f); f--, res++) {}
        }
        return res;
    }

    private void test(String s, int expected) {
        assertEquals(expected, minDeletions(s));
        assertEquals(expected, minDeletions2(s));
        assertEquals(expected, minDeletions3(s));
    }

    @Test public void test() {
        test("bbcebab", 2);
        test("aab", 0);
        test("aaabbbcc", 2);
        test("ceabaacb", 2);
        test("cacddeeeffghhijkklmmnqqqrsstttuvwxxxyyzzzeabaacb", 38);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
