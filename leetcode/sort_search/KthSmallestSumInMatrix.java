import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1439: https://leetcode.com/problems/find-the-kth-smallest-sum-of-a-matrix-with-sorted-rows/
//
// You are given an m * n matrix, mat, and an integer k, which has its rows sorted in non-decreasing
// order. You are allowed to choose exactly 1 element from each row to form an array. Return the Kth
// smallest array sum among all possible arrays.
// Constraints:
// m == mat.length
// n == mat.length[i]
// 1 <= m, n <= 40
// 1 <= k <= min(200, n ^ m)
// 1 <= mat[i][j] <= 5000
// mat[i] is a non decreasing array.
public class KthSmallestSumInMatrix {
    // Heap
    // time complexity: O(M*N*K*log(K))), space complexity: O(K)
    // 284 ms(60.00%), 39.2 MB(100%) for 71 tests
    public int kthSmallest(int[][] mat, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(k + 1, Collections.reverseOrder());
        pq.offer(0);
        for (int[] row : mat) {
            PriorityQueue<Integer> npq = new PriorityQueue<>(Collections.reverseOrder());
            for (int prev : pq) {
                for (int cur : row) {
                    npq.offer(prev + cur);
                    if (npq.size() > k) {
                        npq.poll();
                    }
                }
            }
            pq = npq;
        }
        return pq.isEmpty() ? -1 : pq.peek();
    }

    // Heap + Set
    // time complexity: O(M*K*(N+log(K)+log(M)), space complexity: O(K * M)
    // 23 ms(100.00%), 40.3 MB(100%) for 71 tests
    public int kthSmallest2(int[][] mat, int k) {
        int r = mat.length;
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(x -> x[0]));
        {
            int[] first = new int[r + 1];
            for (int[] row : mat) {
                first[0] += row[0];
            }
            pq.offer(first);
        }
        Set<Integer> visited = new HashSet<>();
        for (int loop = k - 1, c = mat[0].length; loop > 0; loop--) {
            int[] cur = pq.poll();
            for (int i = 0; i < r; i++) {
                int[] next = cur.clone();
                if (++next[i + 1] == c) { continue; }

                next[0] += mat[i][next[i + 1]] - mat[i][next[i + 1] - 1];
                // HACK! hash code could collide, fixed by the following solution
                if (visited.add(Arrays.hashCode(next))) {
                    pq.offer(next);
                }
            }
        }
        return Objects.requireNonNull(pq.peek())[0];
    }

    // Heap + Set
    // time complexity: O(M*K*(N+log(K)+log(M)), space complexity: O(K * M)
    // 23 ms(100.00%), 40.3 MB(100%) for 71 tests
    public int kthSmallest3(int[][] mat, int k) {
        int r = mat.length;
        PriorityQueue<Array> pq = new PriorityQueue<>();
        {
            int sum = 0;
            for (int[] row : mat) {
                sum += row[0];
            }
            pq.offer(new Array(new int[r], sum));
        }
        Set<Array> visited = new HashSet<>();
        for (int loop = k - 1, c = mat[0].length; loop > 0; loop--) {
            Array cur = pq.poll();
            for (int i = 0; i < r; i++) {
                int[] v = cur.vector.clone();
                if (++v[i] == c) { continue; }

                Array next = new Array(v, cur.value + mat[i][v[i]] - mat[i][v[i] - 1]);
                if (visited.add(next)) {
                    pq.offer(next);
                }
            }
        }
        return Objects.requireNonNull(pq.peek()).value;
    }

    private static class Array implements Comparable<Array> {
        private final int[] vector;
        private final int value;

        public Array(int[] vector, int value) {
            this.vector = vector;
            this.value = value;
        }

        @Override public boolean equals(Object obj) {
            Array other = (Array)obj;
            return value == other.value && Arrays.equals(vector, other.vector);
        }

        @Override public int hashCode() {
            return Arrays.hashCode(vector);
        }

        @Override public int compareTo(Array o) {
            return Integer.compare(value, o.value);
        }
    }

    private void test(int[][] mat, int k, int expected) {
        assertEquals(expected, kthSmallest(mat, k));
        assertEquals(expected, kthSmallest2(mat, k));
        assertEquals(expected, kthSmallest3(mat, k));
    }

    @Test public void test() {
        test(new int[][] {{1, 3, 11}, {2, 4, 6}}, 5, 7);
        test(new int[][] {{1, 3, 11}, {2, 4, 6}}, 9, 17);
        test(new int[][] {{1, 10, 10}, {1, 4, 5}, {2, 3, 6}}, 7, 9);
        test(new int[][] {{1, 1, 10}, {2, 2, 9}}, 7, 12);
        test(new int[][] {{1, 2, 7, 8, 10}, {4, 4, 5, 5, 6}, {3, 3, 5, 6, 7}, {2, 4, 7, 9, 9}}, 7,
             11);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
