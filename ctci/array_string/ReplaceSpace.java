import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 1.4:
 * Replace all spaces in a string with '%20'. You may assume that the string
 *  has sufficient space at the end of the string to hold the additional
 *  characters, and that you are given the "true" length of the string.
 *  (Note: if implementing in Java, please use a character array so that you
 *  can perform this operation in place.)
 */
public class ReplaceSpace {
    public void replaceSpace(char[] str, int len) {
        if (str == null) return;

        int spaceCount = 0;
        for (int i = 0; i < len; ++i) {
            if (str[i] == ' ') {
                spaceCount++;
            }
        }
        if (spaceCount == 0) return;

        int end = len - 1 + 2 * spaceCount;
        str[end + 1] = '\0';
        for (int i = len - 1; i >= 0; --i) {
            char c = str[i];
            if (c != ' ') {
                str[end--] = c;
            } else {
                str[end--] = '0';
                str[end--] = '2';
                str[end--] = '%';
            }
        }
    }

    void test(String str, String expected) {
        if (str == null) return;

        int len = str.length();
        char[] chars = Arrays.copyOf(str.toCharArray(), 3 * len + 1);
        replaceSpace(chars, len);
        int newLen = 0;
        for (char c : chars) {
            if (c == '\0') break;
            ++newLen;
        }
        assertEquals(expected, new String(chars, 0, newLen));
    }

    @Test
    public void test0() {
        test(null, null);
    }

    @Test
    public void test1() {
        test("", "");
    }

    @Test
    public void test2() {
        test("ab", "ab");
    }

    @Test
    public void test3() {
        test(" ", "%20");
    }

    @Test
    public void test4() {
        test("a b", "a%20b");
    }

    @Test
    public void test5() {
        test("a   b", "a%20%20%20b");
    }

    @Test
    public void test6() {
        test("a   b ", "a%20%20%20b%20");
    }

    @Test
    public void test7() {
        test(" a   b ", "%20a%20%20%20b%20");
    }

    @Test
    public void test8() {
        test("     ", "%20%20%20%20%20");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ReplaceSpace");
    }
}
