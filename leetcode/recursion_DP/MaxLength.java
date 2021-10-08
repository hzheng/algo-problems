import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1239: https://leetcode.com/problems/maximum-length-of-a-concatenated-string-with-unique-characters/
//
// Given an array of strings arr. String s is a concatenation of a sub-sequence of arr which have
// unique characters. Return the maximum possible length of s.
// Constraints:
// 1 <= arr.length <= 16
// 1 <= arr[i].length <= 26
// arr[i] contains only lower case English letters.
public class MaxLength {
    // Recursion + Dynamic Programming(Top-Down) + Set + Bit Manipulation
    // 2 ms(93.44%), 36.7 MB(87.51%) for 89 tests
    public int maxLength(List<String> arr) {
        Set<Integer> cand = new HashSet<>();
        for (String a : arr) {
            cand.add(encode(a));
        }
        cand.remove(0);
        return dfs(cand.toArray(new Integer[0]), 0, 0, new int[1]);
    }

    private int dfs(Integer[] candidates, int index, int mask, int[] res) {
        if (res[0] == 26) {return res[0];}

        if (index == candidates.length) {
            return res[0] = Math.max(res[0], Integer.bitCount(mask));
        }

        int cur = candidates[index];
        if ((cur & mask) == 0) {
            dfs(candidates, index + 1, mask | cur, res);
        }
        return dfs(candidates, index + 1, mask, res);
    }

    private int encode(String word) {
        int[] count = new int[26];
        for (char c : word.toCharArray()) {
            if (++count[c - 'a'] > 1) {return 0;}
        }
        int res = 0;
        for (int i = count.length - 1; i >= 0; i--) {
            if (count[i] != 0) {
                res |= (1 << i);
            }
        }
        return res;
    }

    // Bit Manipulation
    // 6 ms(83.88%), 38.8 MB(64.41%) for 89 tests
    public int maxLength2(List<String> arr) {
        List<Integer> candidates = new ArrayList<>();
        candidates.add(0);
        int res = 0;
        outer:
        for (String s : arr) {
            int code = 0;
            for (char c : s.toCharArray()) {
                int mask = 1 << (c - 'a');
                if ((code & mask) > 0) {continue outer;}
                code |= mask;
            }
            for (int i = candidates.size() - 1; i >= 0; i--) {
                int cand = candidates.get(i);
                if ((cand & code) == 0) {
                    candidates.add(cand | code);
                    res = Math.max(res, Integer.bitCount(cand | code));
                }
            }
        }
        return res;
    }

    private void test(String[] arr, int expected) {
        assertEquals(expected, maxLength(Arrays.asList(arr)));
        assertEquals(expected, maxLength2(Arrays.asList(arr)));
    }

    // test
    @Test public void test() {
        test(new String[] {"un", "iq", "ue"}, 4);
        test(new String[] {"cha", "r", "act", "ers"}, 6);
        test(new String[] {"cha", "r", "act", "ss", "ers"}, 6);
        test(new String[] {"abcdefghijklmnopqrstuvwxyz"}, 26);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
