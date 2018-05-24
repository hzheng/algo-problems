import org.junit.Test;
import static org.junit.Assert.*;

// LC829: https://leetcode.com/problems/consecutive-numbers-sum/
//
// Given a positive integer N, how many ways can we write it as a sum of
// consecutive positive integers?
public class ConsecutiveNumbersSum {
    // time complexity: O(N ^ 1/2), space complexity: O(1)
    // beats %(20 ms for 170 tests)
    public int consecutiveNumbersSum(int N) {
        int res = 0;
        for (int n = 2 * N, k = (int) Math.sqrt(n); k > 0; k--) {
            if ((n % k == 0) && ((n / k - k) % 2 == 1)) {
                res++;
            }
        }
        return res;
    }

    // time complexity: O(N ^ 1/2), space complexity: O(1)
    // beats %(23 ms for 170 tests)
    public int consecutiveNumbersSum2(int N) {
        int res = 0;
        for (int k = (int)Math.sqrt(2 * N); k > 0; k--) {
            if ((N - (k * (k - 1) / 2)) % k == 0) {
                res++;
            }
        }
        return res;
    }

    // time complexity: O(N ^ 1/2), space complexity: O(1)
    // beats %(21 ms for 170 tests)
    public int consecutiveNumbersSum3(int N) {
        int res = 0;
        for (int k = 1, n = N - 1; n >= 0; n -= ++k) {
            if ((n % k) == 0) {
                res++;
            }
        }
        return res;
    }

    // time complexity: O(log(N)), space complexity: O(1)
    // beats %(14 ms for 170 tests)
    public int consecutiveNumbersSum4(int N) {
        // if x > k, N = (x - k) + ... + x + ... + (x + k)
        // else N = (k - x + 1) + ... + k + (k + 1) + ... + (k + x)
        // problem reduced to counting all possible odd factors of N
        int res = 1;
        for (; (N & 1) == 0; N /= 2) {}
        for (int i = 3, count; i * i <= N; i += 2) {
            for (count = 0; N % i == 0; N /= i, count++) {}
            res *= (count + 1);
        }
        return (N == 1) ? res : res * 2;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats %(1000 ms for 170 tests)
    public int consecutiveNumbersSum5(int N) {
        int res = 0;
        for (int k = 1; k <= 2 * N; k++)
            if (2 * N % k == 0) {
                int m = 2 * N / k - k;
                if (m <= 0) break;

                res += m & 1;
            }
        return res;
    }

    void test(int N, int expected) {
        assertEquals(expected, consecutiveNumbersSum(N));
        assertEquals(expected, consecutiveNumbersSum2(N));
        assertEquals(expected, consecutiveNumbersSum3(N));
        assertEquals(expected, consecutiveNumbersSum4(N));
        assertEquals(expected, consecutiveNumbersSum5(N));
    }

    @Test
    public void test() {
        test(2, 1);
        test(5, 2);
        test(9, 3);
        test(15, 4);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
