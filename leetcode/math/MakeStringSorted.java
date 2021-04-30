import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1830: https://leetcode.com/problems/minimum-number-of-operations-to-make-string-sorted/
//
// You are given a string s (0-indexed). You are asked to perform the following operation on s
// until you get a sorted string:
// Find the largest index i such that 1 <= i < s.length and s[i] < s[i - 1].
// Find the largest index j such that i <= j < s.length and s[k] < s[i - 1] for all the possible
// values of k in the range [i, j] inclusive.
// Swap the two characters at indices i - 1 and j.
// Reverse the suffix starting at index i.
// Return the number of operations needed to make the string sorted. Since the answer can be too
// large, return it modulo 10^9 + 7.
//
// Constraints:
// 1 <= s.length <= 3000
// s consists only of lowercase English letters.
public class MakeStringSorted {
    private static final int MOD = 1000_000_007;

    // Dynamic Programming
    // Time Limit Exceeded
    public int makeStringSorted0(String s) {
        char[] cs = s.toCharArray();
        Integer[] map = new Integer[26];
        Map<List<Integer>, Integer> dp = new HashMap<>();
        Arrays.fill(map, 0);
        for (char c : cs) {
            map[c - 'a']++;
        }
        List<Integer> mapList = Arrays.asList(map);
        int res = permuate(mapList, dp) - 1;
        List<Integer> list = new ArrayList<>(mapList);
        for (char c : cs) {
            for (int j = c - 'a' + 1; j < 26; j++) {
                int cnt = list.get(j);
                if (cnt == 0) { continue; }

                list.set(j, cnt - 1);
                res = (res + MOD - dp.get(list)) % MOD;
                list.set(j, cnt);
            }
            list.set(c - 'a', list.get(c - 'a') - 1);
        }
        return res;
    }

    private int permuate(List<Integer> freq, Map<List<Integer>, Integer> dp) {
        Integer cached = dp.get(freq);
        if (cached != null) { return cached; }

        int size = new HashSet<>(freq).size();
        int res = 0;
        if (size <= 1) {
            res = size;
        } else {
            for (int i = 0, n = freq.size(); i < n; i++) {
                int f = freq.get(i);
                if (f == 0) { continue; }

                freq.set(i, f - 1);
                res = (res + permuate(freq, dp)) % MOD;
                freq.set(i, f);
            }
        }
        dp.put(new ArrayList<>(freq), res);
        return res;
    }

    // Combinatorics + Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // 56 ms(88.15%), 38.6 MB(89.63%) for 73 tests
    public int makeStringSorted(String s) {
        int n = s.length();
        final long[] factorials = new long[n + 1];
        final long[] revFactorials = new long[n + 1];
        factorials[0] = revFactorials[0] = 1;
        for (int i = 1; i <= n; i++) {
            factorials[i] = ((long)i) * factorials[i - 1] % MOD;
            revFactorials[i] = modInverse(factorials[i], MOD);
        }
        int[] freq = new int[26];
        long res = 0;
        for (int i = n - 1; i >= 0; i--) {
            int index = s.charAt(i) - 'a';
            freq[index]++;
            int reversedCount = 0;
            for (int j = 0; j < index; j++) {
                reversedCount += freq[j];
            }
            long operations = reversedCount * factorials[n - i - 1] % MOD;
            for (int f : freq) { // repeated considered
                operations = operations * revFactorials[f] % MOD;
            }
            res = (res + operations) % MOD;
        }
        return (int)res;
    }

    private long modInverse(long x, int mod) {
        long res = 1; // a^(-1) = a^(m-2) % (mod m)
        for (int n = mod - 2; n > 0; n >>= 1) {
            if ((n & 1) != 0) {
                res = (res * x) % mod;
            }
            x = (x * x) % mod;
        }
        return res;
    }

    private void test(String s, int expected) {
        if (s.length() < 50) {
            assertEquals(expected, makeStringSorted0(s));
        }
        assertEquals(expected, makeStringSorted(s));
    }

    @Test public void test1() {
        test("ba", 1);
        test("cba", 5);
        test("aabaa", 2);
        test("cdbea", 63);
        test("leetcodeleetcodeleetcode", 982157772);
        test("leetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcodeleetcode",
             281245283);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
