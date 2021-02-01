import org.junit.Test;

import static org.junit.Assert.*;

// LC1744: https://leetcode.com/problems/can-you-eat-your-favorite-candy-on-your-favorite-day/
//
// You are given a (0-indexed) array of positive integers candiesCount where candiesCount[i]
// represents the number of candies of the ith type you have. You are also given a 2D array queries
// where queries[i] = [favoriteTypei, favoriteDayi, dailyCapi].
// You play a game with the following rules:
// You start eating candies on day 0.
// You cannot eat any candy of type i unless you have eaten all candies of type i - 1.
// You must eat at least one candy per day until you have eaten all the candies.
// Construct a boolean array answer such that answer.length == queries.length and answer[i] is true
// if you can eat a candy of type favoriteTypei on day favoriteDayi without eating more than
// dailyCapi candies on any day, and false otherwise. Note that you can eat different types of candy
// on the same day, provided that you follow rule 2.
// Return the constructed array answer.
//
// Constraints:
// 1 <= candiesCount.length <= 10^5
// 1 <= candiesCount[i] <= 10^5
// 1 <= queries.length <= 10^5
// queries[i].length == 3
// 0 <= favoriteTypei < candiesCount.length
// 0 <= favoriteDayi <= 10^9
// 1 <= dailyCapi <= 10^9
public class CanEat {
    // Hash Table
    // time complexity: O(C+Q), space complexity: O(Q)
    // 7 ms(100.00%), 152.3 MB(11.11%) for 62 tests
    public boolean[] canEat(int[] candiesCount, int[][] queries) {
        int n = queries.length;
        int m = candiesCount.length;
        long[] sum = new long[m + 1];
        for (int i = 0; i < m; i++) {
            sum[i + 1] = sum[i] + candiesCount[i];
        }
        boolean[] res = new boolean[n];
        int i = 0;
        for (int[] q : queries) {
            int type = q[0];
            int day = q[1] + 1;
            long cap = q[2];
            res[i++] = (sum[type] < cap * day && sum[type + 1] >= day);
        }
        return res;
    }

    private void test(int[] candiesCount, int[][] queries, boolean[] expected) {
        assertArrayEquals(expected, canEat(candiesCount, queries));
    }

    @Test public void test() {
        test(new int[] {7, 4, 5, 3, 8}, new int[][] {{0, 2, 2}, {4, 2, 4}, {2, 13, 1000000000}},
             new boolean[] {true, false, true});
        test(new int[] {5, 2, 6, 4, 1},
             new int[][] {{3, 1, 2}, {4, 10, 3}, {3, 10, 100}, {4, 100, 30}, {1, 3, 1}},
             new boolean[] {false, true, true, false, false});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
