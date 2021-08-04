import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC394: https://leetcode.com/contest/3/problems/decode-string/
//
// Given an encoded string, return it's decoded string.
// The encoding rule is: k[encoded_string], where the encoded_string inside the
// square brackets is being repeated exactly k times. Note that k is guaranteed
// to be a positive integer.
// You may assume that the input string is always valid; No extra white spaces,
// square brackets are well-formed, etc.
// Furthermore, you may assume that the original data does not contain any
// digits and that digits are only for those repeat numbers, k.
public class DecodeString {
    // Stack
    // beats 22.14%(5 ms for 27 tests)
    public String decodeString(String s) {
        Deque<Integer> counts = new ArrayDeque<>();
        Deque<StringBuilder> strs = new ArrayDeque<>();
        int count = 0;
        strs.push(new StringBuilder());
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                count = count * 10 + (c - '0');
            } else if (c == '[') {
                strs.push(new StringBuilder());
                counts.push(count);
                count = 0;
            } else if (c == ']') {
                StringBuilder oldTop = strs.pop();
                StringBuilder newTop = strs.peek();
                for (int times = counts.pop(); times > 0; times--) {
                    newTop.append(oldTop);
                }
            } else {
                strs.peek().append(c);
            }
        }
        return strs.peek().toString();
    }

    // Solution of Choice
    // Stack
    // 0 ms(100.00%), 36.9 MB(90.05%) for 34 tests
    public String decodeString_2(String s) {
        Deque<Integer> counts = new ArrayDeque<>();
        Deque<StringBuilder> strs = new ArrayDeque<>();
        StringBuilder buffer = new StringBuilder();
        int count = 0;
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                count = count * 10 + (c - '0');
            } else if (c == '[') {
                counts.push(count);
                count = 0;
                strs.push(buffer);
                buffer = new StringBuilder();
            } else if (c == ']') {
                String repeatedBuffer = buffer.toString().repeat(counts.pop());
                buffer = strs.pop();
                buffer.append(repeatedBuffer);
            } else {
                buffer.append(c);
            }
        }
        return buffer.toString();
    }

    // Recursion
    // beats 83.64%(3 ms for 27 tests)
    public String decodeString2(String s) {
        return decode(s.toCharArray(), new int[1]);
    }

    private String decode(char[] s, int[] index) {
        StringBuilder res = new StringBuilder();
        for (; index[0] < s.length && s[index[0]] != ']'; index[0]++) {
            if (!Character.isDigit(s[index[0]])) {
                res.append(s[index[0]]);
            } else {
                int count = 0;
                while (Character.isDigit(s[index[0]])) {
                    count = count * 10 + s[index[0]++] - '0';
                }
                index[0]++;     //'['
                String expand = decode(s, index);
                while (count-- > 0) {
                    res.append(expand);
                }
            }
        }
        return res.toString();
    }

    void test(String s, String expected) {
        assertEquals(expected, decodeString(s));
        assertEquals(expected, decodeString_2(s));
        assertEquals(expected, decodeString2(s));
    }

    @Test
    public void test1() {
        test("3[a]", "aaa");
        test("3[a]2[bc]", "aaabcbc");
        test("3[a2[c]]", "accaccacc");
        test("2[abc]3[cd]ef", "abcabccdcdcdef");
        test("3[a2[b3[cd]]]", "abcdcdcdbcdcdcdabcdcdcdbcdcdcdabcdcdcdbcdcdcd");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
