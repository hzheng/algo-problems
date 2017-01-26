import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Utils;

// LC491: https://leetcode.com/problems/increasing-subsequences/
//
// Given an integer array, your task is to find all the different possible
// increasing subsequences of the given array, and the length of an increasing
// subsequence should be at least 2 .
public class IncreasingSubsequences {
    // Recursion + DFS + Backtracking + Set
    // beats 49.42%(54 ms for 57 tests)
    public List<List<Integer> > findSubsequences(int[] nums) {
        Set<List<Integer> > res = new HashSet<>();
        findSubsequences(nums, 0, new ArrayList<>(), res);
        return new ArrayList<>(res);
    }

    private void findSubsequences(int[] nums, int index, List<Integer> path, Set<List<Integer> > res) {
        int size = path.size();
        if (size > 1) {
            res.add(new ArrayList<>(path));
        }
        for (int i = index; i < nums.length; i++) {
            if (size == 0 || path.get(size - 1) <= nums[i]) {
                // if (index == 0 || nums[index - 1] <= nums[i]) { // slower???
                path.add(nums[i]);
                findSubsequences(nums, i + 1, path, res);
                path.remove(size);
            }
        }
    }

    // Recursion + DFS + Backtracking + Set(or List)
    // beats 89.77%(21 ms for 57 tests)
    public List<List<Integer> > findSubsequences2(int[] nums) {
        List<List<Integer> > res = new ArrayList<>();
        findSubsequences(nums, 0, new ArrayList<>(), res);
        return res;
    }

    private void findSubsequences(int[] nums, int index, List<Integer> path, List<List<Integer> > res) {
        int size = path.size();
        if (size > 1) {
            res.add(new ArrayList<>(path));
        }
        List<Integer> uniq = new ArrayList<>();
        // Set<Integer> uniq = new HashSet<>(); // slower???
        for (int i = index; i < nums.length; i++) {
            int num = nums[i];
            if ((size == 0 || path.get(size - 1) <= num) && !uniq.contains(num)) {
                uniq.add(num);
                path.add(num);
                findSubsequences(nums, i + 1, path, res);
                path.remove(size);
            }
        }
    }

    void test(int[] nums, int[][] expected) {
        Arrays.sort(expected, new Utils.IntArrayComparator());
        assertArrayEquals(expected, Utils.toSortedInts(findSubsequences(nums)));
        assertArrayEquals(expected, Utils.toSortedInts(findSubsequences2(nums)));
    }

    @Test
    public void test() {
        test(new int[] {1, 4, 2, 3},
             new int[][] {{1, 4}, {1, 2}, {1, 3}, {2, 3}, {1, 2, 3}});
        test(new int[] {4}, new int[][] {});
        test(new int[] {4, 6, 7, 7},
             new int[][] {{4, 6}, {4, 7}, {4, 6, 7}, {4, 6, 7, 7}, {6, 7},
                          {6, 7, 7}, {7,7}, {4,7,7}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("IncreasingSubsequences");
    }
}
