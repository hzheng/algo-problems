import org.junit.Test;
import static org.junit.Assert.*;

// LC233: https://leetcode.com/problems/number-of-digit-one/
//
// Given an integer n, count the total number of digit 1 appearing in all
// non-negative integers less than or equal to n.
public class CountDigitOne {
    // Recursion
    // beats 20.14%(0 ms)
    public int countDigitOne(int n) {
        if (n < 1) return 0;
        if (n < 10) return 1;

        int digits = 0;
        int firstDigit = 0;
        int power = 1;
        for (int i = n;; i /= 10, power *= 10) {
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

    // beats 20.14%(0 ms)
    public int countDigitOne2(int n) {
        int digits = 1;
        int power = 1;
        for (int i = n; i >= 10; i /= 10, power *= 10, digits++) {}
        int total = 0;
        for (int i = n; i > 0; power /= 10, digits--) {
            int firstDigit = i / power;
            if (firstDigit == 0) continue;

            total += (digits - 1) * (power / 10) * firstDigit;
            i %= power;
            total += (firstDigit > 1) ? power : i + 1;
        }
        return total;
    }

    // https://discuss.leetcode.com/topic/18054/4-lines-o-log-n-c-java-python
    // beats 20.14%(0 ms)
    public int countDigitOne3(int n) {
        int count = 0;
        for (long power = 1; power <= n; power *= 10) {
            long left = n / power; // left part 1's(split by power)
            count += (left + 8) / 10 * power; // full streak
            if (left % 10 == 1) { // partial streak
                count += n % power + 1;
            }
        }
        return count;
    }

    // Recursion
    // beats 20.14%(0 ms)
    public int countDigitOne4(int n) {
        if (n < 1) return 0;

        int power = 1;
        for (int i = n; i >= 10; i /= 10, power *= 10) {}
        int firstDigit = n / power;
        int count = firstDigit * countDigitOne4(power - 1) + countDigitOne4(n % power);
        return count += (firstDigit > 1) ? power : (n % power) + 1;
    }

    // Solution of Choice(almost identical to countDigitOne3)
    // beats 14.53%(0 ms for 40 tests)
    public int countDigitOne5(int n) {
        int count = 0;
        for (int i = n, power = 1; i > 0; power *= 10) {
            int lastDigit = i % 10;
            count += (i /= 10) * power;
            if (lastDigit > 1) {
                count += power;
            } else if (lastDigit == 1) {
                count += n % power + 1;
            }
        }
        return count;
    }

    void test(int n, int expected) {
        assertEquals(expected, countDigitOne(n));
        assertEquals(expected, countDigitOne2(n));
        assertEquals(expected, countDigitOne3(n));
        assertEquals(expected, countDigitOne4(n));
        assertEquals(expected, countDigitOne5(n));
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
        test(500, 200);
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
