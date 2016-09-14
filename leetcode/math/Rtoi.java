import org.junit.Test;
import static org.junit.Assert.*;

// LC013: https://leetcode.com/problems/roman-to-integer/
//
// Given a roman numeral, convert it to an integer.
// Input is guaranteed to be within the range from 1 to 3999.
public class Rtoi {
    // more rigid on input which doesn't match the accepted answer
    public int romanToInt(String s) {
        if (s == null) return 0;

        int len = s.length();
        if (len == 0) return 0;

        int val = 0;
        int count = 0;
        int lastUnit = 0;
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            int curUnit = getValue(c);
            if (curUnit == 0) continue;

            if (curUnit == lastUnit) {
                if (++count > 4) return 0; // invalid
                continue;
            }

            if (curUnit < lastUnit) {
                val += lastUnit * count;
                count = 1;
            } else if (lastUnit == 0) {
                count = 1;
            } else {
                if (!canPrecede(lastUnit, curUnit)) return 0;
                if (count > 1) return 0;

                val += curUnit - lastUnit * count;
                count = 0;
            }
            lastUnit = curUnit;
        }
        return val + lastUnit * count;
    }

    private static final String ROMAN_LETTERS = "IVXLCDM";
    private static final int[] ROMAN_VALUES = {1, 5, 10, 50, 100, 500, 1000};

    private int getValue(char c) {
        int index = ROMAN_LETTERS.indexOf(c);
        return (index < 0) ? 0 : ROMAN_VALUES[index];
    }

    private boolean canPrecede(int unit1, int unit2) {
        if (unit1 != 1 && unit1 != 10 && unit1 != 100) return false;

        return (unit1 * 5 == unit2) || (unit1 * 10 == unit2);
    }

    // beats 8X.??%(6 ms)
    public int romanToInt2(String s) {
        int[] vals = new int[26];
        vals['I' - 'A'] = 1;
        vals['V' - 'A'] = 5;
        vals['X' - 'A'] = 10;
        vals['L' - 'A'] = 50;
        vals['C' - 'A'] = 100;
        vals['D' - 'A'] = 500;
        vals['M' - 'A'] = 1000;
        int val = 0;
        int len = s.length();

        for (int i = 0; i < len; i++) {
            int cur = vals[s.charAt(i) - 'A'];
            int next = (i == len - 1) ? 0 : vals[s.charAt(i + 1) - 'A'];
            if (cur >= next) {
                val += cur;
            } else {
                val += next - cur;
                ++i;
            }
        }
        return val;
    }

    // Solution of Choice
    // beats 8X.??%(6 ms)
    public int romanToInt3(String s) {
        int[] vals = new int[26];
        vals['I' - 'A'] = 1;
        vals['V' - 'A'] = 5;
        vals['X' - 'A'] = 10;
        vals['L' - 'A'] = 50;
        vals['C' - 'A'] = 100;
        vals['D' - 'A'] = 500;
        vals['M' - 'A'] = 1000;
        int num = 0;
        for (int i = s.length() - 1, last = 0; i >= 0; i--) {
            int cur = vals[s.charAt(i) - 'A'];
            num += (cur < last) ? -cur : cur;
            last = cur;
        }
        return num;
    }

    // beats 82.35%(7 ms)
    public int romanToInt4(String s) {
        int num = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            switch (s.charAt(i)) {
            case 'I':
                num += (num < 5) ? 1 : -1;
                break;
            case 'V':
                num += 5;
                break;
            case 'X':
                num += (num < 50) ? 10 : -10;
                break;
            case 'L':
                num += 50;
                break;
            case 'C':
                num += (num < 500) ? 100 : -100;
                break;
            case 'D':
                num += 500;
                break;
            case 'M':
                num += 1000;
                break;
            }
        }
        return num;
    }

    void test(String x, int expected) {
        assertEquals(expected, romanToInt(x));
        assertEquals(expected, romanToInt2(x));
        assertEquals(expected, romanToInt3(x));
        assertEquals(expected, romanToInt4(x));
    }

    @Test
    public void test1() {
        test("I", 1);
        test("V", 5);
        test("X", 10);
        test("L", 50);
        test("C", 100);
        test("D", 500);
        test("M", 1000);
        test("XL", 40);
        test("CD", 400);
        test("CM", 900);
        test("XCIV", 94);
        test("CXIV", 114);
        test("MMXIV", 2014);
        test("MCMXC", 1990);
        test("MCMLIV", 1954);
        test("MMMCMXCIX", 3999);
    }

    @Test
    public void test2() {
        assertEquals(5, romanToInt2("IIV"));
        assertEquals(999, romanToInt2("IM"));
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Rtoi");
    }
}
