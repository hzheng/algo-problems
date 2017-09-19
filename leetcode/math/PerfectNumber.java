import org.junit.Test;
import static org.junit.Assert.*;

// LC507: https://leetcode.com/problems/perfect-number/
//
// We define the Perfect Number is a positive integer that is equal to the sum of all its positive divisors except itself.
public class PerfectNumber {
    // beats N/A(14 ms for 155 tests)
    // time complexity: O(N ^ 1/2), space complexity: O(1)
    public boolean checkPerfectNumber(int num) {
        if (num <= 1) return false;

        int sum = 1;
        for (int i = 2; i * i <= num; i++) {
            if (num % i == 0) {
                sum += i;
                if (i * i < num) {
                    sum += num / i;
                }
            }
        }
        return sum == num;
    }

    void test(int num, boolean expected) {
        assertEquals(expected, checkPerfectNumber(num));
    }

    @Test
    public void test() {
        test(1, false);
        test(2, false);
        test(38, false);
        test(6, true);
        test(28, true);
        test(496, true);
        test(8128, true);
        test(33550336, true);
        // test(8589869056L, true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PerfectNumber");
    }
}
