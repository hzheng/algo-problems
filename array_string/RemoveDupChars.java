import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(4ed) Problem 1.3:
 * Remove the duplicate characters in a string without using any additional buffer
 */
public class RemoveDupChars {
    public int removeDup(char[] str) {
        if (str == null) return 0;

        int len = str.length;
        if (len < 2) return len;

        int index = 1;
        for (int i = 1, j; i < len; ++i) {
            for (j = 0; j < index; ++j) {
                if (str[i] == str[j]) break;
            }
            if (j == index) // a distinct character found
                str[index++] = str[i];
        }
        return index;
    }

    void test(String str, String expected) {
        if (str == null) return;

        char[] chars = str.toCharArray();
        int len = removeDup(chars);
        assertEquals(expected, new String(chars, 0, len));
    }

    @Test
    public void test1() {
        test("abadfzzyya", "abdfzy");
    }

    @Test
    public void test2() {
        test("ab", "ab");
    }

    @Test
    public void test3() {
        test("aa", "a");
    }

    @Test
    public void test4() {
        test("", "");
    }


    @Test
    public void test5() {
        test(null, null);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RemoveDupChars");
    }
}
