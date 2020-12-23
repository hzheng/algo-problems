import java.util.*;
import java.util.stream.Collectors;

import common.TrieNode;
import org.junit.Test;

import static org.junit.Assert.*;

// LC1023: https://leetcode.com/problems/camelcase-matching/
//
// A query word matches a given pattern if we can insert lowercase letters to the pattern word so
// that it equals the query. (We may insert each character at any position, and may insert 0
// characters.) Given a list of queries, and a pattern, return an answer list of booleans, where
// answer[i] is true if and only if queries[i] matches the pattern.
//
// Note:
// 1 <= queries.length <= 100
// 1 <= queries[i].length <= 100
// 1 <= pattern.length <= 100
// All strings consists only of lower and upper case English letters.
public class CamelMatch {
    // Two Pointers
    // time complexity: O((N+M)*Q), space complexity: O(N+M)
    // 0 ms(100.00%), 37.1 MB(80.34%) for 36 tests
    public List<Boolean> camelMatch(String[] queries, String pattern) {
        List<Boolean> res = new ArrayList<>();
        char[] pat = pattern.toCharArray();
        for (String query : queries) {
            res.add(match(query, pat));
        }
        return res;
    }

    private boolean match(String query, char[] pattern) {
        for (int i = 0, j = 0, n = query.length(), m = pattern.length; ; i++) {
            if (i == n) { return j == m; }

            char c = query.charAt(i);
            if (j < m && c == pattern[j]) {
                j++;
            } else if (Character.isUpperCase(c)) { return false; }
        }
    }

    // Dynamic Programming
    // time complexity: O(N*M*Q), space complexity: O(N*M)
    // 3 ms(6.78%), 37.8 MB(16.95%) for 36 tests
    public List<Boolean> camelMatch2(String[] queries, String pattern) {
        List<Boolean> res = new ArrayList<>();
        char[] pat = pattern.toCharArray();
        for (String query : queries) {
            res.add(isMatch(query, pat));
        }
        return res;
    }

    private boolean isMatch(String query, char[] pattern) {
        int n = query.length();
        int m = pattern.length;
        boolean[][] dp = new boolean[n + 1][m + 1];
        dp[0][0] = true;
        for (int i = 0; i < n; i++) {
            if (Character.isLowerCase(query.charAt(i))) {
                dp[i + 1][0] = dp[i][0];
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (query.charAt(i) == pattern[j]) {
                    dp[i + 1][j + 1] = dp[i][j];
                } else if (Character.isLowerCase(query.charAt(i))) {
                    dp[i + 1][j + 1] = dp[i][j + 1];
                }
            }
        }
        return dp[n][m];
    }

    // Trie
    // time complexity: O((N+M)*Q), space complexity: O(N+M)
    // 1 ms(31.19%), 37.9 MB(15.93%) for 36 tests
    public List<Boolean> camelMatch3(String[] queries, String pattern) {
        List<Boolean> res = new ArrayList<>();
        TrieNode root = new TrieNode();
        root.insert(pattern);
        for (String query : queries) {
            res.add(match(root, query));
        }
        return res;
    }

    private static class TrieNode {
        private final TrieNode[] children = new TrieNode[128];
        private boolean isEnd;

        public void insert(String word) {
            TrieNode cur = this;
            for (char c : word.toCharArray()) {
                TrieNode child = cur.children[c - 'A'];
                if (child == null) {
                    child = cur.children[c - 'A'] = new TrieNode();
                }
                cur = child;
            }
            cur.isEnd = true;
        }
    }

    private boolean match(TrieNode root, String query) {
        TrieNode cur = root;
        for (char c : query.toCharArray()) {
            TrieNode next = cur.children[c - 'A'];
            if (next != null) {
                cur = next;
            } else if (Character.isUpperCase(c)) { return false; }
        }
        return cur.isEnd;
    }

    // RegEx
    // time complexity: O((N+M)*Q), space complexity: O(N+M)
    // 15 ms(5.08%), 39.4 MB(5.09%) for 36 tests
    public List<Boolean> camelMatch4(String[] queries, String pattern) {
        return Arrays.stream(queries).map(q -> q
                .matches("[a-z]*" + String.join("[a-z]*", pattern.split("")) + "[a-z]*"))
                     .collect(Collectors.toList());
    }

    private void test(String[] queries, String pattern, Boolean[] expected) {
        List<Boolean> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, camelMatch(queries, pattern));
        assertEquals(expectedList, camelMatch2(queries, pattern));
        assertEquals(expectedList, camelMatch3(queries, pattern));
        assertEquals(expectedList, camelMatch4(queries, pattern));
    }

    @Test public void test() {
        test(new String[] {"aksvbjLiknuTzqon", "ksvjLimflkpnTzqn", "mmkasvjLiknTxzqn",
                           "ksvjLiurknTzzqbn", "ksvsjLctikgnTzqn", "knzsvzjLiknTszqn"},
             "ksvjLiknTzqn", new Boolean[] {true, true, true, true, true, true});
        test(new String[] {"FooBar", "FooBarTest", "FootBall", "FrameBuffer", "ForceFeedBack"},
             "FB", new Boolean[] {true, false, true, true, false});
        test(new String[] {"FooBar", "FooBarTest", "FootBall", "FrameBuffer", "ForceFeedBack"},
             "FoBa", new Boolean[] {true, false, true, false, false});
        test(new String[] {"FooBar", "FooBarTest", "FootBall", "FrameBuffer", "ForceFeedBack"},
             "FoBaT", new Boolean[] {false, true, false, false, false});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
