import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Implement wildcard pattern matching with support for '?' and '*'.
public class WildcardMatch {
    // beats 54.54%
    public boolean isMatch(String s, String p) {
        if (p.isEmpty()) return s.isEmpty();

        return isMatch(s.toCharArray(), 0, s.length() - 1,
                       p.toCharArray(), 0, p.length() - 1);
    }

    private boolean isMatch(char[] s, int sStart, int sEnd,
                            char[] p, int pStart, int pEnd) {
        int[] sRange = {sStart, sEnd};
        int[] pRange = {pStart, pEnd};
        int count = check(s, sRange, p, pRange);
        if (count < 0) return false;
        if (count == 0) return true;

        sStart = sRange[0];
        sEnd = sRange[1];
        pStart = pRange[0];
        pEnd = pRange[1];
        // now p must begin and end with *
        int[] range = longestNonwildSubstr(p, pStart + 1, pEnd - 1);
        int len = range[1] - range[0] + 1;
        if (len == 0) {
            for (int i = pStart; i <= pEnd; i++) {
                if (p[i] == '?') {
                    sEnd--;
                }
            }
            return sEnd + 1 >= sStart;
        }

        String pStr = new String(p, range[0], len);
        String sStr = new String(s, sStart, sEnd - sStart + 1);
        for (int i = 0; i <= sEnd - len + 1; i++) {
            i = sStr.indexOf(pStr, i);
            if (i < 0) return false;

            // split both s and p by the pStr
            // left part
            if (!isMatch(s, sStart, sStart + i - 1, p, pStart, range[0] - 1)) continue;
            // right part
            if (isMatch(s, sStart + i + len, sEnd, p, range[1] + 1, pEnd)) return true;
        }
        return false;
    }

    private int[] longestNonwildSubstr(char[] p, int start, int end) {
        int max = 0;
        int maxEnd = 0;
        int count = 0;
        for (int i = start; i <= end; i++) {
            if (p[i] != '*' && p[i] != '?') {
                count++;
            } else {
                if (count > max) {
                    max = count;
                    maxEnd = i - 1;
                }
                count = 0;
            }
        }
        if (count > max) {
            max = count;
            maxEnd = end;
        }
        return new int[] {maxEnd - max + 1, maxEnd};
    }

    private int check(char[] s, int[] sRange, char[] p, int[] pRange) {
        int sStart = sRange[0];
        int sEnd = sRange[1];
        int pStart = pRange[0];
        int pEnd = pRange[1];
        if (sStart > sEnd) return checkEmpty(s, sStart, sEnd, p, pStart, pEnd);

        // shorten left
        for (; sStart <= sEnd && pStart <= pEnd; sStart++, pStart++) {
            if (s[sStart] == p[pStart] || p[pStart] == '?') continue;

            if (p[pStart] != '*') return -1;

            // skip redundant *'s
            while (pStart < pEnd && p[pStart + 1] == '*') {
                pStart++;
            }
            break;
        }

        if (sStart > sEnd) return checkEmpty(s, sStart, sEnd, p, pStart, pEnd);

        // shorten right
        for (; sEnd >= sStart && pEnd >= pStart; sEnd--, pEnd--) {
            if (s[sEnd] == p[pEnd] || p[pEnd] == '?') continue;

            if (p[pEnd] != '*') return -1;

            // skip redundant *'s
            while (pEnd > pStart && p[pEnd - 1] == '*') {
                pEnd--;
            }
            break;
        }

        if (pStart == pEnd) return 0; // only '*'

        if (pStart > pEnd) return (sStart > sEnd) ? 0 : -1; // both exhausted

        if (sStart > sEnd) return -1;

        sRange[0] = sStart;
        sRange[1] = sEnd;
        pRange[0] = pStart;
        pRange[1] = pEnd;
        int count = 0;
        for (int i = pStart + 1; i < pEnd; i++) {
            if (p[i] != '*') {
                count++;
            }
        }
        return (sEnd - sStart + 1 >= count) ? count : -1;
    }

    private int checkEmpty(char[] s, int sStart, int sEnd,
                           char[] p, int pStart, int pEnd) {
        for (int i = pStart; i <= pEnd; i++) {
            if (p[i] != '*') return -1;
        }
        return 0;
    }

    // Time Limit Exceeded
    public boolean isMatch2(String s, String p) {
        if (p.isEmpty()) return s.isEmpty();

        return isMatch2(s.toCharArray(), 0, s.length() - 1,
                        p.toCharArray(), 0, p.length() - 1);
    }

    private boolean isMatch2(char[] s, int sStart, int sEnd,
                             char[] p, int pStart, int pEnd) {
        int[] sRange = {sStart, sEnd};
        int[] pRange = {pStart, pEnd};
        int count = check(s, sRange, p, pRange);
        if (count < 0) return false;
        if (count == 0) return true;

        sStart = sRange[0];
        sEnd = sRange[1];
        pStart = pRange[0];
        pEnd = pRange[1];

        // now p must begin and end with *
        // consume starting *
        for (int i = sStart; i <= sEnd - count + 1; i++) {
            if (isMatch2(s, i, sEnd, p, pStart + 1, pEnd)) return true;
        }
        // even slower if consume both end *
        // pStart++; pEnd--;
        // for (int i = sStart; i <= sEnd - count + 1; i++) {
        //     for (int j = i + count - 1; j <= sEnd ; j++) {
        //         if (isMatch2(s, i, j, p, pStart, pEnd)) return true;
        //     }
        // }
        return false;
    }

