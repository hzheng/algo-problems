import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC528: https://leetcode.com/problems/random-pick-with-weight/
//
// Given an array w of positive integers, write a function which randomly picks
// an index in proportion to its weight.
public class RandomPickWithWeight {
    static interface IRandomPickWithWeight {
        public int pickIndex();
    }

    // SortedMap
    // beats 51.11%(104 ms for 57 tests)
    static class RandomPickWithWeight1 implements IRandomPickWithWeight {
        private Random rand = new Random();
        private NavigableMap<Integer, Integer> map = new TreeMap<>();
        private int sum;

        public RandomPickWithWeight1(int[] w) {
            int i = 0;
            for (int weight : w) {
                map.put(sum += weight, i++);
            }
        }

        public int pickIndex() {
            int pick = rand.nextInt(sum);
            return map.get(map.higherKey(pick));
        }
    }

    // Binary Search
    // beats 95.97%(89 ms for 57 tests)
    static class RandomPickWithWeight2 implements IRandomPickWithWeight {
        private Random rand = new Random();
        private int[] sum;

        public RandomPickWithWeight2(int[] w) {
            sum = new int[w.length];
            sum[0] = w[0];
            for (int i = 1; i < w.length; i++) {
                sum[i] = sum[i - 1] + w[i];
            }
        }

        public int pickIndex() {
            int pick = rand.nextInt(sum[sum.length - 1]) + 1;
            int idx = Arrays.binarySearch(sum, pick);
            return idx >= 0 ? idx : -idx - 1;
        }
    }

    // void test(, int expected) {
    // assertEquals(expected, f());
    // }

    // TODO: add tests
    @Test
    public void test() {
        // test(new int[]{3, 14, 1, 7})
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
