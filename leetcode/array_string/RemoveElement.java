import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.*;

// Given an array and a value, remove all instances of that value in place and
// return the new length. Do not allocate extra space for another array,
// The order of elements can be changed.
public class RemoveElement {
    // beats 70.92%
    public int removeElement(int[] nums, int val) {
        int len = nums.length;
        if (len < 1) return 0;

        int begin = 0;
        for (int end = len - 1; begin <= end; ++begin) {
            if (nums[begin] == val) {
                while (end > begin && nums[end] == val) end--;
                if (end == begin) break;

                nums[begin] = nums[end--];
            }
        }
        return begin;
    }

    // from solution
    // beat 3.73%
    public int removeElement2(int[] nums, int val) {
        int i = 0;
        for (int j = 0; j < nums.length; j++) {
            if (nums[j] != val) {
                nums[i] = nums[j];
                i++;
            }
        }
        return i;
    }

    // from solution
    // beat 3.73%
    public int removeElement3(int[] nums, int val) {
        int i = 0;
        int n = nums.length;
        while (i < n) {
            if (nums[i] == val) {
                nums[i] = nums[n - 1];
                n--;
            } else {
                i++;
            }
        }
        return n;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<int[], Integer, Integer> remove, int [] nums, int val,
              int[] expected) {
        nums = nums.clone();
        int count = remove.apply(nums, val);
        int[] removed = Arrays.copyOf(nums, count);
        Arrays.sort(removed);
        assertArrayEquals(expected, removed);
    }

    void test(int[] nums, int val, int[] expected) {
        RemoveElement rm = new RemoveElement();
        test(rm::removeElement, nums, val, expected);
        test(rm::removeElement2, nums, val, expected);
        test(rm::removeElement3, nums, val, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1, 1}, 1, new int[] {});
        test(new int[] {1, 1, 2}, 1, new int[] {2});
        test(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9}, 3,
             new int[] {1, 2, 4, 5, 6, 7, 8, 9});
        test(new int[] {1, 2, 2, 4, 6, 6, 8, 8, 9}, 6,
             new int[] {1, 2, 2, 4, 8, 8, 9});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RemoveElement");
    }
}
