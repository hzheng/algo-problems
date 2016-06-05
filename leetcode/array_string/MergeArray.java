import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// Given two sorted integer arrays, merge them as one sorted array.
public class MergeArray {
    // beats 7.48%
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        if (n == 0) return;

        for (int i = m - 1, j = n - 1, k = m + n - 1; ; k--) {
            if (i < 0 || nums1[i] < nums2[j]) {
                nums1[k] = nums2[j--];
                if (j < 0) return;
            } else {
                nums1[k] = nums1[i--];
            }
        }
    }

    void test(int[] nums1, int[] nums2, int ... expected) {
        int[] res = Arrays.copyOf(nums1, nums1.length + nums2.length);
        merge(res, nums1.length, nums2, nums2.length);
        System.out.println(Arrays.toString(res));
        assertArrayEquals(expected, res);
    }

    @Test
    public void test1() {
        test(new int[] {1, 3, 5}, new int[] {2, 4, 6}, 1, 2, 3, 4, 5, 6);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MergeArray");
    }
}
