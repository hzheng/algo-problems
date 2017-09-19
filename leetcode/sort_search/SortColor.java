import org.junit.Test;
import static org.junit.Assert.*;

// LC075: https://leetcode.com/problems/sort-colors/
//
// Given an array with n objects colored red, white or blue, sort them so that
// objects of the same color are adjacent, with the colors in the order red,
// white and blue.
public class SortColor {
    // Solution of Choice
    // Two Pointers
    // beats 53.70%(0 ms)
    public void sortColors(int[] nums) {
        for (int cur = 0, red = 0, blue = nums.length - 1; cur <= blue; cur++) {
            if (nums[cur] == 0) {
                swap(nums, cur, red++);
            } else if (nums[cur] == 2) {
                swap(nums, cur--, blue--);
            }
        }
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    // Three Pointers
    // http://www.lifeincode.net/programming/leetcode-sort-colors-java/
    // beats 53.70%(0 ms)
    public void sortColors2(int[] nums) {
        int redPos = 0;
        int whitePos = 0;
        int bluePos = 0;
        for(int i = 0; i < nums.length; i++) {
            switch (nums[i]) {
            case 0:
                nums[bluePos++] = 2;
                nums[whitePos++] = 1;
                nums[redPos++] = 0;
                break;
            case 1:
                nums[bluePos++] = 2;
                nums[whitePos++] = 1;
                break;
            case 2:
                nums[bluePos++] = 2;
            }
        }
    }

    @FunctionalInterface
    interface Function<A> {
        public void apply(A a);
    }

    void test(Function<int[]> sortColors, int[] nums, int ... expected) {
        int[] nums2 = nums.clone();
        sortColors.apply(nums2);
        assertArrayEquals(expected, nums2);
    }

    void test(int[] nums, int ... expected) {
        SortColor s = new SortColor();
        test(s::sortColors, nums, expected);
        test(s::sortColors2, nums, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1, 0}, 0, 1);
        test(new int[] {0, 2, 0, 0}, 0, 0, 0, 2);
        test(new int[] {0, 2, 0, 2}, 0, 0, 2, 2);
        test(new int[] {0}, 0);
        test(new int[] {1}, 1);
        test(new int[] {2}, 2);
        test(new int[] {1, 1}, 1, 1);
        test(new int[] {0, 1, 2, 0, 1, 1, 1, 2, 1, 0, 2, 2},
             0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SortColor");
    }
}
