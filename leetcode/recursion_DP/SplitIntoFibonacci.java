import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC842: https://leetcode.com/problems/split-array-into-fibonacci-sequence/
//
// Given a string S of digits, such as S = "123456579", we can split it into a
// Fibonacci-like sequence [123, 456, 579]. When splitting the string into
// pieces, each piece must not have extra leading zeroes, except if the piece is
// the number 0 itself. Return any Fibonacci-like sequence split from S, or
// return [] if it cannot be done.
public class SplitIntoFibonacci {
    // Recursion + DFS + Backtracking
    // beats %(12 ms for 70 tests)
    public List<Integer> splitIntoFibonacci(String S) {
        List<Integer> res = new ArrayList<>();
        split(S.toCharArray(), 0, res);
        return res;
    }

    private boolean split(char[] cs, int from, List<Integer> path) {
        int len = path.size();
        int n = cs.length;
        if (from == n) return len > 2;

        long sum = (len < 2) ? -1 : (long)path.get(len - 1) + path.get(len - 2);
        for (int i = from, m = (cs[from] == '0') ? from + 1 : n; i < m; i++) {
            long k = Long.parseLong(String.valueOf(cs, from, i + 1 - from));
            if (k >= Integer.MAX_VALUE || (k > sum && sum >= 0)) break;
            if (k < sum) continue;

            path.add((int)k);
            if (split(cs, i + 1, path)) return true;

            path.remove(path.size() - 1);
        }
        return false;
    }

    // time complexity: O(N ^ 3 * log(N)), space complexity: O(N)
    // beats %(14 ms for 70 tests)
    public List<Integer> splitIntoFibonacci2(String S) {
        int n = S.length();
        for (int i = 0; i < Math.min(10, n); i++) {
            if (S.charAt(0) == '0' && i > 0) break;

            long a = Long.valueOf(S.substring(0, i + 1));
            if (a >= Integer.MAX_VALUE) break;

            search : for (int j = i + 1; j < Math.min(i + 10, n); j++) {
                if (S.charAt(i + 1) == '0' && j > i + 1) break;

                long b = Long.valueOf(S.substring(i + 1, j + 1));
                if (b >= Integer.MAX_VALUE) break;

                List<Integer> res = new ArrayList<>();
                res.add((int)a);
                res.add((int)b);
                for (int k = j + 1; k < n; ) {
                    long s = res.get(res.size() - 2) + res.get(res.size() - 1);
                    String next = String.valueOf(s);
                    if (s > Integer.MAX_VALUE
                        || !S.substring(k).startsWith(next)) {
                        continue search;
                    }
                    k += next.length();
                    res.add((int)s);
                }
                if (res.size() > 2) return res;
            }
        }
        return new ArrayList<>();
    }

    void test(String S, Integer ... expected) {
        List<Integer> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, splitIntoFibonacci(S));
        assertEquals(expectedList, splitIntoFibonacci2(S));
    }

    @Test
    public void test() {
        test("11235813", new Integer[] { 1, 1, 2, 3, 5, 8, 13 });
        test("123456579", new Integer[] { 123, 456, 579 });
        test("1320581321313221264343965566089105744171833277577",
             new Integer[] { 13205, 8, 13213, 13221, 26434, 39655, 66089, 
                             105744, 171833, 277577 });
        test("36115373839853435918344412703521047933751454799388550714335002319"
             + "007375250760715149824021158955352571955641615091673346471089497"
             + "381762843852852341234615185087467526311208271139195502377031632"
             + "94909", new Integer[] {});
        test("0123", new Integer[] {});
        test("1011", new Integer[] {1, 0, 1, 1});
        test("1101111", new Integer[] {11, 0, 11, 11});
        // test("1101111", new Integer[] {110, 1, 111}); // also right
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
