import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// Given an array of non-negative integers, you are initially positioned at the
// first index of the array. Each element in the array represents your maximum
// jump length at that position.
// Your goal is to reach the last index in the minimum number of jumps.
public class JumpGame2 {
    // beats 8.92%
    public int jump(int[] nums) {
        int n = nums.length;
        if (n < 2) return 0;

        int jumps = 1;
        int max = nums[0];
        for (int i = 0; i < n && max < n - 1; jumps++) {
            int nextIndex = n; // exit signal
            for (int j = nums[i]; j >= 1; j--) {
                if ((i + j) < n && nums[i + j] + i + j > max) {
                    nextIndex = i + j;
                    max = nums[nextIndex] + nextIndex;
                }
            }
            i = nextIndex;
        }
        return (max < n - 1) ? -1 : jumps;
    }

    // beats 56.79%
    public int jump2(int[] nums) {
        int n = nums.length;
        int max = 0;
        int jumps = 0;
        for (int i = 0; max < n - 1; jumps++) {
            int oldMax = max;
            for (; i <= oldMax; i++) {
                max = Math.max(max, i + nums[i]);
            }
            if (oldMax == max) return -1;
        }
        return jumps;
    }

    // http://www.jiuzhang.com/solutions/jump-game-ii/
    // Time Limit Exceeded 
    public int jump3(int[] nums) {
        int n = nums.length;
        int[] jumps = new int[n];
        jumps[0] = 0;
        for (int i = 1; i < n; i++) {
            jumps[i] = -1;
            for (int j = 0; j < i; j++) {
                if (jumps[j] >= 0 && j + nums[j] >= i) {
                    jumps[i] = jumps[j] + 1;
                    break;
                }
            }
        }
        return jumps[n - 1];
    }

    void test(int expected, int ... nums) {
        assertEquals(expected, jump(nums));
        assertEquals(expected, jump2(nums));
        assertEquals(expected, jump3(nums));
    }

    @Test
    public void test1() {
        test(3, 1, 1, 1, 1);
        test(0, 0);
        test(-1, 0, 1);
        test(1, 1, 2);
        test(2, 1, 1, 1);
        test(2, 2, 3, 1, 1, 4);
        test(-1, 2, 3, 1, 1, 0, 0, 4);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("JumpGame2");
    }
}
