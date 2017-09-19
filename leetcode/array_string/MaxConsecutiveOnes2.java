import org.junit.Test;
import static org.junit.Assert.*;

// LC487: https://leetcode.com/problems/max-consecutive-ones-ii/
//
// Given a binary array, find the maximum number of consecutive 1s in this array
// if you can flip at most one 0.
// Follow up:
// What if the input numbers come in one by one as an infinite stream? In other
// words, you can't store all numbers coming from the stream as it's too large
// to hold in memory. Could you solve it efficiently?
public class MaxConsecutiveOnes2 {
    // beats 43.56%(14 ms for 41 tests)
    public int findMaxConsecutiveOnes(int[] nums) {
        int n = nums.length;
        int max = 0;
        int zero1 = -1;
        int zero2 = -1;
        for (int i = 0; i <= n; i++) {
            if (i < n && nums[i] != 0) continue;

            max = Math.max(max, Math.max(i - zero1 - 1, i - zero2));
            zero1 = zero2;
            zero2 = i;
        }
        return zero1 < 0 || zero2 < 0 ? n : max;
    }

    // beats 27.57%(16 ms for 41 tests)
    public int findMaxConsecutiveOnes2(int[] nums) {
        int max = 0;
        int zero1 = 0;
        int zero2 = 0;
        for (int num : nums) {
            zero1++;
            zero2++;
            if (num == 0) {
                zero1 = zero2;
                zero2 = 0;
            }
            max = Math.max(max, Math.max(zero1, zero2));
        }
        return max;
    }

    // beats 43.56%(14 ms for 41 tests)
    public int findMaxConsecutiveOnes3(int[] nums) {
        int max = 0;
        int zero1 = 0;
        int zero2 = 0;
        for (int num : nums) {
            zero2++;
            if (num == 0) {
                zero1 = zero2;
                zero2 = 0;
            }
            max = Math.max(max, zero1 + zero2);
        }
        return max;
    }

    // Generalized version
    // Two Pointers
    // beats 43.56%(14 ms for 41 tests)
    public int findMaxConsecutiveOnes4(int[] nums) {
        int max = 0;
        int zero = 0;
        int k = 1;   // flip at most k zero
        for (int i = 0, j = 0; j < nums.length; j++) {
            if (nums[j] == 0) {
                zero++;
            }
            while (zero > k) {
                if (nums[i++] == 0) {
                    zero--;
                }
            }
            max = Math.max(max, j - i + 1);
        }
        return max;
    }

    // beats 67.13%(12 ms for 41 tests)
    public int findMaxConsecutiveOnes5(int[] nums) {
        int max = 0;
        int zero = -1;
        for (int i = 0, j = 0; j < nums.length; j++) {
            if (nums[j] == 0) {
                i = zero + 1;
                zero = j;
            }
            max = Math.max(max, j - i + 1);
        }
        return max;
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, findMaxConsecutiveOnes(nums));
        assertEquals(expected, findMaxConsecutiveOnes2(nums));
        assertEquals(expected, findMaxConsecutiveOnes3(nums));
        assertEquals(expected, findMaxConsecutiveOnes4(nums));
        assertEquals(expected, findMaxConsecutiveOnes5(nums));
    }

    @Test
    public void test() {
        test(new int[] {}, 0);
        test(new int[] {0}, 1);
        test(new int[] {0, 0}, 1);
        test(new int[] {1, 1, 1}, 3);
        test(new int[] {0, 1, 1}, 3);
        test(new int[] {1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 0, 1, 0, 1}, 6);
        test(new int[] {1, 0, 1, 1, 0}, 4);
        test(new int[] {1, 1, 0, 1, 1, 1, 1, 0, 0, 1}, 7);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxConsecutiveOnes2");
    }
}
