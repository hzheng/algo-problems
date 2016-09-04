import org.junit.Test;
import static org.junit.Assert.*;

// LC006: https://leetcode.com/problems/zigzag-conversion/
public class ZigZag {
    // Solution of Choice
    // beats 91.35%(6 ms)
    public String convert(String s, int numRows) {
        int len = s.length();
        if (len == 0 || numRows <= 1) return s;

        char[] t = new char[len];
        final int module = 2 * numRows - 2;
        int index = 0;
        for (int row = 0; row < numRows; row++) {
            for (int i = row; i < len; i += module) {
                t[index++] = s.charAt(i);
                if (row > 0 && row < (numRows - 1)) {
                    int j = i + module - 2 * row;
                    if (j < len) {
                        t[index++] = s.charAt(j);
                    }
                }
            }
        }
        return String.valueOf(t);
    }

    void test(String s, int row, String expected) {
        assertEquals(expected, convert(s, row));
    }

    @Test
    public void test1() {
        test("PAYPALISHIRING", 3, "PAHNAPLSIIGYIR");
        test("abcde", 4, "abced");
        test("abcdefghijklmnop", 4, "agmbfhlnceikodjp");
        test("abcdefghijklmnopqrstuvwxyz", 4, "agmsybfhlnrtxzceikoquwdjpv");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ZigZag");
    }
}
