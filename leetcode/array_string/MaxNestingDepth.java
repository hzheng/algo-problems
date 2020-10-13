import org.junit.Test;

import static org.junit.Assert.*;

// LC1614: https://leetcode.com/problems/maximum-nesting-depth-of-the-parentheses/
//
// A string is a valid parentheses string (denoted VPS) if it meets one of the following:
// It is an empty string "", or a single character not equal to "(" or ")", It can be written as AB
// (A concatenated with B), where A and B are VPS's, or It can be written as (A), where A is a VPS.
//We can similarly define the nesting depth depth(S) of any VPS S as follows:
// depth("") = 0
// depth(A + B) = max(depth(A), depth(B)), where A and B are VPS's
// depth("(" + A + ")") = 1 + depth(A), where A is a VPS.
// Given a VPS represented as string s, return the nesting depth of s.
public class MaxNestingDepth {
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100%), 37 MB(60.96%) for 163 tests
    public int maxDepth(String s) {
        int res = 0;
        int level = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') {
                res = Math.max(res, ++level);
            } else if (c == ')') {
                level--;
            }
        }
        return res;
    }

    private void test(String s, int expected) {
        assertEquals(expected, maxDepth(s));
    }

    @Test public void test() {
        test("(1+(2*3)+((8)/4))+1", 3);
        test("(1)+((2))+(((3)))", 3);
        test("1+(2*3)/(2-1)", 1);
        test("1", 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
