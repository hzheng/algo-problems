import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 1.5:
 * Perform basic string compression using the counts of repeated characters.
 * For example, the string aabcccccaaa would become a2b1c5a3. If the
 *  "compressed" string would not become smaller than the original string,
 *  return the original string.
 */
public class StrCompress {
    // time complexity: O(n), space complexity: O(n)
    String compress(String str) {
        if (str == null) {
            return null;
        }

        int len = str.length();
        if (len < 2) {
            return str;
        }

        char[] compressed = new char[len * 2];
        int index = 0;
        char last = str.charAt(0);
        int count = 1;
        for (int i = 1; i < len; i++) {
            char c = str.charAt(i);
            if (c == last) {
                count++;
            } else {
                // write out
                index = setChar(compressed, last, index, count);
                // reset
                last = c;
                count = 1;
            }
        }
        index = setChar(compressed, last, index, count);
        if (index >= len) {
            return str;
        }
        return new String(compressed, 0, index);
    }

    private int setChar(char[] chars, char c, int index, int count) {
        chars[index++] = c;
        String countStr = String.valueOf(count);
        for (int j = 0; j < countStr.length(); j++) {
            chars[index++] = countStr.charAt(j);
        }
        return index;
    }

    void test(String str, String expected) {
        assertEquals(expected, compress(str));
    }

    @Test
    public void test1() {
        test("aabcccccaaa", "a2b1c5a3");
    }

    @Test
    public void test2() {
        test("aabcccccaaab", "a2b1c5a3b1");
    }

    @Test
    public void test3() {
        test("abc", "abc");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("StrCompress");
    }
}
