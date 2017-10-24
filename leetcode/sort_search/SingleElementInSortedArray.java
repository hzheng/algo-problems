import org.junit.Test;
import static org.junit.Assert.*;

// LC540: https://leetcode.com/problems/single-element-in-a-sorted-array/
//
// Given a sorted array consisting of only integers where every element appears
// twice except for one element which appears once. Find this single element
// that appears only once.
// Note: Note: Your solution should run in O(log n) time and O(1) space.
public class SingleElementInSortedArray {
    // Binary Search
    // time complexity: O(long(N)), space complexity: O(1)
    // beats 14.68%(1 ms for 7 tests)
    public int singleNonDuplicate(int[] nums) {
        for (int low = 0, high = nums.length - 1;; ) {
            int mid = (low + high) >>> 1;
            if (low >= high || (nums[mid] != nums[mid - 1])
                && (nums[mid] != nums[mid + 1])) {
                return nums[mid];
            }
            if (nums[mid] == nums[mid - 1]) {
                if ((mid - low) % 2 == 1) {
                    low = mid + 1;
                } else {
                    high = mid - 2;
                }
            } else if ((high - mid) % 2 == 1) {
                high = mid - 1;
            } else {
                low = mid + 2;
            }
        }
    }

    // Binary Search
    // time complexity: O(long(N)), space complexity: O(1)
    // beats 14.68%(1 ms for 7 tests)
    public static int singleNonDuplicate2(int[] nums) {
        int low = 0;
        for (int high = nums.length - 1; low < high; ) {
            // first element of the middle pair
            int mid = ((low + high) >>> 1) & (~1);
            if (nums[mid] == nums[mid + 1]) {
                low = mid + 2;
            } else {
                high = mid;
            }
        }
        return nums[low];
    }

    // Binary Search
    // time complexity: O(long(N)), space complexity: O(1)
    // beats 14.68%(1 ms for 7 tests)
    public static int singleNonDuplicate3(int[] nums) {
        int low = 0;
        for (int high = nums.length - 1; low < high; ) {
            int mid = (low + high) >>> 1;
            if (nums[mid] == nums[mid ^ 1]) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return nums[low];
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, singleNonDuplicate(nums));
        assertEquals(expected, singleNonDuplicate2(nums));
        assertEquals(expected, singleNonDuplicate3(nums));
    }

    @Test
    public void test() {
        test(new int[] {1}, 1);
        test(new int[] {1, 1, 2}, 2);
        test(new int[] {1, 1, 2, 3, 3, 4, 4, 8, 8}, 2);
        test(new int[] {3, 3, 7, 7, 10, 11, 11}, 10);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
