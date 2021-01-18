import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1727: https://leetcode.com/problems/largest-submatrix-with-rearrangements/
//
// You are given a binary matrix matrix of size m x n, and you are allowed to rearrange the columns
// of the matrix in any order. Return the area of the largest submatrix within matrix where every
// element of the submatrix is 1 after reordering the columns optimally.
//
// Constraints:
// m == matrix.length
// n == matrix[i].length
// 1 <= m * n <= 10^5
// matrix[i][j] is 0 or 1.
public class LargestSubmatrix {
    // SortedMap + Heap
    // time complexity: O(M*N*log(M)), space complexity: O(M*N)
    // 106 ms(50.00%), 122.7 MB(16.67%) for 57 tests
    public int largestSubmatrix(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        NavigableMap[] intervals = new TreeMap[n];
        for (int j = 0; j < n; j++) {
            NavigableMap<Integer, Integer> map = intervals[j] = new TreeMap<>();
            for (int i = 0; i < m; i++) {
                if (matrix[i][j] == 0) { continue; }

                if (i == 0 || matrix[i - 1][j] == 0) {
                    map.put(i, i);
                } else {
                    int prev = map.lastKey();
                    map.put(prev, map.get(prev) + 1);
                }
            }
        }
        int res = 0;
        for (int i = 0; i < m; i++) {
            PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> b - a);
            for (NavigableMap<Integer, Integer> map : intervals) {
                Integer from = map.floorKey(i);
                if (from == null) { continue; }

                int to = map.get(from);
                if (to >= i) {
                    pq.offer(to - i + 1);
                }
            }
            for (int count = 1; !pq.isEmpty(); count++) {
                res = Math.max(res, count * pq.poll());
            }
        }
        return res;
    }

    // Sort
    // time complexity: O(M*N*log(M)), space complexity: O(N)
    // 19 ms(100.00%), 59.8 MB(100%) for 57 tests
    public int largestSubmatrix2(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int[] count = new int[n];
        int res = 0;
        for (int i = m - 1; i >= 0; i--) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 1) {
                    count[j]++;
                } else {
                    count[j] = 0;
                }
            }
            int[] tmpCount = count.clone();
            Arrays.sort(tmpCount);
            for (int j = 0; j < n; j++) {
                res = Math.max(res, tmpCount[j] * (n - j));
            }
        }
        return res;
    }

    // time complexity: O(M^2*N), space complexity: O(N)
    // 43 ms(83.33%), 125.5 MB(16.67%) for 57 tests
    public int largestSubmatrix3(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        int res = 0;
        for (int i = 0; i < m && (m - i) * n > res; i++) {
            List<Integer> cols = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                cols.add(j);
            }
            for (int j = i; j < m && (m - i) * cols.size() > res; j++) {
                List<Integer> all1Cols = new ArrayList<>();
                for (int k : cols) {
                    if (matrix[j][k] == 1) {
                        all1Cols.add(k);
                    }
                }
                cols = all1Cols;
                res = Math.max(res, (j - i + 1) * cols.size());
            }
        }
        return res;
    }

    private void test(int[][] matrix, int expected) {
        assertEquals(expected, largestSubmatrix(matrix));
        assertEquals(expected, largestSubmatrix2(matrix));
        assertEquals(expected, largestSubmatrix3(matrix));
    }

    @Test public void test() {
        test(new int[][] {{0, 0, 1}, {1, 1, 1}, {1, 0, 1}}, 4);
        test(new int[][] {{1, 0, 1, 0, 1}}, 3);
        test(new int[][] {{1, 1, 0}, {1, 0, 1}}, 2);
        test(new int[][] {{0, 0}, {0, 0}}, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
