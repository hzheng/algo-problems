import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC786: https://leetcode.com/problems/k-th-smallest-prime-fraction/
//
// A sorted list A contains 1, plus some number of primes. For every p < q in
// the list, we consider the fraction p/q. What is the K-th smallest fraction
// considered?  Return your answer, where answer[0] = p and answer[1] = q.
public class KthSmallestPrimeFraction {
    // Binary Search
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 77.38%(20 ms for 62 tests)
    public int[] kthSmallestPrimeFraction(int[] A, int K) {
        int n = A.length;
        double EPSILON = 1e-8;
        double[] B = new double[n];
        for (int i = 0; i < n; i++) {
            B[i] = A[i];
        }
        double low = 0;
        for (double high = 1; low + EPSILON < high; ) {
            double mid = (low + high) / 2;
            if (count(B, mid) < K) {
                low = mid;
            } else {
                high = mid;
            }
        }
        for (int a : A) {
            int x = (int) (a / low);
            int i = Arrays.binarySearch(A, x);
            if (i < 0 || Math.abs((double) a / A[i] - low) > EPSILON) continue;

            return new int[] { a, x };
        }
        return new int[] { 0, 0 };
    }

    private int count(double[] A, double max) {
        int res = 0;
        for (double a : A) {
            res += A.length + Arrays.binarySearch(A, a / max) + 1;
        }
        return res;
    }

    // Binary Search
    // time complexity: O(N), space complexity: O(N)
    // beats 82.54%(12 ms for 62 tests)
    public int[] kthSmallestPrimeFraction2(int[] A, int K) {
        double EPSILON = 1e-8;
        int[] res = new int[2];
        for (double low = 0, high = 1; low + EPSILON < high; ) {
            double mid = (low + high) / 2;
            if (check(mid, A, K, res)) {
                low = mid;
            } else {
                high = mid;
            }
        }
        return res;
    }

    private boolean check(double mid, int[] A, int K, int[] res) {
        int p = 0;
        int q = 0;
        int n = A.length;
        int total = 0;
        for (int i = 0, j = 0; i < n; i++) {
            for (; j < n; j++) {
                if (i < j && A[i] < A[j] * mid) {
                    if (p == 0 || p * A[j] < A[i] * q) {
                        p = A[i];
                        q = A[j];
                    }
                    break;
                }
            }
            total += n - j;
        }
        if (total > K) return false;

        if (res[0] == 0 || res[0] * q < p * res[1]) {
            res[0] = p;
            res[1] = q;
        }
        return true;
    }

    // Heap
    // time complexity: O(K * log(N)), space complexity: O(N)
    // beats 34.13%(747 ms for 62 tests)
    public int[] kthSmallestPrimeFraction3(int[] A, int K) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return A[a[0]] * A[b[1]] - A[b[0]] * A[a[1]];
            }
        });
        int n = A.length;
        for (int i = 0; i < n - 1; i++) {
            pq.offer(new int[] { i, n - 1 });
        }
        for (int i = 0; i < K - 1; i++) { // mutiway merge
            int[] first = pq.poll();
            if (--first[1] > first[0]) {
                pq.offer(first);
            }
        }
        int[] first = pq.peek();
        return new int[] { A[first[0]], A[first[1]] };
    }

    void test(int[] A, int K, int[] expected) {
        assertArrayEquals(expected, kthSmallestPrimeFraction(A, K));
        assertArrayEquals(expected, kthSmallestPrimeFraction2(A, K));
        assertArrayEquals(expected, kthSmallestPrimeFraction3(A, K));
    }

    @Test
    public void test() {
        test(new int[] { 1, 2, 3, 5 }, 3, new int[] { 2, 5 });
        test(new int[] { 1, 7 }, 1, new int[] { 1, 7 });
        test(new int[] { 1, 19, 71, 107, 307, 367, 419, 1009, 1153, 1297, 1373,
                         1693, 1931, 2389, 2609, 2731, 2917,
                         3461, 3613, 3677, 4001, 4013, 4201, 4513, 4691, 5323,
                         5333, 5503, 6701, 7283, 7433, 7621, 7673, 8053,
                         8191, 8387, 9043, 9239, 9433, 9923, 10321, 10627,
                         10639, 10723, 11279, 11411, 11779, 11801, 12437,
                         12473, 12703, 13799, 13997, 14051, 14251, 14653, 14683,
                         14759, 14797, 15091, 15149, 15217, 16987, 17467,
                         18253, 18541, 18731, 19051, 19259, 19813, 19963, 20149,
                         20347, 20369, 20879, 20899, 21521, 22079, 22571,
                         22709, 22783, 22859, 23087, 23567, 23593, 24847, 24917,
                         25117, 25601, 25903, 26029, 26407, 26437, 26573,
                         27271, 27803, 27901, 27961, 28307, 29017 }, 4733,
             new int[] { 19259, 20369 });
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
