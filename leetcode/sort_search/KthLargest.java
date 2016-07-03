import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/kth-largest-element-in-an-array/
//
// Find the kth largest element in an unsorted array. Note that it is the kth
// largest element in the sorted order, not the kth distinct element.
public class KthLargest {
    // min heap
    // time complexity: O(N * log(K)), space complexity: O(K)
    // beats 71.82%(7 ms)
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (int i = 0; i < k; i++) {
            pq.offer(nums[i]);
        }
        for (int i = nums.length - 1; i >= k; i--) {
            if (pq.peek() < nums[i]) {
                pq.poll();
                pq.offer(nums[i]);
            }
        }
        return pq.peek();
    }

    // sort
    // time complexity: O(N log(N)), space complexity: O(N)
    // beats 79.20%(4 ms)
    public int findKthLargest2(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length - k];
    }

    // divide and conquer(Median of medians)
    // time complexity: O(N), space complexity: O(N)
    // beats 69.64%(8 ms)
    public int findKthLargest3(int[] nums, int k) {
        int n = nums.length;
        return findKthSmallest(nums, 0, n - 1, n - k + 1);
    }

    private int findKthSmallest(int[] nums, int start, int end, int k) {
        int len = end - start + 1;
        int pivotIndex = findPivot(nums, start, end, len / 2 + 1);
        int pivot = nums[pivotIndex];
        int i = start;
        for (int j = end; i < j; ) {
            while (i < j && nums[i] < pivot) {
                i++;
            }
            while (i < j && nums[j] > pivot) {
                j--;
            }
            if (i < j) {
                swap(nums, i++, j--);
            }
        }
        if (i + 1 == k + start) return pivot;

        if (i + 1 > k + start) return findKthSmallest(nums, start, i - 1, k);

        return findKthSmallest(nums, i, end, k - i + start);
    }

    private int findPivot(int[] nums, int start, int end, int k) {
        int len = end - start + 1;
        if (len <= 5) {
            Arrays.sort(nums, start, end + 1);
            return Math.max(0, start + k - 1);
        }

        for (int i = 0; i < len / 5; i++) {
            int left = start + 5 * i;
            int right = Math.min(left + 4, end);
            int median = findPivot(nums, left, right, 3);
            swap(nums, median, start + i); // put median at start
        }
        return findPivot(nums, start, start + len / 5, len / 10 + 1);
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    void test(int[] nums, int k, int expected) {
        assertEquals(expected, findKthLargest(nums.clone(), k));
        assertEquals(expected, findKthLargest2(nums.clone(), k));
        assertEquals(expected, findKthLargest3(nums.clone(), k));
    }

    @Test
    public void test1() {
        test(new int[] {2, 1}, 2, 1);
        test(new int[] {2, 1}, 1, 2);
        test(new int[]{3, 3, 4}, 1, 4);
        test(new int[]{3, 3, 3, 3, 4, 3, 3, 3, 3}, 1, 4);
        test(new int[] {99, 99}, 1, 99);
        test(new int[] {3, 2, 1, 5, 6, 4}, 2, 5);
        test(new int[] {3, 2, 1, 5, 6, 4, 6}, 2, 6);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("KthLargest");
    }
}
