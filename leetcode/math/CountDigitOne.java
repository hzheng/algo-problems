import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/number-of-digit-one/
//
// Given an integer n, count the total number of digit 1 appearing in all
// non-negative integers less than or equal to n.
public class CountDigitOne {
    // beats 20.14%(0 ms)
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

    // non-recursion
    // beats 20.14%(0 ms)
    public int countDigitOne2(int n) {
        int digits = 1;
        int power = 1;
        for (int i = n; i >= 10; i /= 10, power *= 10, digits++) ;
        int total = 0;

        for (int i = n; i > 0; power /= 10, digits--) {
            int firstDigit = i / power;
            if (firstDigit == 0) continue;

            int count = (digits - 1) * (power / 10);
            count *= firstDigit;
            i %= power;
            count += (firstDigit > 1) ? power : i + 1;
            total += count;
        }
        return total;
    }


    void test(int n, int expected) {
        assertEquals(expected, countDigitOne(n));
        assertEquals(expected, countDigitOne2(n));
    }

    @Test
    public void test1() {
        test(0, 0);
        test(1, 1);
        test(9, 1);
        test(10, 2);
        test(11, 4);
        test(13, 6);
        test(20, 12);
        test(21, 13);
        test(100, 21);
        test(129, 63);
        test(141006540, 159616096);
        test(1410065408, 1737167499);
        test(214748364, 275627942);
        test(2147483647, -1323939513); // overflow, which is correct
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CountDigitOne");
    }
}
