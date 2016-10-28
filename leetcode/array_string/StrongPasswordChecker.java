import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC420: https://leetcode.com/problems/strong-password-checker/
//
// A password is considered strong if below conditions are all met:
// It has at least 6 characters and at most 20 characters.
// It must contain at least one lowercase letter, at least one uppercase letter,
// and at least one digit.
// It must NOT contain three repeating characters in a row.
// Write a function to return the MINIMUM change required to make a string a
// strong password. If it is already strong, return 0.
// Insertion, deletion or replace of any one character are all considered as one change.
public class StrongPasswordChecker {
    private static final int MIN_COUNT = 6;
    private static final int MAX_COUNT = 20;

    // beats 7.50%(5 ms for 38 tests)
    // FIXME: not 100% sure
    public int strongPasswordChecker(String s) {
        int[] letterFlag = new int[3]; // digit, lowercase and uppercase
        int len = s.length();
        char last = 0;
        int repeated = 1;
        int replaceRepeat = 0;
        int redundant = 0;
        List<Integer> repeatList = len > MAX_COUNT ? new ArrayList<>() : null;
        for (int i = 0; i <= len; i++) {
            char c = (i == len) ? 0 : s.charAt(i);
            if (i == len) {
            } else if (Character.isDigit(c)) {
                letterFlag[0] = 1;
            } else if (Character.isLowerCase(c)) {
                letterFlag[1] = 1;
            } else {
                letterFlag[2] = 1;
            }
            if (c == last) {
                repeated++;
            } else {
                if (repeated > 2) {
                    if (repeatList == null) {
                        replaceRepeat += repeated / 3;
                    } else {
                        repeatList.add(repeated);
                        redundant += repeated - 2;
                    }
                }
                repeated = 1;
            }
            last = c;
        }
        int needChange = Math.max(MIN_COUNT - len,
                                  3 - letterFlag[0] - letterFlag[1] - letterFlag[2]);
        if (len <= MAX_COUNT) return Math.max(needChange, replaceRepeat);
        // deal with long password
        int extra = len - MAX_COUNT;
        if (redundant <= extra) return needChange + extra;

        Collections.sort(repeatList);
        for (int repeat : repeatList) {
            if (extra >= repeat - 2) {
                extra -= (repeat - 2);
            } else {
                replaceRepeat += (repeat - extra) / 3;
                extra = 0;
            }
        }
        return Math.max(needChange, replaceRepeat) + len - MAX_COUNT;
    }

    void test(String s, int expected) {
        assertEquals(expected, strongPasswordChecker(s));
    }

    @Test
    public void test1() {
        test("abc", 3);
        test("abcd", 2);
        test("abcde", 2);
        test("aaabc", 2);
        test("aaaaa", 2);
        test("aaabcd", 2);
        test("aaabcdf", 2);
        test("1aaabcdf", 1);
        test("1Aaabcdf", 0);
        test("12345678901234567890Abc", 3);
        test("12345678901234567890abc", 4);
        test("12345678901234567890aaabc", 6);
        test("12345678901234567890aaaaabc", 8);
        test("12345678901234567890aaabaaac", 9);
        test("aaaabbaaabbaaa123456A", 3);
        test("aaaaaaaaaaaaaaaaaaaaa", 7);
        test("1234567890aaaaaAaaaaaA", 4);
        test("12345678901234567890aaabaaaA", 8);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("StrongPasswordChecker");
    }
}
