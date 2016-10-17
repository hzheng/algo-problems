import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC412: https://leetcode.com/problems/fizz-buzz/
//
// Write a program that outputs the string representation of numbers from 1 to n.
// But for multiples of three it should output “Fizz” instead of the number and
// for the multiples of five output “Buzz”. For numbers which are multiples of
// both three and five output “FizzBuzz”.
public class FizzBuzz {
    // beats N/A(4 ms for 8 tests)
    public List<String> fizzBuzz(int n) {
        List<String> res = new LinkedList<>();
        for (int i = 1; i <= n; i++) {
            if (i % 3 != 0 && i % 5 != 0) {
                res.add(String.valueOf(i));
            } else if (i % 3 == 0 && i % 5 == 0) {
                res.add("FizzBuzz");
            } else if (i % 3 == 0) {
                res.add("Fizz");
            } else {
                res.add("Buzz");
            }
        }
        return res;
    }

    void test(int n, String... expected) {
        assertArrayEquals(expected, fizzBuzz(n).toArray(new String[0]));
    }

    @Test
    public void test1() {
        test(15, "1", "2", "Fizz", "4", "Buzz", "Fizz", "7", "8", "Fizz",
             "Buzz", "11", "Fizz", "13", "14", "FizzBuzz");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FizzBuzz");
    }
}
