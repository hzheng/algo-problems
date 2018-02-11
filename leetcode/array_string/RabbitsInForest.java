import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC781: https://leetcode.com/problems/rabbits-in-forest/
//
// In a forest, each rabbit has some color. Some subset of rabbits tell you how
// many other rabbits have the same color as them. Those answers are placed in
// an array.
// Return the minimum number of rabbits that could be in the forest.
// Note:
// answers will have length at most 1000.
// Each answers[i] will be an integer in the range [0, 999]
public class RabbitsInForest {
    // Sort
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    // beats %(7 ms for 54 tests)
    public int numRabbits(int[] answers) {
        Arrays.sort(answers);
        int res = 0;
        for (int i = answers.length - 1; i >= 0; ) {
            int answer = answers[i];
            int j = i - 1;
            for (; j >= 0 && answers[j] == answer; j--) {}
            res += (i - j + answer) / (answer + 1) * (answer + 1);
            i = j;
        }
        return res;
    }

    // Sort
    // time complexity: O(N * log(N)), space complexity: O(log(N))
    // beats %(7 ms for 54 tests)
    public int numRabbits2(int[] answers) {
        Arrays.sort(answers);
        int res = 0;
        for (int i = answers.length - 1, remainder = 0; i >= 0; i--) {
            if (remainder-- == 0 || answers[i] != answers[i + 1]) {
                remainder = answers[i];
                res += answers[i] + 1;
            }
        }
        return res;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(1)
    // beats %(6 ms for 54 tests)
    public int numRabbits3(int[] answers) {
        int[] count = new int[1000];
        for (int a : answers) {
            count[a]++;
        }
        int res = 0;
        for (int i = 0; i < count.length; i++) {
            // res += (count[i] + i) / (i + 1) * (i + 1);
            res += Math.floorMod(-count[i], i + 1) + count[i];
        }
        return res;
    }

    void test(int[] answers, int expected) {
        assertEquals(expected, numRabbits(answers));
        assertEquals(expected, numRabbits2(answers));
        assertEquals(expected, numRabbits3(answers));
    }

    @Test
    public void test() {
        test(new int[] {}, 0);
        test(new int[] {1, 1, 2}, 5);
        test(new int[] {10, 10, 10}, 11);
        test(new int[] {0, 0, 1, 1, 1}, 6);
        test(new int[] {1, 0, 1, 0, 0}, 5);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
