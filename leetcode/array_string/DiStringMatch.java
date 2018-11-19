import java.util.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC942: https://leetcode.com/problems/di-string-match/
//
// Given a string S that only contains "I" (increase) or "D" (decrease).
// Return any permutation A of [0, 1, ..., N] such that for all i = 0, ..., N-1:
// If S[i] == "I", then A[i] < A[i+1]
// If S[i] == "D", then A[i] > A[i+1]
public class DiStringMatch {
    // beats %(7 ms for 95 tests)
    public int[] diStringMatch(String S) {
        int n = S.length() + 1;
        int[] res = new int[n];
        int max = 0;
        int min = 0;
        for (int i = 0; i < n - 1; i++) {
            if (S.charAt(i) == 'I') {
                res[i + 1] = ++max;
            } else {
                res[i + 1] = --min;
            }
        }
        for (int i = 0; i < n; i++) {
            res[i] -= min;
        }
        return res;
    }

    // beats %(8 ms for 95 tests)
    public int[] diStringMatch2(String S) {
        int n = S.length();
        int min = 0;
        int max = n;
        int[] res = new int[n + 1];
        for (int i = 0; i < n; ++i) {
            if (S.charAt(i) == 'I') {
                res[i] = min++;
            } else {
                res[i] = max--;
            }
        }
        res[n] = min;
        return res;
    }

    void test(String S) {
        DiStringMatch d = new DiStringMatch();
        test(S, d::diStringMatch);
        test(S, d::diStringMatch2);
    }

    void test(String S, Function<String, int[]> fun) {
        int[] res = fun.apply(S);
        assertEquals("result array length", S.length() + 1, res.length);
        for (int i = 0; i < res.length - 1; i++) {
            assertEquals("index " + i + " increase ", (S.charAt(i) == 'I'), res[i + 1] > res[i]);
        }
        // System.out.println(Arrays.toString(res));
        Arrays.sort(res);
        assertEquals(0, res[0]);
        assertEquals(res.length - 1, res[res.length - 1]);
    }

    @Test
    public void test() {
        test("IDID");
        test("III");
        test("DDI");
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
