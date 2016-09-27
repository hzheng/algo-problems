import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC058: https://leetcode.com/problems/length-of-last-word/
//
// Given a string s consists of upper/lower-case alphabets and empty space
// characters ' ', return the length of last word in the string.
public class LastWordLen {
    // beats 37.07%(5 ms)
    public int lengthOfLastWord(String s) {
        int end = -1;
        int start = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == ' ') {
                if (end >= 0) {
                    start = i + 1;
                    break;
                }
            } else if (end < 0) {
                end = i;
            }
        }
        return end < 0 ? 0 : end - start + 1;
    }

    // Solution of Choice
    // beats 54.75%(4 ms)
    public int lengthOfLastWord2(String s) {
        int len = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) != ' ') {
                len++;
            } else if (len > 0) break;
        }
        return len;
    }

    void test(int expected, String s) {
        assertEquals(expected, lengthOfLastWord(s));
        assertEquals(expected, lengthOfLastWord2(s));
    }

    @Test
    public void test1() {
        test(5, "Hello World");
        test(5, "Hello World  ");
        test(11, "Hello_World  ");
        test(0, "");
        test(0, "   ");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LastWordLen");
    }
}
