import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.*;

// Returns the index of the first occurrence of needle in haystack
public class StrStr {
    // beats 69.32%
    public int strStr(String haystack, String needle) {
        // if (haystack.isEmpty() || needle.isEmpty()) return -1;

        int nLen = needle.length();
        int hLen = haystack.length();
        for (int i = nLen - 1; i < hLen; i++) {
            int nIndex = nLen - 1;
            int hIndex = i;
            for (; nIndex >= 0; nIndex--, hIndex--) {
                if (needle.charAt(nIndex) != haystack.charAt(hIndex)) break;
            }
            if (nIndex < 0) return hIndex + 1;
        }

        return -1;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<String, String, Integer> strstr, String haystack,
              String needle, int expected) {
        assertEquals(expected, (long)strstr.apply(haystack, needle));
    }

    void test(String haystack, String needle, int expected) {
        StrStr s = new StrStr();
        test(s::strStr, haystack, needle, expected);
    }

    @Test
    public void test1() {
        test("", "abc", -1);
        test("abc", "", -1);
        test("abc", "abc", 0);
        test("abcdefghi", "def", 3);
        test("abcddefghi", "ddef", 3);
        test("abcddef", "ddef", 3);
        test("abcefghi", "def", -1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("StrStr");
    }
}
