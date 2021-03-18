import java.math.BigInteger;
import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1297: https://leetcode.com/problems/maximum-number-of-occurrences-of-a-substring/
//
// Given a string s, return the maximum number of ocurrences of any substring under the following
// rules:
// The number of unique characters in the substring must be less than or equal to maxLetters.
// The substring size must be between minSize and maxSize inclusive.
//
// Constraints:
// 1 <= s.length <= 10^5
// 1 <= maxLetters <= 26
// 1 <= minSize <= maxSize <= min(26, s.length)
// s only contains lowercase English letters.
public class MaxFreq {
    // Rabin-Karp algorithm(Rolling Hash) + Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 40 ms(75.68%), 40.9 MB(93.99%) for 40 tests
    public int maxFreq(String s, int maxLetters, int minSize, int maxSize) {
        final int MOD = 1_000_000_007;
        final int base = 29; //26;
        final int inverse = BigInteger.valueOf(base).modInverse(BigInteger.valueOf(MOD)).intValue();
        long power = 1;
        long hash = 0;
        Map<Integer, Integer> freq = new HashMap<>();
        for (int i = 0; ; i++) {
            int k = s.charAt(i) - 'a';
            freq.put(k, freq.getOrDefault(k, 0) + 1);
            hash += power * k;
            if (i == minSize - 1) { break; }

            power = (power * base) % MOD;
        }
        Map<Long, Integer> hashCount = new HashMap<>();
        if (freq.size() <= maxLetters) {
            hashCount.put(hash, 1);
        }
        int n = s.length();
        for (int i = minSize; i < n; i++) {
            int oldIndex = s.charAt(i - minSize) - 'a';
            int oldCount = freq.get(oldIndex);
            if (oldCount == 1) {
                freq.remove(oldIndex);
            } else {
                freq.put(oldIndex, oldCount - 1);
            }
            hash = ((hash - oldIndex) * inverse) % MOD;
            int newIndex = s.charAt(i) - 'a';
            hash = (hash + power * newIndex) % MOD;
            freq.put(newIndex, freq.getOrDefault(newIndex, 0) + 1);
            if (freq.size() <= maxLetters) {
                hashCount.put(hash, hashCount.getOrDefault(hash, 0) + 1);
            }
        }
        int res = 0;
        for (int v : hashCount.values()) {
            res = Math.max(res, v);
        }
        return res;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 39 ms(76.58%), 40.9 MB(93.99%) for 40 tests
    public int maxFreq2(String s, int maxLetters, int minSize, int maxSize) {
        Map<String, Integer> count = new HashMap<>();
        Map<Character, Integer> freq = new HashMap<>();
        for (int i = 0; i < minSize; i++) {
            char c = s.charAt(i);
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }
        int res = 0;
        for (int i = minSize - 1, n = s.length(); i < n; i++) {
            if (i >= minSize) {
                char old = s.charAt(i - minSize);
                int oldCount = freq.get(old);
                if (oldCount == 1) {
                    freq.remove(old);
                } else {
                    freq.put(old, oldCount - 1);
                }
                freq.put(s.charAt(i), freq.getOrDefault(s.charAt(i), 0) + 1);
            }
            String substr = s.substring(i - minSize + 1, i + 1);
            if (freq.size() <= maxLetters) {
                int cnt = count.getOrDefault(substr, 0) + 1;
                count.put(substr, cnt);
                res = Math.max(res, cnt);
            }
        }
        return res;
    }

    // Two Pointers + Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 23 ms(99.10%), 41.7 MB(50.45%) for 40 tests
    public int maxFreq3(String s, int maxLetters, int minSize, int maxSize) {
        int res = 0;
        Map<String, Integer> count = new HashMap<>();
        int[] freq = new int[26];
        for (int i = 0, j = 0, unique = 0, n = s.length(); j < n; j++) {
            if (freq[s.charAt(j) - 'a']++ == 0) {
                unique++;
            }
            if (j - i >= minSize && (--freq[s.charAt(i++) - 'a'] == 0)) {
                unique--;
            }
            if (unique <= maxLetters && j - i + 1 == minSize) {
                String substr = s.substring(i, i + minSize);
                int cnt = count.getOrDefault(substr, 0) + 1;
                count.put(substr, cnt);
                res = Math.max(res, cnt);
            }
        }
        return res;
    }

    // Bit Manipulation + Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 23 ms(99.10%), 41 MB(86.19%) for 40 tests
    public int maxFreq4(String s, int maxLetters, int minSize, int maxSize) {
        int res = 0;
        Map<String, Integer> count = new HashMap<>();
        for (int i = minSize - 1, n = s.length(); i < n; i++) {
            String substr = s.substring(i - minSize + 1, i + 1);
            int bitset = 0;
            for (char c : substr.toCharArray()) {
                bitset |= (1 << c - 'a');
            }
            if (Integer.bitCount(bitset) > maxLetters) { continue; }

            int cnt = count.getOrDefault(substr, 0) + 1;
            count.put(substr, cnt);
            res = Math.max(res, cnt);
        }
        return res;
    }

    private void test(String s, int maxLetters, int minSize, int maxSize, int expected) {
        assertEquals(expected, maxFreq(s, maxLetters, minSize, maxSize));
        assertEquals(expected, maxFreq2(s, maxLetters, minSize, maxSize));
        assertEquals(expected, maxFreq3(s, maxLetters, minSize, maxSize));
        assertEquals(expected, maxFreq4(s, maxLetters, minSize, maxSize));
    }

    @Test public void test() {
        test("aababcaab", 2, 3, 4, 2);
        test("aaaa", 1, 3, 3, 2);
        test("aabcabcab", 2, 2, 3, 3);
        test("abcde", 2, 3, 3, 0);
        test("babcbceccaaacddbdaedbadcddcbdbcbaaddbcabcccbacebda", 1, 1, 1, 13);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
