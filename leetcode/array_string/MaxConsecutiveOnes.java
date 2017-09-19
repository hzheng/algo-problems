import org.junit.Test;
import static org.junit.Assert.*;

// LC485: https://leetcode.com/problems/max-consecutive-ones/
//
// Given a binary array, find the maximum number of consecutive 1s in this array.
public class MaxConsecutiveOnes {
    // beats 46.24%(11 ms for 41 tests)
    public int findMaxConsecutiveOnes(int[] nums) {
        int max = 0;
        int cur = 0;
        boolean inStream = false;
        for (int i : nums) {
            if (i == 1) {
                cur++;
                inStream = true;
            } else if (inStream) {
                max = Math.max(max, cur);
                cur = 0;
                inStream = false;
            }
        }
        return inStream ? Math.max(max, cur) : max;
    }

    // beats 46.24%(11 ms for 41 tests)
    public int findMaxConsecutiveOnes2(int[] nums) {
        int max = 0;
        int cur = 0;
        for (int i : nums) {
            if (i == 0) {
                cur = 0;
            } else {
                cur++;
                max = Math.max(max, cur);
            }
        }
        return max;
    }

    void test(int[] nums, int expected) {
        assertEquals(expected, findMaxConsecutiveOnes(nums));
        assertEquals(expected, findMaxConsecutiveOnes2(nums));
    }

    @Test
    public void test() {
        test(new int[] {}, 0);
        test(new int[] {0, 0}, 0);
        test(new int[] {1, 1, 0, 1, 1, 1, 0}, 3);
        test(new int[] {1, 1, 0, 1, 1, 1}, 3);
        test(new int[] {1, 1, 1, 1, 0, 1, 1, 1}, 4);
        test(new int[] {1, 1, 1}, 3);
        test(new int[] {1, 1, 0, 1, 1, 1, 1, 0, 0, 1}, 4);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxConsecutiveOnes");
    }
}
