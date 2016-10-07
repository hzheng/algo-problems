import java.util.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC135: https://leetcode.com/problems/candy/
//
// N children standing in a line. Each child is assigned a rating value. You
// are giving candies to these children subjected to the following requirements:
// Each child must have at least one candy.
// Children with a higher rating get more candies than their neighbors.
// What is the minimum candies you must give?
public class Candy {
    // beats 78.77%(4 ms)
    // time complexity: O(N), space complexity: O(1)
    public int candy(int[] ratings) {
        int n = ratings.length;
        if (n < 2) return n;

        int total = 1;
        int cur = 1;
        for (int i = 1; i < n; ) {
            int diff;
            while ((diff = ratings[i] - ratings[i - 1]) >= 0) {
                if (diff == 0) {
                    cur = 1;
                } else {
                    cur++;
                }
                total += cur;
                if (++i == n) return total;
            }

            int decreasingCount = 0;
            for (; i < n && (diff = ratings[i] - ratings[i - 1]) < 0; i++) {
                decreasingCount++;
                cur--;
                total += cur;
            }
            if (cur > 1) {
                total -= decreasingCount * (cur - 1);
            } else if (cur < 1) {
                total += (decreasingCount + 1) * (1 - cur);
            }
            cur = 1;
        }
        return total;
    }

    // Solution of Choice
    // Greedy(2-pass)
    // beats 59.97%(4 ms)
    // time complexity: O(N), space complexity: O(N)
    public int candy2(int[] ratings) {
        int n = ratings.length;
        int[] candies = new int[n];
        for (int i = 1; i < n; i++) { // from left to right
            if (ratings[i] > ratings[i - 1]) {
                candies[i] = candies[i - 1] + 1;
            }
        }
        for (int i = n - 2; i >= 0; i--) { // from right to left
            if (ratings[i] > ratings[i + 1] && candies[i] <= candies[i + 1]) {
                candies[i] = candies[i + 1] + 1;
            }
        }
        int total = n;
        for (int candy : candies) {
            total += candy;
        }
        return total;
    }

    // Greedy(1-pass)
    // https://discuss.leetcode.com/topic/8208/one-pass-constant-space-java-solution
    // beats 59.97%(4 ms)
    public int candy3(int[] ratings) {
        int total = 1;
        int prev = 1;
        int countDown = 0;
        for (int i = 1, n = ratings.length; i <= n; i++) {
            if (i < n && ratings[i] < ratings[i - 1]) {
                countDown++;
                continue;
            }
            if (countDown > 0) {
                total += countDown * (countDown + 1) / 2;
                if (countDown >= prev) {
                    total += countDown - prev + 1;
                }
                countDown = 0;
                prev = 1;
            }
            if (i < n) {
                prev = (ratings[i] == ratings[i - 1]) ? 1 : prev + 1;
                total += prev;
            }
        }
        return total;
    }

    void test(Function<int[], Integer> candy, String name,
              int expected, int ... ratings) {
        assertEquals(expected, (int)candy.apply(ratings));
    }

    void test(int expected, int ... ratings) {
        Candy c = new Candy();
        test(c::candy, "candy", expected, ratings);
        test(c::candy2, "candy2", expected, ratings);
        test(c::candy3, "candy3", expected, ratings);
    }

    @Test
    public void test1() {
        test(9, 10, 2, 1, 1, 3);
        test(10, 1, 10, 2, 1, 1, 3);
        test(12, 5, 1, 10, 2, 1, 1, 3);
        test(13, 5, 1, 10, 2, 1, 1, 1, 3);
        test(15, 5, 1, 1, 1, 10, 2, 1, 1, 1, 3);
        test(6, 1, 2, 2, 3);
        test(5, 1, 0, 2);
        test(4, 1, 2, 2);
        test(32, 1, 2, 3, 6, 5, 4, 3, 2, 1, 2, 3);
        test(27, 1, 2, 3, 6, 5, 4, 3, 2, 1);
        test(1, 0);
        test(6, 1, 2, 3);
        test(4, 1, 3, 2);
        test(6, 3, 2, 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Candy");
    }
}
