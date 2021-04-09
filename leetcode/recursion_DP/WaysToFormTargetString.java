import org.junit.Test;

import static org.junit.Assert.*;

// LC1639: https://leetcode.com/problems/number-of-ways-to-form-a-target-string-given-a-dictionary/
//
// You are given a list of strings of the same length words and a string target.
// Your task is to form target using the given words under the following rules:
// target should be formed from left to right.
// To form the ith character (0-indexed) of target, you can choose the kth character of the jth
// string in words if target[i] = words[j][k].
// Once you use the kth character of the jth string of words, you can no longer use the xth
// character of any string in words where x <= k. In other words, all characters to the left of or
// at index k become unusuable for every string.
// Repeat the process until you form the string target.
// Notice that you can use multiple characters from the same string in words provided the conditions
// above are met.
// Return the number of ways to form target from words. Since the answer may be too large, return
// it modulo 10^9 + 7.
//
// Constraints:
// 1 <= words.length <= 1000
// 1 <= words[i].length <= 1000
// All strings in words have the same length.
// 1 <= target.length <= 1000
// words[i] and target contain only lowercase English letters.
public class WaysToFormTargetString {
    private static final int MOD = 1_000_000_007;

    // 2D-Dynamic Programming(Top-Down) + Recursion
    // time complexity: O(M*N), space complexity: O(M*N)
    // 38 ms(100.00%), 56.7 MB(13.33%) for 89 tests
    public int numWays(String[] words, String target) {
        int n = words[0].length();
        int[][] freq = new int[n][26];
        for (String w : words) {
            for (int i = 0; i < n; i++) {
                freq[i][w.charAt(i) - 'a']++;
            }
        }
        return dfs(freq, target.toCharArray(), 0, 0, new Integer[n][target.length()]);
    }

    private int dfs(int[][] freq, char[] target, int sIndex, int tIndex, Integer[][] dp) {
        int t = target.length;
        if (tIndex == t) { return 1; }

        int n = freq.length;
        if (n - sIndex < t - tIndex) { return 0; }
        if (dp[sIndex][tIndex] != null) { return dp[sIndex][tIndex]; }

        long res = dfs(freq, target, sIndex + 1, tIndex, dp);
        int index = target[tIndex] - 'a';
        long count = freq[sIndex][index];
        if (count > 0) {
            res = (res + count * dfs(freq, target, sIndex + 1, tIndex + 1, dp)) % MOD;
        }
        return dp[sIndex][tIndex] = (int)res;
    }

    // 1D-Dynamic Programming(Bottom-Up)
    // time complexity: O(M*N), space complexity: O(M)
    // 150 ms(42.67%), 45.5 MB(86.67%) for 89 tests
    public int numWays2(String[] words, String target) {
        int m = target.length();
        long[] dp = new long[m + 1];
        dp[0] = 1;
        for (int i = 0, n = words[0].length(); i < n; i++) {
            int[] freq = new int[26];
            for (String w : words) {
                freq[w.charAt(i) - 'a']++;
            }
            for (int j = m - 1; j >= 0; j--) { // must iterate reversely
                dp[j + 1] += dp[j] * freq[target.charAt(j) - 'a'] % MOD;
            }
        }
        return (int)(dp[m] % MOD);
    }

    private void test(String[] words, String target, int expected) {
        assertEquals(expected, numWays(words, target));
        assertEquals(expected, numWays2(words, target));
    }

