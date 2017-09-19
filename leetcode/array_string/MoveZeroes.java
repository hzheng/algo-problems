import org.junit.Test;
import static org.junit.Assert.*;

// LC283: https://leetcode.com/problems/move-zeroes/
//
// Given an array nums, write a function to move all 0's to the end of it
// while maintaining the relative order of the non-zero elements.
public class MoveZeroes {
    // beats 23.31%(1 ms)
    public void moveZeroes(int[] nums) {
        int n = nums.length;
        int leftShift = 0;
        for (int i = 0; i < n; i++) {
            int num = nums[i];
            if (num == 0) {
                leftShift++;
            } else if (leftShift > 0) {
                nums[i - leftShift] = num;
            }
        }
        for (int i = n - leftShift; i < n; i++) {
            nums[i] = 0;
        }
    }

    // Solution of Choice
    // beats 23.31%(1 ms)
    public void moveZeroes2(int[] nums) {
        int nonzeroPos = 0;
        for (int num : nums) {
            if (num != 0) {
                nums[nonzeroPos++] = num;
            }
        }
        for (int i = nums.length - 1; i >= nonzeroPos; i--) {
            nums[i] = 0;
        }
    }

    // beats 23.31%(1 ms)
    public void moveZeroes3(int[] nums) {
        for (int i = 0, j = 0; j < nums.length; j++) {
            if (nums[j] != 0) {
                swap(i++, j, nums);
            }
        }
    }

    private void swap(int i, int j, int[] nums) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    @FunctionalInterface
    interface Function<A> {
        public void apply(A a);
    }

    void test(Function<int[]> move, int[] nums, int ... expected) {
        int[] res = nums.clone();
        move.apply(res);
        assertArrayEquals(expected, res);
    }

    void test(int[] nums, int ... expected) {
        MoveZeroes m = new MoveZeroes();
        test(m::moveZeroes, nums, expected);
        test(m::moveZeroes2, nums, expected);
        test(m::moveZeroes3, nums, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1}, 1);
        test(new int[] {0}, 0);
        test(new int[] {0, 1, 0, 3, 12}, 1, 3, 12, 0, 0);
        test(new int[] {1, 1, 1, 1}, 1, 1, 1, 1);
        test(new int[] {1, 0, 0, 1}, 1, 1, 0, 0);
        test(new int[] {0, 0, 0, 0}, 0, 0, 0, 0);
        test(new int[] {0, 0, 0, 0, 2}, 2, 0, 0, 0, 0);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MoveZeroes");
    }
}
