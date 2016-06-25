import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/excel-sheet-column-title/
//
// Given a positive integer, return its corresponding column title as appear in
// an Excel sheet.
public class ExcelColTitle {
    // beats 7.23%
    public String convertToTitle(int n) {
        if (n < 1) return "";

        String title = "";
        int radix = 26;
        for (int x = n; x > 0; x /= radix) {
            title = "" + (char)('A' + (--x % radix)) + title;
        }
        return title;
    }

    public String convertToTitle2(int n) {
        if (n < 1) return "";

        int radix = 26;
        return convertToTitle2(--n / radix) + (char)(n % radix + 'A');
    }

    void test(int n, String expected) {
        assertEquals(expected, convertToTitle(n));
        assertEquals(expected, convertToTitle2(n));
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
