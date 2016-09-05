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
    // beats N/A(5 ms)
    public String decodeString(String s) {
        Stack<Integer> counts = new Stack<>();
        Stack<StringBuilder> strs = new Stack<>();
        int count = 0;
        strs.push(new StringBuilder());
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                count = count * 10 + (c - '0');
            } else if (c == '[') {
                strs.push(new StringBuilder());
                counts.push(Math.max(1, count));
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

    void test(String s, String expected) {
        assertEquals(expected, decodeString(s));
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
        org.junit.runner.JUnitCore.main("DecodeString");
    }
}
