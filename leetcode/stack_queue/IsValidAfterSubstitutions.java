import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1003: https://leetcode.com/problems/check-if-word-is-valid-after-substitutions/
//
// Given a string s, determine if it is valid.
// A string s is valid if, starting with an empty string t = "", you can transform t into s after
// performing the following operation any number of times:
// Insert string "abc" into any position in t. More formally, t becomes tleft + "abc" + tright,
// where t == tleft + tright. Note that tleft and tright may be empty.
// Return true if s is a valid string, otherwise, return false.
//
// Constraints:
// 1 <= s.length <= 2 * 10^4
// s consists of letters 'a', 'b', and 'c'
public class IsValidAfterSubstitutions {
    // Stack
    // time complexity: O(N), space complexity: O(N)
    // 7 ms(73.87%), 39.4 MB(39.30%) for 83 tests
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();
        for (char c : s.toCharArray()) {
            if (c == 'c') {
                if (stack.isEmpty() || stack.pop() != 'b') { return false; }
                if (stack.isEmpty() || stack.pop() != 'a') { return false; }
            } else {
                stack.push(c);
            }
        }
        return stack.isEmpty();
    }

    // Stack
    // time complexity: O(N), space complexity: O(N)
    // 5 ms(94.03%), 42.7 MB(5.14%) for 83 tests
    public boolean isValid2(String s) {
        boolean[] stack = new boolean[s.length()];
        int i = -1;
        for (char c : s.toCharArray()) {
            switch (c) {
            case 'a':
                stack[++i] = false;
                break;
            case 'b':
                if (i < 0 || stack[i]) { return false; }

                stack[i] = true;
                break;
            case 'c':
                if (i < 0 || !stack[i--]) { return false; }
            }
        }
        return i < 0;
    }

    // time complexity: O(N^2), space complexity: O(N)
    // 8 ms(53.09%), 39 MB(5.14%) for 83 tests
    public boolean isValid3(String s) {
        for (String abc = "abc"; s.contains(abc); ) {
            s = s.replace(abc, "");
        }
        return s.isEmpty();
    }

    private void test(String s, boolean expected) {
        assertEquals(expected, isValid(s));
        assertEquals(expected, isValid2(s));
        assertEquals(expected, isValid3(s));
    }

    @Test public void test() {
        test("aabcbc", true);
        test("abcabcababcc", true);
        test("abccba", false);
        test("cababc", false);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
