import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.junit.Test;
import static org.junit.Assert.*;

// Find the median of the two sorted arrays. The overall run time complexity
// should be O(log (m+n)).
public class MedianArray {
    // beats 6.24%
    // time complexity: O(log(M + N))
    private double findMedianSortedArrays(int[] nums1, int start1, int end1,
                                          int[] nums2, int start2, int end2) {
        final int len1 = end1 - start1 + 1;
        final int len2 = end2 - start2 + 1;
        int shrink = (len1 + len2) / 4;
        if (shrink < 2) {
            return findMedianArraysSlow(nums1, start1, end1,
                                        nums2, start2, end2);
        }

        // nums2's lens >= shrink * 2 (L = len1 + len2)
        if ((len1 <= shrink)
            || (nums1[start1 + shrink] >= nums2[start2 + shrink])) {
            // nums2's leftmost 1/4L must be on the median's left
            start2 += shrink - 1;
        } else {
            // nums1's lens >= shrink and nums1's 1/4L value < nums2's
            start1 += shrink - 1;
        }
        if ((len1 <= shrink)
            || (nums2[end2 - shrink] >= nums1[end1 - shrink])) {
            // nums2's rightmost 1/4L must be on the median's right
            end2 -= shrink - 1;
        } else {
            // nums1's lens >= shrink and nums1's -1/4L value > nums2's
            end1 -= shrink - 1;
        }

        if (end1 - start1 < end2 - start2) {
            return findMedianSortedArrays(nums1, start1, end1, nums2,
                                          start2, end2);
        } else {
            return findMedianSortedArrays(nums2, start2, end2, nums1,
                                          start1, end1);
        }
    }

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int end1 = nums1.length - 1;
        int end2 = nums2.length - 1;
        if (end1 < 0 && end2 < 0) return 0.0; // in case

