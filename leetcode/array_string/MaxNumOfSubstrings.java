import java.util.*;
import java.util.function.Function;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1520: https://leetcode.com/problems/maximum-number-of-non-overlapping-substrings/
//
// Given a string s of lowercase letters, you need to find the maximum number of non-empty
// substrings of s that meet the following conditions:
// The substrings do not overlap
// A substring that contains a certain character c must also contain all occurrences of c.
// Find the maximum number of substrings that meet the above conditions. If there are multiple
// solutions with the same number of substrings, return the one with minimum total length. It can be
// shown that there exists a unique solution of minimum total length.
// Notice that you can return the substrings in any order.
// Constraints:
// 1 <= s.length <= 10^5
// s contains only lowercase English letters.
public class MaxNumOfSubstrings {
    // Sort + Hash Table
    // time complexity: O(N), space complexity: O(1)
    // 35 ms(38.56%), 40.4 MB(5.23%) for 281 tests
    public List<String> maxNumOfSubstrings(String s) {
        char[] cs = s.toCharArray();
        Map<Character, int[]> map = new HashMap<>();
        for (int i = 0; i < cs.length; i++) {
            final int j = i;
            map.computeIfAbsent(cs[j], x -> new int[] {j, 0})[1] = j;
        }
        List<int[]> ranges = new ArrayList<>(map.values());
        ranges.sort(Comparator.comparingInt(a -> a[0]));
        List<String> res = new ArrayList<>();
        int prev = -1;
        outer:
        for (int[] range : ranges) {
            for (int i = range[0] + 1; i <= range[1]; i++) {
                int[] innerRange = map.get(cs[i]);
                if (innerRange[0] < range[0]) { continue outer; }

                range[1] = Math.max(range[1], innerRange[1]); // extend the end when possible
            }
            String str = String.valueOf(cs, range[0], range[1] - range[0] + 1);
            if (prev > range[0]) { // remove superset
                res.set(res.size() - 1, str);
            } else {
                res.add(str);
            }
            prev = range[1];
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 11 ms(96.51%), 40.2 MB(5.23%) for 281 tests
    public List<String> maxNumOfSubstrings2(String s) {
        char[] cs = s.toCharArray();
        int[] start = new int[26];
        int[] end = new int[26];
        Arrays.fill(start, cs.length);
        for (int i = 0; i < cs.length; i++) {
            int c = cs[i] - 'a';
            start[c] = Math.min(start[c], i);
            end[c] = i;
        }
        List<String> res = new ArrayList<>();
        for (int i = 0, right = -1; i < cs.length; i++) {
            if (i != start[cs[i] - 'a']) { continue; } // only consider the first character

            int newRight = extendRight(cs, i, start, end);
            if (newRight >= 0) {
                if (i > right) {
                    res.add("");
                }
                right = newRight;
                res.set(res.size() - 1, String.valueOf(cs, i, right - i + 1));
            }
        }
        return res;
    }

    private int extendRight(char[] cs, int index, int[] start, int[] end) {
        int right = end[cs[index] - 'a'];
        for (int i = index + 1; i <= right; i++) {
            if (start[cs[i] - 'a'] < index) { return -1; }

            right = Math.max(right, end[cs[i] - 'a']);
        }
        return right;
    }

    private void test(String s, String[] expected) {
        MaxNumOfSubstrings m = new MaxNumOfSubstrings();
        test(m::maxNumOfSubstrings, s, expected);
        test(m::maxNumOfSubstrings2, s, expected);
    }

    private void test(Function<String, List<String>> maxNumOfSubstrings, String s,
                      String[] expected) {
        List<String> expectedList = Arrays.asList(expected);
        expectedList.sort(Comparator.comparing(a -> a));
        List<String> actualList = maxNumOfSubstrings.apply(s);
        actualList.sort(Comparator.comparing(a -> a));
        assertEquals(expectedList, actualList);
    }

    @Test public void test() {
        test("abbaccd", new String[] {"d", "bb", "cc"});
        test("adefaddaccc", new String[] {"e", "f", "ccc"});
        test("bbcacbaba", new String[] {"bbcacbaba"});
        test("zzababzzz" , new String[] {"abab"});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
