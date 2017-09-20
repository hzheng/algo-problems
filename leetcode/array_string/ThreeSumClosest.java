import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC016: https://leetcode.com/problems/3sum-closest/
//
// Given an array of integers, find 3 s.t the sum is closest to a target.
public class ThreeSumClosest {
    // time complexity: O(N ^ 2)
    // beats 26.26%(15 ms)
    public int threeSumClosest(int[] nums, int target) {
        if (nums == null || nums.length < 3) return 0;

        Arrays.sort(nums);
        int minDiff = Integer.MAX_VALUE;
        for (int i = 0; i < nums.length - 2; i++) {
            if (i == 0 || nums[i] > nums[i - 1]) {
                int diff = twoSum(nums, i, target);
                if (Math.abs(diff) < Math.abs(minDiff)) {
                    minDiff = diff;
                }
            }
        }
        return minDiff + target;
    }

    private int twoSum(int[] nums, int start, int target) {
        target -= nums[start];
        int minDiff = Integer.MAX_VALUE;
        for (int i = start + 1, j = nums.length - 1; j > i; ) {
            int diff = nums[i] + nums[j] - target;
            if (Math.abs(diff) < Math.abs(minDiff)) {
                minDiff = diff;
            }
            if (diff == 0) return 0;

            if (diff > 0) {
                j--;
                while (i < j && nums[j] == nums[j + 1]) j--;
            } else {
                i++;
                while (i < j && nums[i] == nums[i - 1]) i++;
            }
        }
        return minDiff;
    }

    // Solution of Choice
    // beats 13.64%(20 ms)
    public int threeSumClosest2(int[] num, int target) {
		Arrays.sort(num);
		int closest = Integer.MAX_VALUE / 2; // otherwise it may overflow
		for (int i = 0; i < num.length - 2; i++) {
			int left = i + 1;
			int right = num.length - 1;
			while (left < right) {
				int sum = num[i] + num[left] + num[right];
				if (Math.abs(sum - target) < Math.abs(closest - target)) {
                    if (sum == target) return sum;

                    closest = sum;
                }
				if (sum < target) {
					left++;
                    // may improve performance
                    // while (left < right && num[left] == num[left - 1]) left++;
				} else {
					right--;
                    // may improve performance
                    // while (left < right && num[right] == num[right + 1]) right--;
				}
			}
		}
		return closest;
	}

    void test(int[] nums, int target, int expected) {
        assertEquals(expected, threeSumClosest(nums, target));
        assertEquals(expected, threeSumClosest2(nums, target));
    }

    @Test
    public void test1() {
        test(new int[] {-1, 2, 1, -4}, 1, 2);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ThreeSumClosest");
    }
}
