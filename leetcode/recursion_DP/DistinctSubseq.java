import java.util.*;
import java.lang.reflect.Array;

import org.junit.Test;
import static org.junit.Assert.*;

// LC115: https://leetcode.com/problems/distinct-subsequences/
//
// Given a string S and a string T, count the number of distinct subsequences of
// T in S.
public class DistinctSubseq {
    private List<Integer[]> getIndexListSeq(String s, String t) {
        boolean[] targetChars = new boolean[256];
        Map<Character, List<Integer> > indexMap = new HashMap<>();

        for (char c : t.toCharArray()) {
            targetChars[c] = true;
        }

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (targetChars[c]) {
                if (!indexMap.containsKey(c)) {
                    indexMap.put(c, new ArrayList<>());
                }
                indexMap.get(c).add(i);
            }
        }

        List<Integer[]> indexListSeq = new ArrayList<>(t.length());
        for (char c : t.toCharArray()) {
            if (!indexMap.containsKey(c)) return null;

            indexListSeq.add(indexMap.get(c).toArray(new Integer[0]));
        }
        return indexListSeq;
    }

    private int nextIndex(Integer[] list, int x) {
        int index = Arrays.binarySearch(list, x);
        if (index >= 0) { // same array
            index++;
        } else { // not found, insertion point
            index = -index - 1;
        }
        return (index == list.length) ? -1 : index;
    }

    // Time Limit Exceeded
    public int numDistinct(String s, String t) {
        List<Integer[]> indexListSeq = getIndexListSeq(s, t);
        if (indexListSeq == null) return 0;

        int seqLen = t.length();
        int count = 0;
        Stack<Integer> stack = new Stack<>();
        stack.push(-1);
        while (!stack.isEmpty()) {
            int pos = stack.pop();
            Integer[] indexList = null;
            while (!stack.isEmpty()) {
                indexList = indexListSeq.get(stack.size());
                if (pos < indexList[indexList.length - 1]) {
                    pos = indexList[nextIndex(indexList, pos)];
                    stack.push(pos);
                    break;
                }
                pos = stack.pop();
            }
            int i = stack.size();
            while (true) {
                indexList = indexListSeq.get(i);
                pos = nextIndex(indexList, pos);
                if (++i == seqLen || pos < 0) break;

                pos = indexList[pos];
                stack.push(pos);
            }
            if (pos >= 0) {
                count += indexList.length - pos;
            }
        }
        return count;
    }

    // Time Limit Exceeded
    public int numDistinct2(String s, String t) {
        List<Integer[]> indexListSeq = getIndexListSeq(s, t);
        if (indexListSeq == null) return 0;

        int count = 0;
        int seqLen = t.length();
        int[] indices = new int[seqLen];
        indices[0] = -1;
        for (int i = 0; i >= 0; i -= 2) {
            int pos = indices[i];
            Integer[] indexList = null;
            while (i >= 0 && pos >= 0) {
                indexList = indexListSeq.get(i);
                if (pos < indexList[indexList.length - 1]) {
                    pos = indexList[nextIndex(indexList, pos)];
                    indices[i++] = pos;
                    break;
                }
                if (--i < 0) return count;

                pos = indices[i];
            }

            while (true) {
                indexList = indexListSeq.get(i);
                pos = nextIndex(indexList, pos);
                if (++i == seqLen || pos < 0) break;

                pos = indexList[pos];
                indices[i - 1] = pos;
            }
            if (pos >= 0) {
                count += indexList.length - pos;
            }
        }
        return count;
    }

    // dynamic programming
    // beats 95.09%(8 ms)
    public int numDistinct3(String s, String t) {
        List<Integer[]> indexListSeq = getIndexListSeq(s, t);
        if (indexListSeq == null) return 0;

        int seqLen = t.length();
        // each element of countSeq represents the count starting
        // from the corresponding position of indexListSeq
        List<int[]> countSeq = new ArrayList<>(seqLen);
        for (Integer[] indices : indexListSeq) {
            countSeq.add(new int[indices.length]);
        }

        int[] counts = countSeq.get(seqLen - 1);
        for (int i = counts.length - 1; i >= 0; i--) {
            counts[i] = counts.length - i;
        }
        for (int i = seqLen - 2; i >= 0; i--) {
            counts = countSeq.get(i);
            int[] nextCounts = countSeq.get(i + 1);
            Integer[] indices = indexListSeq.get(i);
            Integer[] nextIndices = indexListSeq.get(i + 1);
            for (int j = counts.length - 1; j >= 0; j--) {
                int index = nextIndex(nextIndices, indices[j]);
                counts[j] = (index < 0) ? 0 : nextCounts[index];
                if (j < counts.length - 1) {
                    counts[j] += counts[j + 1];
                }
            }
        }
        return countSeq.get(0)[0];
    }

    // Dynamic Programming(2D array)
    // beats 52.62%(17 ms)
    public int numDistinct4(String s, String t) {
        int sLen = s.length();
        int tLen = t.length();

        // counts for string s from start to pos i matches string t from start to pos j
        int[][] dp = new int[sLen + 1][tLen + 1];
        dp[0][0] = 1;
        for (int i = 1; i <= sLen; i++) {
            dp[i][0] = 1;
        }

        for (int i = 1; i <= sLen; i++) {
            // for (int j = 1; j <= tLen; j++) { // beat rate drops 40.61%
            for (int j = tLen; j > 0; j--) {
                dp[i][j] = dp[i - 1][j];
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    dp[i][j] += dp[i - 1][j - 1];
                }
            }
        }
        return dp[sLen][tLen];
    }

    // Dynamic Programming(1D array)
    // beats 86.29%(12 ms)
    public int numDistinct5(String s, String t) {
        int sLen = s.length();
        int tLen = t.length();
        int[] dp = new int[tLen + 1];
        dp[0] = 1;
        for (int i = 1; i <= sLen; i++) {
            for (int j = tLen; j > 0; j--) {
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    dp[j] += dp[j - 1];
                }
            }
        }
        return dp[tLen];
    }

    // Solution of Choice
    // Dynamic Programming(1D array)
    // beats 9X%(7 ms)
    public int numDistinct6(String s, String t) {
        int sLen = s.length();
        int tLen = t.length();
        char[] sChars = s.toCharArray();
        char[] tChars = t.toCharArray();
        int[] dp = new int[tLen + 1];
        dp[0] = 1;
        for (int i = 1; i <= sLen; i++) {
            for (int j = tLen; j > 0; j--) {
                if (sChars[i - 1] == tChars[j - 1]) {
                    dp[j] += dp[j - 1];
                }
            }
        }
        return dp[tLen];
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<String, String, Integer> distinct, String name,
              String s, String t, int expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)distinct.apply(s, t));
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
    }

    void test(String s, String t, int expected) {
        DistinctSubseq d = new DistinctSubseq();
        if (s.length() < 120) {
            test(d::numDistinct, "numDistinct", s, t, expected);
            test(d::numDistinct2, "numDistinct2", s, t, expected);
        }
        test(d::numDistinct3, "numDistinct3", s, t, expected);
        test(d::numDistinct4, "numDistinct4", s, t, expected);
        test(d::numDistinct5, "numDistinct5", s, t, expected);
        test(d::numDistinct6, "numDistinct6", s, t, expected);
    }

    @Test
    public void test1() {
        test("rabbbit","rabbit", 3);
        test("aabbcc","abc", 8);
        test("aabbccaa","abc", 8);
        test("abbccaacb","abc", 6);
        test("aabbccaac","abc", 12);
        test("aabbccaacb","abc", 12);
        test("aabbccabcaacb","abc", 22);
        test("abbccaacac", "abcac", 22);
        test("aabdbaabeeadcbbdedacbbeecbabebaeeecaeabaedadcbdbcdaabebdadbbaeab"
             + "dadeaabbabbecebbebcaddaacccebeaeedababedeacdeaaaeeaecbe",
             "bddabdcae", 10582116);
        test("adbdadeecadeadeccaeaabdabdbcdabddddabcaaadbabaaedeeddeaeebcdeabc"
             + "aaaeeaeeabcddcebddebeebedaecccbdcbcedbdaeaedcdebeecdaaedaacadb"
             + "dccabddaddacdddc", "bcddceeeebecbc", 700531452);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("DistinctSubseq");
    }
}
