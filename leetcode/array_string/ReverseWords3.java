import org.junit.Test;
import static org.junit.Assert.*;

// LC557: https://leetcode.com/problems/reverse-words-in-a-string-iiI
//
// Given a string, you need to reverse the order of characters in each word within
// a sentence while still preserving whitespace and initial word order.
//
// Note: each word is separated by single space and there will not be any extra
// space in the string.
public class ReverseWords3 {
    // beats 79.33%(12 ms for 30 tests)
    public String reverseWords(String s) {
        StringBuilder sb = new StringBuilder();
        for (String w : s.split(" ")) { // or split("\\s")
            sb.append(new StringBuilder(w).reverse()).append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString(); // or: return sb.toString().trim();
    }

    // beats 90.11%(10 ms for 30 tests)
    public String reverseWords2(String s) {
        char[] cs = s.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            if (cs[i] == ' ') continue;

            int j = i;
            for (; j + 1 < cs.length && cs[j + 1] != ' '; j++) {}
            reverse(cs, i, j);
            i = j;
        }
        return new String(cs);
    }

    private void reverse(char[] s, int i, int j) {
        for (; i < j; i++, j--) {
            char tmp = s[i];
            s[i] = s[j];
            s[j] = tmp;
        }
    }

    // beats 95.64%(9 ms for 30 tests)
    public String reverseWords3(String s) {
        char[] cs = s.toCharArray();
        int i = 0;
        for (int j = 0; j < cs.length; j++) {
            if (cs[j] == ' ') {
                reverse(cs, i, j - 1);
                i = j + 1;
            }
        }
        reverse(cs, i, cs.length - 1);
        return new String(cs);
    }

    void test(String s, String expected) {
        assertEquals(expected, reverseWords(s));
        assertEquals(expected, reverseWords2(s));
        assertEquals(expected, reverseWords3(s));
    }

    @Test
    public void test1() {
        test("", "");
        test("ab", "ba");
        test("Let's take LeetCode contest", "s'teL ekat edoCteeL tsetnoc");
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
