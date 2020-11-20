import java.util.*;
import java.util.stream.IntStream;

import org.junit.Test;

import static org.junit.Assert.*;

// LC762: https://leetcode.com/problems/prime-number-of-set-bits-in-binary-representation/
//
// Given two integers L and R, find the count of numbers in the range [L, R] (inclusive) having a
// prime number of set bits in their binary representation.
//
// Note:
// L, R will be integers L <= R in the range [1, 10^6].
// R - L will be at most 10000.
public class CountPrimeSetBits {
    // time complexity: O((R-L)*log(R-L)), space complexity: O(1)
    // 5 ms(75.37%), 36 MB(48.82%) for 200 tests
    public int countPrimeSetBits(int L, int R) {
        int res = 0;
        for (int x = L; x <= R; x++) {
            res += isPrime(Integer.bitCount(x)) ? 1 : 0;
        }
        return res;
    }

    private boolean isPrime(int n) {
        if (n == 1) { return false; }

        for (int x = 2; x * x <= n; x++) {
            if (n % x == 0) { return false; }
        }
        return true;
    }

    // time complexity: O((R-L)*log(R-L)), space complexity: O(1)
    // 4 ms(83.30%), 35.9 MB(48.82%) for 200 tests
    public int countPrimeSetBits2(int L, int R) {
        int res = 0;
        for (int x = L; x <= R; x++) {
            res += isPrime2(Integer.bitCount(x)) ? 1 : 0;
        }
        return res;
    }

    private boolean isPrime2(int x) {
        return (x == 2 || x == 3 || x == 5 || x == 7 || x == 11 || x == 13 || x == 17 || x == 19);
    }

    // Bit Manipulation + Set
    // time complexity: O((R-L)*log(R-L)), space complexity: O(1)
    // 12 ms(48.18%), 35.8 MB(74.09%) for 200 tests
    public int countPrimeSetBits3(int L, int R) {
        Set<Integer> primes = new HashSet<>(Arrays.asList(2, 3, 5, 7, 11, 13, 17, 19));
        int res = 0;
        for (int x = L; x <= R; x++) {
            int bits = 0;
            for (int n = x; n > 0; n >>= 1) {
                bits += (n & 1);
            }
            res += primes.contains(bits) ? 1 : 0;
        }
        return res;
    }

    // time complexity: O((R-L)*log(R-L)), space complexity: O(1)
    // 7 ms(68.09%), 35.8 MB(60.60%) for 200 tests
    public int countPrimeSetBits4(int L, int R) {
        return IntStream.range(L, R + 1).map(i -> 0b10100010100010101100 >> Integer.bitCount(i) & 1)
                        .sum();
    }

    // TODO: Pascal's Triangle

    private void test(int L, int R, int expected) {
        assertEquals(expected, countPrimeSetBits(L, R));
        assertEquals(expected, countPrimeSetBits2(L, R));
        assertEquals(expected, countPrimeSetBits3(L, R));
        assertEquals(expected, countPrimeSetBits4(L, R));
    }

    @Test public void test() {
        test(244, 269, 16);
        test(6, 10, 4);
        test(10, 15, 5);
        test(842, 888, 23);
        test(842, 888888, 282812);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
