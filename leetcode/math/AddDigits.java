import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/add-digits/
//
// Given a non-negative integer num, repeatedly add all its digits until the
// result has only one digit.
// Follow up:
// Could you do it without any loop/recursion in O(1) runtime?
public class AddDigits {
    // beats 10.69%(3 ms)
    public int addDigits(int num) {
        int sum = num;
        do {
            sum = addDigitsOnce(sum);
        } while (sum > 9);
        return sum;
    }

    private int addDigitsOnce(int num) {
        int sum = 0;
        for (int n = num; n > 0; n /= 10) {
            sum += n % 10;
        }
        return sum;
    }

    // time complexity: O(1)
    // beats 23.69%(2 ms)
    public int addDigits2(int num) {
        int mod = num % 9;
        return (num > 0 && mod == 0) ? 9 : mod;
    }

    // https://en.wikipedia.org/wiki/Digital_root
    // time complexity: O(1)
    // beats 10.69%(3 ms)
    public int addDigits3(int num) {
        return num - (num - 1) / 9 * 9;
    }

    // beats 10.69%(3 ms)
    public int addDigits4(int num) {
        return (num - 1) % 9 + 1;
    }

    void test(int x, int expected) {
        assertEquals(expected, addDigits(x));
        assertEquals(expected, addDigits2(x));
        assertEquals(expected, addDigits3(x));
        assertEquals(expected, addDigits4(x));
    }

    @Test
    public void test1() {
        test(0, 0);
        test(8, 8);
        test(10, 1);
        test(38, 2);
        test(358, 7);
        test(189, 9);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("AddDigits");
    }
}
