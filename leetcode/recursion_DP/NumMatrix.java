import java.lang.reflect.*;
import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Utils;

// LC308: https://leetcode.com/problems/range-sum-query-2d-mutable/
//
// Given a 2D matrix matrix, find the sum of the elements inside the rectangle
// defined by its upper left corner (row1, col1) and lower right corner (row2, col2).
public class NumMatrix {
    interface INumMatrix {
        public void update(int row, int col, int val);

        public int sumRegion(int row1, int col1, int row2, int col2);
    }

    // Binary Indexed Tree
    // beats 40.10%(351 ms for 17 tests)
    static class NumMatrix1 implements INumMatrix {
        private int[][] matrix;
        private int[][] bits;

        // time complexity: O(M * N * log(N)), space complexity: O(M * N)
        public NumMatrix1(int[][] matrix) {
            int m = matrix.length;
            if (m == 0) return;

            int n = matrix[0].length;
            bits = new int[m][n + 1];
            this.matrix = matrix;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    add(bits[i], j, matrix[i][j]);
                }
            }
        }

        // time complexity: O(log(N))
        public void update(int row, int col, int val) {
            add(bits[row], col, val - matrix[row][col]);
            matrix[row][col] = val;
        }

        // time complexity: O(M * log(N))
        public int sumRegion(int row1, int col1, int row2, int col2) {
            return sum(row2, col2 + 1) - sum(row2, col1)
                   - sum(row1 - 1, col2 + 1) + sum(row1 - 1, col1);
        }

        private int sum(int row, int col) {
            int sum = 0;
            for (int i = 0; i <= row; i++) {
                int[] bit = bits[i];
                for (int j = col; j > 0; j -= (j & -j)) {
                    sum += bit[j];
                }
            }
            return sum;
        }

        private void add(int[] bit, int i, int diff) {
            for (int j = i + 1; j < bit.length; j += (j & -j)) {
                bit[j] += diff;
            }
        }
    }

    // Binary Indexed Tree
    // beats 76.61%(299 ms for 17 tests)
    static class NumMatrix2 implements INumMatrix {
        private int[][] matrix;
        private int[][] bits;
        private int m;
        private int n;

        // time complexity: O(M * N * log(M) * log(N)), space complexity: O(M * N)
        public NumMatrix2(int[][] matrix) {
            m = matrix.length;
            if (m == 0) return;

            n = matrix[0].length;
            bits = new int[m + 1][n + 1];
            this.matrix = matrix;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    add(i, j, matrix[i][j]);
                }
            }
        }

        // time complexity: O(log(M) * log(N))
        public void update(int row, int col, int val) {
            add(row, col, val - matrix[row][col]);
            matrix[row][col] = val;
        }

        // time complexity: O(log(M) * log(N))
        public int sumRegion(int row1, int col1, int row2, int col2) {
            return sum(row2 + 1, col2 + 1) + sum(row1, col1) - sum(row1, col2 + 1) - sum(row2 + 1, col1);
        }

        private int sum(int row, int col) {
            int sum = 0;
            for (int i = row; i > 0; i -= i & (-i)) {
                for (int j = col; j > 0; j -= j & (-j)) {
                    sum += bits[i][j];
                }
            }
            return sum;
        }

        private void add(int row, int col, int diff) {
            for (int i = row + 1; i <= m; i += i & (-i)) {
                for (int j = col + 1; j <= n; j += j & (-j)) {
                    bits[i][j] += diff;
                }
            }
        }
    }

    private static class SegmentTreeNode {
        int start, end, sum;
        SegmentTreeNode left, right;

        SegmentTreeNode(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    // Segment Tree
    // Time Limit Exceeded
    static class NumMatrix3 implements INumMatrix {
        private SegmentTreeNode[] roots;

        // time complexity: O(M * N), space complexity: O(M * N)
        public NumMatrix3(int[][] matrix) {
            int m = matrix.length;
            if (m == 0) return;

            int n = matrix[0].length;
            roots = new SegmentTreeNode[m];
            for (int i = 0; i < m; i++) {
                roots[i] = buildTree(matrix[i], 0, n - 1);
            }
        }

        private SegmentTreeNode buildTree(int[] nums, int start, int end) {
            if (start > end) return null;

            SegmentTreeNode node = new SegmentTreeNode(start, end);
            if (start == end) {
                node.sum = nums[start];
            } else {
                int mid = (start + end) >>> 1;
                node.left = buildTree(nums, start, mid);
                node.right = buildTree(nums, mid + 1, end);
                node.sum = node.left.sum + node.right.sum;
            }
            return node;
        }

        // time complexity: O(log(N))
        public void update(int row, int col, int val) {
            update(roots[row], col, val);
        }

        private void update(SegmentTreeNode root, int pos, int val) {
            if (root.start == root.end) {
                root.sum = val;
            } else {
                int mid = (root.start + root.end) >>> 1;
                if (pos <= mid) {
                    update(root.left, pos, val);
                } else {
                    update(root.right, pos, val);
                }
                root.sum = root.left.sum + root.right.sum;
            }
        }

        // time complexity: O(M * log(N))
        public int sumRegion(int row1, int col1, int row2, int col2) {
            return sum(row2, col2) - sum(row2, col1 - 1)
                   - sum(row1 - 1, col2) + sum(row1 - 1, col1 - 1);
        }

        private int sum(int row, int col) {
            int sum = 0;
            for (int i = 0; i <= row; i++) {
                sum += sumRange(roots[i], 0, col);
            }
            return sum;
        }

        private int sumRange(SegmentTreeNode root, int start, int end) {
            if (end < 0) return 0;
            if (root.end == end && root.start == start) return root.sum;

            int mid = (root.start + root.end) >>> 1;
            if (end <= mid) return sumRange(root.left, start, end);

            if (start > mid) return sumRange(root.right, start, end);

            return sumRange(root.right, mid + 1, end) + sumRange(root.left, start, mid);
        }
    }

    // Segment Tree
    // Time Limit Exceeded
    static class NumMatrix4 implements INumMatrix {
        private static class TreeNode {
            int row1, row2, col1, col2, sum;
            TreeNode left, right;

            TreeNode(int row1, int row2, int col1, int col2) {
                this.row1 = row1;
                this.row2 = row2;
                this.col1 = col1;
                this.col2 = col2;
            }
        }

        private TreeNode root;

        // time complexity: O(M + N, space complexity: O(M * N)
        public NumMatrix4(int[][] matrix) {
            int m = matrix.length;
            if (m == 0) return;

            root = buildTree(matrix, 0, 0, m - 1, matrix[0].length - 1);
        }

        private TreeNode buildTree(int[][] nums, int row1, int col1,
                                   int row2, int col2) {
            if (row1 > row2 || col1 > col2) return null;

            TreeNode node = new TreeNode(row1, row2, col1, col2);
            if (row1 == row2 && col1 == col2) {
                node.sum = nums[row1][col1];
                return node;
            }
            if (row1 == row2) {
                int mid = (col1 + col2) >>> 1;
                node.left = buildTree(nums, row1, col1, row1, mid);
                node.right = buildTree(nums, row1, mid + 1, row1, col2);
            } else {
                int mid = (row1 + row2) >>> 1;
                node.left = buildTree(nums, row1, col1, mid, col2);
                node.right = buildTree(nums, mid + 1, col1, row2, col2);
            }
            node.sum = node.left.sum + node.right.sum;
            return node;
        }

        // time complexity: O(log(M) * log(N))
        public void update(int row, int col, int val) {
            update(root, row, col, val);
        }

        private void update(TreeNode node, int row, int col, int val) {
            if (node.row1 == node.row2 && node.col1 == node.col2) {
                node.sum = val;
                return;
            }
            if (node.row1 == node.row2) {
                int mid = (node.col1 + node.col2) >>> 1;
                if (col <= mid) {
                    update(node.left, row, col, val);
                } else {
                    update(node.right, row, col, val);
                }
            } else {
                int mid = (node.row1 + node.row2) >>> 1;
                if (row <= mid) {
                    update(node.left, row, col, val);
                } else {
                    update(node.right, row, col, val);
                }
            }
            node.sum = node.left.sum + node.right.sum;
        }

        // time complexity: O(log(M) * log(N))
        public int sumRegion(int row1, int col1, int row2, int col2) {
            return sum(row2, col2) - sum(row2, col1 - 1)
                   - sum(row1 - 1, col2) + sum(row1 - 1, col1 - 1);
        }

        private int sum(int row, int col) {
            return sumRange(root, 0, row, 0, col);
        }

        private int sumRange(TreeNode node, int row1, int row2,
                             int col1, int col2) {
            if (row2 < 0 || col2 < 0) return 0;

            if (node.row1 == node.row2 && node.col1 == node.col2) return node.sum;

            if (node.row1 == node.row2) {
                int mid = (node.col1 + node.col2) >>> 1;
                if (col2 <= mid) return sumRange(node.left, node.row1, node.row1, col1, col2);
                if (col1 > mid) return sumRange(node.right, node.row1, node.row1, col1, col2);
                return sumRange(node.left, node.row1, node.row1, col1, mid) + sumRange(node.right, node.row1, node.row1, mid + 1, col2);
            }
            int mid = (node.row1 + node.row2) >>> 1;
            if (row2 <= mid) return sumRange(node.left, row1, row2, col1, col2);
            if (row1 > mid) return sumRange(node.right, row1, row2, col1, col2);
            return sumRange(node.left, row1, mid, col1, col2) + sumRange(node.right, mid + 1, row2, col1, col2);
        }
    }

    // Segment Tree
    static class NumMatrix5 implements INumMatrix {
        private static class TreeNode {
            int row1, row2, col1, col2, sum;
            TreeNode child1, child2, child3, child4;

            TreeNode(int row1, int col1, int row2, int col2) {
                this.row1 = row1;
                this.col1 = col1;
                this.row2 = row2;
                this.col2 = col2;
            }
        }

        private TreeNode root;

        // time complexity: O(M + N), space complexity: O(M * N)
        public NumMatrix5(int[][] matrix) {
            int m = matrix.length;
            if (m == 0) return;

            root = buildTree(matrix, 0, 0, m - 1, matrix[0].length - 1);
        }

        private TreeNode buildTree(int[][] nums, int row1, int col1, int row2, int col2) {
            if (row2 < row1 || col2 < col1) return null;

            TreeNode node = new TreeNode(row1, col1, row2, col2);
            if (row1 == row2 && col1 == col2) {
                node.sum = nums[row1][col1];
                return node;
            }
            int rowMid = (row1 + row2) >>> 1;
            int colMid = (col1 + col2) >>> 1;
            node.child1 = buildTree(nums, row1, col1, rowMid, colMid);
            node.child2 = buildTree(nums, row1, colMid + 1, rowMid, col2);
            node.child3 = buildTree(nums, rowMid + 1, col1, row2, colMid);
            node.child4 = buildTree(nums, rowMid + 1, colMid + 1, row2, col2);
            node.sum += node.child1 != null ? node.child1.sum : 0;
            node.sum += node.child2 != null ? node.child2.sum : 0;
            node.sum += node.child3 != null ? node.child3.sum : 0;
            node.sum += node.child4 != null ? node.child4.sum : 0;
            return node;
        }

        // time complexity: O(log(M) * log(N))
        public void update(int row, int col, int val) {
            update(root, row, col, val);
        }

        private void update(TreeNode node, int row, int col, int val) {
            if (node.row1 == node.row2 && node.col1 == node.col2) {
                node.sum = val;
                return;
            }
            int rowMid = (node.row1 + node.row2) >>> 1;
            int colMid = (node.col1 + node.col2) >>> 1;
            TreeNode next;
            if (row <= rowMid) {
                next = (col <= colMid) ? node.child1 : node.child2;
            } else {
                next = (col <= colMid) ? node.child3 : node.child4;
            }
            // node.sum -= next.sum;
            update(next, row, col, val);
            // node.sum += next.sum;
        }

        // time complexity: O(log(M) * log(N))
        public int sumRegion(int row1, int col1, int row2, int col2) {
            return sumRegion(root, row1, col1, row2, col2);
        }

        private int sumRegion(TreeNode node, int row1, int col1, int row2, int col2) {
            if (node.row1 == node.row2 && node.col1 == node.col2) return node.sum;
            int rowMid = (node.row1 + node.row2) >>> 1;
            int colMid = (node.col1 + node.col2) >>> 1;
            if (rowMid >= row2) {
                if (colMid >= col2) return sumRegion(node.child1, row1, col1, row2, col2);
                if (colMid + 1 <= col1) return sumRegion(node.child2, row1, col1, row2, col2);
                return sumRegion(node.child1, row1, col1, row2, colMid) + sumRegion(node.child2, row1, colMid + 1, row2, col2);
            }
            if (rowMid + 1 <= row1) {
                if (colMid >= col2) return sumRegion(node.child3, row1, col1, row2, col2);
                if (colMid + 1 <= col1) return sumRegion(node.child4, row1, col1, row2, col2);
                return sumRegion(node.child3, row1, col1, row2, colMid) + sumRegion(node.child4, row1, colMid + 1, row2, col2);
            }
            if (colMid >= col2) return sumRegion(node.child1, row1, col1, rowMid, col2) + sumRegion(node.child3, rowMid + 1, col1, row2, col2);
            if (colMid + 1 <= col1) return sumRegion(node.child2, row1, col1, rowMid, col2) + sumRegion(node.child4, rowMid + 1, col1, row2, col2);
            return sumRegion(node.child1, row1, col1, rowMid, colMid) + sumRegion(node.child2, row1, colMid + 1, rowMid, col2)
                   + sumRegion(node.child3, rowMid + 1, col1, row2, colMid) + sumRegion(node.child4, rowMid + 1, colMid + 1, row2, col2);
        }
    }

    // TODO: Segment Tree in form of array; better Quad Tree & 2D Segment Tree implementations

    // Dynamic Programming
    // beats 94.87%(274 ms for 17 tests)
    static class NumMatrix6 implements INumMatrix {
        private int[][] sums;

        // time complexity: O(M * N), space complexity: O(M * N)
        public NumMatrix6(int[][] matrix) {
            int m = matrix.length;
            if (m == 0) return;

            int n = matrix[0].length;
            sums = new int[m][n + 1];
            for (int i = 0; i < m; i++) {
                int sum = 0;
                for (int j = 0; j < n; j++) {
                    sum += matrix[i][j];
                    sums[i][j + 1] = sum;
                }
            }
        }

        // time complexity: O(N)
        public void update(int row, int col, int val) {
            int diff = sums[row][col] + val - sums[row][col + 1];
            for (int i = col, n = sums[0].length - 1; i < n; i++) {
                sums[row][i + 1] += diff;
            }
        }

        // time complexity: O(M)
        public int sumRegion(int row1, int col1, int row2, int col2) {
            int sum = 0;
            for (int i = row1; i <= row2; i++) {
                sum += sums[i][col2 + 1] - sums[i][col1];
            }
            return sum;
        }
    }

    private void test1(INumMatrix numMatrix) {
        assertEquals(8, numMatrix.sumRegion(2, 1, 4, 3));
        numMatrix.update(3, 2, 2);
        assertEquals(10, numMatrix.sumRegion(2, 1, 4, 3));
        assertEquals(40, numMatrix.sumRegion(0, 0, 4, 3));
        numMatrix.update(0, 1, 1);
        assertEquals(41, numMatrix.sumRegion(0, 0, 4, 3));
    }

    private void test1(String className, int[][] matrix) {
        matrix = Utils.clone(matrix);
        try {
            Class<?> clazz = Class.forName("NumMatrix$" + className);
            Constructor<?> ctor = clazz.getConstructor(int[][].class);
            test1((INumMatrix)ctor.newInstance(new Object[] {matrix}));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void test1(int[][] matrix) {
        test1("NumMatrix1", matrix);
        test1("NumMatrix2", matrix);
        test1("NumMatrix3", matrix);
        test1("NumMatrix4", matrix);
        test1("NumMatrix5", matrix);
        test1("NumMatrix6", matrix);
    }

    private void test2(INumMatrix numMatrix) {
        assertEquals(1, numMatrix.sumRegion(0, 0, 0, 0));
        assertEquals(2, numMatrix.sumRegion(1, 0, 1, 0));
        assertEquals(3, numMatrix.sumRegion(0, 0, 1, 0));
        numMatrix.update(0, 0, 3);
        numMatrix.update(1, 0, 5);
        assertEquals(8, numMatrix.sumRegion(0, 0, 1, 0));
    }

    private void test2(String className, int[][] matrix) {
        matrix = Utils.clone(matrix);
        try {
            Class<?> clazz = Class.forName("NumMatrix$" + className);
            Constructor<?> ctor = clazz.getConstructor(int[][].class);
            test2((INumMatrix)ctor.newInstance(new Object[] {matrix}));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void test2(int[][] matrix) {
        test2("NumMatrix1", matrix);
        test2("NumMatrix2", matrix);
        test2("NumMatrix3", matrix);
        test2("NumMatrix4", matrix);
        test2("NumMatrix5", matrix);
        test2("NumMatrix6", matrix);
    }

    @Test
    public void test() {
        test1(new int[][] {{3, 0, 1, 4, 2}, {5, 6, 3, 2, 1},
                           {1, 2, 0, 1, 5}, {4, 1, 0, 1, 7}, {1, 0, 3, 0, 5}});
        test2(new int[][] {{1}, {2}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("NumMatrix");
    }
}
