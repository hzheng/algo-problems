import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/guess-number-higher-or-lower/
//
// I pick a number from 1 to n. You have to guess which number I picked. Every
// time you guess wrong, I'll tell you whether the number is higher or lower.
class GuessGame {
    private int n;

    public void set(int n) {
        this.n = n;
    }

    int guess(int num) {
        return num == n ? 0 : (num > n ? -1 : 1);
    }
}

public class GuessNumber extends GuessGame {
    // Binary Search
    // time complexity: O(log2(N)), space complexity: O(1)
    // beats 4.63%(2 ms)
    public int guessNumber(int n) {
        int low = 1;
        int high = n;
        while (low < high) {
            int mid = (low + high) >>> 1;
            int answer = guess(mid);
            if (answer == 0) return mid;

            if (answer > 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }

    // ernary Search
    // time complexity: O(log3(N)), space complexity: O(1)
    // https://leetcode.com/articles/guess-number-higher-or-lower/
    // beats 4.63%(2 ms)
    public int guessNumber2(int n) {
        int low = 1;
        int high = n;
        while (low <= high) {
            int mid1 = low + (high - low) / 3;
            int mid2 = high - (high - low) / 3;
            int res1 = guess(mid1);
            if (res1 == 0) return mid1;

            if (res1 < 0) {
                high = mid1 - 1;
                continue;
            }

            int res2 = guess(mid2);
            if (res2 == 0) return mid2;

            if (res2 > 0) {
                low = mid2 + 1;
            } else {
                low = mid1 + 1;
                high = mid2 - 1;
            }
        }
        return -1;
    }

    void test(int n, int expected) {
        set(expected);
        assertEquals(expected, guessNumber(n));
        assertEquals(expected, guessNumber2(n));
    }

    @Test
    public void test1() {
        test(10, 6);
        test(100, 51);
        test(1000, 351);
        test(10000, 3351);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("GuessNumber");
    }
}
