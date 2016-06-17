import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/word-break/
//
// Given a string s and a dictionary of words dict, determine if s can be
// segmented into a space-separated sequence of one or more dictionary words.
public class WordBreak {
    // Time Limited Exceeded
    public boolean wordBreak(String s, Set<String> wordDict) {
        if (wordDict.contains(s)) return true;

        for (int i = 1; i < s.length(); i++) {
            String segment = s.substring(0, i);
            if (wordDict.contains(segment)
                && wordBreak(s.substring(i), wordDict)) {
                return true;
            }
        }
        return false;
    }

    // beats 36.05%
    public boolean wordBreak2(String s, Set<String> wordDict) {
        int len = s.length();
        boolean[] dp = new boolean[len + 1]; // dp[i] : from 0 to i - 1
        dp[0] = true;

        for (int i = 0; i < len; i++) {
            for (int j = i + 1; j <= len; j++) {
                // dp[j] |= dp[i] && wordDict.contains(s.substring(i, j)); // slower
                if (!dp[j] && dp[i] && wordDict.contains(s.substring(i, j))) {
                    dp[j] = true;
                };
            }
        }
        return dp[len];
    }

    // beats 85.12%%
    public boolean wordBreak3(String s, Set<String> wordDict) {
        int len = s.length();
        boolean[] dp = new boolean[len + 1];
        dp[0] = true;

        for (int i = 0; i < len; i++) {
            for (int j = i; j >= 0; j--) {
                if (dp[j] && wordDict.contains(s.substring(j, i + 1))) {
                    dp[i + 1] = true;
                    break;
                };
            }
        }
        return dp[len];
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<String, Set<String>, Boolean> wordBreak, String name,
              String s, boolean expected, String ... dict) {
        Set<String> wordDict = new HashSet<>(Arrays.asList(dict));
        assertEquals(expected, wordBreak.apply(s, wordDict));
    }

    void test(String s, boolean expected, String ... dict) {
        WordBreak w = new WordBreak();
        test(w::wordBreak, "wordBreak", s, expected, dict);
        test(w::wordBreak2, "wordBreak2", s, expected, dict);
        test(w::wordBreak3, "wordBreak3", s, expected, dict);
    }

    @Test
    public void test1() {
        test("leetcode", true, "leet", "code");
        test("aaaaaaa", true, "aaaa", "aaa");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("WordBreak");
    }
}
