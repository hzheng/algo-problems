import org.junit.Test;
import static org.junit.Assert.*;

// LC738: https://leetcode.com/problems/monotone-increasing-digits/
//
// Given a non-negative integer N, find the largest number that is less than or
// equal to N with monotone increasing digits.
public class MonotoneIncreasingDigits {
    // beats 93.03%(12 ms for 302 tests)
    public int monotoneIncreasingDigits(int N) {
        char[] s = String.valueOf(N).toCharArray();
        int n = s.length;
        int i = 1;
        for (; i < n && (s[i] >= s[i - 1]); i++) {}
        for (int j = i; j < n; j++) {
            s[j] = '9';
        }
        if (i < n) {
            for (i--; i >= 0; i--) {
                s[i]--;
                if (i == 0 || s[i] >= s[i - 1]) break;
                s[i] = '9';
            }
        }
        return Integer.valueOf(String.valueOf(s));
    }

    // beats 52.05%(13 ms for 302 tests)
    public int monotoneIncreasingDigits2(int N) {
        char[] s = String.valueOf(N).toCharArray();
        int n = s.length;
        int i = 1;
        for (; i < n && (s[i] >= s[i - 1]); i++) {}
        for (; i > 0 && i < n && s[i] < s[i - 1]; s[--i]--) {}
        for (int j = i + 1; j < n; j++) {
            s[j] = '9';
        }
        return Integer.valueOf(String.valueOf(s));
    }

    // Solution of Choice
    // beats 52.05%(13 ms for 302 tests)
    public int monotoneIncreasingDigits3(int N) {
        char[] s = String.valueOf(N).toCharArray();
        int n = s.length;
        int mark = n;
        for (int i = n - 1; i > 0; i--){
            if (s[i] < s[i - 1]) {
                mark = i;
                s[i - 1]--;
            }
        }
        for (int i = mark; i < n; i++) {
            s[i] = '9';
        }
        return Integer.valueOf(String.valueOf(s));
    }

    // beats 8.02%(20 ms for 302 tests)
    public int monotoneIncreasingDigits4(int N) {
        String S = String.valueOf(N);
        String res = "";
        outer : for (int i = 0; i < S.length(); i++) {
            for (char d = S.charAt(i); d <= '9'; d++) {
                if (S.compareTo(res + repeat(d, S.length() - i)) < 0) {
                    res += (char)(d - 1);
                    continue outer;
                }
            }
            res += '9';
        }
        return Integer.valueOf(res);
    }

    private String repeat(char c, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    void test(int N, int expected) {
        assertEquals(expected, monotoneIncreasingDigits(N));
        assertEquals(expected, monotoneIncreasingDigits2(N));
        assertEquals(expected, monotoneIncreasingDigits3(N));
        assertEquals(expected, monotoneIncreasingDigits4(N));
    }

    @Test
    public void test() {
        test(0, 0);
        test(5, 5);
        test(10, 9);
        test(1234, 1234);
        test(332, 299);
        test(666765, 666699);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
