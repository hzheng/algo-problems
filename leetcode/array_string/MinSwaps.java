import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1850: https://leetcode.com/problems/minimum-adjacent-swaps-to-reach-the-kth-smallest-number/
//
// You are given a string num, representing a large integer, and an integer k.
// We call some integer wonderful if it is a permutation of the digits in num and is greater in
// value than num. There can be many wonderful integers. However, we only care about the
// smallest-valued ones.
//
// For example, when num = "5489355142":
// The 1st smallest wonderful integer is "5489355214".
// The 2nd smallest wonderful integer is "5489355241".
// The 3rd smallest wonderful integer is "5489355412".
// The 4th smallest wonderful integer is "5489355421".
// Return the minimum number of adjacent digit swaps that needs to be applied to num to reach the kth smallest wonderful integer.
//
// The tests are generated in such a way that kth smallest wonderful integer exists.
//
// Constraints:
// 2 <= num.length <= 1000
// 1 <= k <= 1000
// num only consists of digits.
public class MinSwaps {
    // time complexity: O(N^2), space complexity: O(N)
    // 18 ms(91.20%), 37.3 MB(79.47%) for 109 tests
    public int getMinSwaps(String num, int k) {
        char[] s1 = num.toCharArray();
        char[] s2 = num.toCharArray();
        for (int i = 0; i < k; i++) {
            nextPermutation(s1);
        }
        int res = 0;
        for (int i = 0, j, n = s1.length; i < n; i++) {
            for (j = i; s1[j] != s2[i]; j++) {}
            for (; i < j; res++, j--) {
                swap(s1, j, j - 1);
            }
        }
        return res;
    }

    private void nextPermutation(char[] nums) {
        int len = nums.length;
        int i = len - 1;
        for (; i > 0 && nums[i - 1] >= nums[i]; i--) {}
        if (i > 0) {
            int j = len - 1;
            for (; nums[j] <= nums[i - 1]; j--) {}
            swap(nums, i - 1, j);
        }
        for (int j = len - 1; j > i; i++, j--) {
            swap(nums, i, j);
        }
    }

    private void swap(char[] nums, int i, int j) {
        char tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    private void test(String num, int k, int expected) {
        assertEquals(expected, getMinSwaps(num, k));
    }

    @Test public void test1() {
        test("5489355142", 4, 2);
        test("11112", 4, 4);
        test("00123", 1, 1);
        test("00123", 1, 1);
        test("5439235710896355142", 23, 11);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
