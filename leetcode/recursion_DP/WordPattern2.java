import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC291: https://leetcode.com/problems/word-pattern-ii/
//
// Given a pattern and a string str, find if str follows the same pattern.
// Here follow means a full match, such that there is a bijection between a letter in pattern and a non-empty substring in str.
public class WordPattern2 {
    // DFS + Backtracking + Recursion
    // beats 94.31%(8 ms for 22 tests)
    public boolean wordPatternMatch(String pattern, String str) {
        return match(pattern.toCharArray(), 0, str.toCharArray(), 0,
                     new String[26], new HashSet<>());
    }

    private boolean match(char[] pattern, int pIndex, char[] str, int sIndex,
                          String[] map, Set<String> mappedSet) {
        int patLen = pattern.length - pIndex;
        int len = str.length - sIndex;
        if (patLen == 0) return len == 0;
        if (len < patLen) return false;

        char first = pattern[pIndex];
        String mapped = map[first - 'a'];
        if (mapped != null) {
            return new String(str, sIndex, len).startsWith(mapped)
                   && match(pattern, pIndex + 1, str, sIndex + mapped.length(), map, mappedSet);
        }

        for (int i = 1; i <= len; i++) {
            String substr = new String(str, sIndex, i);
            if (mappedSet.contains(substr)) continue;

            map[first - 'a'] = substr;
            mappedSet.add(substr);
            if (match(pattern, pIndex + 1, str, sIndex + i, map, mappedSet)) return true;

            map[first - 'a'] = null;
            mappedSet.remove(substr);
        }
        return false;
    }

    // TODO: improve performance by more pruning

    void test(String pattern, String str, boolean expected) {
        assertEquals(expected, wordPatternMatch(pattern, str));
    }

    @Test
    public void test1() {
        test("d", "e", true);
        test("aba", "aaaa", true);
        test("ab", "cc", false);
        test("ab", "redred", true);
        test("abab", "redblueredblue", true);
        test("aaaa", "asdasdasdasd", true);
        test("aabb", "xyzabcxzyabc", false);
        test("abba", "dogcatcatdog", true);
        test("itwasthebestoftimes", "ittwaastthhebesttoofttimes", true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("WordPattern2");
    }
}
