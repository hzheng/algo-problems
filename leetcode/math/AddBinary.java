import org.junit.Test;
import static org.junit.Assert.*;

// LC067: https://leetcode.com/problems/add-binary/
//
// Given two binary strings, return their sum (also a binary string).
public class AddBinary {
    // beats 96.11%(2 ms)
    public String addBinary(String a, String b) {
        boolean carry = false;
        int len1 = a.length();
        int len2 = b.length();
        int len = Math.max(len1, len2) + 1;
        char[] res = new char[len];
        for (int i = len1 - 1, j = len2 - 1, k = len - 1;
             k > 0; i--, j--, k--) {
            char d1 = (i >= 0) ? a.charAt(i) : '0';
            char d2 = (j >= 0) ? b.charAt(j) : '0';
            res[k] = ((d1 == d2) ^ carry) ? '0' : '1';
            if (carry) {
                carry = (d1 == '1') || (d2 == '1');
            } else {
                carry = (d1 == '1') && (d2 == '1');
            }
        }
        if (carry) {
            res[0] = '1';
            return new String(res);
        }
        return new String(res, 1, len - 1);
    }

    // Solution of Choice
    // beats 84.38%(3 ms)
    public String addBinary2(String a, String b) {
        StringBuilder sb = new StringBuilder();
        for (int i = a.length() - 1, j = b.length() - 1, sum = 0;
             i >= 0 || j >= 0 || sum == 1; sum >>= 1) {
            sum += i >= 0 ? a.charAt(i--) - '0' : 0;
            sum += j >= 0 ? b.charAt(j--) - '0' : 0;
            sb.append((sum & 1) == 0 ? "0" : "1");
        }
        return sb.reverse().toString();
    }

    void test(String a, String b, String expected) {
        assertEquals(expected, addBinary(a, b));
        assertEquals(expected, addBinary2(a, b));
    }

    @Test
    public void test1() {
        test("10", "11", "101");
        test("10", "1", "11");
        test("11", "1", "100");
        test("10", "0", "10");
        test("0", "10000", "10000");
        test("1", "10000", "10001");
        test("11", "11111", "100010");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("AddBinary");
    }
}
