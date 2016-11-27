import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC334: https://leetcode.com/problems/increasing-triplet-subsequence/
//
// Given an unsorted array return whether an increasing subsequence of length 3
// exists or not in the array.
// Your algorithm should run in O(n) time complexity and O(1) space complexity.
public class IncreasingTriplet {
    // beats 40.56%(1 ms)
    public boolean increasingTriplet(int[] nums) {
        int n = nums.length;
        if (n < 3) return false;

        int first = nums[0];
        int i = 1;
        while (i < n && nums[i] <= first) {
            first = nums[i++];
        }
        if (i == n) return false;

        int second = nums[i];
        int firstCandidate = first;
        // keep two invariants:
        // index order: first < second < firstCandidate
        // value order: firstCandidate <= first < second
        while (++i < n) {
            int num = nums[i];
            if (num > second) return true;

            if (num <= firstCandidate) {
                firstCandidate = num;
            } else {
                second = num;
                first = firstCandidate;
            }
        }
        return false;
    }

    // Solution of Choice
    // beats 40.56%(1 ms)
    public boolean increasingTriplet2(int[] nums) {
        int first = Integer.MAX_VALUE;
        int second = Integer.MAX_VALUE;
        for (int num : nums) {
            if (num <= first) {
                first = num;
            } else if (num <= second) {
                second = num;
            } else return true;
        }
        return false;
    }

    void test(boolean expected, int... nums) {
        assertEquals(expected, increasingTriplet(nums));
        assertEquals(expected, increasingTriplet2(nums));
    }

    @Test
    public void test1() {
        test(true, 1, 2, 3, 4, 5);
        test(true, 1, 3, 2, 1, 5);
        test(false, 5, 4, 3, 2, 1);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("IncreasingTriplet");
    }
}