    @Test public void test() {
        test(new String[] {"acca", "bbbb", "caca"}, "aba", 6);
        if (true) return;
        test(new String[] {"abba", "baab"}, "bab", 4);
        test(new String[] {"abcd"}, "abcd", 1);
        test(new String[] {"abab", "baba", "abba", "baab"}, "abba", 16);
        test(new String[] {"bbcabbbacbccbaaaabbbbbcccbcbab", "bbbcaacbbabcacbcbabbcabbbcacaa",
                           "cabcbacabbacccccacccbabcbccbcc", "cbbaacbbbcccbbcbcaacbbaaccbaac",
                           "bcacccccabbbaccbcbcacbcabaacab", "bbbbabcbbacccabcaccabaaabaaabc",
                           "bcbcbacababcbaabaacbabbbcbbacb", "cbaacbcccbacbcbbccbbabbabbbcab",
                           "bcaccabcabaacbaaabccaabcbabcac", "bcababaaaccabccbcccababbbbcbaa",
                           "bbbbacccacababbcacaacbbcbcbabb", "baacaabbcaccbbacabcbaccaaaaabc",
                           "bbcbaaaccbcbbcbaaaccbbbbbbbacc", "abbcacbababbbcbbcaacbbaaaabaca",
                           "abccbcbbcacabaacccacaacbcbcbaa", "cbccaaaacbccbaabbcccbcacbaccba",
                           "bbacbaabaccaabcaacaacccbbbbcba", "ccbbbaaaacaaacabababaacccbaaca",
                           "cccacacaaabaaaabcaccabcacbbccb", "bccaacacccccaaaabacbcbbcccabbb",
                           "bcbaabacbaaabbcbaabaacaacabaca", "cbccccccbaccbcaaaccacccaacaccc",
                           "bbbabbabaaaccccbbabbbcababacca", "acaaacabccacabcbcaaacccaabbacb",
                           "aaaccabbbbbaacacabcbacccbbaaab", "cbbcababababcaabcbaacccbcbacca",
                           "aacabcacbccaababbacbabcbcbbcab", "caaaaabbccccbcccaabacccabacaba",
                           "bbcbaabacbccaabaccababaabbccba", "cbabcacbbbacaaababcaaccccbabba",
                           "caabbcaaaacbcabccbcbcccbbbbacc", "bccabaaaaabbcabbbcabaabbcbbaaa",
                           "baaabaccccaccbcacaacccbbcbacaa", "bccacaacbaabaacbbacbcabaaacbbb",
                           "cabcbccbccbcacabcaacbbbaabaabc", "accaaccbaccbbbaacacabacbaccaac",
                           "cabaabaabcbabbcaaacccbbaccbcaa", "bcabbccabbaccaaaabacbbbbbcacba",
                           "bbccbbcacaaabaaaaaccacbcacbabb", "ccbabaccbbacbbbaaabccbaabcccbc",
                           "ccbcccacacabcbaacbbcbcbacacaac", "acacccbcbaabcacbcbbaababbcabca",
                           "aabcbbcaaabcbbaaacbbcbcabbaacc", "aaacacbabaabbbacaaacbacbaabbaa",
                           "bbabacbbbabcaabbbacabbacbaccaa", "ccabacacaccacabacccabcccaacbcb",
                           "cbbcbbaaabbbbaabcccbcbaacbbacb"}, "acabbaabcabbbcbacacb", 951681100);
        test(new String[] {"cabbaacaaaccaabbbbaccacbabbbcb", "bbcabcbcccbcacbbbaacacaaabbbac",
                           "cbabcaacbcaaabbcbaabaababbacbc", "aacabbbcaaccaabbaccacabccaacca",
                           "bbabbaabcaabccbbabccaaccbabcab", "bcaccbbaaccaabcbabbacaccbbcbbb",
                           "cbbcbcaaaacacabbbabacbaabbabaa", "cbbbbbbcccbabbacacacacccbbccca",
                           "bcbccbccacccacaababcbcbbacbbbc", "ccacaabaaabbbacacbacbaaacbcaca",
                           "bacaaaabaabccbcbbaacacccabbbcb", "bcbcbcabbccabacbcbcaccacbcaaab",
                           "babbbcccbbbbbaabbbacbbaabaabcc", "baaaacaaacbbaacccababbaacccbcb",
                           "babbaaabaaccaabacbbbacbcbababa", "cbacacbacaaacbaaaabacbbccccaca",
                           "bcbcaccaabacaacaaaccaabbcacaaa", "cccbabccaabbcbccbbabaaacbacaaa",
                           "bbbcabacbbcabcbcaaccbcacacccca", "ccccbbaababacbabcaacabaccbabaa",
                           "caaabccbcaaccabbcbcaacccbcacba", "cccbcaacbabaacbaaabbbbcbbbbcbb",
                           "cababbcacbabcbaababcbcabbaabba", "aaaacacaaccbacacbbbbccaabcccca",
                           "cbcaaaaabacbacaccbcbcbccaabaac", "bcbbccbabaccabcccacbbaacbbcbba",
                           "cccbabbbcbbabccbbabbbbcaaccaab", "acccacccaabbcaccbcaaccbababacc",
                           "bcacabaacccbbcbbacabbbbbcaaaab", "cacccaacbcbccbabccabbcbabbcacc",
                           "aacabbabcaacbaaacaabcabcaccaab", "cccacabacbabccbccaaaaabbcacbcc",
                           "cabaacacacaaabaacaabababccbaaa", "caabaccaacccbaabcacbcbbabccabc",
                           "bcbbccbbaaacbaacbccbcbababcacb", "bbabbcabcbbcababbbbccabaaccbca",
                           "cacbbbccabaaaaccacbcbabaabbcba", "ccbcacbabababbbcbcabbcccaccbca",
                           "acccabcacbcbbcbccaccaacbabcaab", "ccacaabcbbaabaaccbabcbacaaabaa",
                           "cbabbbbcabbbbcbccabaabccaccaca", "acbbbbbccabacabcbbabcaacbbaacc",
                           "baaababbcabcacbbcbabacbcbaaabc", "cabbcabcbbacaaaaacbcbbcacaccac"},
             "acbaccacbbaaabbbabac", 555996654);
        test(new String[] {"cbabddddbc", "addbaacbbd", "cccbacdccd", "cdcaccacac", "dddbacabbd",
                           "bdbdadbccb", "ddadbacddd", "bbccdddadd", "dcabaccbbd", "ddddcddadc",
                           "bdcaaaabdd", "adacdcdcdd", "cbaaadbdbb", "bccbabcbab", "accbdccadd",
                           "dcccaaddbc", "cccccacabd", "acacdbcbbc", "dbbdbaccca", "bdbddbddda",
                           "daabadbacb", "baccdbaada", "ccbabaabcb", "dcaabccbbb", "bcadddaacc",
                           "acddbbdccb", "adbddbadab", "dbbcdcbcdd", "ddbabbadbb", "bccbcbbbab",
                           "dabbbdbbcb", "dacdabadbb", "addcbbabab", "bcbbccadda", "abbcacadac",
                           "ccdadcaada", "bcacdbccdb"}, "bcbbcccc", 677452090);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
