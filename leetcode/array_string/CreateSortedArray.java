import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1649: https://leetcode.com/problems/create-sorted-array-through-instructions/
//
// Given an integer array instructions, you are asked to create a sorted array from the elements in
// instructions. You start with an empty container nums. For each element from left to right in
// instructions, insert it into nums. The cost of each insertion is the minimum of the following:
// The number of elements currently in nums that are strictly less than instructions[i].
// The number of elements currently in nums that are strictly greater than instructions[i].
// For example, if inserting element 3 into nums = [1,2,3,5], the cost of insertion is min(2, 1)
// (elements 1 and 2 are less than 3, element 5 is greater than 3) and nums will become [1,2,3,3,5].
// Return the total cost to insert all elements from instructions into nums. Since the answer may be
// large, return it modulo 10^9 + 7
//
// Constraints:
// 1 <= instructions.length <= 10^5
// 1 <= instructions[i] <= 10^5
public class CreateSortedArray {
    private static final int MOD = 1_000_000_007;

    // Binary Indexed Tree + Sort
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 154 ms(36.79%), 62.5 MB(9.85%) for 94 tests
    public int createSortedArray(int[] instructions) {
        int n = instructions.length;
        int[] sorted = instructions.clone();
        Arrays.sort(sorted);
        Map<Integer, Integer> place = new HashMap<>();
        for (int i = 0; i < n; i++) {
            place.put(sorted[i], i + 1);
        }
        long res = 0;
        int[] bit = new int[n + 1];
        Map<Integer, Integer> freq = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int cur = instructions[i];
            for (int j = place.get(cur); j <= n; j += (j & -j)) {
                bit[j]++;
            }
            int same = freq.getOrDefault(cur, 0);
            freq.put(cur, same + 1);
            int smallCount = 0;
            for (int j = place.get(cur) - 1; j > 0; j -= (j & -j)) {
                smallCount += bit[j];
            }
            int bigCount = i - smallCount - same;
            res += Math.min(smallCount, bigCount);
        }
        return (int)(res % MOD);
    }

    // Binary Indexed Tree
    // time complexity: O(N*log(MAX)), space complexity: O(MAX)
    // 31 ms(98.45%), 51.2 MB(95.85%) for 94 tests
    public int createSortedArray2(int[] instructions) {
        long res = 0;
        int max = Arrays.stream(instructions).max().getAsInt();
        int[] bit = new int[max + 1];
        for (int i = 0, n = instructions.length; i < n; i++) {
            res += Math.min(count(bit, instructions[i] - 1), i - count(bit, instructions[i]));
            for (int j = instructions[i]; j <= max; j += (j & -j)) {
                bit[j]++;
            }
        }
        return (int)(res % MOD);
    }

    private int count(int[] bit, int x) {
        int res = 0;
        for (int i = x; i > 0; i -= (i & -i)) {
            res += bit[i];
        }
        return res;
    }

    private void test(int[] instructions, int expected) {
        assertEquals(expected, createSortedArray(instructions));
        assertEquals(expected, createSortedArray2(instructions));
    }

    @Test public void test() {
        test(new int[] {1, 5, 6, 2}, 1);
        test(new int[] {1, 2, 3, 6, 5, 4}, 3);
        test(new int[] {1, 3, 3, 3, 2, 4, 2, 1, 2}, 4);
        test(new int[] {100, 32423, 439, 1083, 78, 93, 32423, 45934, 152, 31, 31, 25, 43, 283, 1, 2,
                        92, 421, 872, 943, 1343, 932, 412, 53, 43, 64, 9, 7, 5234}, 80);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
