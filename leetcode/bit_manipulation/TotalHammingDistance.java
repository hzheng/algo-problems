import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC477: https://leetcode.com/problems/total-hamming-distance/
//
// Find the total Hamming distance between all pairs of the given numbers.
public class TotalHammingDistance {
    // beats N/A(42 ms for 47 tests)
    public int totalHammingDistance(int[] nums) {
        int total = 0;
        int n = nums.length;
        for (int mask = 1 << 30; mask != 0; mask >>= 1) {
            int ones = 0;
            for (int num : nums) {
                if ((num & mask) != 0) {
                    ones++;
                }
            }
            total += ones * (n - ones);
        }
        return total;
    }

    // beats N/A(25 ms for 47 tests)
    public int totalHammingDistance2(int[] nums) {
        int total = 0;
        int n = nums.length;
        for (int i = 0; i < 31; i++) {
            int ones = 0;
            for (int num : nums) {
                ones += ((num >> i) & 1);
            }
            total += ones * (n - ones);
        }
        return total;
    }

    // beats N/A(31 ms for 47 tests)
    public int totalHammingDistance3(int[] nums) {
        int[][] bitCounts = new int[31][2];
        int total = 0;
        for (int x : nums) {
            for (int i = 0; i < 31; i++) {
                int bit = (x >> i) & 1;
                bitCounts[i][bit]++;
                total += bitCounts[i][bit ^ 1];
            }
        }
        return total;
    }

    // Time Limit Exceeded
    public int totalHammingDistance0(int[] nums) {
        int n = nums.length;
        int total = 0;
        Map<Integer, Integer> memo = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                total += hammingWeight(nums[i] ^ nums[j], memo);
            }
        }
        return total;
    }

    private int hammingWeight(int x, Map<Integer, Integer> memo) {
        if (memo.containsKey(x)) {
            return memo.get(x);
        }
        int count = 0;
        for (int a = x; a != 0; a &= (a - 1)) {
            count++;
        }
        memo.put(x, count);
        return count;
    }

    void test(int expected, int ... nums) {
        assertEquals(expected, totalHammingDistance(nums));
        assertEquals(expected, totalHammingDistance0(nums));
        assertEquals(expected, totalHammingDistance2(nums));
        assertEquals(expected, totalHammingDistance3(nums));
    }

    @Test
    public void test() {
        test(7, 0b10100111001, 0b1110010100011);
        test(7, 1337, 7331);
        test(6, 0b101, 0b100, 0b1110);
        test(6, 4, 14, 2);
        test(8, 0b1101, 0b100, 0b1010);
        test(14, 0b1001, 0b1101, 0b100, 0b1010);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("TotalHammingDistance");
    }
}
