import org.junit.Test;
import static org.junit.Assert.*;

// LC321: https://leetcode.com/problems/create-maximum-number/
//
// Given two arrays of length m and n with digits 0-9 representing two numbers.
// Create the maximum number of length k <= m + n from digits of the two.
// The relative order of the digits from the same array must be preserved.
// Return an array of the k digits.
public class CreateMaxNumber {
    // Dynamic Programming + Backtracking + Recursion
    // Time Limit Exceeded
    public int[] maxNumber(int[] nums1, int[] nums2, int k) {
        int[] max = new int[k];
        maxNumber(nums1, createDp(nums1), 0, nums2, createDp(nums2), 0, 0, max);
        return max;
    }

    private void maxNumber(int[] nums1, int[][] dp1, int i1,
                           int[] nums2, int[][] dp2, int i2, int start, int[] max) {
        int k = max.length;
        if (start >= k) return;

        int n1 = nums1.length;
        int n2 = nums2.length;
        int index1 = -1;
        int maxDigit1 = -1;
        if (i1 < n1) {
            index1 = dp1[i1][Math.min(n1 - 1, n1 - (k - start) + (n2 - i2))];
            maxDigit1 = nums1[index1];
        }

        int index2 = -1;
        int maxDigit2 = -1;
        if (i2 < n2) {
            index2 = dp2[i2][Math.min(n2 - 1, n2 - (k - start) + (n1 - i1))];
            maxDigit2 = nums2[index2];
        }

        if (maxDigit1 > maxDigit2) {
            max[start] = maxDigit1;
            maxNumber(nums1, dp1, index1 + 1, nums2, dp2, i2, start + 1, max);
            return;
        }

        if (maxDigit2 > maxDigit1 || start + 1 >= k) {
            max[start] = maxDigit2;
            maxNumber(nums1, dp1, i1, nums2, dp2, index2 + 1, start + 1, max);
            return;
        }

        max[start] = maxDigit2;
        int[] max1 = new int[k - start - 1];
        maxNumber(nums1, dp1, index1 + 1, nums2, dp2, i2, 0, max1);
        int[] max2 = new int[k - start - 1];
        maxNumber(nums1, dp1, i1, nums2, dp2, index2 + 1, 0, max2);
        int[] max0 = max1;
        for (int i = 0; i < max1.length; i++) {
            if (max1[i] > max2[i]) break;

            if (max1[i] < max2[i]) {
                max0 = max2;
                break;
            }
        }
        System.arraycopy(max0, 0, max, start + 1, max0.length);
    }

