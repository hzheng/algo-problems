import org.junit.Test;
import static org.junit.Assert.*;

// LC541: https://leetcode.com/problems/reverse-string-ii
//
// Given a string and an integer k, you need to reverse the first k characters
// for every 2k characters counting from the start of the string. If there are
// less than k characters left, reverse all of them. If there are less than 2k
//  but greater than or equal to k characters, then reverse the first k characters
//  and left the other as original.
public class ReverseString2 {
    // beats N/A(8 ms for 60 tests)
    public String reverseStr(String s, int k) {
        char[] cs = s.toCharArray();
        int n = cs.length;
        for (int i = 0; i < n; i += 2 * k) {
            reverse(cs, i, Math.min(i + k, n) - 1);
        }
        return new String(cs);
    }

    private void reverse(char[] cs, int start, int end) {
        for (int i = start, j = end; i < j; i++, j--) {
            char tmp = cs[i];
            cs[i] = cs[j];
            cs[j] = tmp;
        }
    }

    void test(String s, int k, String expected) {
        assertEquals(expected, reverseStr(s, k));
    }

    @Test
    public void test() {
        test("abcdefg", 2, "bacdfeg");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ReverseString2");
    }
}
