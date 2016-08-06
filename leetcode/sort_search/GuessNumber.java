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

    void test(int n, int expected) {
        set(expected);
        assertEquals(expected, guessNumber(n));
    }

    @Test
    public void test1() {
        test(10, 6);
        test(100, 51);
        test(1000, 351);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("GuessNumber");
    }
}
