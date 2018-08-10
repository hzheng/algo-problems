import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeNoException;

// LC884: https://leetcode.com/problems/decoded-string-at-index/
//
// An encoded string S is given. To find and write the decoded string to a tape,
// the encoded string is read one character at a time and the following steps
// are taken:
// If the character read is a letter, that letter is written onto the tape.
// If the character read is a digit (say d), the entire current tape is
// repeatedly written d-1 more times in total.
// For some encoded string S, and an index K, find and return the K-th letter
// (1 indexed) in the decoded string.
// Note:
// 2 <= S.length <= 100
// S will only contain lowercase letters and digits 2 through 9.
// S starts with a letter.
// 1 <= K <= 10^9
// The decoded string is guaranteed to have less than 2^63 letters.
public class DecodedStringAtIndex {
    // beats 100.00%(2 ms for 45 tests)
    public String decodeAtIndex(String S, int K) {
        long len = 0;
        for (char c : S.toCharArray()) {
            if (Character.isLetter(c)) {
                len++;
            } else {
                len *= (c - '0');
            }
        }
        for (int i = S.length() - 1; ; i--) {
            char c = S.charAt(i);
            K %= len;
            if (c > '0' && c <= '9') {
                len /= (c - '0');
            } else {
                if (K == 0) return Character.toString(c);

                len--;
            }
        }
    }

    // beats 100.00%(2 ms for 45 tests)
    public String decodeAtIndex_2(String S, int K) {
        long len = 0;
        int i = 0;
        for (; len < K; i++) {
            char c = S.charAt(i);
            if (c > '0' && c <= '9') {
                len *= (c - '0');
            } else {
                len++;
            }
        }
        while (true) {
            char c = S.charAt(--i);
            K %= len;
            if (c > '0' && c <= '9') {
                len /= c - '0';
            } else {
                if (K == 0) return Character.toString(c);

                len--;
            }
        }
    }

    // Recursion
    // beats 54.44%(3 ms for 45 tests)
    public String decodeAtIndex2(String S, int K) {
        long len = 0;
        for (char c : S.toCharArray()) {
            if (c > '0' && c <= '9') {
                int repeat = c - '0';
                if (len * repeat >= K) {
                    return decodeAtIndex2(S, (int) ((K - 1) % len) + 1);
                }
                len *= repeat;
            } else if (++len == K) return Character.toString(c);
        }
        return null;
    }

    // TODO: stack

    void test(String S, int K, String expected) {
        assertEquals(expected, decodeAtIndex(S, K));
        assertEquals(expected, decodeAtIndex_2(S, K));
        assertEquals(expected, decodeAtIndex2(S, K));
    }

    @Test
    public void test() {
        test("leet2code3", 10, "o");
        test("ha22", 5, "h");
        test("a2345678999999999999999", 1, "a");
        test("vk6u5xhq9v", 554, "k");
        test("abc", 3, "c");
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
