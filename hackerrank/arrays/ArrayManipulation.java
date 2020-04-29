import java.io.*;
import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// TODO: move to hackerrank dir
// https://www.hackerrank.com/challenges/crush/problem
//
// Starting with a 1-indexed array of zeros and a list of operations, for each operation add a value
// to each of the array element between two given indices, inclusive. Once all operations have been
// performed, return the maximum value in your array.
// Constraints
// 3 <= n <=10^7
// 1 <= m <= 2 * 10^5
// 1 <= a <= b <= n
// 0 <= k <= 10^9
public class ArrayManipulation {
    // Segment Tree
    // time complexity: O(N * log(N)), space complexity: O(N)
    static long arrayManipulation(int n, int[][] queries) {
        SegmentTreeNode root = new SegmentTreeNode(1, n, 0);
        for (int[] query : queries) {
            root.update(query[0], query[1], query[2]);
        }
        return root.max();
    }

    private static class SegmentTreeNode {
        int start, end;
        SegmentTreeNode left, right;
        long data;

        SegmentTreeNode(int start, int end, long data) {
            this.start = start;
            this.end = end;
            this.data = data;
        }

        public SegmentTreeNode getLeft() {
            if (left == null) {
                branch();
            }
            return left;
        }

        public SegmentTreeNode getRight() {
            if (right == null) {
                branch();
            }
            return right;
        }

        private void branch() {
            int mid = (start + end) >>> 1;
            left = new SegmentTreeNode(start, mid, data);
            right = new SegmentTreeNode(mid + 1, end, data);
            data = 0;
        }

        public long max() {
            long res = 0;
            if (left != null) {
                res = Math.max(res, left.max());
            }
            if (right != null) {
                res = Math.max(res, right.max());
            }
            return res + data;
        }

        public void update(int start, int end, int val) {
            if (start == this.start && end == this.end) {
                data += val;
            } else {
                int mid = (this.start + this.end) >>> 1;
                if (end <= mid) {
                    getLeft().update(start, end, val);
                } else if (start > mid) {
                    getRight().update(start, end, val);
                } else {
                    getLeft().update(start, mid, val);
                    getRight().update(mid + 1, end, val);
                }
            }
        }
    }

    // Difference accumulation
    // time complexity: O(N), space complexity: O(N)
    static long arrayManipulation2(int n, int[][] queries) {
        long[] array = new long[n + 1];
        for (int[] query : queries) {
            int start = query[0];
            int end = query[1];
            int value = query[2];
            array[start - 1] += value;
            array[end] -= value;
        }
        long res = 0;
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += array[i];
            res = Math.max(res, sum);
        }
        return res;
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main0(String[] args) throws IOException {
        BufferedWriter bufferedWriter =
                new BufferedWriter(new FileWriter(System.getenv("OUTPUT_PATH")));

        String[] nm = scanner.nextLine().split(" ");

        int n = Integer.parseInt(nm[0]);

        int m = Integer.parseInt(nm[1]);

        int[][] queries = new int[m][3];

        for (int i = 0; i < m; i++) {
            String[] queriesRowItems = scanner.nextLine().split(" ");
            scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

            for (int j = 0; j < 3; j++) {
                int queriesItem = Integer.parseInt(queriesRowItems[j]);
                queries[i][j] = queriesItem;
            }
        }

        long result = arrayManipulation(n, queries);

        bufferedWriter.write(String.valueOf(result));
        bufferedWriter.newLine();

        bufferedWriter.close();

        scanner.close();
    }

    void test(int n, int[][] queries, long expected) {
        assertEquals(expected, arrayManipulation(n, queries));
        assertEquals(expected, arrayManipulation2(n, queries));
    }

    @Test public void test() {
        test(10, new int[][] {{2, 6, 8}, {3, 5, 7}, {1, 8, 1}, {5, 9, 15}}, 31);
        test(10, new int[][] {{1, 5, 3}, {4, 8, 7}, {6, 9, 1}}, 10);
        test(5, new int[][] {{1, 2, 100}, {2, 5, 100}, {3, 4, 100}}, 200);
        test(15, new int[][] {{1, 2, 10}, {2, 5, 50}, {6, 9, 13}, {3, 4, 50}, {12, 15, 20}}, 100);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
