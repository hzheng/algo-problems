import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC519: https://leetcode.com/problems/random-flip-matrix/
//
// Given 2D binary matrix where all values are initially 0. Write a function
// flip which chooses a 0 value uniformly at random, changes it to 1, and then
// returns the position of that value. Also, write a function reset which sets 
// all values back to 0. Try to minimize the number of calls to Math.random() 
// and optimize the time and space complexity.
// Note:
// 1 <= n_rows, n_cols <= 10000
// 0 <= row.id < n_rows and 0 <= col.id < n_cols
// flip will not be called when the matrix has no 0 values left.
// the total number of calls to flip and reset will not exceed 1000.
public class RandomFlipMatrix {
    static interface IRandomFlipMatrix {
        public int[] flip();
        public void reset();
    }

    // Fisher–Yates shuffle
    // beats 90.46%(19 ms for 94 tests)
    static class RandomFlipMatrix1 implements IRandomFlipMatrix {
        Random rand = new Random();
        int rows;
        int cols;
        int zeros;
        Map<Integer, Integer> map = new HashMap<>();

        public RandomFlipMatrix1(int n_rows, int n_cols) {
            rows = n_rows;
            cols = n_cols;
            zeros = rows * cols;
        }

        public int[] flip() {
            int pick = rand.nextInt(zeros--);
            int target = map.getOrDefault(pick, pick); // check if ever replaced
            // replace with the last available zero
            map.put(pick, map.getOrDefault(zeros, zeros));
            return new int[]{target / cols, target % cols};
        }

        public void reset() {
            map.clear();
            zeros = rows * cols;
        }
    }

    // void test(, int expected) {
    // assertEquals(expected, f());
    // }

    // TODO: add tests
    @Test
    public void test() {
        // test();
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
