import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Given an array with n objects colored red, white or blue, sort them so that
// objects of the same color are adjacent, with the colors in the order red,
// white and blue.
public class SortColor {
    // beats 3.63%
    public void sortColors(int[] nums) {
        int redEnd = 0;
        int blueStart = nums.length - 1;
        // the following two while's can be omitted
        while (redEnd < blueStart && nums[redEnd] == 0) {
            redEnd++;
        }
        while (blueStart >= 0 && nums[blueStart] == 2) {
            blueStart--;
        }

        for (int i = redEnd; i <= blueStart; ) {
            switch (nums[i]) {
            case 0:
                swap(nums, i, redEnd++);
                i++;
                break;
            case 1:
                i++;
                break;
            case 2:
                swap(nums, i, blueStart--);
                break;
            }
        }
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    // http://www.lifeincode.net/programming/leetcode-sort-colors-java/
    // beats 3.63%
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

    void test(int[] nums, int ... expected) {
        int[] nums2 = nums.clone();
        sortColors(nums);
        assertArrayEquals(expected, nums);
        System.out.println(Arrays.toString(nums2));
        sortColors2(nums2);
        assertArrayEquals(expected, nums2);
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
