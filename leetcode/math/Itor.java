import org.junit.Test;
import static org.junit.Assert.*;

// LC012: https://leetcode.com/problems/integer-to-roman/
//
// Given an integer, convert it to a roman numeral.
// Input is guaranteed to be within the range from 1 to 3999.
public class Itor {
    private static final int[] ROMAN_VALUES = {1000, 500, 100, 50, 10, 5, 1};
    private static final char[] ROMAN_LETTERS = {'M', 'D', 'C', 'L', 'X', 'V', 'I'};

    // beats 74.73%(8 ms)
    public String intToRoman(int num) {
        if (num <= 0) return "";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ROMAN_LETTERS.length; i += 2) {
            int unit = ROMAN_VALUES[i];
            int digit = num / unit;
            num -= digit * unit;
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
        }
        return sb.toString();
    }

    // Solution of Choice
    // https://discuss.leetcode.com/topic/12384/simple-solution
    // beats 97.85%(6 ms)
    private static final String M[] = {"", "M", "MM", "MMM"};
    private static final String C[] = {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"};
    private static final String X[] = {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"};
    private static final String I[] = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"};

    public String intToRoman2(int num) {
        return M[num / 1000] + C[(num % 1000) / 100] + X[(num % 100) / 10] + I[num % 10];
    }

    // https://discuss.leetcode.com/topic/26543/easy-to-understand-java-solution
    // beats 21.40%(15 ms)
    private static enum Unit {
        M(1000), CM(900), D(500), CD(400), C(100),
        XC(90), L(50), XL(40), X(10), IX(9), V(5), IV(4), I(1);

        private final int value;

        Unit(int value) {
            this.value = value;
        }
    };

    public String intToRoman3(int num) {
        StringBuilder sb = new StringBuilder();
        for (Unit unit : Unit.values()) {
            for (; num >= unit.value; num -= unit.value) {
                sb.append(unit);
            }
        }
        return sb.toString();
    }

    void test(int x, String expected) {
        assertEquals(expected, intToRoman(x));
        assertEquals(expected, intToRoman2(x));
        assertEquals(expected, intToRoman3(x));
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
        test(94, "XCIV");
        test(114, "CXIV");
        test(101, "CI");
        test(1001, "MI");
        test(999, "CMXCIX");
        test(2999, "MMCMXCIX");
        test(3999, "MMMCMXCIX");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Itor");
    }
}
