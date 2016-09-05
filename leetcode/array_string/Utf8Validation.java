import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC393: https://leetcode.com/problems/utf-8-validation/
//
// A character in UTF8 can be from 1 to 4 bytes long, subjected to the following rules:
// For 1-byte character, the first bit is a 0, followed by its unicode code.
// For n-bytes character, the first n-bits are all one's, the n+1 bit is 0,
// followed by n-1 bytes with most significant 2 bits being 10.
// This is how the UTF-8 encoding would work:
//
//   Char. number range  |        UTF-8 octet sequence
//      (hexadecimal)    |              (binary)
//   --------------------+---------------------------------------------
//   0000 0000-0000 007F | 0xxxxxxx
//   0000 0080-0000 07FF | 110xxxxx 10xxxxxx
//   0000 0800-0000 FFFF | 1110xxxx 10xxxxxx 10xxxxxx
//   0001 0000-0010 FFFF | 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
//
// Given an array of integers representing the data, return whether it is a valid utf-8 encoding.
// Note:
// The input is an array of integers. Only the least significant 8 bits of each
// integer is used to store the data. This means each integer represents only 1 byte of data.
public class Utf8Validation {
    // beats N/A(8 ms)
    public boolean validUtf8(int[] data) {
        int len = data.length;
        for (int i = 0; i < len; i++) {
            int nBytes = 0;
            for (int b = data[i], mask = 0x80; mask > 0; mask >>= 1, nBytes++) {
                if ((b & mask) == 0) break;
            }
            if (nBytes > 4 || nBytes == 1) return false;

            for ( ; nBytes > 1; nBytes--) {
                if (++i >= len) return false;

                if ((data[i] >> 6) != 2) return false;
            }
        }
        return true;
    }

    void test(int[] data, boolean expected) {
        assertEquals(expected, validUtf8(data));
    }

    @Test
    public void test1() {
        test(new int[]{197, 130, 1}, true);
        test(new int[]{235, 140, 4}, false);
        test(new int[]{0b11000101, 0b10101111}, true);
        test(new int[]{0b11000101, 0b10101111, 0b10101101}, false);
        test(new int[]{0b11100101, 0b10101111, 0b10101101}, true);
        test(new int[]{0b11100101, 0b10101111, 0b10101111, 0b10101101}, false);
        test(new int[]{0b11110101, 0b10101111, 0b10101111, 0b10101101}, true);
        test(new int[]{0b11110101, 0b10101111, 0b10101111, 0b11101101}, false);
        test(new int[]{0b11110101, 0b10101111, 0b10101111}, false);
        test(new int[]{0b11000101, 0b10101111, 0b11100101, 0b10101111, 0b10101101}, true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Utf8Validation");
    }
}
