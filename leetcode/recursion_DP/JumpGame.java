import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC055: https://leetcode.com/problems/jump-game/
//
// Given an array of non-negative integers, you are initially positioned at the
// first index of the array. Each element in the array represents your maximum
// jump length at that position. Determine if you can reach the last index.
public class JumpGame {
    // beats 37.89%(7 ms)
    public boolean canJump(int[] nums) {
        int needJump = 1;
        for (int i = nums.length - 2; i >= 0; i--) {
            if (nums[i] >= needJump) {
                needJump = 1;
            } else {
                needJump++;
            }
        }
        return needJump == 1;
    }

    // Greedy
    // beats 25.87%(8 ms)
    public boolean canJump2(int[] nums) {
        int n = nums.length;
        int farthest = nums[0];
        for (int i = 1; i < n; i++) {
            if (farthest < i) return false;

            // the following is supposed to improve efficiency,
            // while it drops beat rate to 27.06% or 4.12%
            // if (farthest >= (n - 1)) return true;

            farthest = Math.max(farthest, i + nums[i]);
        }
        return farthest >= (n - 1);
    }

    // Solution of Choice
    // Greedy
    // beats 4.04%(10 ms)
    public boolean canJump3(int[] nums) {
        int n = nums.length;
        int i = 0;
        for (int farthest = 0; i < n && i <= farthest; i++) {
            farthest = Math.max(i + nums[i], farthest);
        }
        return i == n;
    }

    // beats 37.89%(7 ms)
    public boolean canJump4(int[] nums) {
        int last = nums.length - 1;
        for (int i = nums.length - 2; i >= 0; i--) {
            if (i + nums[i] >= last) {
                last = i;
            }
        }
        return last <= 0;
    }

    void test(boolean expected, int ... nums) {
        assertEquals(expected, canJump(nums));
        assertEquals(expected, canJump2(nums));
        assertEquals(expected, canJump3(nums));
        assertEquals(expected, canJump4(nums));
    }

    @Test
    public void test1() {
        test(true, 1);
        test(true, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
        test(true, 2, 3, 1, 1, 4);
        test(false, 3, 2, 1, 0, 4);
        test(false, 2, 0, 1, 1, 2, 1, 0, 0, 0);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("JumpGame");
    }
}
