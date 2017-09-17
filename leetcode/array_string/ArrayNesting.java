import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC565: https://leetcode.com/problems/array-nesting/
//
// A zero-indexed array A consisting of N different integers is given. The array
// contains all integers in the range [0, N - 1].
// Sets S[K] for 0 <= K < N are defined as follows:
// S[K] = { A[K], A[A[K]], A[A[A[K]]], ... }.
// Sets S[K] are finite for each K and should NOT contain duplicates.
// Write a function that given an array A consisting of N integers, return the
// size of the largest set S[K] for this array.
// Note:
// N is an integer within the range [1, 20,000].
// The elements of A are all distinct.
// Each element of array A is an integer within the range [0, N-1].
public class ArrayNesting {
    // Union Find
    // time complexity: O(N), space complexity: O(N)
    // beats 76.70%(41 ms for 856 tests)
    public int arrayNesting(int[] nums) {
        int n = nums.length;
        int[] id = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
        }
        for (int i = 0; i < n; i++) {
            id[getRoot(id, nums[i])] = getRoot(id, i);
        }
        int max = 0;
        int[] count = new int[n];
        for (int i = 0; i < n; i++) {
            max = Math.max(max, ++count[getRoot(id, i)]);
        }
        return max;
    }

    private int getRoot(int[] id, int p) {
        for (; id[p] != p; p = id[p] = id[id[p]]) {}
        return p;
    }

    // time complexity: O(N), space complexity: O(N)
    // beats 69.23%(43 ms for 856 tests)
    public int arrayNesting2(int[] nums) {
        boolean[] visited = new boolean[nums.length];
        int res = 0;
        for (int i = 0; i < nums.length; i++) {
            if (visited[i]) continue;

            int count = 1;
            for (int start = nums[i]; ; count++) {
                start = nums[start];
                visited[start] = true;
                if (start == nums[i]) break;
            }
            res = Math.max(res, count);
        }
        return res;
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, arrayNesting(nums));
        assertEquals(expected, arrayNesting2(nums));
        // assertEquals(expected, arrayNesting3(nums));
    }

    @Test
    public void test() {
        test(new int[] {0, 1}, 1);
        test(new int[] {1, 0}, 2);
        test(new int[] {5, 4, 0, 3, 1, 6, 2}, 4);
        test(new int[] {7, 5, 4, 9, 3, 1, 2, 8, 0, 6}, 5);
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
