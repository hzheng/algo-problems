import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC438: https://leetcode.com/problems/find-all-anagrams-in-a-string/
//
// Given a string s and a non-empty string p, find all the start indices of p's
// anagrams in s. Strings consists of lowercase English letters only and the
// length of both strings s and p will not be larger than 20,100.
public class FindAnagrams {
    // Hash Table + Sliding Window
    // beats N/A(21 ms for 30 tests)
    public List<Integer> findAnagrams(String s, String p) {
        int[] count = new int[26];
        for (char c : p.toCharArray()) {
            count[c - 'a']++;
        }
        List<Integer> res = new ArrayList<>();
        int[] curCount = new int[26];
        for (int i = 0, matched = 0, toMatch = p.length(); i < s.length(); i++) {
            int index = s.charAt(i) - 'a';
            if (count[index] == 0) {
                curCount = new int[26];
                matched = 0;
                continue;
            }
            if (curCount[index] < count[index]) {
                curCount[index]++;
                matched++;
            } else {
                for (int j = i - matched; s.charAt(j) != s.charAt(i); j++) {
                    curCount[s.charAt(j) - 'a']--;
                    matched--;
                }
            }
            if (matched == toMatch) {
                res.add(i - toMatch + 1);
            }
        }
        return res;
    }

    // Hash Table + Two Pointers
    // beats N/A(24 ms for 30 tests)
    public List<Integer> findAnagrams2(String s, String p) {
        List<Integer> res = new ArrayList<>();
        int[] count = new int[26];
        for (char c : p.toCharArray()) {
            count[c - 'a']++;
        }
        for (int i = 0, j = 0, len = p.length(), toMatch = len; j < s.length(); ) {
            if (count[s.charAt(j++) - 'a']-- > 0) {
                toMatch--;
            }
            if (toMatch == 0) {
                res.add(i);
            }
            if (j - i == len && count[s.charAt(i++) - 'a']++ >= 0) {
                toMatch++;
            }
        }
        return res;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<String, String, List<Integer>> findAnagrams,
              String s, String p, Integer ... expected) {
        List<Integer> res = findAnagrams.apply(s, p);
        assertArrayEquals(expected, res.toArray(new Integer[0]));
    }

    void test(String s, String p, Integer ... expected) {
        FindAnagrams f = new FindAnagrams();
        test(f::findAnagrams, s, p, expected);
        test(f::findAnagrams2, s, p, expected);
    }

    @Test
    public void test() {
        test("cbacccab", "abc", 0, 1, 5);
        test("cbaebabacd", "abc", 0, 6);
        test("cbac", "abc", 0, 1);
        test("abab", "ab", 0, 1, 2);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FindAnagrams");
    }
}
