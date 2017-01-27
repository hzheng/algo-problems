import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC168: https://leetcode.com/problems/excel-sheet-column-title/
//
// Given a positive integer, return its corresponding column title as appear in
// an Excel sheet.
public class ExcelColTitle {
    // beats 12.78%(0 ms for 18 tests)
    public String convertToTitle(int n) {
        StringBuilder title = new StringBuilder();
        final int radix = 26;
        for (int x = n - 1; x >= 0; x /= radix, x--) {
            title.insert(0, (char)('A' + (x % radix)));
        }
        return title.toString();
    }

    // Solution of Choice
    // beats 9.21%(0 ms for 18 tests)
    public String convertToTitle2(int n) {
        StringBuilder title = new StringBuilder();
        final int radix = 26;
        for (int x = n - 1; x >= 0; x /= radix, x--) {
            title.append((char)('A' + (x % radix)));
        }
        return title.reverse().toString();
    }

    // Recursion
    // beats 12.78%(0 ms for 18 tests)
    public String convertToTitle3(int n) {
        if (n < 1) return "";

        final int radix = 26;
        return convertToTitle2(--n / radix) + (char)(n % radix + 'A');
    }

    void test(int n, String expected) {
        assertEquals(expected, convertToTitle(n));
        assertEquals(expected, convertToTitle2(n));
        assertEquals(expected, convertToTitle3(n));
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
        org.junit.runner.JUnitCore.main("ExcelColTitle");
    }
}
