import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC763: https://leetcode.com/problems/partition-labels/
//
// A string S of lowercase English letters is given. We want to partition this string into as many
// parts as possible so that each letter appears in at most one part, and return a list of integers
// representing the size of these parts.
//
// Note:
// S will have length in range [1, 500].
// S will consist of lowercase English letters ('a' to 'z') only.
public class PartitionLabels {
    // Hash Table
    // time complexity: O(N), space complexity: O(1)
    // 2 ms(99.18%), 37.7 MB(65.26%) for 24 tests
    public List<Integer> partitionLabels(String S) {
        int[] last = new int[26];
        char[] cs = S.toCharArray();
        int n = cs.length;
        for (int i = 0; i < n; i++) {
            last[cs[i] - 'a'] = i;
        }
        List<Integer> res = new ArrayList<>();
        for (int i = 0, target = 0, prev = -1; i < n; i++) {
            int c = cs[i] - 'a';
            if (last[c] > i) {
                target = Math.max(target, last[c]);
            } else if (i == target) {
                res.add(i - prev);
                prev = i;
                target++;
            }
        }
        return res;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(1)
    // 2 ms(99.18%), 37.7 MB(65.26%) for 24 tests
    public List<Integer> partitionLabels2(String S) {
        int[] last = new int[26];
        char[] cs = S.toCharArray();
        int n = cs.length;
        for (int i = 0; i < n; i++) {
            last[cs[i] - 'a'] = i;
        }
        List<Integer> res = new ArrayList<>();
        for (int i = 0, target = 0, prev = -1; i < n; i++) {
            target = Math.max(target, last[cs[i] - 'a']);
            if (i == target) {
                res.add(i - prev);
                prev = i;
            }
        }
        return res;
    }

    private void test(String S, Integer[] expected) {
        List<Integer> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, partitionLabels(S));
        assertEquals(expectedList, partitionLabels2(S));
    }

    @Test public void test() {
        test("ababcbacadefegdehijhklij", new Integer[] {9, 7, 8});
        test("ababcdbacadefegjdehfiijhklijijrfiouekfjuksywyx", new Integer[] {41, 1, 3, 1});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
