import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 1.1:
 * Implement an algorithm to determine if a string has all unique characters.
    What if you can not use additional data structures?
 */
public class UniqueChar {
    public boolean hasUniqueCharBySet(String str) {
        if (str == null) return false;

        Set<Character> chars = new HashSet<Character>();
        for (char c : str.toCharArray()) {
            chars.add(c);
        }
        return chars.size() == str.length();
    }

    public boolean hasUniqueCharBySort(String str) {
        if (str == null) return false;

        char[] chars = str.toCharArray();
        Arrays.sort(chars);
        Character last = null;
        for (char c : chars) {
            if (last != null && c == last) return false;
            last = c;
        }
        return true;
    }

    public boolean hasUniqueCharByArray(String str) {
        if (str == null) return false;

        // time complexity: O(N), space complexity: O(1)
        if (str.length() > 256) return false;

        boolean[] chars = new boolean[256];
        for (int i = str.length() - 1; i >= 0; --i) {
            int c = str.charAt(i);
            if (chars[c]) {
                return false;
            }
            chars[c] = true;
        }
        return true;
    }

    public boolean hasUniqueCharByBitset(String str) {
        if (str == null) return false;

        int checker = 0;
        for (int i = 0; i < str.length(); i++) {
            int val = str.charAt(i) - 'a';
            if((checker & (i << val)) > 0) {
                return false;
            }
            checker |= (1 << val);
        }
        return true;
    }

    void test(String str, boolean expected) {
        assertEquals(expected, hasUniqueCharBySet(str));
        assertEquals(expected, hasUniqueCharBySort(str));
        assertEquals(expected, hasUniqueCharByArray(str));
        assertEquals(expected, hasUniqueCharByBitset(str));
    }

    @Test
    public void test1() {
        test("abcdb", false);
    }

    @Test
    public void test2() {
        test("a", true);
    }

    @Test
    public void test3() {
        test("", true);
    }

    @Test
    public void test4() {
        test(null, false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("UniqueChar");
    }
}
