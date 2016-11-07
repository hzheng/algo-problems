import java.util.*;
import java.util.stream.IntStream;

import org.junit.Test;
import static org.junit.Assert.*;

// LC453: https://leetcode.com/problems/minimum-moves-to-equal-array-elements/
//
// Given a non-empty integer array of size n, find the minimum number of moves
// required to make all array elements equal, where a move is incrementing n - 1
// elements by 1.
public class MinMoves {
    // beats N/A(114 ms for 84 tests)
    public int minMoves(int[] nums) {
        SortedMap<Integer, Integer> countMap = new TreeMap<>();
        for (int num : nums) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }
        int count = 0;
        int target = countMap.firstKey();
        for (int key : countMap.keySet()) {
            count += (key - target) * countMap.get(key);
        }
        return count;
    }

    // beats N/A(10 ms for 84 tests)
    public int minMoves2(int[] nums) {
        int min = Integer.MAX_VALUE;
        for (int num : nums) {
            min = Math.min(min, num);
        }
        int count = 0;
        for (int num : nums) {
            count += (num - min);
        }
        return count;
    }

    // beats N/A(15 ms for 84 tests)
    public int minMoves3(int[] nums) {
        int min = Integer.MAX_VALUE;
        int sum = 0;
        for (int num : nums) {
            min = Math.min(min, num);
            sum += num;
        }
        return sum - min * nums.length;
    }

    // beats N/A(143 ms for 84 tests)
    public int minMoves4(int[] nums) {
        return IntStream.of(nums).sum()
               - nums.length * IntStream.of(nums).min().getAsInt();
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, minMoves(nums));
        assertEquals(expected, minMoves2(nums));
        assertEquals(expected, minMoves3(nums));
        assertEquals(expected, minMoves4(nums));
    }

    @Test
    public void test() {
        test(new int[] {1, 2, 3}, 3);
        test(new int[] {6, 5, 4, 4}, 3);
        test(new int[] {6, 5, 4, 4, 2}, 11);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MinMoves");
    }
}
