import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/integer-to-english-words/
//
// Convert a non-negative integer to its english words representation.
public class NumberToWords {
    // beats 26.07%(2 ms)
    String[] digits = {"Zero", "One", "Two", "Three", "Four",
                       "Five", "Six", "Seven", "Eight", "Nine"};
    String[] teens = {"Ten", "Eleven", "Twelve", "Thirteen", "Fourteen",
                      "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
    String[] tens = {"Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy",
                     "Eighty", "Ninety"};
    String[] power10s = {"Hundred", "Thousand", "Million", "Billion"};

    public String numberToWords(int num) {
        if (num == 0) return digits[0];

        StringBuilder sb = new StringBuilder();
        for (int i = num, thousandth = 0; i > 0; i /= 1000, thousandth++) {
            int remainder = i % 1000;
            if (remainder == 0) continue;

            String word = lessThanThousand(remainder);
            if (thousandth > 0) {
                word += " " + power10s[thousandth];
                if (sb.length() > 0) {
                    word += " ";
                }
            }
            sb.insert(0, word);
        }
        return sb.toString();
    }

    private String lessThanThousand(int n) {
        if (n < 10) return digits[n];

        if (n < 20) return teens[n - 10];

        String res;
        int remainder;
        if (n < 100) {
            res = tens[n / 10 - 2];
            remainder = n % 10;
        } else {
            res = digits[n / 100] + " " + power10s[0];
            remainder = n % 100;
        }
        return (remainder == 0) ? res : res + " " + lessThanThousand(remainder);
    }

    void test(int num, String expected) {
        assertEquals(expected, numberToWords(num));
    }

    @Test
    public void test1() {
        test(0, "Zero");
        test(1, "One");
        test(10, "Ten");
        test(123, "One Hundred Twenty Three");
        test(313, "Three Hundred Thirteen");
        test(12345, "Twelve Thousand Three Hundred Forty Five");
        test(4000, "Four Thousand");
        test(50000, "Fifty Thousand");
        test(10000, "Ten Thousand");
        test(100000, "One Hundred Thousand");
        test(1000000, "One Million");
        test(10000000, "Ten Million");
        test(100000000, "One Hundred Million");
        test(1000000000, "One Billion");
        test(1000013000, "One Billion Thirteen Thousand");
        test(1002000, "One Million Two Thousand");
        test(1234567, "One Million Two Hundred Thirty Four Thousand "
             + "Five Hundred Sixty Seven");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("NumberToWords");
    }
}
