import java.util.*;
import java.util.regex.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC809: https://leetcode.com/problems/expressive-words/
//
// Sometimes people repeat letters to represent extra feeling, such as "hello"
// -> "heeellooo", "hi" -> "hiiii".  Here, we have groups, of adjacent letters
// that are all the same character, and adjacent characters to the group are
// different.  A group is extended if that group is length 3 or more, so "e" and
// "o" would be extended in the first example, and "i" would be extended in the
// second example.
// For some given string S, a query word is stretchy if it can be made to be
// equal to S by extending some groups. Formally, we are allowed to repeatedly
// choose a group (as defined above) of characters c, and add some number of the
// same character c to it so that the length of the group is 3 or more.  Note
// that we cannot extend a group of size one like "h" to a group of size two
// like "hh" - all extensions must leave the group extended - ie., at least 3
// characters long.
// Given a list of query words, return the number of words that are stretchy.
public class ExpressiveWords {
    // beats %(16 ms for 26 tests)
    public int expressiveWords(String S, String[] words) {
        int res = 0;
        Encode encode = new Encode(S);
        for (String word : words) {
            if (encode.contains(new Encode(word))) {
                res++;
            }
        }
        return res;
    }

    private static class Encode {
        List<Character> chars = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();

        public Encode(String s) {
            int len = s.length();
            for (int i = 0, prev = -1; i < len; i++) {
                if (i == len - 1 || s.charAt(i) != s.charAt(i + 1)) {
                    chars.add(s.charAt(i));
                    counts.add(i - prev);
                    prev = i;
                }
            }
        }

        public boolean contains(Encode other) {
            if (!chars.equals(other.chars)) return false;

            int i = 0;
            for (int c2 : other.counts) {
                int c1 = counts.get(i++);
                if (c1 < c2 || (c1 == 2 && c2 == 1)) return false;
            }
            return true;
        }
    }

    // beats %(9 ms for 26 tests)
    public int expressiveWords2(String S, String[] words) {
        int[][] code1 = encode(S);
        int res = 0;
        outer : for (String w : words) {
            int[][] code2 = encode(w);
            if (code1.length != code2.length) continue;

            for (int i = 0; i < code2.length; i++) {
                if (code1[i][0] != code2[i][0]) continue outer;

                int c1 = code1[i][1];
                int c2 = code2[i][1];
                if (c1 < c2 || (c1 == 2 && c2 == 1)) continue outer;
            }
            res++;
        }
        return res;
    }

    private int[][] encode(String s) {
        int n = s.length();
        int[][] res = new int[n][];
        int len = 0;
        for (int i = 0; i < n; i++) {
            if (i == 0 || s.charAt(i) != s.charAt(i - 1)) {
                res[len++] = new int[] {s.charAt(i), 1};
            } else {
                res[len - 1][1]++;
            }
        }
        return Arrays.copyOf(res, len);
    }

    // beats %(8 ms for 26 tests)
    public int expressiveWords3(String S, String[] words) {
        int[][] code1 = encode(S, -1);
        int res = 0;
        outer : for (String w : words) {
            int[][] code2 = encode(w, code1.length);
            if (code2 == null) continue;

            for (int i = 0; i < code1.length; i++) {
                if (code1[i][0] != code2[i][0]) continue outer;

                int c1 = code1[i][1];
                int c2 = code2[i][1];
                if (c1 < c2 || (c1 == 2 && c2 == 1)) continue outer;
            }
            res++;
        }
        return res;
    }

    private int[][] encode(String s, int expectedLen) {
        int n = s.length();
        int[][] res = new int[n][];
        int len = 0;
        for (int i = 0; i < n; i++) {
            if (i == 0 || s.charAt(i) != s.charAt(i - 1)) {
                res[len++] = new int[] {s.charAt(i), 1};
            } else {
                res[len - 1][1]++;
            }
            if (expectedLen >= 0 && len > expectedLen) return null;
        }
        if (expectedLen < 0) return Arrays.copyOf(res, len);

        return (len == expectedLen) ? res : null;
    }

    // Regex
    // beats %(11 ms for 26 tests)
    public int expressiveWords4(String S, String[] words) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, len = S.length(); i < len; i++) {
            int n = 1;
            for (; i < len - 1 && S.charAt(i) == S.charAt(i + 1); i++, n++) {}
            sb.append(S.charAt(i));
            if (n >= 3) {
                sb.append("{1," + n + "}");
            } else if (n == 2) {
                sb.append(S.charAt(i));
            }
        }
        Pattern p = Pattern.compile(sb.toString());
        Matcher m = p.matcher("");
        int res = 0;
        for (String word : words) {
            if (m.reset(word).matches()) {
                res++;
            }
        }
        return res;
    }

    void test(String S, String[] words, int expected) {
        assertEquals(expected, expressiveWords(S, words));
        assertEquals(expected, expressiveWords2(S, words));
        assertEquals(expected, expressiveWords3(S, words));
        assertEquals(expected, expressiveWords4(S, words));
    }

    @Test
    public void test() {
        test("heeellooo", new String[] {"hello", "hi", "helo"}, 1);
        test("abcd", new String[] {"abc"}, 0);
        test("abbcccccdddd", new String[] {"abc", "abcd", "abbcd"}, 1);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
