import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC828: https://leetcode.com/problems/unique-letter-string/
//
// A character is unique in string S if it occurs exactly once in it.
// Let's define UNIQ(S) as the number of unique characters in string S.
// Given a string S with only uppercases, calculate the sum of UNIQ(substring)
// over all non-empty substrings of S. If there are two or more equal substrings
// at different positions in S, we consider them different.
// Since the answer can be very large, retrun the answer modulo 10 ^ 9 + 7.
public class UniqueLetterString {
    private static final int MOD = 1000_000_007;

    // Hash Table
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats %(421 ms for 62 tests)
    public int uniqueLetterString(String S) {
        int n = S.length();
        Map<Character, List<Integer> > indices = new HashMap<>();
        int i = 0;
        for (char c : S.toCharArray()) {
            List<Integer> index = indices.get(c);
            if (index == null) {
                indices.put(c, index = new ArrayList<>());
            }
            index.add(i++);
        }
        int res = 0;
        i = 0;
        for (char c : S.toCharArray()) {
            List<Integer> index = indices.get(c);
            int pos = index.indexOf(i);
            int j = (pos > 0) ? index.get(pos - 1) : -1;
            int k = (pos < index.size() - 1) ? index.get(pos + 1) : n;
            res += (i - j) * (k - i);
            res %= MOD;
            i++;
        }
        return res;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // beats %(99 ms for 62 tests)
    public int uniqueLetterString2(String S) {
        int n = S.length();
        Map<Character, List<Integer> > indices = new HashMap<>();
        // for (char c : S.toCharArray()) {
        // List<Integer> index = indices.get(c);
        // if (index == null) {
        // indices.put(c, index = new ArrayList<>());
        // // index.add(-1);
        // }
        // index.add(i++);
        // }
        for (int i = 0; i < n; i++) { // 32 ms if not using lambda
            indices.computeIfAbsent(S.charAt(i), x -> new ArrayList<>()).add(i);
        }
        int res = 0;
        for (List<Integer> index : indices.values()) {
            for (int j = index.size() - 1, next = n; j >= 0; j--, res %= MOD) {
                int cur = index.get(j);
                res += (cur - (j > 0 ? index.get(j - 1) : -1)) * (next - cur);
                next = cur;
            }
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(1)
    // beats %(13 ms for 62 tests)
    public int uniqueLetterString3(String S) {
        int res = 0;
        int[] prevPos = new int[128];
        int[] distance = new int[128];
        for (int i = 0, cur = 0, n = S.length(); i < n; i++) {
            char c = S.charAt(i);
            cur -= distance[c];
            distance[c] = i - prevPos[c] + 1;
            cur += distance[c];
            prevPos[c] = i + 1;
            res = (res + cur) % MOD;
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats %(21 ms for 62 tests)
    public int uniqueLetterString4(String S) {
        int[][] indices = new int[26][2];
        for (int[] index : indices) {
            Arrays.fill(index, -1);
        }
        int res = 0;
        int n = S.length();
        for (int i = 0; i < n; i++) {
            int c = S.charAt(i) - 'A';
            res += (i - indices[c][1]) * (indices[c][1] - indices[c][0]);
            indices[c][0] = indices[c][1];
            indices[c][1] = i;
        }
        for (int[] index : indices) {
            res += (n - index[1]) * (index[1] - index[0]) % MOD;
        }
        return res % MOD;
    }

    // time complexity: O(N), space complexity: O(N)
    // beats %(14 ms for 62 tests)
    public int uniqueLetterString5(String S) {
        int res = 0;
        char[] s = S.toCharArray();
        for (int i = 0, n = s.length; i < n; i++, res %= MOD) {
            int l = i - 1;
            for (; l >= 0 && s[l] != s[i]; l--) {}
            int r = i + 1;
            for (; r < n && s[r] != s[i]; r++) {}
            res += (r - i) * (i - l);
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(N)
    // beats %(12 ms for 62 tests)
    public int uniqueLetterString6(String S) {
        int res = 0;
        int n = S.length();
        int[] prev = new int[n];
        int[] last = new int[26];
        Arrays.fill(last, -1);
        for (int i = 0; i < n; i++) {
            int j = S.charAt(i) - 'A';
            prev[i] = last[j];
            last[j] = i;
        }
        Arrays.fill(last, n);
        for (int i = n - 1; i >= 0; i--) {
            int j = S.charAt(i) - 'A';
            res += (last[j] - i) * (i - prev[i]);
            last[j] = i;
        }
        return res;
    }

    void test(String S, int expected) {
        assertEquals(expected, uniqueLetterString(S));
        assertEquals(expected, uniqueLetterString2(S));
        assertEquals(expected, uniqueLetterString3(S));
        assertEquals(expected, uniqueLetterString4(S));
        assertEquals(expected, uniqueLetterString5(S));
        assertEquals(expected, uniqueLetterString6(S));
    }

    @Test
    public void test() {
        test("ABC", 10);
        test("ABA", 8);
        test("ABCDAD", 44);
        test("AABCCDAEDEFAB", 184);
        test("AEFABCCDAEDEFABCDEFGHIJKLMLNOPQRSTUSW", 4899);
        test("AEFABCCDAEDEFABCDEFGHIJKLMLNOPQRSTUSWXZWXYZOABCDE", 10759);
        test("AZXZMNEFABCCDAEDEFABCDEFGHIJKZFWXBZONECDEFTWFFDDFGGRSCFHIJKLMNP"
             + "GHGFGFHFDFDSFGHGVHGHYJYJGFGFDGFGFQRSALMLNOPQRSTUSWXZWXYZOABCDE"
             + "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMOPQRSTUVWXYZ", 95889);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
