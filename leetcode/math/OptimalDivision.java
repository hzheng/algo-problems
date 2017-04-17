import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC553: https://leetcode.com/problems/optimal-division/
//
// Given a list of positive integers, the adjacent integers will perform the float division.
// You can add any number of parenthesis at any position to change the priority of operations.
// Find out how to add parenthesis to get the maximum result, and return the corresponding
// expression in string format. Your expression should NOT contain redundant parenthesis.
public class OptimalDivision {
    // Bit Manipulation
    // Unnecessarily complext, but can be generalized when numbers may be less than 1
    // beats N/A(10 ms for 93 tests)
    public String optimalDivision(int[] nums) {
        int n = nums.length;
        if (n == 1) return String.valueOf(nums[0]);
        if (n == 2) return nums[0] + "/" + nums[1];

        double maxVal = 0;
        int maxMask = 0;
        double v = (double)nums[0] / (double)nums[1];
        for (int mask = (1 << (n - 2)) - 1; mask >= 0; mask--) {
            double val = v;
            for (int i = 0; i < n - 2; i++) {
                if (((1 << i) & mask) != 0) {
                    val /= nums[i + 2];
                } else {
                    val *= nums[i + 2];
                }
            }
            if (val > maxVal) {
                maxVal = val;
                maxMask = mask;
            }
        }
        String res = "" + nums[1];
        boolean last = false;
        for (int i = 0; i < n - 2; i++) {
            boolean cur = ((1 << i) & maxMask) != 0;
            if (cur != last) {
                res = "(" + res + ")";
            }
            res += "/" + nums[i + 2];
            last = cur;
        }
        return ((maxMask & 1) == 0) ? nums[0] + "/(" + res + ")" : nums[0] + "/" + res;
    }

    // beats N/A(7 ms for 93 tests)
    public String optimalDivision2(int[] nums) {
        int n = nums.length;
        if (n == 1) return String.valueOf(nums[0]);
        if (n == 2) return nums[0] + "/" + nums[1];

        StringBuilder sb = new StringBuilder(nums[0] + "/(" + nums[1]);
        for (int i = 2; i < n; i++) {
            sb.append("/").append(nums[i]);
        }
        return sb.append(")").toString();
    }

    // beats N/A(8 ms for 93 tests)
    public String optimalDivision3(int[] nums) {
        int n = nums.length;
        StringBuilder sb = new StringBuilder();
        sb.append(nums[0]);
        for (int i = 1; i < n; i++) {
            if (i == 1 && n > 2) {
                sb.append("/(").append(nums[i]);
            } else {
                sb.append("/").append(nums[i]);
            }
        }
        return (n > 2 ? sb.append(")") : sb).toString();
    }

    void test(int[] nums, String expected) {
        assertEquals(expected, optimalDivision(nums));
        assertEquals(expected, optimalDivision2(nums));
        assertEquals(expected, optimalDivision3(nums));
    }

    @Test
    public void test() {
        test(new int[] {1000}, "1000");
        test(new int[] {1000, 100}, "1000/100");
        test(new int[] {1000, 100, 10, 2}, "1000/(100/10/2)");
        test(new int[] {10, 1000, 100, 10, 2}, "10/(1000/100/10/2)");
        test(new int[] {10, 1000, 100, 10, 20, 2}, "10/(1000/100/10/20/2)");
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
