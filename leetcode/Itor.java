import org.junit.Test;
import static org.junit.Assert.*;

// Given an integer, convert it to a roman numeral.
// Input is guaranteed to be within the range from 1 to 3999.
public class Itor {
    private static final int[] ROMAN_VALUES = {1000, 500, 100, 50, 10, 5, 1};
    private static final char[] ROMAN_LETTERS = {'M', 'D', 'C', 'L', 'X', 'V', 'I'};

    // beats 68.22%
    public String intToRoman(int num) {
        if (num <= 0) return "";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ROMAN_LETTERS.length; i += 2) {
            int unit = ROMAN_VALUES[i];
            int digit = num / unit;
            if (digit == 9) {
                sb.append(ROMAN_LETTERS[i]);
                sb.append(ROMAN_LETTERS[i - 2]);
            } else if (digit >= 5) {
                sb.append(ROMAN_LETTERS[i - 1]);
                for (int j = digit - 5; j > 0; --j) {
                    sb.append(ROMAN_LETTERS[i]);
                }
            } else if (digit == 4) {
                sb.append(ROMAN_LETTERS[i]);
                sb.append(ROMAN_LETTERS[i - 1]);
            } else if (digit > 0) {
                for (int j = digit; j > 0; --j) {
                    sb.append(ROMAN_LETTERS[i]);
                }
            }
            num -= digit * ROMAN_VALUES[i];
        }
        return sb.toString();
    }

    void test(int x, String expected) {
        assertEquals(expected, intToRoman(x));
    }

    @Test
    public void test1() {
        test(1, "I");
        test(5, "V");
        test(10, "X");
        test(50, "L");
        test(100, "C");
        test(500, "D");
        test(1000, "M");
        test(40, "XL");
        test(400, "CD");
        test(900, "CM");
        test(2014, "MMXIV");
        test(1990, "MCMXC");
        test(1954, "MCMLIV");
        test(114, "CXIV");
        test(101, "CI");
        test(1001, "MI");
        test(999, "CMXCIX");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Itor");
    }
}
