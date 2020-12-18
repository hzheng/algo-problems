import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1006: https://leetcode.com/problems/clumsy-factorial/
//
// Normally, the factorial of a positive integer n is the product of all positive integers less than
// or equal to n. We instead make a clumsy factorial: using the integers in decreasing order, we
// swap out the multiply operations for a fixed rotation of operations: multiply (*), divide (/),
// add (+) and subtract (-) in this order.
// For example, clumsy(10) = 10 * 9 / 8 + 7 - 6 * 5 / 4 + 3 - 2 * 1. However, these operations are
// still applied using the usual order of operations of arithmetic: we do all multiplication and
// division steps before any addition or subtraction steps, and multiplication and division steps
// are processed left to right. Additionally, the division that we use is floor division such that
// 10 * 9 / 8 equals 11.  This guarantees the result is an integer.
// Implement the clumsy function as defined above: returns the clumsy factorial of N.
//
// Note:
// 1 <= N <= 10000
// -2^31 <= answer <= 2^31 - 1  (The answer is guaranteed to fit within a 32-bit integer.)
public class Clumsy {
    // Math
    // time complexity: O(1), space complexity: O(1)
    // 0 ms(100.00%), 35.7 MB(73.05%) for 84 tests
    public int clumsy(int N) {
        // e.g. (n+2)*(n+1)/n+(n-1)-(n-2)*(n-3)/(n-4)+(n-5)-(n-6)*(n-7)
        int firstPositiveProduct = N + 1; // (n+2)*(n+1)/n in e.g.
        int secondProduct = N - 3; // (n-2)*(n-3)/(n-4) in e.g.
        int sumOfNegProducts = 0;
        if (N >= 7) {
            int lastProduct = firstPositiveProduct - 4 * (N / 4 - ((N % 4 == 3) ? 0 : 1));
            sumOfNegProducts = (secondProduct + lastProduct) * ((N - 7) / 4 + 1) / 2;
        }
        int res = firstPositiveProduct - sumOfNegProducts;
        int firstSum = N - 3; // (n-1) in e.g.
        int lastSum = (N + 1) % 4; // (n-5) in e.g.
        res += (firstSum + lastSum) * (firstSum / 4 + 1) / 2;
        switch (N % 4) {
        case 0:
            res += (N >= 7) ? -1 : 1; // extra 2/n in (n+2)(n+1)/n=n+3+2/n
            break;
        case 3:
            res += (N >= 7) ? -2 : 2; // extra 2/n in (n+2)(n+1)/n=n+3+2/n
            break;
        default:
            res -= N % 4; // last term: -1 or -2*1
        }
        return res;
    }

    // Math
    // time complexity: O(1), space complexity: O(1)
    // 0 ms(100.00%), 35.7 MB(73.05%) for 84 tests
    public int clumsy2(int N) {
        // e.g. (n+2)*(n+1)/n+(n-1)-(n-2)*(n-3)/(n-4)+(n-5)-(n-6)*(n-7)/(n-8)+...
        // (n+3)+[(n-1)-(n-1)]+[(n-5)-(n-5)]+...
        if (N <= 2) { return N; }
        if (N <= 4) { return N + 3; }

        if (N % 4 == 0) { return N + 1; }

        return (N % 4 == 3) ? N - 1 : N + 2;
    }

    // Recursion
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 35.7 MB(73.05%) for 84 tests
    public int clumsy3(int N) {
        return (N <= 2) ? N : N * (N - 1) / (N - 2) + calc(N - 3);
    }

    private int calc(int N) {
        if (N == 0) { return 0; }
        if (N < 4) { return 1; }
        return N - (N - 1) * (N - 2) / (N - 3) + calc(N - 4);
    }

    // Stack
    // time complexity: O(N), space complexity: O(N)
    // 9 ms(16.31%), 38.3 MB(19.15%) for 84 tests
    public int clumsy4(int N) {
        final char[] operator = new char[]{ '*', '/', '+', '-' };
        Stack<Integer> stack = new Stack<>();
        stack.push(N);
        for (int n = N - 1, index = 0; n > 0; n--, index = ++index % 4) {
            switch (operator[index]) {
            case '*':
                stack.push(stack.pop() * n);
                break;
            case '/':
                stack.push(stack.pop() / n);
                break;
            case '+':
                stack.push(n);
                break;
            case '-':
                stack.push(-n);
                break;
            }
        }
        int res = 0;
        while (!stack.isEmpty()) {
            res += stack.pop();
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // 2 ms(46.21%), 35.9 MB(62.76%) for 84 tests
    public int clumsy5(int N) {
        int res = 0;
        for (int i = 0; i * 4 < N; i++) {
            res += (i == 0 ? 1 : -1) * product(N - i * 4);
            res += Math.max(N - i * 4 - 3, 0);
        }
        return res;
    }

    private int product(int n) {
        return n <= 2 ? n : n * (n - 1) / (n - 2);
    }

    private void test(int N, int expected) {
        assertEquals(expected, clumsy(N));
        assertEquals(expected, clumsy2(N));
        assertEquals(expected, clumsy3(N));
        assertEquals(expected, clumsy4(N));
        assertEquals(expected, clumsy5(N));
    }

    @Test public void test() {
        test(1, 1);
        test(2, 2);
        test(3, 6);
        test(4, 7);
        test(5, 7);
        test(6, 8);
        test(7, 6);
        test(8, 9);
        test(9, 11);
        test(10, 12);
        test(11, 10);
        test(12, 13);
        test(13, 15);
        test(14, 16);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
