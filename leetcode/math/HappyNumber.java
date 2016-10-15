import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC202: https://leetcode.com/problems/happy-number/
//
// Write an algorithm to determine if a number is "happy".
// A happy number is a number defined by the following process: Starting with
// any positive integer, replace the number by the sum of the squares of its
// digits, and repeat the process until the number equals 1 (where it will stay),
// or it loops endlessly in a cycle which does not include 1. Those numbers for
// which this process ends in 1 are happy numbers.
public class HappyNumber {
    // Solution of Choice
    // Hash Table
    // beats 68.29%(4 ms for 401 tests)
    public boolean isHappy(int n) {
        Set<Integer> calculated = new HashSet<>();
        for (int i = n; i != 1; i = calculate(i)) {
            if (!calculated.add(i)) return false;
        }
        return true;
    }

    private int calculate(int n) {
        int res = 0;
        for (int x = n; x > 0; x /= 10) {
            int mod = x % 10;
            res += mod * mod;
        }
        return res;
    }

    // Floyd Cycle detection algorithm
    // beats 86.99%(2 ms for 401 tests)
    public boolean isHappy2(int n) {
        int slow = n;
        int fast = n;
        do {
            slow = calculate(slow);
            fast = calculate(calculate(fast));
        } while (slow != fast);
        return slow == 1;
    }

    // https://discuss.leetcode.com/topic/12710/my-java-solution-find-1-or-7-when-happy-sum-is-a-single-digit
    // beats 96.03%(1 ms for 401 tests)
    public boolean isHappy3(int n) {
        for (int sum = n; ; ) {
            sum = calculate(sum);
            if (sum < 10) return sum == 1 || sum == 7;
        }
    }

    void test(int n, boolean expected) {
        assertEquals(expected, isHappy(n));
        assertEquals(expected, isHappy2(n));
        assertEquals(expected, isHappy3(n));
    }

    @Test
    public void test1() {
        test(19, true);
        test(1, true);
        test(1234, false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("HappyNumber");
    }
}
