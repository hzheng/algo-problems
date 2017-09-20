import java.util.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC442: https://leetcode.com/problems/find-all-duplicates-in-an-array/
//
// Given an array of integers, 1 ≤ a[i] ≤ n (n = size of array), some elements
// appear twice and others appear once.
// Find all the elements that appear twice in this array.
// Could you do it without extra space and in O(n) runtime?
public class FindDuplicates {
    // beats 17.68%(40 ms for 27 tests)
    public List<Integer> findDuplicates(int[] nums) {
        Set<Integer> res = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            if (num == i + 1) {
            } else if (nums[num - 1] == num) {
                res.add(num);
            } else {
                swap(nums, i, num - 1);
                i--;
            }
        }
        return new ArrayList<>(res);
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    // beats 6.193%(17 ms for 27 tests)
    public List<Integer> findDuplicates2(int[] nums) {
        List<Integer> res = new ArrayList<>();
        for (int num : nums) {
            int pos = Math.abs(num) - 1;
            if (nums[pos] < 0) {
                res.add(pos + 1);
            } else {
                nums[pos] = -nums[pos];
            }
        }
        /*
        for (int i = 0; i < nums.length; i++) { // restore
            nums[i] = Math.abs(nums[i]);
        }*/
        return res;
    }

    void test(int[] nums, Integer ... expected) {
        FindDuplicates f = new FindDuplicates();
        test(f::findDuplicates, nums.clone(), expected);
        test(f::findDuplicates2, nums.clone(), expected);
    }

    void test(Function<int[], List<Integer> > findDuplicates,
              int[] nums, Integer ... expected) {
        List<Integer> res = findDuplicates.apply(nums);
        assertArrayEquals(expected, res.toArray(new Integer[0]));
    }

    @Test
    public void test1() {
        test(new int[] {4, 3, 2, 7, 8, 2, 3, 1}, 2, 3);
        test(new int[] {4, 3, 2, 6, 3, 1}, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FindDuplicates");
    }
}
