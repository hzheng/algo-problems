import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/count-of-smaller-numbers-after-self/
//
// You are given an integer array nums and you have to return a new counts array.
// The counts array has the property where counts[i] is the number of smaller
// elements to the right of nums[i].
public class CountSmaller {
    // Binary Indexed Tree
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats 46.27%(20 ms)
    public List<Integer> countSmaller(int[] nums) {
        int n = nums.length;
        if (n == 0) return Collections.emptyList();

        int[] buffer = nums.clone();
        Arrays.sort(buffer);
        // change comparison from numbers themselves to their places
        Map<Integer, Integer> place = new HashMap<>();
        for (int i = 0; i < n; i++) {
            place.put(buffer[i], i + 1);
        }

        buffer[n - 1] = 0; // buffer is now used to save result
        int[] bit = new int[n + 1];
        for (int i = n - 2; i >= 0; i--) {
            for (int j = place.get(nums[i + 1]); j <= n; j += (j & -j)) {
                bit[j]++;
            }
            int count = 0;
            for (int j = place.get(nums[i]) - 1; j > 0; j -= (j & -j)) {
                count += bit[j]; // "-1" to skip all equal values
            }
            buffer[i] = count;
        }

        List<Integer> res = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            res.add(buffer[i]);
        }
        return res;
    }

    void test(int[] nums, Integer... expected) {
        assertArrayEquals(expected, countSmaller(nums).toArray(new Integer[0]));
    }

    @Test
    public void test1() {
        test(new int[] {5, 2, 6, 1}, 2, 1, 1, 0);
        test(new int[] {5, 2, 8, 9, 7, 4, 3, 6, 1}, 4, 1, 5, 5, 4, 2, 1, 1, 0);
        test(new int[] {5, 2, 6, 3, 7, 4, 3, 6, 1}, 5, 1, 4, 1, 4, 2, 1, 1, 0);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CountSmaller");
    }
}