    // simple but too slow
    public boolean isMatch3(String s, String p) {
        if (p.isEmpty()) return s.isEmpty();

        char firstPat = p.charAt(0);
        if (firstPat == '*') {
            return (!s.isEmpty() && isMatch3(s.substring(1), p))
                   || isMatch3(s, p.substring(1));
        }

        if (s.isEmpty()) return false;

        if (firstPat != s.charAt(0) && firstPat != '?') return false;

        return isMatch3(s.substring(1), p.substring(1));
    }

    // better than last one, but still very slow
    public boolean isMatch4(String s, String p) {
        if (p.isEmpty()) return s.isEmpty();

        if (!check(s, p)) return false;

        char firstPat = p.charAt(0);
        if (firstPat == '*') {
            if (!s.isEmpty() && isMatch4(s.substring(1), p)) return true;

            int i = 1;
            while (i < p.length() && (p.charAt(i) == '*')) {
                i++;
            }
            return isMatch4(s, p.substring(i));
        }

        if (s.isEmpty()) return false;

        if (firstPat != s.charAt(0) && firstPat != '?') return false;

        return isMatch4(s.substring(1), p.substring(1));
    }

    private boolean check(String s, String p) {
        int count = 0;
        for (int i = p.length() - 1; i >= 0; i--) {
            if (p.charAt(i) != '*') {
                count++;
            }
        }
        return s.length() >= count;
    }

    void test(String s, String p, boolean expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, isMatch(s, p));
        if (p.length() < 50) {
            assertEquals(expected, isMatch2(s, p));
            assertEquals(expected, isMatch3(s, p));
            assertEquals(expected, isMatch4(s, p));
        } else {
            System.out.format("%.3f ms\n", (System.nanoTime() - t1) * 1e-6);
        }
    }

    @Test
    public void test1() {
        test("", "", true);
        test("aa", "??", true);
        test("aa","a", false);
        test("aa","aa", true);
        test("aaa","aa", false);
        test("aa", "a*", true);
        test("aa", "?*", true);
        test("ab", "?*", true);
        test("aab", "c*a*b", false);
        test("aab", "c*a*b?", false);
        test("aabd", "*a*b?", true);
        test("babcd", "b?*", true);
        test("babcd", "a?*", false);
        test("abcd", "?*d", true);
        test("babcd", "b?*d", true);
        test("aa", "b*a", false);
        test("aaa", "ab*a", false);
        test("", "b*", false);
        test("", "*", true);
        test("", "*****", true);
        test("a", "a*", true);
        test("a", "a***", true);
        test("abcd", "a?*d", true);
        test("cde89", "cde*", true);
        test("abc12dcde89xyz", "a?c**cde**xyz", true);
        test("c", "*?*", true);
        test("abcd", "*?*?*?*?", true);
        test("abcde", "a*?*?*?*?e", false);
        test("aabcde", "a*?*?*?*?e", true);
        test("abc12rrrrrrrrrrrdcde89xyz", "a?c**cde*abcd**c**cd*xyz", false);
        test("abcg12cde0abcd8ccd9xyz", "a?c**cde*abcd**c**cd*xyz", true);
        test("12cde0abcd8ccd9", "**cde*abcd**c**cd*", true);
    }

    @Test
    public void test2() {
        test("aaaabaabbabbbaababbbba", "*****b*aba***b*a*a*a*b**ba***a*a*", false);
        test("abbaabbbbababaababababbabaaaabbbbbbaaabbabbbbababbbaaabbabbabb",
             "***b**a*a*b***b*a*b*bbb**baa*bba**b**bb***b*a*aab*a**", true);
        test("aaaabaaaabbbbaabbbaabbaababbabbaaaababaaabbbbbbaabbbabababbaaabaa"
             + "baaaaaabbaabbbbaababbababaabbbaababbbba",
             "*****b*aba***babaa*bbaba***a*aaba*b*aa**a*b**ba***a*a*", true);
        test("abbaabbbbababaababababbabbbaaaabbbbaaabbbabaabbbbbabbbbabbabbaaab"
             + "aaaabbbbbbaaabbabbbbababbbaaabbabbabb",
             "***b**a*a*b***b*a*b*bbb**baa*bba**b**bb***b*a*aab*a**", true);
        test("abbabaaabbabbaababbabbbbbabbbabbbabaaaaababababbbabababaabbababaa"
             + "bbbbbbaaaabababbbaabbbbaabbbbababababbaabbaababaabbbababababbbb"
             + "aaabbbbbabaaaabbababbbbaababaabbababbbbbababbbabaaaaaaaabbbbbaa"
             + "baaababaaaabb",
             "**aa*****ba*a*bb**aa*ab****a*aaaaaa***a*aaaa**bbabb*b*b**aaaaaaaa"
             + "a*a********ba*bbb***a*ba*bb*bb**a*b*bb", false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("WildcardMatch");
    }
}
