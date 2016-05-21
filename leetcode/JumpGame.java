import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// Given an array of non-negative integers, you are initially positioned at the
// first index of the array. Each element in the array represents your maximum
// jump length at that position. Determine if you can reach the last index.
public class JumpGame {
    // beats 77.22%
    public boolean canJump(int[] nums) {
        int needJump = 1;
        for (int i = nums.length - 2; i >= 0; i--) {
            int jump = nums[i];
            if (jump >= needJump) {
                needJump = 1;
            } else {
                needJump++;
            }
        }
        return needJump == 1;
    }

    // http://www.jiuzhang.com/solutions/jump-game/
    // beats 77.22%(or 27.06% 4.12%)
    public boolean canJump2(int[] nums) {
        int n = nums.length;
        int farthest = nums[0];
        for (int i = 1; i < n; i++) {
            if (farthest < i) return false;

            // the following is supposed to improve efficiency,
            // while it drops beat rate to 27.06% or 4.12%
            if (farthest >= (n - 1)) return true;

            farthest = Math.max(farthest, i + nums[i]);
        }
        return farthest >= (n - 1);
    }

    void test(boolean expected, int ... nums) {
        assertEquals(expected, canJump(nums));
        assertEquals(expected, canJump2(nums));
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
