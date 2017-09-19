import org.junit.Test;
import static org.junit.Assert.*;

import java.util.function.Function;

// LC152: https://leetcode.com/problems/maximum-product-subarray/
//
// Find the contiguous subarray within an array which has the largest product.
public class MaxProductSubarray {
    // beats 92.75%(2 ms)
    public int maxProduct(int[] nums) {
        int n = nums.length;
        if (n == 1) return nums[0];

        int max = 0;
        for (int i = 0; i < n; ) {
            while (i < n && nums[i] == 0) {
                i++;
            }
            if (i == n) break;

            int start = i;
            while (++i < n && nums[i] != 0) {}
            max = Math.max(max, maxProduct(nums, start, i));
        }
        return max;
    }

    // nonzeros' product
    private int maxProduct(int[] nums, int start, int end) {
        if (start + 1 == end) return nums[start];

        int firstNeg = -1;
        for (int i = start; i < end; i++) {
            if (nums[i] < 0) {
                firstNeg = i;
                break;
            }
        }
        if (firstNeg < 0) return product(nums, start, end);

        int lastNeg = -1;
        for (int i = end - 1; i >= start; i--) {
            if (nums[i] < 0) {
                lastNeg = i;
                break;
            }
        }
        if (firstNeg == lastNeg) {
            return Math.max(product(nums, start, firstNeg),
                            product(nums, firstNeg + 1, end));
        }

        boolean isPostive = true;
        for (int i = firstNeg + 1; i < lastNeg; i++) {
            if (nums[i] < 0) {
                isPostive = !isPostive;
            }
        }
        if (isPostive) return product(nums, start, end);

        // start - lastNeg or: firstNeg - end
        return Math.min(product(nums, start, firstNeg + 1),
                        product(nums, lastNeg, end))
               * product(nums, firstNeg + 1, lastNeg);
    }

    private int product(int[] nums, int start, int end) {
        if (start == end) return Integer.MIN_VALUE;

        int product = 1;
        for (int i = start; i < end; i++) {
            product *= nums[i];
        }
        return product;
    }

    // Solution of Choice
    // 0-D Dynamic Programming
    // beats 49.97%(3 ms for 183 tests)
    public int maxProduct2(int[] nums) {
        int localMax = 1;
        int localMin = 1;
        int max = Integer.MIN_VALUE;
        for (int num : nums) {
            int oldLocalMax = localMax;
            localMax = Math.max(Math.max(num * localMax, num), localMin *= num);
            localMin = Math.min(Math.min(num * oldLocalMax, num), localMin);
            max = Math.max(max, localMax);
        }
        return max;
    }

    // 0-D Dynamic Programming
    // beats 49.97%(3 ms for 183 tests)
    public int maxProduct3(int[] nums) {
        if (nums.length == 1) return nums[0];

        int localMax = 1;
        int localMin = 1;
        int max = 0;
        for (int num : nums) {
            int curMax = num * localMax;
            int curMin = num;
            if (curMax < num) {
                curMin = curMax;
                curMax = num;
            }
            int local = num * localMin;
            localMax = Math.max(curMax, local);
            localMin = Math.min(curMin, local);
            max = Math.max(max, localMax);
        }
        return max;
    }

    // 0-D Dynamic Programming
    // beats 80.08%(3 ms)
    public int maxProduct4(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        if (n == 1) return nums[0];

        boolean isPostive = false;
        for (int i = 0; i < n; i++) {
            if (nums[i] == 0) continue;

            if (nums[i] > 0 || (i > 0 && nums[i - 1] < 0)) {
                isPostive = true;
                break;
            }
        }
        if (!isPostive) return 0;

        int localMax = 1;
        int localMin = 1;
        int max = 1;
        for (int num : nums) {
            if (num > 0) {
                localMax *= num;
                localMin *= num;
                localMin = Math.min(localMin, 1);
            } else if (num == 0) {
                localMax = 1;
                localMin = 1;
            } else {
                int oldLocalMax = localMax;
                localMax = Math.max(localMin * num, 1);
                localMin = oldLocalMax * num;
            }
            max = Math.max(max, localMax);
        }
        return max;
    }

    // Solution of Choice
    // 0-D Dynamic Programming
    // beats 49.97%(3 ms for 183 tests)
    public int maxProduct5(int[] nums) {
        int forwardProduct = 1;
    	int backwardProduct = 1;
  		int max = Integer.MIN_VALUE;
  		for (int i = 0, n = nums.length; i < n; ++i) {
  			forwardProduct *= nums[i];
  			backwardProduct *= nums[n - i - 1];
  			max = Math.max(max, Math.max(forwardProduct, backwardProduct));
  		    forwardProduct = (forwardProduct == 0) ? 1 : forwardProduct;
  		    backwardProduct = (backwardProduct == 0) ? 1 : backwardProduct;
        }
        return max;
    }

    void test(Function<int[], Integer> maxProduct, int expected, int ... nums) {
        assertEquals(expected, (int)maxProduct.apply(nums));
    }

    void test(int expected, int ... nums) {
        MaxProductSubarray m = new MaxProductSubarray();
        test(m::maxProduct, expected, nums);
        test(m::maxProduct2, expected, nums);
        test(m::maxProduct3, expected, nums);
        test(m::maxProduct4, expected, nums);
        test(m::maxProduct5, expected, nums);
    }

    @Test
    public void test1() {
        test(0, -4, 0, -2);
        test(-4, -4);
        test(3, -2, 3);
        test(2, 0, 2);
        test(6, 2, 3, -2, 4);
        test(12, -3, -4);
        test(12, -3, -4, 0, -2, -5);
        test(4320, -2, -3, 8, 9, 10, 0, 3, 8, 9, -2, 5, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxProductSubarray");
    }
}
