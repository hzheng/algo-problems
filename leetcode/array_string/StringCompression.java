import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC443: https://leetcode.com/problems/string-compression/
//
// Given an array of characters, compress it in-place.
// The length after compression must always be smaller than or equal to the
// original array.
// Every element of the array should be a character (not int) of length 1.
// After you are done modifying the input array in-place, return the new length
// of the array.
// Note:
// All characters have an ASCII value in [35, 126].
// 1 <= len(chars) <= 1000.
public class StringCompression {
    // time complexity: O(N), space complexity: O(1)
    // beats %(8 ms for 70 tests)
    public int compress(char[] chars) {
        int n = chars.length;
        int end = 0;
        for (int i = 0, j; i < n; i = j) {
            j = i + 1;
            for (; j < n && chars[j] == chars[i]; j++) {}
            int count = j - i;
            chars[end++] = chars[i];
            if (count > 1) {
                String str = String.valueOf(count);
                for (int k = 0; k < str.length(); k++) {
                    chars[end++] = str.charAt(k);
                }
            }
        }
        return end;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats %(8 ms for 70 tests)
    public int compress2(char[] chars) {
        int n = chars.length;
        int write = 0;
        for (int read = 0, anchor = 0; read < n; read++) {
            if (read + 1 < n && chars[read + 1] == chars[read]) continue;

            chars[write++] = chars[anchor];
            if (read > anchor) {
                for (char c : ("" + (read - anchor + 1)).toCharArray()) {
                    chars[write++] = c;
                }
            }
            anchor = read + 1;
        }
        return write;
    }

    void test(char[] chars, String expected) {
        StringCompression s = new StringCompression();
        test(chars, expected, s::compress);
        test(chars, expected, s::compress2);
    }

    void test(char[] chars, String expected,
              Function<char[], Integer> compress) {
        char[] cs = chars.clone();
        assertEquals(expected, new String(cs, 0, compress.apply(cs)));
    }

    @Test
    public void test() {
        test(new char[] {'a', 'a', 'b', 'b', 'c', 'c', 'c'}, "a2b2c3");
        test(new char[] {'a'}, "a");
        test(new char[] {'a', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b',
                         'b', 'b'}, "ab12");
        test(new char[] {'a', 'a', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b', 'b',
                         'b', 'b', 'b'}, "a2b12");
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
