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
    // beats 39.77%(8 ms for 49 tests)
    public boolean validUtf8(int[] data) {
        int len = data.length;
        for (int i = 0; i < len; i++) {
            int bits = 0;
            for (int b = data[i], mask = 0x80; (b & mask) != 0; mask >>= 1, bits++) {}
            if (bits > 4 || bits == 1) return false;

            for (; bits > 1; bits--) {
                if (++i >= len || (data[i] >> 6) != 2) return false;
            }
        }
        return true;
    }

    // beats 69.02%(7 ms for 49 tests)
    public boolean validUtf8_2(int[] data) {
        int bits = 0;
        for (int n : data) {
            if (bits == 0) {
                if ((n >> 5) == 0b110) {
                    bits = 1;
                } else if ((n >> 4) == 0b1110) {
                    bits = 2;
                } else if ((n >> 3) == 0b11110) {
                    bits = 3;
                } else if ((n >> 7) == 1) return false;
            } else if ((n >> 6) == 0b10) {
                bits--;
            } else return false;
        }
        return bits == 0;
    }

    // Solution of Choice
    // beats 39.77%(8 ms for 49 tests)
    public boolean validUtf8_3(int[] data) {
        int bits = 0;
        for (int n : data) {
            if (bits == 0) {
                if ((n >>= 3) == 0b11110) {
                    bits = 3;
                } else if ((n >>= 1) == 0b1110) {
                    bits = 2;
                } else if ((n >>= 1) == 0b110) {
                    bits = 1;
                } else if ((n >> 2) == 1) return false;
            } else if ((n >> 6) == 0b10) {
                bits--;
            } else return false;
        }
        return bits == 0;
    }

    // beats 39.77%(8 ms for 49 tests)
    public boolean validUtf8_4(int[] data) {
        int bits = 0;
        for (int n : data) {
            if (n >= 0b11000000) {
                if (bits != 0 || n >= 0b11111000) return false;
                if (n >= 0b11110000) {
                    bits = 3;
                } else if (n >= 0b11100000) {
                    bits = 2;
                } else {
                    bits = 1;
                }
            } else if (n >= 0b10000000) {
                if (--bits < 0) return false;
            } else if (bits > 0) return false;
        }
        return bits == 0;
    }

    void test(int[] data, boolean expected) {
        assertEquals(expected, validUtf8(data));
        assertEquals(expected, validUtf8_2(data));
        assertEquals(expected, validUtf8_3(data));
        assertEquals(expected, validUtf8_4(data));
    }

    @Test
    public void test1() {
        test(new int[] {197, 130, 1}, true);
        test(new int[] {235, 140, 4}, false);
        test(new int[] {248, 130, 130, 130}, false);
        test(new int[] {0b11000101, 0b10101111}, true);
        test(new int[] {0b11000101, 0b10101111, 0b10101101}, false);
        test(new int[] {0b11100101, 0b10101111, 0b10101101}, true);
        test(new int[] {0b11100101, 0b10101111, 0b10101111, 0b10101101}, false);
        test(new int[] {0b11110101, 0b10101111, 0b10101111, 0b10101101}, true);
        test(new int[] {0b11110101, 0b10101111, 0b10101111, 0b11101101}, false);
        test(new int[] {0b11110101, 0b10101111, 0b10101111}, false);
        test(new int[] {0b11000101, 0b10101111, 0b11100101, 0b10101111, 0b10101101}, true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Utf8Validation");
    }
}
