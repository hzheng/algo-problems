import java.util.Arrays;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// Follow up for "Remove Duplicates":
// What if duplicates are allowed at most twice?
public class RemoveDup2 {
    // beats 62.51%
    public int removeDuplicates(int[] nums) {
        if (nums.length < 2) return nums.length;

        int end = 0;
        boolean dup = false;
        for (int cur = 1; cur < nums.length; cur++) {
            if (dup && nums[cur] == nums[end]) continue;

            dup = (nums[cur] == nums[end]);
            nums[++end] = nums[cur];
        }
        return end + 1;
    }

    // beats 6.71%
    public int removeDuplicates2(int[] nums) {
        if (nums.length < 3) return nums.length;

        int end = 1;
        for (int cur = 2; cur < nums.length; cur++) {
            if (nums[cur] != nums[end - 1]) {
                nums[++end] = nums[cur];
            }
        }
        return end + 1;
    }

    void test(Function<int[], Integer> removeDup, int [] nums, int[] expected) {
        nums = nums.clone();
        int count = removeDup.apply(nums);
        int[] removed = Arrays.copyOf(nums, count);
        // System.out.println(Arrays.toString(removed));
        assertArrayEquals(expected, removed);
    }

    void test(int[] nums, int[] expected) {
        RemoveDup2 rm = new RemoveDup2();
        test(rm::removeDuplicates, nums, expected);
        test(rm::removeDuplicates2, nums, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1, 1}, new int[] {1, 1});
        test(new int[] {1, 2, 2}, new int[] {1, 2, 2});
        test(new int[] {1, 2, 3}, new int[] {1, 2, 3});
        test(new int[] {1, 1, 1, 2, 2, 3}, new int[] {1, 1, 2, 2, 3});
        test(new int[] {1, 1, 1, 1, 3, 3}, new int[] {1, 1, 3, 3});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RemoveDup2");
    }
}
