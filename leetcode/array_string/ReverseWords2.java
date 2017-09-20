import org.junit.Test;
import static org.junit.Assert.*;

// LC186: https://leetcode.com/problems/reverse-words-in-a-string-ii/?tab=Description
//
// Given an input string, reverse the string word by word.
// The input string does not contain leading or trailing spaces and the words
// are always separated by a single space.
// Could you do it in-place without allocating extra space?
public class ReverseWords2 {
    // beats 95.88%(2 ms for 17 tests)
    public void reverseWords(char[] s) {
        int n = s.length;
        reverse(s, 0, n - 1);
        for (int i = 0; i < n; i++) {
            int start = i;
            for (i = start + 1; i < n && s[i] != ' '; i++) {}
            reverse(s, start, i - 1);
        }
    }

    private void reverse(char[] chars, int start, int end) {
        for (int i = start, j = end; i < j; i++, j--) {
            char tmp = chars[i];
            chars[i] = chars[j];
            chars[j] = tmp;
        }
    }

    // beats 36.81%(3 ms for 17 tests)
    public void reverseWords2(char[] s){
        int n = s.length;
        reverse(s, 0, n - 1);
        for (int i = 0, start = 0; i <= n; i++) {
            if (i == n || s[i] == ' ') {
                reverse(s, start, i - 1);
                start = i + 1;
            }
        }
    }

    @FunctionalInterface
    interface Function<A> {
        public void apply(A a);
    }

    void test(Function<char[]> reverse, String s, String expected) {
        char[] cs = s.toCharArray();
        reverse.apply(cs);
        assertArrayEquals(expected.toCharArray(), cs);

    }

    void test(String s, String expected) {
        ReverseWords2 r = new ReverseWords2();
        test(r::reverseWords, s, expected);
        test(r::reverseWords2, s, expected);
    }

    @Test
    public void test1() {
        test("blue", "blue");
        test("the sky is blue", "blue is sky the");
        test("the sky is blues", "blues is sky the");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ReverseWords2");
    }
}
