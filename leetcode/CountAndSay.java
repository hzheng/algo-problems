import org.junit.Test;
import static org.junit.Assert.*;

// The count-and-say sequence is the sequence of integers beginning as follows:
// 1, 11, 21, 1211, 111221, ...
public class CountAndSay {
    // beats 84.47%
    public String countAndSay(int n) {
        if (n == 0) return "";

        if (n == 1) return "1";

        StringBuilder sb = new StringBuilder();
        char[] digits = countAndSay(n - 1).toCharArray();
        char lastDigit = digits[0];
        int lastCount = 1;
        // for (char c : countAndSay(n - 1).toCharArray()) {
        for (int i = 1; i < digits.length; i++) {
            char c = digits[i];
            if (c == lastDigit) {
                lastCount++;
            } else {
                sb.append(lastCount).append(lastDigit);
                lastCount = 1;
                lastDigit = c;
            }
        }
        if (lastCount > 0) {
            sb.append(lastCount).append(lastDigit);
        }
        return sb.toString();
    }

    // beats 64.44%
    public String countAndSay2(int n) {
        StringBuilder last = new StringBuilder("1");
        while (--n > 0) {
            StringBuilder sb = new StringBuilder();
            int lastCount = 1;
            char lastDigit = last.charAt(0);
            for (int j = 1; j < last.length(); ++j) {
                char c = last.charAt(j);
                if (c == lastDigit) {
                    lastCount++;
                } else {
                    sb.append(lastCount).append(lastDigit);
                    lastCount = 1;
                    lastDigit = c;
                }
            }
            if (lastCount > 0) {
                sb.append(lastCount).append(lastDigit);
            }
            last = sb;
        }
        return last.toString();
    }

    void test(String... expected) {
        int n = expected.length;
        for (int i = 1; i <= n; i++) {
            assertEquals(expected[i - 1], countAndSay(i));
            assertEquals(expected[i - 1], countAndSay2(i));
        }
    }

    @Test
    public void test1() {
        test("1", "11", "21", "1211", "111221", "312211", "13112221");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CountAndSay");
    }
}
