import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.collection.IsIn.*;

// LC1111: https://leetcode.com/problems/maximum-nesting-depth-of-two-valid-parentheses-strings/
//
// A string is a valid parentheses string (denoted VPS) if and only if it consists of "(" and ")"
// characters only, and:
// It is the empty string, or
// It can be written as AB (A concatenated with B), where A and B are VPS's, or
// It can be written as (A), where A is a VPS.
// We can similarly define the nesting depth depth(S) of any VPS S as follows:
// depth("") = 0
// depth(A + B) = max(depth(A), depth(B)), where A and B are VPS's
// depth("(" + A + ")") = 1 + depth(A), where A is a VPS.
// For example,  "", "()()", and "()(()())" are VPS's (with nesting depths 0, 1, and 2), and ")("
// and "(()" are not VPS's.
// Given a VPS seq, split it into two disjoint subsequences A and B, such that A and B are VPS's
// (and A.length + B.length = seq.length).
// Now choose any such A and B such that max(depth(A), depth(B)) is the minimum possible value.
// Return an answer array (of length seq.length) that encodes such a choice of A and B:
// answer[i] = 0 if seq[i] is part of A, else answer[i] = 1.  Note that even though multiple answers
// may exist, you may return any of them.
//
// Constraints:
// 1 <= seq.size <= 10000
public class MaxDepthAfterSplit {
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(100.00%), 38.7 MB(100.00%) for 31 tests
    public int[] maxDepthAfterSplit(String seq) {
        char[] s = seq.toCharArray();
        int maxLevel = 0;
        int level = 0;
        for (char c : s) {
            level += (c == '(') ? 1 : -1;
            maxLevel = Math.max(maxLevel, level);
        }
        int n = s.length;
        int[] res = new int[n];
        for (int i = 0, tgt = maxLevel / 2; i < n; i++) {
            level += (s[i] == '(') ? 1 : -1;
            if (level > tgt || level == tgt && s[i] == ')') {
                res[i] = 1;
            }
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(N)
    // 1 ms(100.00%), 39.2 MB(64.32%) for 31 tests
    public int[] maxDepthAfterSplit2(String seq) {
        int[] res = new int[seq.length()];
        int level = 0;
        int i = 0;
        for (char c : seq.toCharArray()) { // separate parentheses by its depth's parity
            res[i++] = ((c == '(') ? ++level : level--) & 1;
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(N)
    // 1 ms(100.00%), 39.2 MB(64.32%) for 31 tests
    public int[] maxDepthAfterSplit3(String seq) {
        int n = seq.length();
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = ((seq.charAt(i) == '(') ? 1 : 0) ^ (i & 1);
        }
        return res;
    }

    private void test(Function<String, int[]> maxDepthAfterSplit, String seq, int[]... expected) {
        int[] res = maxDepthAfterSplit.apply(seq);
        List<Integer> resList = Arrays.stream(res).boxed().collect(Collectors.toList());
        List<List<Integer>> expectedList = new ArrayList<>();
        for (int[] e : expected) {
            expectedList.add(Arrays.stream(e).boxed().collect(Collectors.toList()));
        }
        assertThat(resList, in(expectedList));
    }

    private void test(String seq, int[]... expected) {
        MaxDepthAfterSplit m = new MaxDepthAfterSplit();
        test(m::maxDepthAfterSplit, seq, expected);
        test(m::maxDepthAfterSplit2, seq, expected);
        test(m::maxDepthAfterSplit3, seq, expected);
    }

    @Test public void test() {
        test("(()())", new int[] {0, 1, 1, 1, 1, 0}, new int[] {1, 0, 0, 0, 0, 1});
        test("()(())()", new int[] {0, 0, 0, 1, 1, 0, 1, 1}, new int[] {0, 0, 0, 1, 1, 0, 0, 0},
             new int[] {1, 1, 1, 0, 0, 1, 1, 1});
        test("()(())()(())", new int[] {0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0},
             new int[] {1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 1});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
