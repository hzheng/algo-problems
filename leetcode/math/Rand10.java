import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC470: https://leetcode.com/problems/implement-rand10-using-rand7/
//
// Given a function rand7 which generates a uniform random integer in the range
// 1 to 7, write a function rand10 which generates a uniform random integer in 
// the range 1 to 10.
public class Rand10 {
    private static Random random = new Random();

    public int rand7() {
        return random.nextInt(7) + 1;
    }

    // beats 12.57%(15 ms for 10 tests)
    public int rand10() {
        int sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += rand7();
        }
        return sum % 10 + 1;
    }

    // Rejection Sampling
    // beats 73.57%(6 ms for 10 tests)
    public int rand10_2() {
        int index;
        do {
            int row = rand7();
            int col = rand7();
            index = col + (row - 1) * 7;
        } while (index > 40);
        return 1 + (index - 1) % 10;
    }

    // https://leetcode.com/articles/implement-rand10-using-rand7/
    // beats 73.57%(6 ms for 10 tests)
    public int rand10_3() {
        while (true) {
            int a = rand7();
            int b = rand7();
            int index = b + (a - 1) * 7;
            if (index <= 40) return 1 + (index - 1) % 10;

            a = index - 40;
            b = rand7();
            // get uniform dist from 1 - 63
            index = b + (a - 1) * 7;
            if (index <= 60) return 1 + (index - 1) % 10;

            a = index - 60;
            b = rand7();
            // get uniform dist from 1 - 21
            index = b + (a - 1) * 7;
            if (index <= 20) return 1 + (index - 1) % 10;
        }
    }

    // void test(, int expected) {
    // assertEquals(expected, rand10());
    // }

    // TODO: add tests
    @Test
    public void test() {
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
