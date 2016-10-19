import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC215: https://leetcode.com/problems/kth-largest-element-in-an-array/
//
// Find the kth largest element in an unsorted array. Note that it is the kth
// largest element in the sorted order, not the kth distinct element.
public class KthLargest {
    // Heap
    // time complexity: O(N * log(K)), space complexity: O(K)
    // beats 70.77%(9 ms for 31 tests)
    public int findKthLargest(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(k);
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

    // Solution of Choice
    // Heap
    // time complexity: O(N * log(K)), space complexity: O(K)
    // beats 55.60%(15 ms for 31 tests)
    public int findKthLargest1(int[] nums, int k) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(k + 1);
        for (int num : nums) {
            pq.offer(num);
            if (pq.size() > k) {
                pq.poll();
            }
        }
        return pq.peek();
    }

    // Sort
    // time complexity: O(N log(N)), space complexity: O(N)
    // beats 87.26%(3 ms for 31 tests)
    public int findKthLargest2(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length - k];
    }

    // Solution of Choice
    // Divide & Conquer(Median of medians)
    // time complexity: O(N), space complexity: O(N)
    // beats 78.14%(6 ms for 31 tests)
    public int findKthLargest3(int[] nums, int k) {
        int n = nums.length;
        return findKthSmallest(nums, 0, n - 1, n - k + 1);
    }

    private int findKthSmallest(int[] nums, int start, int end, int k) {
        int len = end - start + 1;
        int pivot = nums[findPivot(nums, start, end, len / 2 + 1)];
        int i = start;
        for (int j = end; i < j; ) {
            for (; i < j && nums[i] < pivot; i++) {}
            for (; i < j && nums[j] > pivot; j--) {}
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
            return start + k - 1;
        }

        for (int i = 0; i < len / 5; i++) {
            int left = start + 5 * i;
            int right = Math.min(left + 4, end);
            int median = findPivot(nums, left, right, 3);
            swap(nums, median, start + i); // put median at start
        }
        return findPivot(nums, start, start + len / 5, len / 10 + 1);
    }

    // Divide & Conquer(QuickSelect)
    // https://en.wikipedia.org/wiki/Quickselect
    // average time complexity: O(N), space complexity: O(N)
    // beats 36.91%(41 ms for 31 tests)
    public int findKthLargest4(int[] nums, int k) {
        int n = nums.length;
        return nums[quickSelect(nums, 0, n - 1, n - k + 1)];
    }

    private int quickSelect(int[] nums, int low, int high, int k) {
        int i = low;
        for (int j = high, pivot = nums[high]; i < j; ) {
            if (nums[i++] > pivot) {
                swap(nums, --i, --j);
            }
        }
        swap(nums, i, high);
        int smallCount = i - low + 1;
        if (smallCount == k) return i;

        return (smallCount > k) ? quickSelect(nums, low, i - 1, k)
               : quickSelect(nums, i + 1, high, k - smallCount);
    }

    // Solution of Choice
    // Iterative QuickSelect
    // average time complexity: O(N), space complexity: O(N)
    // beats 80.35%(5 ms for 31 tests)
    // beats 45.13%(23 ms for 31 tests) (without shuffle)
    public int findKthLargest5(int[] nums, int k) {
        shuffle(nums);
        int index = nums.length - k;
        for (int start = 0, end = nums.length - 1; ; ) {
            int pivot = partition(nums, start, end);
            if (pivot < index) {
                start = pivot + 1;
            } else if (pivot > index) {
                end = pivot - 1;
            } else return nums[index];
        }
    }

    private int partition(int[] nums, int start, int end) {
        int i = start;
        for (int j = end, pivot = nums[end]; i < j; ) {
            if (nums[i++] > pivot) {
                swap(nums, --i, --j);
            }
        }
        swap(nums, i, end);
        return i;
    }

    private void shuffle(int nums[]) {
        Random random = new Random();
        for (int i = 1; i < nums.length; i++) {
            swap(nums, i, random.nextInt(i + 1));
        }
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    void test(int[] nums, int k, int expected) {
        assertEquals(expected, findKthLargest(nums.clone(), k));
        assertEquals(expected, findKthLargest1(nums.clone(), k));
        assertEquals(expected, findKthLargest2(nums.clone(), k));
        assertEquals(expected, findKthLargest3(nums.clone(), k));
        assertEquals(expected, findKthLargest4(nums.clone(), k));
        assertEquals(expected, findKthLargest5(nums.clone(), k));
    }

    @Test
    public void test1() {
        test(new int[] {2, 1}, 2, 1);
        test(new int[] {2, 1}, 1, 2);
        test(new int[] {3, 3, 4}, 1, 4);
        test(new int[] {3, 3, 3, 3, 4, 3, 3, 3, 3}, 1, 4);
        test(new int[] {99, 99}, 1, 99);
        test(new int[] {3, 2, 1, 5, 6, 4}, 2, 5);
        test(new int[] {3, 2, 1, 5, 6, 4, 6}, 2, 6);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("KthLargest");
    }
}