    private int[][] createDp(int[] nums) {
        // dp[i][j]: the min index of the max digit between i and j
        int n = nums.length;
        int[][] dp = new int[n][n];
        for (int j = n - 1; j >= 0; j--) {
            dp[j][j] = j;
            for (int i = j - 1; i >= 0; i--) {
                if (nums[i] >= nums[dp[i + 1][j]]) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = dp[i + 1][j];
                }
            }
        }
        return dp;
    }

    // Dynamic Programming + Greedy
    // beats 92.87%(17 ms)
    public int[] maxNumber2(int[] nums1, int[] nums2, int k) {
        int[] max = new int[k];
        int[][] dp1 = createDp(nums1);
        int[][] dp2 = createDp(nums2);
        for (int i = 0, i1 = 0, i2 = 0; i < k; i++) {
            int index = nextIndex(nums1, dp1, i1, nums2, dp2, i2, i, k);
            if (index > 0) {
                i1 = index - 1;
                max[i] = nums1[i1++];
            } else if (index < 0) {
                i2 = -index - 1;
                max[i] = nums2[i2++];
            }
        }
        return max;
    }

    private int nextIndex(int[] nums1, int[][] dp1, int i1,
                          int[] nums2, int[][] dp2, int i2, int start, int k) {
        if (start >= k) return 0;

        int n1 = nums1.length;
        int n2 = nums2.length;
        int end1 = n1 - (k - start) + (n2 - i2);
        int end2 = n2 - (k - start) + (n1 - i1);

        int index1 = (i1 < n1) ? dp1[i1][Math.min(n1 - 1, end1)] : -1;
        if (i2 >= n2) return index1 + 1;

        int index2 = dp2[i2][Math.min(n2 - 1, end2)];
        if (i1 >= n1) return -(index2 + 1);

        int max1 = nums1[index1];
        int max2 = nums2[index2];
        if (max1 > max2) return index1 + 1;
        if (max1 < max2) return -(index2 + 1);

        // at this point, next candidates are the same
        int nextMax1 = -1;
        int nextIndex1 = -1;
        if (index1 + 1 < n1) {
            nextIndex1 = dp1[index1 + 1][Math.min(n1 - 1, end1 + 1)];
            nextMax1 = nums1[nextIndex1];
        } else { // finished, use the other
            nextMax1 = nums2[dp2[index2][Math.min(n2 - 1, end2 + i1 - index1)]];
        }

        int nextMax2 = -1;
        int nextIndex2 = -1;
        if (index2 + 1 < n2) {
            nextIndex2 = dp2[index2 + 1][Math.min(n2 - 1, end2 + 1)];
            nextMax2 = nums2[nextIndex2];
        } else { // finished, use the other
            nextMax2 = nums1[dp1[index1][Math.min(n1 - 1, end1 + i2 - index2)]];
        }

        if (nextMax1 < nextMax2) return -(index2 + 1);
        if (nextMax1 > nextMax2 || max1 > nextMax1) return index1 + 1;

        // at this point, next candidates and next's next candidates are the
        // same and the former <= the latter.

        // FIXME: performance booster, but too complex and may have bugs
        if (max1 == nums1[dp1[index1][n1 - 1]]
            && max1 == nums2[dp2[index2][n2 - 1]]) { // global max
            int count1 = 0;
            int last1 = 0;
            for (; nextIndex1 + 1 < n1; count1++) {
                nextIndex1 = dp1[nextIndex1 + 1][Math.min(n1 - 1, end1 + count1)];
                if ((last1 = nums1[nextIndex1]) < max1) break;
            }
            int count2 = 0;
            int last2 = 0;
            for (; nextIndex2 + 1 < n2; count2++) {
                nextIndex2 = dp2[nextIndex2 + 1][Math.min(n2 - 1, end2 + count2)];
                if ((last2 = nums2[nextIndex2]) < max1) break;
            }

            if (count1 > count2) return index1 + 1;
            if (count1 < count2) return -(index2 + 1);
            return (last1 > last2) ? (index1 + 1) : -(index2 + 1);
        }

        // no luck, resort to recursion
        int i11 = index1 + 1;
        int i12 = i2;
        int i21 = i1;
        int i22 = index2 + 1;
        for (int i = start + 1;; i++) {
            int next1 = nextIndex(nums1, dp1, i11, nums2, dp2, i12, i, k);
            int digit1 = next1 == 0 ? -1
                         : (next1 > 0 ? nums1[next1 - 1] : nums2[-next1 - 1]);

            int next2 = nextIndex(nums1, dp1, i21, nums2, dp2, i22, i, k);
            int digit2 = next2 == 0 ? -1
                         : (next2 > 0 ? nums1[next2 - 1] : nums2[-next2 - 1]);

            if (digit1 > digit2) return index1 + 1;
            if (digit1 < digit2 || next1 == 0) return -(index2 + 1);

            if (next1 > 0) {
                i11 = next1;
            } else if (next1 < 0) {
                i12 = -next1;
            }
            if (next2 > 0) {
                i21 = next2;
            } else if (next2 < 0) {
                i22 = -next2;
            }
        }
    }

    // Solution of Choice
    // Greedy + Merge
    // https://discuss.leetcode.com/topic/32272/share-my-greedy-solution
    // time complexity: O((M+N)^3), space complexity: O(K)
    // beats 71.24%(20 ms)
    public int[] maxNumber3(int[] nums1, int[] nums2, int k) {
        int n1 = nums1.length;
        int n2 = nums2.length;
        int[] max = new int[k];
        for (int i = Math.max(0, k - n2); i <= k && i <= n1; i++) {
            int[] candidate = merge(maxArray(nums1, i), maxArray(nums2, k - i), k);
            if (compare(candidate, 0, max, 0)) {
                max = candidate;
            }
        }
        return max;
    }

    private int[] merge(int[] nums1, int[] nums2, int k) {
        int[] res = new int[k];
        for (int i1 = 0, i2 = 0, i = 0; i < k; i++) {
            res[i] = compare(nums1, i1, nums2, i2) ? nums1[i1++] : nums2[i2++];
        }
        return res;
    }

    private boolean compare(int[] nums1, int i1, int[] nums2, int i2) {
        while (i1 < nums1.length && i2 < nums2.length && nums1[i1] == nums2[i2]) {
            i1++;
            i2++;
        }
        return i2 == nums2.length || (i1 < nums1.length && nums1[i1] > nums2[i2]);
    }

    private int[] maxArray(int[] nums, int k) {
        int n = nums.length;
        int[] res = new int[k];
        for (int i = 0, j = 0; i < n; i++) {
            while (n - i + j > k && j > 0 && res[j - 1] < nums[i]) {
                j--;
            }
            if (j < k) {
                res[j++] = nums[i];
            }
        }
        return res;
    }

    // Merge + Dynamic Programming
    // beats 23.53%(32 ms)
    public int[] maxNumber4(int[] nums1, int[] nums2, int k) {
        int[] max = new int[k];
        int n1 = nums1.length;
        int n2 = nums2.length;
        int[][] dp1 = createDp(nums1);
        int[][] dp2 = createDp(nums2);

        for (int i = Math.max(0, k - n2); i <= k && i <= n1; i++) {
            int[] candidate = merge(maxArray(nums1, i, dp1),
                                    maxArray(nums2, k - i, dp2), k);
            if (compare(candidate, 0, max, 0)) {
                max = candidate;
            }
        }
        return max;
    }

    private int[] maxArray(int[] nums, int k, int[][] dp) {
        int n = nums.length;
        int[] res = new int[k];
        for (int i = 0, j = 0, end = n - k; i < k; i++, j++, end++) {
            j = dp[j][Math.min(n - 1, end)];
            res[i] = nums[j];
        }
        return res;
    }

    @FunctionalInterface
    interface Function<A, B, C, D> {
        public D apply(A a, B b, C c);
    }

    void test(Function<int[], int[], Integer, int[]> max, String name,
              int k, int[] nums1, int[] nums2, int ... expected) {
        long t1 = System.nanoTime();
        assertArrayEquals(expected, max.apply(nums1, nums2, k));
        if (k > 50) {
            System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        }
    }

    void test(int k, int[] nums1, int[] nums2, int ... expected) {
        CreateMaxNumber c = new CreateMaxNumber();
        if (k < 70) {
            test(c::maxNumber, "maxNumber", k, nums1, nums2, expected);
        }
        test(c::maxNumber2, "maxNumber2", k, nums1, nums2, expected);
        test(c::maxNumber3, "maxNumber3", k, nums1, nums2, expected);
        test(c::maxNumber4, "maxNumber4", k, nums1, nums2, expected);
    }

    @Test
    public void test1() {
        test(3, new int[] {8, 9}, new int[] {3, 9}, 9, 8, 9);
        test(3, new int[] {2}, new int[] {2, 9}, 2, 9, 2);
        test(3, new int[] {8, 6, 9}, new int[] {1, 7, 5}, 9, 7, 5);
        test(5, new int[] {3, 4, 6, 5}, new int[] {9, 1, 2, 5, 8, 3},
             9, 8, 6, 5, 3);
        test(5, new int[] {6, 7}, new int[] {6, 0, 4}, 6, 7, 6, 0, 4);
        test(3, new int[] {3, 9}, new int[] {8, 9}, 9, 8, 9);
        test(8, new int[] {3, 4, 6, 5, 0, 4, 5},
             new int[] {9, 1, 2, 5, 8, 0, 7, 3}, 9, 8, 7, 6, 5, 4, 5, 3);

        test(44, new int[] {2},
             new int[] {2, 9, 7, 7, 9, 2, 5, 5, 1, 9, 6, 8, 4, 5, 1, 3, 3, 1, 4,
                        8, 7, 1, 8, 2, 2, 9, 6, 9, 9, 7, 4, 0, 6, 5, 9, 0, 4, 7,
                        6, 8, 0, 1, 6},
             2, 9, 7, 7, 9, 2, 5, 5, 2, 1, 9, 6, 8, 4, 5, 1, 3, 3, 1, 4, 8, 7,
             1, 8, 2, 2, 9, 6, 9, 9, 7, 4, 0, 6, 5, 9, 0, 4, 7, 6, 8, 0, 1, 6);

        test(38, new int[] {2, 4, 6, 3, 3, 9, 3, 0, 9, 2, 7, 4, 1, 8, 0, 2, 0,
                            8, 4},
             new int[] {2, 4, 2, 9, 7, 5, 1, 3, 4, 5, 9, 2, 5, 5, 9, 8, 8, 1, 0},
             2, 4, 6, 3, 3, 9, 3, 2, 4, 2, 9, 7, 5, 1, 3, 4, 5, 9, 2, 5, 5, 9,
             8, 8, 1, 0, 9, 2, 7, 4, 1, 8, 0, 2, 0, 8, 4, 0);

        test(57, new int[] {3, 9, 1, 0, 0, 5, 6, 3, 7, 3, 1, 6, 1, 9, 5, 4, 3,
                            6,4,0,8,8,2,8},
             new int[] {1, 8, 7, 9, 7, 8, 0, 1, 4, 7, 5, 6, 9, 9, 8, 7, 1, 1,
                        4,2,5,5,0,0,8,0,6,5,4,1,2,3,3},
             3, 9, 1, 8, 7, 9, 7, 8, 1, 0, 1, 4, 7, 5, 6, 9, 9, 8, 7, 1, 1, 4,
             2, 5, 5, 0, 0, 8, 0, 6, 5, 4, 1, 2, 3, 3, 0, 0, 5, 6, 3, 7, 3, 1,
             6, 1, 9, 5, 4, 3, 6, 4, 0, 8, 8, 2, 8);

        test(60, new int[] {4, 6, 9, 1, 0, 6, 3, 1, 5, 2, 8, 3, 8, 8, 4, 7, 2,
                            0, 7, 1, 9, 9, 0, 1, 5, 9, 3, 9, 3, 9, 7, 3, 0, 8,
                            1, 0, 9, 1, 6, 8, 8, 4, 4, 5, 7, 5, 2, 8, 2, 7, 7,
                            7, 4, 8, 5, 0, 9, 6, 9, 2},
             new int[] {9, 9, 4, 5, 1, 2, 0, 9, 3, 4, 6, 3, 0, 9, 2, 8, 8, 2, 4,
                        8, 6, 5, 4, 4, 2, 9, 5, 0, 7, 3, 7, 5, 9, 6, 6, 8, 8, 0,
                        2, 4, 2, 2, 1, 6, 6, 5, 3, 6, 2, 9, 6, 4, 5, 9, 7, 8, 0,
                        7, 2, 3},
             9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 8, 8, 6, 8, 8, 4, 4, 5,
             7, 5, 2, 8, 2, 7, 7, 7, 4, 8, 5, 0, 9, 6, 9, 2, 0, 2, 4, 2, 2,
             1, 6, 6, 5, 3, 6, 2, 9, 6, 4, 5, 9, 7, 8, 0, 7, 2, 3);

        test(90,  new int[] {6, 3, 1, 7, 6, 6, 1, 4, 7, 8, 4, 1, 4, 6, 1, 0, 8,
                             9, 6, 2, 3, 1, 5, 4, 9, 5, 4, 2, 1, 7, 7, 1, 4, 0,
                             6, 2, 8, 6, 2, 4, 9, 8, 5, 5, 5, 1, 3, 5, 4, 2, 3,
                             8, 4, 1, 1, 1, 0, 9, 6, 7, 2, 3, 8, 9, 0, 3, 3, 4,
                             6, 3, 7, 7, 0, 7, 9, 7, 2, 8, 8, 9, 8, 0, 8, 2, 1,
                             9, 8, 0, 8, 4},
             new int[] {6, 4, 1, 5, 0, 8, 7, 6, 3, 2, 7, 7, 4, 1, 1, 5, 3, 5, 5,
                        9, 2, 2, 0, 8, 0, 5, 7, 3, 9, 9, 1, 2, 2, 4, 2, 7, 4, 5,
                        1, 5, 6, 4, 7, 5, 5, 0, 0, 9, 7, 3, 4, 2, 3, 1, 6, 8,
                        9, 8, 3, 7, 2, 8, 5, 8, 5, 4, 4, 7, 6, 8, 1, 0, 0, 5,
                        7, 9, 5, 1, 6, 8, 9, 7, 8, 6, 8, 6, 7, 5, 2, 7},
             9, 9, 9, 9, 9, 9, 9, 9, 8, 8, 8, 7, 7, 7, 5, 4, 3, 5, 5, 9, 2, 2,
             0, 8, 0, 5, 7, 3, 9, 9, 1, 2, 2, 4, 2, 7, 4, 5, 1, 5, 6, 4, 7, 5,
             5, 0, 0, 9, 7, 3, 4, 2, 3, 1, 6, 8, 9, 8, 3, 7, 2, 8, 5, 8, 5,
             4, 4, 7, 6, 8, 1, 0, 0, 5, 7, 9, 5, 1, 6, 8, 9, 7, 8, 6, 8, 6,
             7, 5, 2, 7);

        test(100,  new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                              1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                              1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                              1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                              1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                              1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                              1, 1, 1, 1, 1, 1, 1, 1, 1},
             new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                        1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
             1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
             1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
             1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
             1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
             1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);

        test(500,  new int[] {
            8, 9, 7, 3, 5, 9, 1, 0, 8, 5, 3, 0, 9, 2, 7, 4, 8, 9, 8, 1, 0, 2, 0,
            2, 7, 2, 3, 5, 4, 7, 4, 1, 4, 0, 1, 4, 2, 1, 3, 1, 5, 3, 9, 3, 9, 0,
            1, 7, 0, 6, 1, 8, 5, 6, 6, 5, 0, 4, 7, 2, 9, 2, 2, 7, 6, 2, 9, 2, 3,
            5, 7, 4, 7, 0, 1, 8, 3, 6, 6, 3, 0, 8, 5, 3, 0, 3, 7, 3, 0, 9, 8, 5,
            1, 9, 5, 0, 7, 9, 6, 8, 5, 1, 9, 6, 5, 8, 2, 3, 7, 1, 0, 1, 4, 3, 4,
            4, 2, 4, 0, 8, 4, 6, 5, 5, 7, 6, 9, 0, 8, 4, 6, 1, 6, 7, 2, 0, 1, 1,
            8, 2, 6, 4, 0, 5, 5, 2, 6, 1, 6, 4, 7, 1, 7, 2, 2, 9, 8, 9, 1, 0, 5,
            5, 9, 7, 7, 8, 8, 3, 3, 8, 9, 3, 7, 5, 3, 6, 1, 0, 1, 0, 9, 3, 7, 8,
            4, 0, 3, 5, 8, 1, 0, 5, 7, 2, 8, 4, 9, 5, 6, 8, 1, 1, 8, 7, 3, 2, 3,
            4, 8, 7, 9, 9, 7, 8, 5, 2, 2, 7, 1, 9, 1, 5, 5, 1, 3, 5, 9, 0, 5, 2,
            9, 4, 2, 8, 7, 3, 9, 4, 7, 4, 8, 7, 5, 0, 9, 9, 7, 9, 3, 8, 0, 9, 5,
            3, 0, 0, 3, 0, 4, 9, 0, 9, 1, 6, 0, 2, 0, 5, 2, 2, 6, 0, 0, 9, 6, 3,
            4, 1, 2, 0, 8, 3, 6, 6, 9, 0, 2, 1, 6, 9, 2, 4, 9, 0, 8, 3, 9, 0, 5,
            4, 5, 4, 6, 1, 2, 5, 2, 2, 1, 7, 3, 8, 1, 1, 6, 8, 8, 1, 8, 5, 6, 1,
            3, 0, 1, 3, 5, 6, 5, 0, 6, 4, 2, 8, 6, 0, 3, 7, 9, 5, 5, 9, 8, 0, 4,
            8, 6, 0, 8, 6, 6, 1, 6, 2, 7, 1, 0, 2, 2, 4, 0, 0, 0, 4, 6, 5, 5, 4,
            0, 1, 5, 8, 3, 2, 0, 9, 7, 6, 2, 6, 9, 9, 9, 7, 1, 4, 6, 2, 8, 2, 5,
            3, 4, 5, 2, 4, 4, 4, 7, 2, 2, 5, 3, 2, 8, 2, 2, 4, 9, 8, 0, 9, 8, 7,
            6, 2, 6, 7, 5, 4, 7, 5, 1, 0, 5, 7, 8, 7, 7, 8, 9, 7, 0, 3, 7, 7, 4,
            7, 2, 0, 4, 1, 1, 9, 1, 7, 5, 0, 5, 6, 6, 1, 0, 6, 9, 4, 2, 8, 0, 5,
            1, 9, 8, 4, 0, 3, 1, 2, 4, 2, 1, 8, 9, 5, 9, 6, 5, 3, 1, 8, 9, 0, 9,
            8, 3, 0, 9, 4, 1, 1, 6, 0, 5, 9, 0, 8, 3, 7, 8, 5
        }, new int[] {
            7, 8, 4, 1, 9, 4, 2, 6, 5, 2, 1, 2, 8, 9, 3, 9, 9, 5, 4, 4, 2, 9, 2,
            0, 5, 9, 4, 2, 1, 7, 2, 5, 1, 2, 0, 0, 5, 3, 1, 1, 7, 2, 3, 3, 2, 8,
            2, 0, 1, 4, 5, 1, 0, 0, 7, 7, 9, 6, 3, 8, 0, 1, 5, 8, 3, 2, 3, 6, 4,
            2, 6, 3, 6, 7, 6, 6, 9, 5, 4, 3, 2, 7, 6, 3, 1, 8, 7, 5, 7, 8, 1, 6,
            0, 7, 3, 0, 4, 4, 4, 9, 6, 3, 1, 0, 3, 7, 3, 6, 1, 0, 0, 2, 5, 7, 2,
            9, 6, 6, 2, 6, 8, 1, 9, 7, 8, 8, 9, 5, 1, 1, 4, 2, 0, 1, 3, 6, 7, 8,
            7, 0, 5, 6, 0, 1, 7, 9, 6, 4, 8, 6, 7, 0, 2, 3, 2, 7, 6, 0, 5, 0, 9,
            0, 3, 3, 8, 5, 0, 9, 3, 8, 0, 1, 3, 1, 8, 1, 8, 1, 1, 7, 5, 7, 4, 1,
            0, 0, 0, 8, 9, 5, 7, 8, 9, 2, 8, 3, 0, 3, 4, 9, 8, 1, 7, 2, 3, 8, 3,
            5, 3, 1, 4, 7, 7, 5, 4, 9, 2, 6, 2, 6, 4, 0, 0, 2, 8, 3, 3, 0, 9, 1,
            6, 8, 3, 1, 7, 0, 7, 1, 5, 8, 3, 2, 5, 1, 1, 0, 3, 1, 4, 6, 3, 6, 2,
            8, 6, 7, 2, 9, 5, 9, 1, 6, 0, 5, 4, 8, 6, 6, 9, 4, 0, 5, 8, 7, 0, 8,
            9, 7, 3, 9, 0, 1, 0, 6, 2, 7, 3, 3, 2, 3, 3, 6, 3, 0, 8, 0, 0, 5, 2,
            1, 0, 7, 5, 0, 3, 2, 6, 0, 5, 4, 9, 6, 7, 1, 0, 4, 0, 9, 6, 8, 3, 1,
            2, 5, 0, 1, 0, 6, 8, 6, 6, 8, 8, 2, 4, 5, 0, 0, 8, 0, 5, 6, 2, 2, 5,
            6, 3, 7, 7, 8, 4, 8, 4, 8, 9, 1, 6, 8, 9, 9, 0, 4, 0, 5, 5, 4, 9, 6,
            7, 7, 9, 0, 5, 0, 9, 2, 5, 2, 9, 8, 9, 7, 6, 8, 6, 9, 2, 9, 1, 6, 0,
            2, 7, 4, 4, 5, 3, 4, 5, 5, 5, 0, 8, 1, 3, 8, 3, 0, 8, 5, 7, 6, 8, 7,
            8, 9, 7, 0, 8, 4, 0, 7, 0, 9, 5, 8, 2, 0, 8, 7, 0, 3, 1, 8, 1, 7, 1,
            6, 9, 7, 9, 7, 2, 6, 3, 0, 5, 3, 6, 0, 5, 9, 3, 9, 1, 1, 0, 0, 8, 1,
            4, 3, 0, 4, 3, 7, 7, 7, 4, 6, 4, 0, 0, 5, 7, 3, 2, 8, 5, 1, 4, 5, 8,
            5, 6, 7, 5, 7, 3, 3, 9, 6, 8, 1, 5, 1, 1, 1, 0, 3
        },
             9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9,
             9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9,
             9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 8, 8, 8, 8, 5, 3, 6, 4,
             2, 6, 3, 6, 7, 6, 6, 9, 5, 4, 3, 2, 7, 6, 3, 1, 8, 7, 5, 7, 8, 1, 6,
             0, 7, 3, 0, 4, 4, 4, 9, 6, 3, 1, 0, 3, 7, 3, 6, 1, 0, 0, 2, 5, 7, 2,
             9, 6, 6, 2, 6, 8, 1, 9, 7, 8, 8, 9, 5, 1, 1, 4, 2, 0, 1, 3, 6, 7, 8,
             7, 0, 5, 6, 0, 1, 7, 9, 6, 4, 8, 6, 7, 0, 2, 3, 2, 7, 6, 0, 5, 0, 9,
             0, 3, 3, 8, 5, 0, 9, 3, 8, 0, 1, 3, 1, 8, 1, 8, 1, 1, 7, 5, 7, 4, 1,
             0, 0, 0, 8, 9, 5, 7, 8, 9, 2, 8, 3, 0, 3, 4, 9, 8, 1, 7, 2, 3, 8, 3,
             5, 3, 1, 4, 7, 7, 5, 4, 9, 2, 6, 2, 6, 4, 0, 0, 2, 8, 3, 3, 0, 9, 1,
             6, 8, 3, 1, 7, 0, 7, 1, 5, 8, 3, 2, 5, 1, 1, 0, 3, 1, 4, 6, 3, 6, 2,
             8, 6, 7, 2, 9, 5, 9, 1, 6, 0, 5, 4, 8, 6, 6, 9, 4, 0, 5, 8, 7, 0, 8,
             9, 7, 3, 9, 0, 1, 0, 6, 2, 7, 3, 3, 2, 3, 3, 6, 3, 0, 8, 0, 0, 5, 2,
             1, 0, 7, 5, 0, 3, 2, 6, 0, 5, 4, 9, 6, 7, 1, 0, 4, 0, 9, 6, 8, 3, 1,
             2, 5, 0, 1, 0, 6, 8, 6, 6, 8, 8, 2, 4, 5, 0, 0, 8, 0, 5, 6, 2, 2, 5,
             6, 3, 7, 7, 8, 4, 8, 4, 8, 9, 1, 6, 8, 9, 9, 0, 4, 0, 5, 5, 4, 9, 6,
             7, 7, 9, 0, 5, 0, 9, 2, 5, 2, 9, 8, 9, 7, 6, 8, 6, 9, 2, 9, 1, 6, 0,
             2, 7, 4, 4, 5, 3, 4, 5, 5, 5, 0, 8, 1, 3, 8, 3, 0, 8, 5, 7, 6, 8, 7,
             8, 9, 7, 0, 8, 4, 0, 7, 0, 9, 5, 8, 2, 0, 8, 7, 0, 3, 1, 8, 1, 7, 1,
             6, 9, 7, 9, 7, 2, 6, 3, 0, 5, 3, 6, 0, 5, 9, 3, 9, 1, 1, 0, 0, 8, 1,
             4, 3, 0, 4, 3, 7, 7, 7, 4, 6, 4, 0, 0, 5, 7, 3, 2, 8, 5, 1, 4, 5, 8,
             5, 6, 7, 5, 7, 3, 3, 9, 6, 8, 1, 5, 1, 1, 1, 0, 3);

        test(600, new int[] {
            2, 0, 2, 1, 2, 2, 2, 2, 0, 1, 0, 0, 2, 0, 2, 0, 2, 1, 0, 1, 1, 0, 1,
            0, 1, 2, 1, 1, 1, 0, 1, 2, 2, 1, 0, 0, 1, 2, 1, 2, 2, 1, 1, 0, 1, 2,
            0, 2, 0, 1, 2, 0, 2, 1, 1, 1, 2, 0, 0, 1, 0, 2, 1, 2, 0, 1, 0, 0, 0,
            1, 2, 1, 0, 1, 1, 2, 0, 2, 2, 0, 0, 1, 1, 2, 2, 1, 1, 2, 2, 1, 0, 1,
            2, 0, 1, 2, 2, 0, 0, 0, 2, 0, 2, 0, 2, 2, 0, 1, 1, 1, 1, 2, 2, 2, 2,
            0, 0, 2, 2, 2, 2, 0, 2, 0, 1, 0, 0, 2, 1, 0, 0, 2, 0, 2, 1, 1, 1, 1,
            0, 1, 2, 0, 2, 1, 0, 1, 1, 1, 0, 0, 2, 2, 2, 0, 2, 1, 1, 1, 2, 2, 0,
            0, 2, 2, 2, 2, 2, 0, 2, 0, 2, 0, 2, 0, 0, 1, 0, 1, 1, 0, 0, 2, 1, 1,
            2, 2, 2, 1, 2, 2, 0, 0, 2, 1, 0, 2, 1, 2, 1, 1, 1, 0, 2, 0, 1, 1, 2,
            1, 1, 0, 0, 1, 0, 1, 2, 2, 2, 0, 2, 2, 1, 0, 1, 2, 1, 2, 0, 2, 2, 0,
            1, 2, 2, 1, 2, 2, 1, 1, 2, 2, 2, 2, 2, 1, 2, 0, 1, 1, 1, 2, 2, 2, 0,
            2, 0, 2, 0, 2, 1, 1, 0, 2, 2, 2, 1, 0, 2, 1, 2, 2, 2, 0, 1, 1, 1, 1,
            1, 1, 0, 0, 0, 2, 2, 0, 1, 2, 1, 0, 0, 2, 2, 2, 2, 1, 0, 2, 0, 1, 2,
            0
        }, new int[] {
            1, 1, 1, 0, 0, 1, 1, 0, 2, 1, 0, 1, 2, 1, 0, 2, 2, 1, 0, 2, 0, 1, 1,
            0, 0, 2, 2, 0, 1, 0, 2, 0, 2, 2, 2, 2, 1, 1, 1, 1, 0, 0, 0, 0, 2, 1,
            0, 2, 1, 1, 2, 1, 2, 2, 0, 2, 1, 0, 2, 0, 0, 2, 0, 2, 2, 1, 0, 1, 0,
            0, 2, 1, 1, 1, 2, 2, 0, 0, 0, 1, 1, 2, 0, 2, 2, 0, 1, 0, 2, 1, 0, 2,
            1, 1, 1, 0, 1, 1, 2, 0, 2, 0, 1, 1, 2, 0, 2, 0, 1, 2, 1, 0, 2, 0, 1,
            0, 0, 0, 1, 2, 1, 2, 0, 1, 2, 2, 1, 1, 0, 1, 2, 1, 0, 0, 1, 0, 2, 2,
            1, 2, 2, 0, 0, 0, 2, 0, 0, 0, 1, 0, 2, 0, 2, 1, 0, 0, 1, 2, 0, 1, 1,
            0, 1, 0, 2, 2, 2, 1, 1, 0, 1, 1, 2, 1, 0, 2, 2, 2, 1, 2, 2, 2, 2, 0,
            1, 1, 0, 1, 2, 1, 2, 2, 0, 0, 0, 0, 0, 1, 1, 1, 2, 1, 2, 1, 1, 0, 1,
            2, 0, 1, 2, 1, 2, 2, 2, 2, 0, 0, 0, 0, 2, 0, 1, 2, 0, 1, 1, 1, 1, 0,
            1, 2, 2, 1, 0, 1, 2, 2, 1, 2, 2, 2, 0, 2, 0, 1, 1, 2, 0, 0, 2, 2, 0,
            1, 0, 2, 1, 0, 0, 1, 1, 1, 1, 0, 0, 2, 2, 2, 2, 0, 0, 1, 2, 1, 1, 2,
            0, 1, 2, 1, 0, 2, 0, 0, 2, 1, 1, 0, 2, 1, 1, 2, 2, 0, 1, 0, 2, 0, 1,
            0
        },
             2, 1, 1, 1, 0, 2, 1, 2, 2, 2, 2, 0, 1, 0, 0, 2, 0, 2, 0, 2, 1, 0, 1,
             1, 0, 1, 0, 1, 2, 1, 1, 1, 0, 1, 2, 2, 1, 0, 0, 1, 2, 1, 2, 2, 1, 1,
             0, 1, 2, 0, 2, 0, 1, 2, 0, 2, 1, 1, 1, 2, 0, 0, 1, 1, 0, 2, 1, 0, 1,
             2, 1, 0, 2, 2, 1, 0, 2, 0, 1, 1, 0, 0, 2, 2, 0, 1, 0, 2, 0, 2, 2, 2,
             2, 1, 1, 1, 1, 0, 0, 1, 0, 2, 1, 2, 0, 1, 0, 0, 0, 1, 2, 1, 0, 1, 1,
             2, 0, 2, 2, 0, 0, 1, 1, 2, 2, 1, 1, 2, 2, 1, 0, 1, 2, 0, 1, 2, 2, 0,
             0, 0, 2, 0, 2, 0, 2, 2, 0, 1, 1, 1, 1, 2, 2, 2, 2, 0, 0, 2, 2, 2, 2,
             0, 2, 0, 1, 0, 0, 2, 1, 0, 0, 2, 0, 2, 1, 1, 1, 1, 0, 1, 2, 0, 2, 1,
             0, 1, 1, 1, 0, 0, 2, 2, 2, 0, 2, 1, 1, 1, 2, 2, 0, 0, 2, 2, 2, 2, 2,
             0, 2, 0, 2, 0, 2, 0, 0, 1, 0, 1, 1, 0, 0, 2, 1, 1, 2, 2, 2, 1, 2, 2,
             0, 0, 2, 1, 0, 2, 1, 2, 1, 1, 1, 0, 2, 0, 1, 1, 2, 1, 1, 0, 0, 1, 0,
             1, 2, 2, 2, 0, 2, 2, 1, 0, 1, 2, 1, 2, 0, 2, 2, 0, 1, 2, 2, 1, 2, 2,
             1, 1, 2, 2, 2, 2, 2, 1, 2, 0, 1, 1, 1, 2, 2, 2, 0, 2, 0, 2, 0, 2, 1,
             1, 0, 2, 2, 2, 1, 0, 2, 1, 2, 2, 2, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 2,
             2, 0, 1, 2, 1, 0, 0, 2, 2, 2, 2, 1, 0, 2, 0, 1, 2, 0, 0, 0, 0, 2, 1,
             0, 2, 1, 1, 2, 1, 2, 2, 0, 2, 1, 0, 2, 0, 0, 2, 0, 2, 2, 1, 0, 1, 0,
             0, 2, 1, 1, 1, 2, 2, 0, 0, 0, 1, 1, 2, 0, 2, 2, 0, 1, 0, 2, 1, 0, 2,
             1, 1, 1, 0, 1, 1, 2, 0, 2, 0, 1, 1, 2, 0, 2, 0, 1, 2, 1, 0, 2, 0, 1,
             0, 0, 0, 1, 2, 1, 2, 0, 1, 2, 2, 1, 1, 0, 1, 2, 1, 0, 0, 1, 0, 2, 2,
             1, 2, 2, 0, 0, 0, 2, 0, 0, 0, 1, 0, 2, 0, 2, 1, 0, 0, 1, 2, 0, 1, 1,
             0, 1, 0, 2, 2, 2, 1, 1, 0, 1, 1, 2, 1, 0, 2, 2, 2, 1, 2, 2, 2, 2, 0,
             1, 1, 0, 1, 2, 1, 2, 2, 0, 0, 0, 0, 0, 1, 1, 1, 2, 1, 2, 1, 1, 0, 1,
             2, 0, 1, 2, 1, 2, 2, 2, 2, 0, 0, 0, 0, 2, 0, 1, 2, 0, 1, 1, 1, 1, 0,
             1, 2, 2, 1, 0, 1, 2, 2, 1, 2, 2, 2, 0, 2, 0, 1, 1, 2, 0, 0, 2, 2, 0,
             1, 0, 2, 1, 0, 0, 1, 1, 1, 1, 0, 0, 2, 2, 2, 2, 0, 0, 1, 2, 1, 1, 2,
             0, 1, 2, 1, 0, 2, 0, 0, 2, 1, 1, 0, 2, 1, 1, 2, 2, 0, 1, 0, 2, 0, 1,
             0, 0);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CreateMaxNumber");
    }
}
