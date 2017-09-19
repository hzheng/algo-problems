import org.junit.Test;
import static org.junit.Assert.*;

// LC171: https://leetcode.com/problems/excel-sheet-column-number/
//
// Given a column title in an Excel sheet, return its corresponding column number.
public class ExcelColNum {
    // Solution of Choice
    // beats 73.83%(2 ms)
    public int titleToNumber(String s) {
        final int radix = 26;
        int n = 0;
        for (char c : s.toCharArray()) {
            n *= radix;
            n += (c - 'A' + 1);
        }
        return n;
    }

    void test(int expected, String s) {
        assertEquals(expected, titleToNumber(s));
    }

    @Test
    public void test1() {
        test(1, "A");
        test(3, "C");
        test(26, "Z");
        test(27, "AA");
        test(28, "AB");
        test(52, "AZ");
        test(100, "CV");
        test(1000, "ALL");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ExcelColNum");
    }
}
