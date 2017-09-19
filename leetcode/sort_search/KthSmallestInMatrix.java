import java.util.*;
import java.util.PriorityQueue;

import org.junit.Test;
import static org.junit.Assert.*;

// LC378: https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/
//
// Given a n x n matrix where each of the rows and columns are sorted in
// ascending order, find the kth smallest element in the matrix.
// Note:
// You may assume k is always valid, 1 ≤ k ≤ n ^ 2.
public class KthSmallestInMatrix {
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 24.86%(42 ms)
    public int kthSmallest(int[][] matrix, int k) {
        if (k == 1) return matrix[0][0];

        int n = matrix.length;
        int m = (int)Math.ceil(Math.sqrt(k));
        int pivot = matrix[m - 1][m - 1];
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            public int compare(Integer a, Integer b) {
                return b - a;
            }
        });
        for (int i = 0; i < m - 1; i++) { // upper-right corner less than pivot
            for (int j = m; j < n; j++) {
                if (matrix[i][j] < pivot) {
                    pq.offer(matrix[i][j]);
                } else break;
            }
        }
        for (int i = m; i < n; i++) { // lower-left corner less than pivot
            for (int j = 0; j < m - 1; j++) {
                if (matrix[i][j] < pivot) {
                    pq.offer(matrix[i][j]);
                } else break;
            }
        }

        int needRemove = pq.size() + m * m - 1 - k;
        if (needRemove < 0) return matrix[m - 1][m - 1];

        int i = m - 1;
        for (; i > 0; i--) {
            for (int j = i - 1; j >= 0; j--) { // add two sides
                pq.offer(matrix[j][i]);
                pq.offer(matrix[i][j]);
            }

            int corner = matrix[i - 1][i - 1];
            while (needRemove > 0 && !pq.isEmpty() && pq.peek() >= corner) {
                pq.poll();
                needRemove--;
            }
            if (needRemove == 0) break;

            pq.offer(corner);
        }
        while (needRemove-- > 0) {
            pq.poll();
        }
        return i == 0 ? pq.peek() : Math.max(pq.peek(), matrix[i - 1][i - 1]);
    }

    // Heap + Binary Search
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 64.42%(18 ms for 85 tests)
    public int kthSmallest2(int[][] matrix, int k) {
        int n = matrix.length;
        int m = (int)Math.ceil(Math.sqrt(k));
        int pivot = matrix[m - 1][m - 1];
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return matrix[b[0]][b[1]] - matrix[a[0]][a[1]];
            }
        });
        int count = 0;
        for (int i = 0; i < n; i++) {
            int colIndex = Arrays.binarySearch(matrix[i], pivot);
            if (colIndex < 0) {
                colIndex = -colIndex - 2;
                if (colIndex < 0) break;
                // the following 'else' part may be needed in case of duplicate pivots
                // even the code is accepted without it
                // } else {
                // while (colIndex + 1 < n && matrix[i][colIndex + 1] == pivot) {
                //     colIndex++;
                // }
            }
            count += colIndex + 1;
            pq.offer(new int[] {i, colIndex});
        }
        for (int needRemove = count - k; needRemove > 0; needRemove--) {
            int[] pos = pq.poll();
            if (pos[1] > 0) {
                pq.offer(new int[] {pos[0], pos[1] - 1});
            }
        }
        int[] pos = pq.peek();
        return matrix[pos[0]][pos[1]];
    }

    // Solution of Choice
    // Heap
    // time complexity: O(N * log(K)), space complexity: O(K)
    // beats 28.49%(39 ms for 85 tests)
    public int kthSmallest3(int[][] matrix, int k) {
        int n = matrix.length;
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return matrix[a[0]][a[1]] - matrix[b[0]][b[1]];
            }
        });
        for (int i = Math.min(n, k) - 1; i >= 0; i--) {
            pq.offer(new int[] {0, i});
        }
        for (int i = k - 1; i > 0; i--) {
            int[] pos = pq.poll();
            if (pos[0] + 1 < n) {
                pq.offer(new int[] {pos[0] + 1, pos[1]});
            }
        }
        int[] pos = pq.peek();
        return matrix[pos[0]][pos[1]];
    }

    // Binary Search
    // time complexity: O(N * log(Max-Min)), space complexity: O(1)
    // beats 79.43%(1 ms for 85 tests)
    public int kthSmallest4(int[][] matrix, int k) {
        int n = matrix.length;
        int low = matrix[0][0];
        for (int high = matrix[n - 1][n - 1] + 1; low < high; ) {
            int mid = low + (high - low) / 2;
            int count = 0;
            for (int i = 0, j = n - 1; i < n; i++) {
                for (; j >= 0 && matrix[i][j] > mid; j--) {}
                // j = Arrays.binarySearch(matrix[i], mid); // why BS is slower?
                // if (j < 0) {
                //     j = -j - 2;
                // }
                // for (; j < n - 1 && matrix[i][j + 1] <= mid; j++) {}
                count += (j + 1);
            }
            if (count < k) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    // Solution of Choice
    // Binary Search
    // time complexity: O(N * log(Max-Min)), space complexity: O(1)
    // beats 79.43%(1 ms for 85 tests)
    public int kthSmallest5(int[][] matrix, int k) {
        int n = matrix.length;
        int low = matrix[0][0];
        for (int high = matrix[n - 1][n - 1] + 1; low <= high; ) {
            int mid = low + (high - low) / 2;
            int count = 0;
            for (int i = n - 1, j = 0; i >= 0 && j < n; ) {
                if (matrix[i][j] > mid) {
                    i--;
                } else {
                    count += i + 1;
                    j++;
                }
            }
            if (count < k) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }

    // TODO: time complexity: O(N)
    // https://discuss.leetcode.com/topic/54262/c-o-n-time-o-n-space-solution-with-detail-intuitive-explanation

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<int[][], Integer, Integer> smallest,
              int [][] matrix, int[] ... expected) {
        for (int[] x : expected) {
            assertEquals(x[1], (int)smallest.apply(matrix, x[0]));
        }
    }

    void test(int[][] matrix, int[] ... expected) {
        KthSmallestInMatrix k = new KthSmallestInMatrix();
        test(k::kthSmallest, matrix, expected);
        test(k::kthSmallest2, matrix, expected);
        test(k::kthSmallest3, matrix, expected);
        test(k::kthSmallest4, matrix, expected);
        test(k::kthSmallest5, matrix, expected);
    }

    @Test
    public void test1() {
        test(new int[][] {{-5}}, new int[] {1, -5});
        test(new int[][] {{1, 2}, {1, 3}}, new int[] {4, 3});
        test(new int[][] {{1, 2}, {3, 3}}, new int[] {4, 3});
        test(new int[][] {{1, 2, 6}, {3, 6, 10}, new int[] {7, 11, 12}},
             new int[] {4, 6}, new int[] {5, 6}, new int[] {6, 7});
        test(new int[][] {{1, 3, 4}, {1, 8, 8}, {4, 12, 17}}, new int[] {5, 4});
        test(new int[][] {{1,  5,  9, 10}, {10, 11, 13, 14}, {12, 13, 15, 16},
                          {13, 14, 15, 18}},
             new int[] {1, 1}, new int[] {6, 11}, new int[] {7, 12},
             new int[] {10, 13}, new int[] {11, 14});
        test(new int[][] {{1,  5,  9}, {10, 11, 13}, {12, 13, 15}},
             new int[] {8, 13}, new int[] {3, 9}, new int[] {4, 10});
        test(new int[][] {{1, 2, 3, 4, 5}, {6, 7, 8, 9, 10}, {11, 12, 13, 14, 15},
                          {16, 17, 18, 19, 20}, {21, 22, 23, 24, 25}},
             new int[] {5, 5}, new int[] {6, 6}, new int[] {7, 7},
             new int[] {8, 8}, new int[] {9, 9}, new int[] {10, 10},
             new int[] {11, 11}, new int[] {12, 12}, new int[] {13, 13},
             new int[] {20, 20}, new int[] {1, 1}, new int[] {25, 25});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("KthSmallestInMatrix");
    }
}
