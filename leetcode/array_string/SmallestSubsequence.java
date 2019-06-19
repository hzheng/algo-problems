import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1081: https://leetcode.com/problems/smallest-subsequence-of-distinct-characters/submissions/
//
// Return the lexicographically smallest subsequence of text that contains all the distinct
// characters of text exactly once.
// Note:
// 1 <= text.length <= 1000
// text consists of lowercase English letters.
public class SmallestSubsequence {
    // Deque
    // time complexity: O(N), space complexity: O(1)
    // 2 ms(92.30%), 35.6 MB(100%) for 65 tests
    public String smallestSubsequence(String text) {
        final int SIZE = 26;
        Deque[] indices = new Deque[SIZE];
        int total = 0;
        for (int i = text.length() - 1; i >= 0; i--) {
            int j = text.charAt(i) - 'a';
            if (indices[j] == null) {
                indices[j] = new LinkedList<>();
                total++;
            }
            indices[j].push(i);
        }
        char[] res = new char[total];
        for (int i = 0; i < total; ) {
            mid:
            for (int j = 0; j < SIZE; j++) {
                Deque<Integer> indexQueue = (Deque<Integer>)indices[j];
                if (indexQueue == null) {
                    continue;
                }
                int start = indexQueue.peekFirst();
                for (int k = 0; k < SIZE; k++) {
                    if (k != j && indices[k] != null && (int)indices[k].peekLast() < start) {
                        continue mid;
                    }
                }
                res[i++] = (char)('a' + j);
                indices[j] = null;
                for (int k = 0; k < SIZE; k++) {
                    if (indices[k] != null) {
                        for (; (int)indices[k].peekFirst() < start; indices[k].pop()) {}
                    }
                }
                break;
            }
        }
        return String.valueOf(res);
    }

    // Monotonic Stack
    // time complexity: O(N), space complexity: O(1)
    // 2 ms(92.30%), 35.6 MB(100%) for 65 tests
    public String smallestSubsequence2(String text) {
        int[] last = new int[26];
        for (int i = 0; i < text.length(); i++) {
            last[text.charAt(i) - 'a'] = i;
        }
        Stack<Integer> stack = new Stack<>();
        boolean[] chosen = new boolean[26];
        for (int i = 0; i < text.length(); i++) {
            int c = text.charAt(i) - 'a';
            if (chosen[c]) {
                continue;
            }
            while (!stack.isEmpty() && stack.peek() > c && i < last[stack.peek()]) {
                chosen[stack.pop()] = false;
            }
            stack.push(c);
            chosen[c] = true;
        }
        StringBuilder sb = new StringBuilder();
        for (int i : stack) {
            sb.append((char)('a' + i));
        }
        return sb.toString();
    }

    void test(String text, String expected) {
        assertEquals(expected, smallestSubsequence(text));
        assertEquals(expected, smallestSubsequence2(text));
    }

    @Test
    public void test() {
        test("cdadabcc", "adbc");
        test("abcd", "abcd");
        test("ecbacba", "eacb");
        test("leetcode", "letcod");
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
