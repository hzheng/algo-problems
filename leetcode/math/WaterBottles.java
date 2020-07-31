import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1518: https://leetcode.com/problems/water-bottles/
//
// Given numBottles full water bottles, you can exchange numExchange empty water bottles for one
// full water bottle. The operation of drinking a full water bottle turns it into an empty bottle.
//
// Return the maximum number of water bottles you can drink.
public class WaterBottles {
    // time complexity: O(log(numBottles)), space complexity: O(1)
    // 0 ms(100%), 36.5 MB(100%) for 64 tests
    public int numWaterBottles(int numBottles, int numExchange) {
        int res = numBottles;
        for (int b = numBottles, left; b >= numExchange; b += left) {
            left = b % numExchange;
            b /= numExchange;
            res += b;
        }
        return res;
    }

    // time complexity: O(log(numBottles)), space complexity: O(1)
    // 0 ms(100%), 36.4 MB(100%) for 64 tests
    public int numWaterBottles2(int numBottles, int numExchange) {
        return numBottles + (numBottles - 1) / (numExchange - 1);
    }

    void test(int numBottles, int numExchange, int expected) {
        assertEquals(expected, numWaterBottles(numBottles, numExchange));
        assertEquals(expected, numWaterBottles2(numBottles, numExchange));
    }

    @Test
    public void test() {
        test(9, 3, 13);
        test(15, 4, 19);
        test(5, 5, 6);
        test(2, 3, 2);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
