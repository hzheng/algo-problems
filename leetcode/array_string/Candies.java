import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC575: https://leetcode.com/problems/distribute-candies/
//
// Given an integer array with even length, where different numbers in this array
// represent different kinds of candies. Each number means one candy of the corresponding
// kind. You need to distribute these candies equally in number to brother and sister.
// Return the maximum number of kinds of candies the sister could gain.
public class Candies {
    // Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 35.93%(124 ms for 207 tests)
    public int distributeCandies(int[] candies) {
        Arrays.sort(candies);
        int count = 1;
        for (int i = 1; i < candies.length && count < candies.length / 2; i++) {
            if (candies[i] != candies[i - 1]) {
                count++;
            }
        }
        return count;
    }

    // Set
    // time complexity: O(N), space complexity: O(N)
    // beats 76.34%(99 ms for 207 tests)
    public int distributeCandies2(int[] candies) {
        Set<Integer> set = new HashSet<>();
        for (int candy : candies) {
            set.add(candy);
        }
        return Math.min(set.size(), candies.length / 2);
    }

    void test(int[] candies, int expected) {
        assertEquals(expected, distributeCandies(candies.clone()));
        assertEquals(expected, distributeCandies2(candies));
    }

    @Test
    public void test() {
        test(new int[] {1, 1, 2, 2, 3, 3}, 3);
        test(new int[] {1, 1, 2, 3}, 2);
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
