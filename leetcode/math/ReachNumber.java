import org.junit.Test;

import static org.junit.Assert.*;

// LC754: https://leetcode.com/problems/reach-a-number/
//
// You are standing at position 0 on an infinite number line. There is a goal at position target.
// On each move, you can either go left or right. During the n-th move (starting from 1), you take
// n steps. Return the minimum number of steps required to reach the destination.
//
// Note:
// target will be a non-zero integer in the range [-10^9, 10^9].
public class ReachNumber {
    // Math
    // time complexity: O(TARGET^1/2), space complexity: O(1)
    // 1 ms(88.51%), 35.7 MB(8.11%) for 73 tests
    public int reachNumber(int target) {
        int tgt = Math.abs(target);
        int step = 0;
        for (int sum = 0; sum < tgt || (sum - tgt) % 2 != 0; step++, sum += step) {}
        return step;
    }

    // Math
    // time complexity: O(TARGET^1/2), space complexity: O(1)
    // 1 ms(88.51%), 35.7 MB(8.11%) for 73 tests
    public int reachNumber2(int target) {
        int tgt = Math.abs(target);
        int step = 0;
        for (; tgt > 0; tgt -= ++step) {}
        return (tgt % 2 == 0) ? step : (step + 1 + step % 2);
    }

    // Math
    // time complexity: O(1), space complexity: O(1)
    // 0 ms(100.00%), 35.7 MB(8.11%) for 73 tests
    public int reachNumber3(int target) {
        int tgt = Math.abs(target);
        int step = (int)Math.ceil((-1.0 + Math.sqrt(1 + 8.0 * tgt)) / 2);
        tgt -= step * (step + 1) / 2;
        return (tgt % 2 == 0) ? step : (step + 1 + step % 2);
    }

    private void test(int target, int expected) {
        assertEquals(expected, reachNumber(target));
        assertEquals(expected, reachNumber2(target));
        assertEquals(expected, reachNumber3(target));
    }

    @Test public void test() {
        test(-100, 15);
        test(3, 2);
        test(2, 3);
        test(9, 5);
        test(-9, 5);
        test(100000000, 14143);
        test(999999999, 44721);
        test(-1000000000, 44723);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
