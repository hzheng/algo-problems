import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1047: https://leetcode.com/problems/remove-all-adjacent-duplicates-in-string/
//
// Given a string S of lowercase letters, a duplicate removal consists of choosing two adjacent and
// equal letters, and removing them. We repeatedly make duplicate removals on S until we no longer
// can. Return the final string after all such duplicate removals have been made.  It is guaranteed
// the answer is unique.
// Note:
// 1 <= S.length <= 20000
// S consists only of English lowercase letters.
public class RemoveAdjacentDuplicates {
    // Deque
    // time complexity: O(N), space complexity: O(N)
    // 14 ms(79.99%),  37.4 MB(100%) for 98 tests
    public String removeDuplicates(String S) {
        Deque<Character> deque = new LinkedList<>();
        for (char c : S.toCharArray()) {
            if (!deque.isEmpty() && c == deque.peekLast()) {
                deque.pollLast();
            } else {
                deque.offerLast(c);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (char c : deque) {
            sb.append(c);
        }
        return sb.toString();
    }

    // Array Stack
    // time complexity: O(N), space complexity: O(N)
    // 4 ms(98.52%),  37.4 MB(100%) for 98 tests
    public String removeDuplicates2(String S) {
        char[] stack = S.toCharArray();
        int i = 0;
        for (char c : S.toCharArray()) {
            if (i > 0 && stack[i - 1] == c) {
                i--;
            } else {
                stack[i++] = c;
            }
        }
        return new String(stack, 0, i);
    }

    // Recursion
    // time complexity: O(N), space complexity: O(N)
    // 190 ms(16.32%),  43.1 MB(100%) for 98 tests
    public String removeDuplicates3(String S) {
        for (int i = 1; i < S.length(); i++) {
            if (S.charAt(i - 1) == S.charAt(i)) {
                return removeDuplicates3(S.substring(0, i - 1) + S.substring(i + 1));
            }
        }
        return S;
    }

    void test(String S, String expected) {
        assertEquals(expected, removeDuplicates(S));
        assertEquals(expected, removeDuplicates2(S));
        assertEquals(expected, removeDuplicates3(S));
    }

    @Test
    public void test() {
        test("abbaca", "ca");
        test("bbccaa", "");
        test("bbaccaax", "ax");
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