        if (end1 < end2) {
            return findMedianSortedArrays(nums1, 0, end1, nums2, 0, end2);
        } else {
            return findMedianSortedArrays(nums2, 0, end2, nums1, 0, end1);
        }
    }

    // beats 6.24%(same?)
    public double findMedianSortedArraysIterative(int[] nums1, int[] nums2) {
        int end1 = nums1.length - 1;
        int end2 = nums2.length - 1;
        if (end1 < 0 && end2 < 0) return 0.0; // in case

        int start1 = 0;
        int start2 = 0;
        while (true) {
            int len1 = end1 - start1 + 1;
            int len2 = end2 - start2 + 1;
            if (len1 > len2) { // swap
                int tmp = len1;
                len1 = len2;
                len2 = tmp;
                tmp = start1;
                start1 = start2;
                start2 = tmp;
                tmp = end1;
                end1 = end2;
                end2 = tmp;
                int[] tmps = nums1;
                nums1 = nums2;
                nums2 = tmps;
            }
            int shrink = (len1 + len2) / 4;
            if (shrink < 2) {
                return findMedianArraysSlow(nums1, start1, end1,
                                            nums2, start2, end2);
            }

            // nums2's lens >= shrink * 2 (L = len1 + len2)
            if ((len1 <= shrink)
                || (nums1[start1 + shrink] >= nums2[start2 + shrink])) {
                // nums2's leftmost 1/4L must be on the median's left
                start2 += shrink - 1;
            } else {
                // nums1's lens >= shrink and nums1's 1/4L value < nums2's
                start1 += shrink - 1;
            }
            if ((len1 <= shrink)
                || (nums2[end2 - shrink] >= nums1[end1 - shrink])) {
                // nums2's rightmost 1/4L must be on the median's right
                end2 -= shrink - 1;
            } else {
                // nums1's lens >= shrink and nums1's -1/4L value > nums2's
                end1 -= shrink - 1;
            }
        }
    }

    private double median(int[] nums, int start, int end) {
        int mid = (start + end) / 2;
        if (((end - start) & 1) == 0) {
            return (double)nums[mid];
        }
        return (nums[mid] + nums[mid + 1]) / 2.0;
    }

    public double findMedianArraysSlow(int[] nums1, int[] nums2) {
        if (nums1.length == 0 && nums2.length == 0) return 0.0; // in case
        return findMedianArraysSlow(nums1, 0, nums1.length - 1,
                                    nums2, 0, nums2.length - 1);
    }

    private double findMedianArraysSlow(int[] nums1, int start1, int end1,
                                        int[] nums2, int start2, int end2) {
        int l1 = end1 - start1 + 1;
        if (l1 == 0) return median(nums2, start2, end2);

        int l2 = end2 - start2 + 1;
        if (l2 == 0) return median(nums1, start1, end1);

        if (l1 == 1 && l2 == 1) return (nums1[start1] + nums2[start2]) / 2d;

        if (l1 == 1 && l2 == 2) {
            if (nums1[start1] <= nums2[start2]) return nums2[start2];
            if (nums1[start1] >= nums2[start2 + 1]) return nums2[start2 + 1];
            return nums1[start1];
        }

        if (l2 == 1 && l1 == 2) {
            if (nums2[start2] <= nums1[start1]) return nums1[start1];
            if (nums2[start2] >= nums1[start1 + 1]) return nums1[start1 + 1];
            return nums2[start2];
        }

        if (l1 == 2 && l2 == 2) {
            return (Math.max(nums1[start1], nums2[start2])
                    + Math.min(nums1[start1 + 1], nums2[start2 + 1])) / 2d;
        }

        int[] nums = new int[l1 + l2];
        int i = 0;
        for (; i < l1; i++) {
            nums[i] = nums1[start1 + i];
        }
        for (int j = 0; j < l2; j++) {
            nums[i + j] = nums2[start2 + j];
        }
        Arrays.sort(nums);
        return median(nums, 0, l1 + l2 - 1);
    }

    // www.programcreek.com/2012/12/leetcode-median-of-two-sorted-arrays-java/
    // beats 40.63% (but may casue stackoverflow)
    public double findMedianSortedArrays2(int[] nums1, int[] nums2) {
        int l1 = nums1.length;
        int l2 = nums2.length;

        int total = l1 + l2;
        if (total == 0) return 0.0; // in case

        if ((total & 1) == 1) {
            return findKth(nums1, 0, l1 - 1,
                           nums2, 0, l2 - 1, total / 2);
        }
        return (findKth(nums1, 0, l1 - 1, nums2, 0, l2 - 1, total / 2) +
                findKth(nums1, 0, l1 - 1, nums2, 0, l2 - 1, total / 2 - 1)) / 2;
    }

    private double findKth(int[] nums1, int start1, int end1,
                           int[] nums2, int start2, int end2, int k) {
        int len1 = end1 - start1 + 1;
        if (len1 == 0) return nums2[start2 + k];

        int len2 = end2 - start2 + 1;
        if (len2 == 0) return nums1[start1 + k];

        if (k == 0) return Math.min(nums1[start1], nums2[start2]);

        int k1 = len1 * k / (len1 + len2);
        int k2 = k - k1 - 1;
        if (nums1[k1 + start1] > nums2[k2 + start2]) {
            k -= k2 + 1;
            end1 = start1 + k1;
            start2 += k2 + 1;
        } else {
            k -= k1 + 1;
            end2 = start2 + k2;
            start1 += k1 + 1;
        }
        return findKth(nums1, start1, end1, nums2, start2, end2, k);
    }

    // www.programcreek.com/2012/12/leetcode-median-of-two-sorted-arrays-java/
    // BUG: although median or median pair are kept during division,
    // it doesn't guarante the right answer if excluded values are not
    // properly paired. Actually, this method works when two arrays have
    // the same length.
    public double findMedianSortedArrays3(int[] nums1, int[] nums2) {
        if (nums1.length == 0 && nums2.length == 0) return 0.0; // in case

        return findMedianSortedArrays3(nums1, 0, nums1.length - 1,
                                       nums2, 0, nums2.length - 1);
    }

    private double findMedianSortedArrays3(int[] nums1, int start1, int end1,
                                           int[] nums2, int start2, int end2) {
        int l1 = end1 - start1 + 1;
        if (l1 == 0) return median(nums2, start2, end2);

        int l2 = end2 - start2 + 1;
        if (l2 == 0) return median(nums1, start1, end1);
        System.out.println("l1="+l1);
        System.out.println("l2="+l2);

        double m1 = median(nums1, start1, end1);
        double m2 = median(nums2, start2, end2);

        if (Math.abs(m1 - m2) < 1e-8) return m1;

        if (l1 == 1 && l2 == 1) return (nums1[start1] + nums2[start2]) / 2.0;

        if (l1 == 2 && l2 == 2) {
            return (Math.max(nums1[start1], nums2[start2])
                    + Math.min(nums1[start1 + 1], nums2[start2 + 1])) / 2.0;
        }
        if (l1 == 1 && l2 == 2) {
            if (nums1[start1] <= nums2[start2]) return nums2[start2];
            if (nums1[start1] >= nums2[start2 + 1]) return nums2[start2 + 1];
            return nums1[start1];
        }
        if (l2 == 1 && l1 == 2) {
            if (nums2[start2] <= nums1[start1]) return nums1[start1];
            if (nums2[start2] >= nums1[start1 + 1]) return nums1[start1 + 1];
            return nums2[start2];
        }

        if (m1 < m2) {
            start1 = (start1 + end1) / 2;
            end2 = (start2 + end2) / 2 + 1;
        } else {
            end1 = (start1 + end1) / 2 + 1;
            start2 = (start2 + end2) / 2;
        }
        return findMedianSortedArrays3(nums1, start1, end1,
                                       nums2, start2, end2);
    }

    private void test(int[] nums1, int[] nums2, double expected) {
        assertEquals(expected, findMedianSortedArrays(nums1, nums2), 1e-8);
        assertEquals(expected, findMedianArraysSlow(nums1, nums2), 1e-8);
        assertEquals(expected, findMedianSortedArrays2(nums1, nums2), 1e-8);
        // assertEquals(expected, findMedianSortedArrays3(nums1, nums2), 1e-8);
    }

    @Test
    public void test1() {
        test(new int[] {1}, new int[] {}, 1);
        test(new int[] {}, new int[] {1}, 1);
        test(new int[] {1}, new int[] {2}, 1.5);
        test(new int[] {1, 1}, new int[] {90}, 1);
        test(new int[] {1, 3}, new int[] {90}, 3);
        test(new int[] {1, 3, 5}, new int[] {2, 4}, 3);
        test(new int[] {1}, new int[] {2, 3, 4}, 2.5);
        test(new int[] {1}, new int[] {2, 3, 4, 5, 6}, 3.5);
        test(new int[] {1}, new int[] {2, 3, 4, 5, 6, 7, 8, 9, 10}, 5.5);
        test(new int[] {1, 3, 5, 7, 20 }, new int[] {2, 4, 6, 8}, 5);
        test(new int[] {1, 3, 5, 7, 20 }, new int[] {2, 4, 6, 8, 10, 12}, 6);
        test(new int[] {1, 3, 5, 7, 20 }, new int[] {0, 2, 4, 6, 8, 10, 12}, 5.5);
    }

    private void test(int[] nums1, int[] nums2) {
        double expected = findMedianArraysSlow(nums1, nums2);
        assertEquals(expected,
                     findMedianSortedArrays(nums1, nums2), 1e-8);
        assertEquals(expected,
                     findMedianSortedArrays2(nums1, nums2), 1e-8);
        // assertEquals(expected,
        //              findMedianSortedArrays3(nums1, nums2), 1e-8);
    }

    @Test
    public void test2() {
        test(new int[] {1, 3, 5, 7, 20 }, new int[] {0, 2, 4, 6, 8, 10, 12}, 5.5);
        test(new int[] {1, 2, 3, 4, 5, 6, 7},
             new int[] {-4, -3, -2, -1, 0, 1, 2, 3, 4});
        test(new int[] {-992, -361, 372, 491, 505, 785, 837, 861, 881},
             new int[] {-912, -759, -445, -239, -185, -98, -78, 192, 532});
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    private double test(Function<int[], int[], Double> findMedian, String name,
                        int[] a1, int[] a2) {
        long t1 = System.nanoTime();
        double median = findMedian.apply(a1, a2);
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        return median;
    }

    private void test(int l1, int l2) {
        int min = -1000;
        int max = 1000;
        l1 = ThreadLocalRandom.current().nextInt(l1);
        l2 = ThreadLocalRandom.current().nextInt(l2);
        int[] nums1 = IntStream.range(1, l1).map(
            i -> ThreadLocalRandom.current().nextInt(min, max)).toArray();
        int[] nums2 = IntStream.range(1, l2).map(
            i -> ThreadLocalRandom.current().nextInt(min, max)).toArray();
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        if (l1 + l2 < 25) {
            System.out.println("n1=" + Arrays.toString(nums1));
            System.out.println("n2=" + Arrays.toString(nums2));
        }
        MedianArray ma = new MedianArray();
        double fastMedian = test(ma::findMedianSortedArrays,
                                 "findMedianSortedArrays", nums1, nums2);
        double iterativeMedian = test(ma::findMedianSortedArraysIterative,
                                      "findMedianSortedArraysIterative", nums1, nums2);
        double slowMedian = test(ma::findMedianArraysSlow,
                                 "findMedianArraysSlow", nums1, nums2);
        if (l1 < 1000 && l2 < 1000) { // may case stack overflow
            double kthMedian = test(ma::findMedianSortedArrays2,
                                    "findMedianSortedArrays2", nums1, nums2);
            assertEquals(kthMedian, fastMedian, 1e-8);
        }
        assertEquals(slowMedian, fastMedian, 1e-8);
        assertEquals(iterativeMedian, fastMedian, 1e-8);
        // double median3 = test(ma::findMedianSortedArrays3,
        //                          "findMedianSortedArrays3", nums1, nums2);
        // assertEquals(median3, fastMedian, 1e-8);
        System.out.println("====================");
    }

    @Test
    public void test3() {
        for (int i = 0; i < 50; i++) {
            test(1000, 1000);
            test(1000000, 1000000);
        }
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MedianArray");
    }
}
