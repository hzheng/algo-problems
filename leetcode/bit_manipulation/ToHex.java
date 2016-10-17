import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC405: https://leetcode.com/problems/convert-a-number-to-hexadecimal/
//
// Given an integer, write an algorithm to convert it to hexadecimal.
// For negative integer, two’s complement method is used.
//
// Note:
// All letters in hexadecimal (a-f) must be in lowercase.
// The hexadecimal string must not contain extra leading 0s. If the number is
// zero, it is represented by a single zero character '0'; otherwise, the first
// character in the hexadecimal string will not be the zero character.
// The given number is guaranteed to fit within the range of a 32-bit signed integer.
// You must not use any method provided by the library which converts/formats
// the number to hex directly.
public class ToHex {
    // beats 15.57%(10 ms for 100 tests)
    public String toHex(int num) {
        if (num == 0) return "0";
        if (num == Integer.MIN_VALUE) return "80000000";

        boolean isNegative = num < 0;
        StringBuilder sb = new StringBuilder();
        for (int n = Math.abs(num); n > 0; n >>= 1) {
            sb.append(n & 1);
        }
        while (sb.length() < 32) {
            sb.append(0);
        }
        sb.reverse();
        if (isNegative) {
            for (int i = 0; i < 32; i++) {
                char c = sb.charAt(i);
                sb.setCharAt(i, c == '0' ? '1' : '0');
            }
            for (int i = 31; i >= 0; i--) {
                if (sb.charAt(i) == '1') {
                    sb.setCharAt(i, '0');
                } else {
                    sb.setCharAt(i, '1');
                    break;
                }
            }
        }
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int n = 0;
            for (int j = i * 4; j < i * 4 + 4; j++) {
                n <<= 1;
                n += (sb.charAt(j) - '0');
            }
            res.append((char)(n < 10 ? n + '0' : (n - 10 + 'a')));
        }
        while (res.charAt(0) == '0') {
            res.deleteCharAt(0);
        }
        return res.toString();
    }

    private static final char[] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6',
                                             '7','8','9','a','b','c','d','e','f'};

    // beats 51.92%(8 ms for 100 tests)
    public String toHex2(int num) {
        StringBuilder sb = new StringBuilder();
        for (int n = num; n != 0; n >>>= 4) {
            sb.append(HEX_CHARS[n & 15]);
        }
        return (num == 0) ? "0" : sb.reverse().toString();
    }

    void test(int num, String expected) {
        assertEquals(expected, toHex(num));
        assertEquals(expected, toHex2(num));
    }

    @Test
    public void test1() {
        test(26, "1a");
        test(-1, "ffffffff");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ToHex");
    }
}
