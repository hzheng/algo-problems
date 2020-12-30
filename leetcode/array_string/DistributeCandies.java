import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1103: https://leetcode.com/problems/distribute-candies-to-people/
//
// We distribute some number of candies, to a row of n = num_people people in the following way:
// We then give 1 candy to the first person, 2 candies to the second person, and so on until we give
// n candies to the last person. Then, we go back to the start of the row, giving n + 1 candies to
// the first person, n + 2 candies to the second person, and so on until we give 2 * n candies to
// the last person. This process repeats (with us giving one more candy each time, and moving to the
// start of the row after we reach the end) until we run out of candies. The last person will
// receive all of our remaining candies (not necessarily one more than the previous gift).
// Return an array that represents the final distribution of candies.
//
// Constraints:
// 1 <= candies <= 10^9
// 1 <= num_people <= 1000
public class DistributeCandies {
    // time complexity: O(C^0.5/N + N), space complexity: O(N)
    // 0 ms(100.00%), 36.6 MB(68.30%) for 27 tests
    public int[] distributeCandies(int candies, int num_people) {
        int round;
        int extra;
        for (round = 1; ; round++) {
            extra = (int)(round * num_people * (round * num_people + 1L) / 2 - candies);
            if (extra >= 0) {
                extra = candies - (--round) * num_people * (round * num_people + 1) / 2;
                break;
            }
        }
        int[] res = new int[num_people];
        for (int i = 1; i <= num_people; i++) {
            res[i - 1] = ((round - 1) * num_people + 2 * i) * round / 2;
            if (extra > 0) {
                int more = Math.min(extra, num_people * round + i);
                res[i - 1] += more;
                extra -= more;
            }
        }
        return res;
    }

    // time complexity: O(C^0.5), space complexity: O(N)
    // 1 ms(91.84%), 36.6 MB(68.30%) for 27 tests
    public int[] distributeCandies2(int candies, int num_people) {
        int[] res = new int[num_people];
        for (int i = 0, remain = candies; remain > 0; remain -= i) {
            res[i % num_people] += Math.min(remain, ++i);
        }
        return res;
    }

    // Solution of Choice
    // time complexity: O(C^0.5), space complexity: O(N)
    // 1 ms(91.84%), 37.1 MB(16.08%) for 27 tests
    public int[] distributeCandies3(int candies, int num_people) {
        int[] res = new int[num_people];
        int i = 0;
        for (; candies > 0; candies -= i) {
            res[i % num_people] += ++i;
        }
        res[(i - 1) % num_people] += candies;
        return res;
    }

    // Math
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(100.00%), 36.7 MB(51.98%) for 27 tests
    public int[] distributeCandies4(int candies, int num_people) {
        int[] res = new int[num_people];
        int round = (int)(Math.sqrt(candies * 2 + 0.25) - 0.5);
        for (int i = 0; i < num_people; i++) {
            int k = round / num_people + ((round % num_people > i) ? 1 : 0);
            res[i] = k * (i + 1) + k * (k - 1) / 2 * num_people;
        }
        res[round % num_people] += candies - round * (round + 1) / 2;
        return res;
    }

    private void test(int candies, int num_people, int[] expected) {
        assertArrayEquals(expected, distributeCandies(candies, num_people));
        assertArrayEquals(expected, distributeCandies2(candies, num_people));
        assertArrayEquals(expected, distributeCandies3(candies, num_people));
        assertArrayEquals(expected, distributeCandies4(candies, num_people));
    }

    @Test public void test() {
        test(7, 4, new int[] {1, 2, 3, 1});
        test(10, 3, new int[] {5, 2, 3});
        test(70, 5, new int[] {18, 13, 11, 13, 15});
        test(10003, 15,
             new int[] {685, 695, 705, 715, 725, 727, 603, 612, 621, 630, 639, 648, 657, 666, 675});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
