import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1105: https://leetcode.com/problems/filling-bookcase-shelves/
//
// We have a sequence of books: the i-th book has thickness books[i][0] and height books[i][1].
// We want to place these books in order onto bookcase shelves that have total width shelf_width.
// We choose some of the books to place on this shelf (such that the sum of their thickness is
// <= shelf_width), then build another level of shelf of the bookcase so that the total height of
// the bookcase has increased by the maximum height of the books we just put down. We repeat this
// process until there are no more books to place.
// Note again that at each step of the above process, the order of the books we place is the same
// order as the given sequence of books. For example, if we have an ordered list of 5 books, we
// might place the first and second book onto the first shelf, the third book on the second shelf,
// and the fourth and fifth book on the last shelf. Return the minimum possible height that the
// total bookshelf can be after placing shelves in this manner.
//
// Constraints:
// 1 <= books.length <= 1000
// 1 <= books[i][0] <= shelf_width <= 1000
// 1 <= books[i][1] <= 1000
public class MinHeightShelves {
    // 2-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N^2), space complexity: O(N^2)
    // 2 ms(15.35%), 40.6 MB(18.64%) for 20 tests
    public int minHeightShelves(int[][] books, int shelf_width) {
        int n = books.length;
        int[][] dp = new int[n + 1][n + 1]; // dp[i+1][j+1]: totalH from j to i
        for (int[] a : dp) {
            Arrays.fill(a, Integer.MAX_VALUE);
        }
        dp[0][0] = 0;
        int res = 0;
        for (int i = 0; i < n; i++) {
            res = dp[i + 1][i + 1] = res + books[i][1]; // new row
            for (int j = i, maxH = 0, h = books[i][1], totalW = books[i][0];
                 j > 0 && (totalW += books[j - 1][0]) <= shelf_width; j--) {
                maxH = Math.max(maxH, books[j - 1][1]);
                dp[i + 1][j] = Math.min(dp[i + 1][j], dp[i][j] - maxH + Math.max(maxH, h));
                res = Math.min(res, dp[i + 1][j]);
            }
        }
        return res;
    }

    // 1-D Dynamic Programming(Bottom-Up)
    // time complexity: O(N^2), space complexity: O(N^2)
    // 0 ms(100.00%), 38.7 MB(33.33%) for 20 tests
    public int minHeightShelves2(int[][] books, int shelf_width) {
        int n = books.length;
        int[] dp = new int[n + 1];
        for (int i = 0; i < n; i++) {
            int totalW = books[i][0];
            int maxH = books[i][1];
            dp[i + 1] = dp[i] + maxH;
            for (int j = i; j > 0 && (totalW += books[j - 1][0]) <= shelf_width; j--) {
                maxH = Math.max(maxH, books[j - 1][1]);
                dp[i + 1] = Math.min(dp[i + 1], dp[j - 1] + maxH);
            }
        }
        return dp[n];
    }

    private void test(int[][] books, int shelf_width, int expected) {
        assertEquals(expected, minHeightShelves(books, shelf_width));
        assertEquals(expected, minHeightShelves2(books, shelf_width));
    }

    @Test public void test() {
        test(new int[][] {{1, 1}, {2, 3}, {2, 3}, {1, 1}, {1, 1}, {1, 1}, {1, 2}}, 4, 6);
        test(new int[][] {{3, 1}, {1, 2}, {2, 3}, {2, 3}, {1, 1}, {1, 1}, {1, 1}, {1, 2}, {2, 5},
                          {3, 7}, {1, 2}}, 5, 15);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
