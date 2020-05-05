import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

// LC1380: https://leetcode.com/problems/lucky-numbers-in-a-matrix/
//
// Given a m * n matrix of distinct numbers, return all lucky numbers in the matrix in any order.
// A lucky number is an element of the matrix such that it is the minimum element in its row and
// maximum in its column.
// Constraints:
// m == mat.length
// n == mat[i].length
// 1 <= n, m <= 50
// 1 <= matrix[i][j] <= 10^5.
// All elements in the matrix are distinct.
public class LuckyNumbersInMatrix {
    // Set
    // time complexity: O(N * M), space complexity: O(1)
    // 2 ms(70.54%), 40.1 MB(100%) for 62 tests
    public List<Integer> luckyNumbers(int[][] matrix) {
        Set<Integer> minSet = new HashSet<>();
        for (int[] row : matrix) {
            int min = Integer.MAX_VALUE;
            for (int a : row) {
                min = Math.min(min, a);
            }
            minSet.add(min);
        }
        for (int col = 0; col < matrix[0].length; col++) {
            int max = 0;
            for (int[] row : matrix) {
                max = Math.max(max, row[col]);
            }
            if (minSet.contains(max)) { // logic guarantees it's lucky and unique
                return Collections.singletonList(max);
            }
        }
        return Collections.emptyList();
    }

    // time complexity: O(N * M), space complexity: O(1)
    // 1 ms(98.16%), 40.3 MB(100%) for 62 tests
    public List<Integer> luckyNumbers2(int[][] matrix) {
        int luckyCol = 0;
        int luckyVal = 0;
        for (int[] row : matrix) {
            int minRowY = 0;
            for (int j = 1; j < row.length; j++) {
                if (row[j] < row[minRowY]) {
                    minRowY = j;
                }
            }
            if (luckyVal < row[minRowY]) {
                luckyVal = row[minRowY];
                luckyCol = minRowY;
            }
        }
        for (int[] row : matrix) {
            if (row[luckyCol] > luckyVal) {
                return Collections.emptyList();
            }
        }
        return Collections.singletonList(luckyVal);
    }

    @Test public void test() {
        test(new int[][] {{3, 7, 8}, {9, 11, 13}, {15, 16, 17}}, new Integer[] {15});
        test(new int[][] {{1, 10, 4, 2}, {9, 3, 8, 7}, {15, 16, 17, 12}}, new Integer[] {12});
        test(new int[][] {{7, 8}, {1, 2}}, new Integer[] {7});
        test(new int[][] {{36376, 85652, 21002, 4510}, {68246, 64237, 42962, 9974},
                          {32768, 97721, 47338, 5841}, {55103, 18179, 79062, 46542}},
             new Integer[] {});
    }

    private void test(int[][] nums, Integer[] expected) {
        List<Integer> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, luckyNumbers(nums));
        assertEquals(expectedList, luckyNumbers2(nums));
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
