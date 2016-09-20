import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC072: https://leetcode.com/problems/edit-distance/
//
// Given two words word1 and word2, find the minimum number of steps required to
// convert word1 to word2. (each operation is counted as 1 step.)
// The following 3 operations permitted on a word:
// a) Insert a character
// b) Delete a character
// c) Replace a character
public class EditDistance {
    // recursion
    // Time Limit Exceeded
    public int minDistance(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();
        int len = Math.min(len1, len2);

        int i = 0;
        for (; i < len && word1.charAt(i) == word2.charAt(i); i++);

        if (i > 0) {
            word1 = word1.substring(i);
            word2 = word2.substring(i);
            len1 -= i;
            len2 -= i;
        }

        if (len1 == 0) return len2;
        if (len2 == 0) return len1;

        // replace the first letter
        int distance1 = minDistance(word1.substring(1), word2.substring(1));
        // delete the first letter
        int distance2 = minDistance(word1.substring(1), word2);
        // add a letter(equivalently remove the other one)
        int distance3 = minDistance(word1, word2.substring(1));

        return 1 + min(distance1, distance2, distance3);
    }

    private int min(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    // Dynamic Programming(2D array)
    // beats 47.83%(15 ms)
    public int minDistance2(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();
        if (len1 == 0) return len2;
        if (len2 == 0) return len1;

        int[][] d = new int[len1 + 1][len2 + 1];
        for (int i = 1; i <= len2; i++) {
            d[0][i] = Integer.MAX_VALUE;
        }
        for (int i = 1; i <= len1; i++) {
            d[i][0] = Integer.MAX_VALUE;
        }

        boolean[] rowMatched = new boolean[len1 + 1];
        boolean[] colMatched = new boolean[len2 + 1];
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                boolean matched = word1.charAt(i - 1) == word2.charAt(j - 1);
                int left = d[i][j - 1];
                int top = d[i - 1][j];
                if (matched) {
                    if (rowMatched[i]) {
                        left++;
                    }
                    if (colMatched[j]) {
                        top++;
                    }
                }
                d[i][j] = min(d[i - 1][j - 1], left, top);
                if (matched) {
                    rowMatched[i] = true;
                    colMatched[j] = true;
                } else {
                    d[i][j]++;
                }
            }
        }
        return d[len1][len2];
    }

    // Dynamic Programming(2D array)
    // http://www.geeksforgeeks.org/dynamic-programming-set-5-edit-distance/
    // beats 56.78%(14 ms)
    public int minDistance3(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();
        int d[][] = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {
            for (int j = 0; j <= len2; j++) {
                if (i == 0) {
                    d[i][j] = j;
                } else if (j == 0) {
                    d[i][j] = i;
                } else if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    d[i][j] = d[i - 1][j - 1];
                } else {
                    d[i][j] = 1 + min(d[i][j - 1], d[i - 1][j], d[i - 1][j - 1]);
                }
            }
        }

        return d[len1][len2];
    }

    // Dynamic Programming(2D array)
    // https://en.wikipedia.org/wiki/Wagner%E2%80%93Fischer_algorithm
    // beats 31.71%(16 ms)
    public int minDistance4(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();
        int dp[][] = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len2; i++) {
            dp[0][i] = i;
        }
        for (int i = 1; i <= len1; i++) {
            dp[i][0] = i;
            for (int j = 1; j <= len2; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + min(dp[i][j - 1], dp[i - 1][j], dp[i - 1][j - 1]);
                }
            }
        }
        return dp[len1][len2];
    }

    // Solution of Choice
    // Dynamic Programming(1D array)
    // https://en.wikipedia.org/wiki/Levenshtein_distance
    // beats 93.36%(10 ms)
    public int minDistance5(String word1, String word2) {
        int len1 = word1.length();
        int len2 = word2.length();
        // if (len1 > len2) return minDistance5(word2, word1); // optimize

        int[] dp = new int[len1 + 1];
        for (int i = 0; i <= len1; i++) {
            dp[i] = i;
        }
        char[] chars1 = word1.toCharArray();
        char[] chars2 = word2.toCharArray();
        int[] buffer = new int[len1 + 1];
        for (int i = 0; i < len2; i++) {
            buffer[0] = i + 1;
            for (int j = 0; j < len1; j++) {
                if (chars1[j] == chars2[i]) {
                    buffer[j + 1] = dp[j];
                } else {
                    buffer[j + 1] = min(dp[j], dp[j + 1], buffer[j]) + 1;
                }
            }
            // or: we could recreate buffer each loop and assign buffer to dp;
            System.arraycopy(buffer, 0, dp, 0, len1 + 1);
        }
        return dp[len1];
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<String, String, Integer> distance, String name,
              String word1, String word2, int expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)distance.apply(word1, word2));
        System.out.format("%s %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
    }

    void test(String word1, String word2, int expected) {
        EditDistance e = new EditDistance();
        if (word1.length() + word2.length() < 20) {
            test(e::minDistance, "minDistance", word1, word2, expected);
        }
        test(e::minDistance2, "minDistance2", word1, word2, expected);
        test(e::minDistance3, "minDistance3", word1, word2, expected);
        test(e::minDistance4, "minDistance4", word1, word2, expected);
        test(e::minDistance5, "minDistance5", word1, word2, expected);
    }

    @Test
    public void test1() {
        test("og", "olog", 2);
        test("olog", "og", 2);
        test("ologa", "og", 3);
        test("ologicoarcha", "og", 10);
        test("zoologicoarchaeologist", "zoogeologist", 10);
        test("b", "abc", 2);
        test("abc", "b", 2);
        test("abc", "ac", 1);
        test("abc", "adc", 1);
        test("abcd", "adcd", 1);
        test("abcdefg", "adcefgd", 3);
        test("abcdefg", "b", 6);
        test("abcdefg", "bcdijk", 4);
        test("abcdefghij", "bcdxfgh", 4);
        test("dinitrophenylhydrazine", "acetylphenylhydrazine", 6);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("EditDistance");
    }
}
