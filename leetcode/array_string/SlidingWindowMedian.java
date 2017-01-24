import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC480: https://leetcode.com/problems/sliding-window-median/
//
// Given an array nums, there is a sliding window of size k which is moving from
// the very left of the array to the very right. You can only see the k numbers
// in the window. Each time the sliding window moves right by one position. Your
// job is to output the median array for each window in the original array.
public class SlidingWindowMedian {
    // SortedSet
    // time complexity: O(N * log(K))
    // beats 79.77%(62 ms for 42 tests)
    public double[] medianSlidingWindow(int[] nums, int k) {
        class Entry implements Comparable<Entry> {
            long val;
            int index;
            Entry(int index) {
                this.index = index;
                this.val = nums[index];
            }

            public int compareTo(Entry other) {
                return val != other.val ? Long.compare(val, other.val)
                       : Integer.compare(index, other.index);
            }
        }

        int n = nums.length;
        double[] res = new double[n - k + 1];
        Entry[] win = new Entry[k];
        for (int i = 0; i < k; i++) {
            win[i] = new Entry(i);
        }
        Arrays.sort(win);
        Entry median = win[(k - 1) / 2];
        res[0] = (k & 1) == 1 ? median.val : (median.val + win[k / 2].val) / 2.0;
        NavigableSet<Entry> winSet = new TreeSet<>();
        for (int i = 0; i < k; i++) {
            winSet.add(new Entry(i));
        }
        for (int i = 1; i <= n - k; i++) {
            Entry oldItem = new Entry(i - 1);
            Entry newItem = new Entry(i + k - 1);
            winSet.remove(oldItem);
            winSet.add(newItem);
            if (oldItem.compareTo(median) * newItem.compareTo(median) <= 0) {
                median = newItem.compareTo(oldItem) > 0 ? winSet.higher(median) : winSet.lower(median);
            }
            res[i] = (k & 1) == 1 ? median.val : (median.val + winSet.higher(median).val) / 2.0;
        }
        return res;
    }

    // SortedSet
    // time complexity: O(N * log(K))
    // beats 78.20%(63 ms for 42 tests)
    public double[] medianSlidingWindow2(int[] nums, int k) {
        Comparator<int[]> cmp = new Comparator<int[]>(){
            public int compare(int[] a, int[] b) {
                return a[1] != b[1] ? Integer.compare(a[1], b[1]) : Integer.compare(a[0], b[0]);
            }
        };
        int n = nums.length;
        double[] res = new double[n - k + 1];
        int[][] win = new int[k][2];
        for (int i = 0; i < k; i++) {
            win[i] = new int[] {i, nums[i]};
        }
        Arrays.sort(win, cmp);
        int[] median = win[(k - 1) / 2];
        NavigableSet<int[]> winSet = new TreeSet<>(cmp);
        for (int i = 0; i < k; i++) {
            winSet.add(new int[] {i, nums[i]});
        }
        for (int i = 1;; i++) {
            res[i - 1] = (k & 1) == 1 ? median[1] : ((long)median[1] + winSet.higher(median)[1]) / 2.0;
            if (i > n - k) return res;

            int[] oldItem = new int[] {i - 1, nums[i - 1]};
            int[] newItem = new int[] {i + k - 1, nums[i + k - 1]};
            winSet.remove(oldItem);
            winSet.add(newItem);
            if (cmp.compare(oldItem, median) * cmp.compare(newItem, median) <= 0) {
                median = cmp.compare(newItem, oldItem) > 0 ? winSet.higher(median) : winSet.lower(median);
            }
        }
    }

    // SortedSet
    // time complexity: O(N * log(K))
    // beats 86.29%(58 ms for 42 tests)
    public double[] medianSlidingWindow3(int[] nums, int k) {
        int n = nums.length;
        double[] res = new double[n - k + 1];
        double[] win = new double[k];
        for (int i = 0; i < k; i++) {
            win[i] = (long)nums[i] + 1 - 1.0 / (i + 1); // avoid duplicate
        }
        Arrays.sort(win);
        double median = win[(k - 1) / 2];
        NavigableSet<Double> winSet = new TreeSet<>();
        for (int i = 0; i < k; i++) {
            winSet.add((long)nums[i] + 1 - 1.0 / (i + 1));
        }
        for (int i = 1;; i++) {
            res[i - 1] = (k & 1) == 1 ? Math.floor(median)
                         : (Math.floor(median) + Math.floor(winSet.higher(median))) / 2.0;
            if (i > n - k) return res;

            double oldItem = (long)nums[i - 1] + 1 - 1.0 / i;
            double newItem = (long)nums[i + k - 1] + 1 - 1.0 / (i + k);
            winSet.remove(oldItem);
            winSet.add(newItem);
            if ((oldItem - median) * (newItem - median) <= 0.0) {
                median = newItem > oldItem ? winSet.higher(median) : winSet.lower(median);
            }
        }
    }

    // Note: Use double PQ like https://leetcode.com/problems/find-median-from-data-stream/
    // is not good enough since we need remove operation here which has linear complexity

    void test(int[] nums, int k, double[] expected) {
        assertArrayEquals(expected, medianSlidingWindow(nums, k), 1e-6);
        assertArrayEquals(expected, medianSlidingWindow2(nums, k), 1e-6);
        assertArrayEquals(expected, medianSlidingWindow3(nums, k), 1e-6);
    }

    @Test
    public void test() {
        test(new int[] {2, 2}, 2, new double[] {2});
        test(new int[] {1, 3, 3, 3}, 3, new double[] {3, 3});
        test(new int[] {4, 1, 7, 1, 8, 7, 8, 7, 7, 4}, 4,
             new double[] {2.5, 4, 7, 7.5, 7.5, 7, 7});
        test(new int[] {6, 5, 9, 5, 4, 9, 1, 7, 5, 5}, 4,
             new double[] {5.5, 5, 7, 4.5, 5.5, 6, 5});
        test(new int[] {1, 3, -1, -3, 5, 3, 6, 7, 8, 4, 1, 3, 3, 0, 1}, 3,
             new double[] {1, -1, -1, 3, 5, 6, 7, 7, 4, 3, 3, 3, 1});
        test(new int[] {1, 3, -1, -3, 5, 3, 6, 7, 8, 4, 1, 3, 3, 0, 1}, 4,
             new double[] {0, 1, 1, 4, 5.5, 6.5, 6.5, 5.5, 3.5, 3, 2, 2});
        test(new int[] {2147483647, 2147483647}, 2, new double[] {2147483647});
        test(new int[] {-2147483648,-2147483648,2147483647,-2147483648,
                        -2147483648,-2147483648,2147483647,2147483647,2147483647,
                        2147483647,-2147483648,2147483647,-2147483648}, 2,
             new double[] {-2147483648, -0.5, -0.5, -2147483648, -2147483648, -0.5,
                           2147483647, 2147483647, 2147483647, -0.5, -0.5, -0.5});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SlidingWindowMedian");
    }
}
