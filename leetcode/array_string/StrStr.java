import org.junit.Test;
import static org.junit.Assert.*;

// LC028: https://leetcode.com/problems/implement-strstr/
//
// Returns the index of the first occurrence of needle in haystack
public class StrStr {
    // beats 35.03%(6 ms)
    public int strStr(String haystack, String needle) {
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

    // Solution of Choice
    // KMP algorithm
    // http://www.personal.kent.edu/~rmuhamma/Algorithms/MyAlgorithms/StringMatch/kuthMP.htm
    // beats 18.54%(9 ms)
    public int strStr2(String haystack, String needle) {
        int nLen = needle.length();
        if (nLen == 0) return 0;

        int hLen = haystack.length();
        if (nLen > hLen) return -1;

        char[] pattern = needle.toCharArray();
        int[] table = new int[nLen]; // failure table
        for (int i = 1, j = 0; i < nLen - 1; ) {
            if (pattern[i] == pattern[j]) { // can expand
                table[i++] = ++j;
            } else if (j == 0) { // failed to expand
                i++;
            } else { // try to expand the next best(largest) match
                j = table[j - 1];
            }
        }
        for (int i = 0, j = 0; i < hLen; ) {
            if (haystack.charAt(i) == pattern[j]) { // can expand
                i++;
                if (++j == nLen) return i - nLen;
            } else if (j == 0) { // failed to expand
                i++;
            } else { // try to expand the next best(largest) match
                j = table[j - 1];
            }
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
        test(s::strStr2, haystack, needle, expected);
    }

    @Test
    public void test1() {
        test("", "abc", -1);
        test("abc", "", 0);
        test("abc", "abc", 0);
        test("abcdefghi", "def", 3);
        test("abcddefghi", "ddef", 3);
        test("abcddef", "ddef", 3);
        test("abcddddddef", "ddef", 7);
        test("abcddddfeddef", "ddef", 9);
        test("abcefghi", "def", -1);
        test("babbbbbabb", "bbab", 5);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
