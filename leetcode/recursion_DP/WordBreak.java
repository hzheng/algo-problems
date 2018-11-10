import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC139: https://leetcode.com/problems/word-break/
//
// Given a string s and a dictionary of words dict, determine if s can be
// segmented into a space-separated sequence of one or more dictionary words.
public class WordBreak {
    // Time Limited Exceeded
    public boolean wordBreak(String s, Set<String> wordDict) {
        if (wordDict.contains(s)) return true;

        for (int i = 1; i < s.length(); i++) {
            String segment = s.substring(0, i);
            if (wordDict.contains(segment) && wordBreak(s.substring(i), wordDict)) {
                return true;
            }
        }
        return false;
    }

    // Dynamic Programming
    // Dynamic Programming(Bottom-up)
    // beats 36.05%(12 ms)
    public boolean wordBreak2(String s, Set<String> wordDict) {
        int len = s.length();
        boolean[] dp = new boolean[len + 1]; // dp[i] : from 0 to i - 1
        dp[0] = true;
        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j <= len; j++) {
                // dp[j] |= dp[i] && wordDict.contains(s.substring(i, j)); // slower
                if (!dp[j] && dp[i] && wordDict.contains(s.substring(i, j))) {
                    dp[j] = true;
                }
            }
        }
        return dp[len];
    }

    // Solution of Choice
    // Dynamic Programming(Bottom-up)
    // beats 85.12%%(5 ms)
    public boolean wordBreak3(String s, Set<String> wordDict) {
        int len = s.length();
        boolean[] dp = new boolean[len + 1];
        dp[0] = true;
        for (int i = 0; i < len; i++) {
            for (int j = i; j >= 0; j--) {
                if (dp[j] && wordDict.contains(s.substring(j, i + 1))) {
                    dp[i + 1] = true;
                    break;
                }
            }
        }
        return dp[len];
    }

    // DFS + Recursion + Dynamic Programming(Top-down)
    // beats 4.85%(21 ms)
    public boolean wordBreak4(String s, Set<String> wordDict) {
        return wordBreak4(s, 0, wordDict, new HashSet<>());
    }

    private boolean wordBreak4(String s, int start, Set<String> wordDict, Set<Integer> visited) {
        int len = s.length();
        if (start >= len) return true;

        if (visited.contains(start)) return false;

        for (int i = start + 1; i <= len; i++) {
            if (wordDict.contains(s.substring(start, i))) {
                if (wordBreak4(s, i, wordDict, visited)) return true;
            }
        }
        visited.add(start);
        return false;
    }

    // BFS + Queue
    // beats 11.18%(17 ms)
    public boolean wordBreak5(String s, Set<String> wordDict) {
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        queue.offer(0);
        for (int len = s.length(); !queue.isEmpty();) {
            int start = queue.poll();
            if (!visited.add(start)) continue;

            for (int i = start + 1; i <= len; i++) {
                if (wordDict.contains(s.substring(start, i))) {
                    if (i == len) return true;

                    queue.offer(i);
                }
            }
        }
        return false;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<String, Set<String>, Boolean> wordBreak, String name, String s,
              boolean expected, String... dict) {
        Set<String> wordDict = new HashSet<>(Arrays.asList(dict));
        assertEquals(expected, wordBreak.apply(s, wordDict));
    }

    void test(String s, boolean expected, String... dict) {
        WordBreak w = new WordBreak();
        test(w::wordBreak, "wordBreak", s, expected, dict);
        test(w::wordBreak2, "wordBreak2", s, expected, dict);
        test(w::wordBreak3, "wordBreak3", s, expected, dict);
        test(w::wordBreak4, "wordBreak4", s, expected, dict);
        test(w::wordBreak5, "wordBreak5", s, expected, dict);
    }

    @Test
    public void test1() {
        test("leetcode", true, "leet", "code");
        test("aaaaaaa", true, "aaaa", "aaa");
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
