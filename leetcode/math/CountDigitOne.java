import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/number-of-digit-one/
//
// Given an integer n, count the total number of digit 1 appearing in all
// non-negative integers less than or equal to n.
public class CountDigitOne {
    // beats 20.14%
    public int countDigitOne(int n) {
        if (n < 1) return 0;
        if (n < 10) return 1;

        int digits = 0;
        int firstDigit = 0;
        int power = 1;
        for (int i = n; ; i /= 10, power *= 10) {
            digits++;
            if (i < 10) {
                firstDigit = i;
                break;
            }
        }
        // from 0 to 10 ^ (digits - 1) (each decimal position has 1 every one of ten)
        int count = (digits - 1) * (power / 10);
        // from 0 to firstDigit * 10 ^ (digits - 1) except the highest 1's
        count *= firstDigit;
        // from 0 to firstDigit * 10 ^ (digits - 1)
        if (firstDigit > 1) {
            count += power;
        } else {
            count += (n % power) + 1;
        }
        return count + countDigitOne(n % power);
    }

    void test(int n, int expected) {
        assertEquals(expected, countDigitOne(n));
    }

    @Test
    public void test1() {
        test(1410065408, 1737167499);
        test(141006540, 159616096);
        test(1, 1);
        test(10, 2);
        test(11, 4);
        test(13, 6);
        test(20, 12);
        test(21, 13);
        test(129, 63);
        test(214748364, 275627942);
        test(2147483647, -1323939513); // overflow, which is correct
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CountDigitOne");
    }
}
