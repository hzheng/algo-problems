import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a non-negative number represented as an array of digits, plus 1 to it.
public class PlusOne {
    // beats 41.14%
    public int[] plusOne(int[] digits) {
        int n = digits.length;
        int[] res = new int[n];
        int sum = digits[n - 1] + 1;
        int carry = sum / 10;
        res[n - 1] = sum % 10;
        for (int i = n - 2; i >= 0; i--) {
            sum = digits[i] + carry;
            res[i] = sum % 10;
            carry = sum / 10;
        }
        if (carry == 0) return res;

        res = new int[n + 1];
        for (int i = n - 1; i > 0; i--) {
            res[i] = 0;
        }
        res[0] = 1;
        return res;
    }

    // beats 41.14%
    public int[] plusOne2(int[] digits) {
        int n = digits.length;
        int carry = 1;
        for (int i = n - 1; i >= 0 && carry > 0; i--) {
            int sum = digits[i] + carry;
            digits[i] = sum % 10;
            carry = sum / 10;
        }
        if (carry == 0) return digits;

        int[] res = new int[n + 1];
        for (int i = n - 1; i > 0; i--) {
            res[i] = 0;
        }
        res[0] = 1;
        return res;
    }


    void test(int[] digits, int ... expected) {
        assertArrayEquals(expected, plusOne(digits));
        assertArrayEquals(expected, plusOne2(digits));
    }

    @Test
    public void test1() {
        test(new int[] {1, 2}, 1, 3);
        test(new int[] {1, 2}, 1, 3);
        test(new int[] {1}, 2);
        test(new int[] {0}, 1);
        test(new int[] {9, 9}, 1, 0, 0);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PlusOne");
    }
}
