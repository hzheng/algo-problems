import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC473: https://leetcode.com/problems/matchsticks-to-square/
//
// Remember the story of Little Match Girl? By now, you know exactly what
// matchsticks the little match girl has, please find out a way you can make one
// square by using up all those matchsticks. You should not break any stick, but
// you can link them up, and each matchstick must be used exactly one time.
// Your input will be several matchsticks the girl has, represented with their
// stick length. Your output will either be true or false, to represent whether
// you can save this little girl or not.
public class MakeSquare {
    // Recursion + Bit Manipulation
    // beats N/A(34 ms for 173 tests)
    public boolean makesquare(int[] nums) {
        int total = 0;
        for (int num : nums) {
            total += num;
        }
        if (total == 0 || (total & 3) != 0) return false;

        int len = total >> 2;
        int sidesLeft = 4;
        List<Integer> parts = new ArrayList<>();
        for (int num : nums) {
            if (num > len) return false;
            if (num == len) {
                sidesLeft--;
            } else if (num > 0) {
                parts.add(num);
            }
        }
        return sidesLeft == 0 || makesquare(parts, len, sidesLeft);
    }

    private boolean makesquare(List<Integer> parts, int len, int left) {
        int last = parts.size() - 1;
        int needed = len - parts.get(last);
        for (int mask = ((1 << last) - 1); mask > 0; mask--) {
            int want = needed;
            for (int i = 0; i < last; i++) {
                if ((mask & (1 << i)) == 0) continue;
                if ((want -= parts.get(i)) < 0) break;
            }
            if (want != 0) continue;

            List<Integer> newParts = new ArrayList<>();
            for (int i = 0; i < last; i++) {
                if ((mask & (1 << i)) == 0) {
                    newParts.add(parts.get(i));
                }
            }
            if (left == 1 || makesquare(newParts, len, left - 1)) return true;
        }
        return false;
    }

    // Recursion + DFS
    // beats N/A(20 ms for 173 tests)
    public boolean makesquare2(int[] nums) {
        int total = 0;
        for (int num : nums) {
            total += num;
        }
        if (total == 0 || (total & 3) != 0) return false;

        int len = total >> 2;
        return makesquare2(nums, new boolean[nums.length], 0, 0, len, len, 4);
    }

    private boolean makesquare2(int[] nums, boolean[] visited, int start,
                                int cur, int len, int lenLeft, int sideLeft) {
        if (lenLeft == 0) {
            return sideLeft == 1 ||
                   makesquare2(nums, visited, start + 1, start + 1, len, len, sideLeft - 1);
        }
        for (int i = cur; i < nums.length; i++) {
            if (visited[i] || lenLeft < nums[i]) continue;

            visited[i] = true;
            if (makesquare2(nums, visited, start, i + 1, len,
                            lenLeft - nums[i], sideLeft)) return true;
            visited[i] = false;
        }
        return false;
    }

    // Recursion + DFS + Dynamic Programming
    // beats N/A(1350 ms for 173 tests)
    public boolean makesquare3(int[] nums) {
        int total = 0;
        for (int num : nums) {
            total += num;
        }
        return total > 0 && (total & 3) == 0
               && makesquare3(nums, new int[4], 0, total >> 2);
    }

    private boolean makesquare3(int[] nums, int[] sums, int index, int side) {
        if (index == nums.length) {
            return sums[0] == side && sums[1] == side && sums[2] == side;
        }
        for (int i = 0; i < 4; i++) {
            if (sums[i] + nums[index] > side) continue;

            sums[i] += nums[index];
            if (makesquare3(nums, sums, index + 1, side)) return true;
            sums[i] -= nums[index];
        }
        return false;
    }

    // beats N/A(578 ms for 173 tests)
    public boolean makesquare4(int[] nums) {
        int total = 0;
        for (int num : nums) {
            total += num;
        }
        if (total == 0 || (total & 3) != 0) return false;

        Set<Integer> combinations = new HashSet<>();
        makesquare4(nums, combinations, 0, total >> 2);
        int maxMask = (1 << nums.length) - 1;
        boolean[] dp = new boolean[maxMask + 1];
        dp[0] = true;
        for (int combination : combinations) {
            for (int mask = maxMask; mask >= 0; mask--) {
                dp[mask | combination] |= (dp[mask] && (combination & mask) == 0);
            }
        }
        return dp[maxMask];
    }

    private void makesquare4(int[] nums, Set<Integer> combinations, int flag, int lenLeft) {
        if (lenLeft == 0) {
            combinations.add(flag);
            return;
        }
        if (nums.length == 0) return;

        int[] nextNums = Arrays.copyOfRange(nums, 1, nums.length);
        if (nums[0] <= lenLeft) {
            makesquare4(nextNums, combinations,
                        flag | (1 << nums.length - 1), lenLeft - nums[0]);
        }
        makesquare4(nextNums, combinations, flag, lenLeft);
    }

    void test(boolean expected, int ... nums) {
        assertEquals(expected, makesquare(nums));
        assertEquals(expected, makesquare2(nums));
        assertEquals(expected, makesquare3(nums));
        assertEquals(expected, makesquare4(nums));
    }

    @Test
    public void test() {
        test(true, 1, 1, 2, 2, 2);
        test(false, 3, 3, 3, 3, 4);
        test(true, 1, 2, 3, 2, 4, 4);
        test(true, 2, 2, 2, 3, 4, 4, 4, 5, 6);
        test(true, 5, 5, 5, 5, 4, 4, 4, 4, 3, 3, 3, 3);
        test(true, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        test(false, 5, 5, 5, 5, 16, 4, 4, 4, 4, 4, 3, 3, 3, 3, 4);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MakeSquare");
    }
}
