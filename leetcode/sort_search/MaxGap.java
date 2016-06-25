import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.function.Function;

// https://leetcode.com/problems/maximum-gap/
//
// Given an unsorted array, find the maximum difference between the successive
// elements in its sorted form. Try to solve it in linear time/space.
// Return 0 if the array contains less than 2 elements.
// Assume all elements in the array are non-negative integers and fit in the
// 32-bit signed integer range.
public class MaxGap {
    // beats 84.93%(4 ms)
    public int maximumGap(int[] nums) {
        int n = nums.length;
        if (n < 2) return 0;

        int min = nums[0];
        int max = min;
        for (int num : nums) {
            if (num < min) {
                min = num;
            } else if (num > max) {
                max = num;
            }
        }

        int bucketSize = Math.max((max - min) / (n - 1), 1);
        int bucketCount = (max - min) / bucketSize + 1;
        int[][] buckets = new int[bucketCount][];
        for (int num : nums) {
            int index = (num - min) / bucketSize;
            if (buckets[index] == null) {
                buckets[index] = new int[]{num, num};
            } else {
                int[] range = buckets[index];
                range[0] = Math.min(range[0], num);
                range[1] = Math.max(range[1], num);
            }
        }
        int lastBound = -1;
        int maxGap = 0;
        // max gap must be in different buckets
        for (int[] range : buckets) {
            if (range != null) {
                if (lastBound >= 0) {
                    maxGap = Math.max(maxGap, range[0] - lastBound);
                }
                lastBound = range[1];
            }
        }
        return maxGap;
    }

    void test(Function<int[], Integer> maxGap, int expected, int... nums) {
        assertEquals(expected, (int)maxGap.apply(nums));
    }

    void test(int expected, int ... nums) {
        MaxGap m = new MaxGap();
        test(m::maximumGap, expected, nums);
    }

    @Test
    public void test1() {
        test(0, 2, 2, 2, 2);
        test(3, 3, 2, 1, 5, 7, 6, 9, 12, 14);
        test(5, 2, 5, 7, 10, 8, 13, 21, 10, 18);
        test(2901, 15252,16764,27963,7817,26155,20757,3478,22602,20404,6739,16790,10588,16521,6644,20880,15632,27078,25463,20124,15728,30042,16604,17223,4388,23646,32683,23688,12439,30630,3895,7926,22101,32406,21540,31799,3768,26679,21799,23740);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxGap");
    }
}
