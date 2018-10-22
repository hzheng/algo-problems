import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC925: https://leetcode.com/problems/long-pressed-name/
//
// Your friend is typing his name into a keyboard.  Sometimes, when typing a
// character c, the key might get long pressed, and the character will be typed
// 1 or more times. You examine the typed characters of the keyboard.  Return
// True if it is possible that it was your friends name, with some characters
// (possibly none) being long pressed.
public class LongPressedName {
    // time complexity: O(N + T), space complexity: O(1)
    // beats %(3 ms for 71 tests)
    public boolean isLongPressedName(String name, String typed) {
        int nLen = name.length();
        int tLen = typed.length();
        for (int i = 0, j = 0; i < nLen;) {
            char c = name.charAt(i);
            int nRepeat = 1;
            for (++i; i < nLen && name.charAt(i) == c; i++, nRepeat++) {}
            int tRepeat = 0;
            for (; j < tLen && typed.charAt(j) == c; j++, tRepeat++) {}
            if (nRepeat > tRepeat) return false;
        }
        return true;
    }

    // time complexity: O(N + T), space complexity: O(1)
    // beats %(3 ms for 71 tests)
    public boolean isLongPressedName2(String name, String typed) {
        int nLen = name.length();
        int tLen = typed.length();
        int i = 0;
        for (int j = 0; j < tLen; j++) {
            if (i < nLen && name.charAt(i) == typed.charAt(j)) {
                i++;
            } else if (j == 0 || typed.charAt(j) != typed.charAt(j - 1)) return false;
        }
        return i == nLen;
    }

    void test(String name, String typed, boolean expected) {
        assertEquals(expected, isLongPressedName(name, typed));
        assertEquals(expected, isLongPressedName2(name, typed));
    }

    @Test
    public void test() {
        test("alex", "aaleex", true);
        test("saeed", "ssaaedd", false);
        test("leelee", "lleeelee", true);
        test("laiden", "laiden", true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
