import org.junit.Test;
import static org.junit.Assert.*;

// LC273: https://leetcode.com/problems/integer-to-english-words/
//
// Convert a non-negative integer to its english words representation.
public class NumberToWords {
    static final String[] DIGITS = {"Zero", "One", "Two", "Three", "Four",
                                    "Five", "Six", "Seven", "Eight", "Nine"};
    static final String[] TEENS = {"Ten", "Eleven", "Twelve", "Thirteen",
                                   "Fourteen", "Fifteen", "Sixteen",
                                   "Seventeen", "Eighteen", "Nineteen"};
    static final String[] TENS = {"Twenty", "Thirty", "Forty", "Fifty",
                                  "Sixty", "Seventy", "Eighty", "Ninety"};
    static final String[] POWER10S = {"Hundred", "Thousand", "Million", "Billion"};

    // Recursion
    // beats 34.81%(5 ms for 601 tests)
    public String numberToWords(int num) {
        if (num == 0) return DIGITS[0];

        StringBuilder sb = new StringBuilder();
        for (int i = num, thousandth = 0; i > 0; i /= 1000, thousandth++) {
            int remainder = i % 1000;
            if (remainder == 0) continue;

            String word = lessThanThousand(remainder);
            if (thousandth > 0) {
                word += " " + POWER10S[thousandth];
                if (sb.length() > 0) {
                    word += " ";
                }
            }
            sb.insert(0, word);
        }
        return sb.toString();
    }

    private String lessThanThousand(int n) {
        if (n < 10) return DIGITS[n];

        if (n < 20) return TEENS[n - 10];

        String res;
        int remainder;
        if (n < 100) {
            res = TENS[n / 10 - 2];
            remainder = n % 10;
        } else {
            res = DIGITS[n / 100] + " " + POWER10S[0];
            remainder = n % 100;
        }
        return (remainder == 0) ? res : res + " " + lessThanThousand(remainder);
    }

    private final String[] LESS_THAN_20
        = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight",
           "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen",
           "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
    private final String[] TYS = {"", "Ten", "Twenty", "Thirty", "Forty",
                                  "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
    private final String[] THOUSANDS = {"", "Thousand", "Million", "Billion"};

    // Solution of Choice
    // Recursion
    // beats 34.81%(5 ms for 601 tests)
    public String numberToWords2(int num) {
        if (num == 0) return "Zero";

        String words = "";
        for (int i = 0; num > 0; num /= 1000, i++) {
            if (num % 1000 != 0) {
                words = smallNum(num % 1000) + THOUSANDS[i] + " " + words;
            }
        }
        return words.trim();
    }

    private String smallNum(int n) {
        if (n == 0) return "";

        if (n < 20) return LESS_THAN_20[n] + " ";

        if (n < 100) return TYS[n / 10] + " " + smallNum(n % 10);

        return LESS_THAN_20[n / 100] + " Hundred " + smallNum(n % 100);
    }

    void test(int num, String expected) {
        assertEquals(expected, numberToWords(num));
        assertEquals(expected, numberToWords2(num));
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
