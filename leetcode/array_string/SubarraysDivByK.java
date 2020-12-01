import org.junit.Test;

import static org.junit.Assert.*;

// LC974: https://leetcode.com/problems/subarray-sums-divisible-by-k/
//
// Given an array A of integers, return the number of (contiguous, non-empty) subarrays that have a
// sum divisible by K.
//
// Note:
// 1 <= A.length <= 30000
// -10000 <= A[i] <= 10000
// 2 <= K <= 10000
public class SubarraysDivByK {
    // time complexity: O(N), space complexity: O(K)
    // 5 ms(84.67%), 41.7 MB(85.15%) for 73 tests
    public int subarraysDivByK(int[] A, int K) {
        int[] count = new int[K];
        count[0] = 1;
        int sum = 0;
        for (int a : A) {
            sum += a;
            count[(sum % K + K) % K]++;
        }
        int res = 0;
        for (int c : count) {
            res += c * (c - 1) / 2;
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(K)
    // 5 ms(84.67%), 41.5 MB(88.99%) for 73 tests
    public int subarraysDivByK2(int[] A, int K) {
        int[] count = new int[K];
        count[0] = 1;
        int sum = 0;
        int res = 0;
        for (int a : A) {
            sum = (sum + a % K + K) % K;
            res += count[sum]++;
        }
        return res;
    }

    private void test(int[] A, int K, int expected) {
        assertEquals(expected, subarraysDivByK(A, K));
        assertEquals(expected, subarraysDivByK2(A, K));
    }

    @Test public void test() {
        test(new int[] {-2}, 6, 0);
        test(new int[] {4, 5, 0, -2, -3, 1}, 5, 7);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
