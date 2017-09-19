import org.junit.Test;
import static org.junit.Assert.*;

// LC033: https://leetcode.com/problems/search-in-rotated-sorted-array/
//
// Suppose a sorted array is rotated at some pivot unknown to you beforehand.
// You may assume no duplicate exists in the array.
public class SearchRotatedSortedArray {
    // beats 5.36%(1 ms)
    public int search(int[] nums, int target) {
        // find pivot
        int left  = 0;
        int right = nums.length - 1;
        int pivot = 0;
        int first = nums[0];
        if (nums[right] < first) {
            while (right > left) {
                pivot = left + (right - left) / 2;
                if (nums[pivot] >= first) {
                    left = pivot + 1;
                } else {
                    right = pivot;
                }
            }
            pivot = (nums[right] >= nums[left]) ? left : right;
        }
        // determine bounds
        if (target >= first) {
            left = 0;
            right = pivot - 1;
            if (right < 0) {
                right += nums.length;
            }
        } else {
            left = pivot;
            right = nums.length - 1;
        }
        // search
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) return mid;

            if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }

    // beats 1.77%(2 ms)
    public int search2(int[] nums, int target) {
        int left = 0;
        int right = nums.length - 1;
        int first = nums[0];
        int last = nums[right];
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int midVal = nums[mid];
            if (target == midVal) return mid;

            if (target > midVal) {
                if (target <= last || midVal > last) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            } else {
                if (target >= first || midVal < first) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }
        return -1;
    }

    // from Cracking the Coding Interview(5ed) Problem
    // beats 5.36%(1 ms)
    public int search3(int[] nums, int target) {
        return search(nums, 0, nums.length - 1, target);
    }

    private int search(int nums[], int left, int right, int target) {
        int mid = (left + right) >>> 1;
        if (target == nums[mid]) return mid;

        if (right < left) return -1;

        if (nums[left] <= nums[mid]) { // take use of uniqueness of nums
            if (target >= nums[left] && target <= nums[mid]) {
                return search(nums, left, mid - 1, target);
            } else {
                return search(nums, mid + 1, right, target);
            }
        } else {
            if (target >= nums[mid] && target <= nums[right]) {
                return search(nums, mid + 1, right, target);
            } else {
                return search(nums, left, mid - 1, target);
            }
        }
    }

    // Solution of Choice
    // http://www.programcreek.com/2014/06/leetcode-search-in-rotated-sorted-array-java/
    // beats 5.36%(1 ms)
    public int search4(int[] nums, int target) {
        for (int left = 0, right = nums.length - 1; left <= right; ) {
            int mid = (left + right) >>> 1;
            int midVal = nums[mid];
            if (target == midVal) return mid;

            if (nums[left] > midVal) {
                if (midVal < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            } else {
                if (nums[left] <= target && target < midVal) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }
        return -1;
    }

    // https://discuss.leetcode.com/topic/28367/c-4-lines-4ms
    // beats 5.36%(1 ms)
    public int search5(int[] nums, int target) {
        for (int low = 0, high = nums.length; low < high; ) {
            int mid = (low + high) >>> 1;
            int num = (nums[mid] < nums[0]) ^ (target < nums[0])
                         ? target < nums[0] ? Integer.MIN_VALUE : Integer.MAX_VALUE
                         : nums[mid];
            if (num < target) {
                low = mid + 1;
            } else if (num > target) {
                high = mid;
            } else return mid;
        }
        return -1;
    }

    void test(int expected, int target, int ... nums) {
        assertEquals(expected, search(nums, target));
        assertEquals(expected, search2(nums, target));
        assertEquals(expected, search3(nums, target));
        assertEquals(expected, search4(nums, target));
        assertEquals(expected, search5(nums, target));
    }

    @Test
    public void test1() {
        test(0, 1, 1, 3);
        test(1, 3, 1, 3);
        test(4, 0, 4, 5, 6, 7, 0, 1, 2);
        test(4, 8, 4, 5, 6, 7, 8, 1, 2, 3);
        test(2, 1, 3, 5, 1);
        test(1, 1, 3, 1);
        test(1, 1, 3, 1, 2);
        test(0, 5, 5, 1, 3);
        test(-1, 3, 4, 5, 6, 7, 8, 9);
        test(1, 5, 4, 5, 6, 7, 8, 9);
        test(3, 7, 4, 5, 6, 7, 0, 1, 2);
        test(-1, 3, 4, 5, 6, 7, 0, 1, 2);
        test(3, 7, 4, 5, 6, 7, 8, 0, 1, 2, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SearchRotatedSortedArray");
    }
}
