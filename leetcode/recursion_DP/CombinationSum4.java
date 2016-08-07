import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
// https://leetcode.com/problems/combination-sum-iv/
//
// Given an integer array with all positive numbers and no duplicates, find the
// number of possible combinations that add up to a positive integer target.
public class CombinationSum4 {
    // Time Limit Exceeded
    public int combinationSum4(int[] nums, int target) {
        List<Integer> smalls = new ArrayList<>();
        List<Integer> bigs = new ArrayList<>();
        int count = 0;
        for (int num : nums) {
            if (num < target / 2) {
                smalls.add(num);
            } else if (num < target) {
                bigs.add(num);
                if (num * 2 == target) {
                    count++;
                }
            } else if (num == target) {
                count++;
            }
        }
        int[] smallArray = new int[smalls.size()];
        for (int i = smalls.size() - 1; i >= 0; i--) {
            smallArray[i] = smalls.get(i);
        }
        count += combinationSum4(smallArray, target, false);
        for (int big : bigs) {
            count += combinationSum4(smallArray, target - big, true);
        }
        return count;
    }

    private int combinationSum4(int[] nums, int target, boolean extra) {
        List<int[]> res = new ArrayList<>();
        combinationSum4(nums, 0, target, new int[nums.length], res);
        int sum = 0;
        for (int[] selection : res) {
            int count = 1;
            if (extra) {
                for (int i : selection) {
                    count += i;
                }
            }
            sum += combine(selection) * count;
        }
        return sum;
    }

    private void combinationSum4(int[] nums, int start, int target,
                                 int[] list, List<int[]> res) {
        if (start >= nums.length) {
            if (target == 0) {
                res.add(list.clone());
            }
            return;
        }

        int num = nums[start];
        for (int i = target / num; i >= 0; i--) {
            list[start] = i;
            combinationSum4(nums, start + 1, target - num * i, list, res);
        }
    }

    private int combine(int[] nums) {
        return combine(nums, 0, 0, 1);
    }

    private int combine(int[] nums, int start, int count, int res) {
        if (start == nums.length) return res;

        int cur = nums[start];
        if (count == 0) return combine(nums, start + 1, cur, res);

        // deduce (m + 2) * ... * (m + n + 1) from (m + 1) * ... * (m + n)
        for (int i = 1; i <= cur; i++) { // combinatorics
            res *= (i + count);
            res /= i;
        }
        return combine(nums, start + 1, count + cur, res);
    }

    static class CombineList {
        List<int[]> lists = new ArrayList<>();

        void add(int n) {
            lists.add(new int[] {n});
        }

        void add(CombineList base, int n) {
            for (int[] list : base.lists) {
                int[] newList = Arrays.copyOf(list, list.length + 1);
                newList[list.length] = n;
                lists.add(newList);
            }
        }
    }

    // beats N/A(48 ms)
    public int combinationSum4_2(int[] nums, int target) {
        int n = nums.length;
        if (n == 0 || target <= 0) return 0;

        Arrays.sort(nums);
        if (nums[n - 1] > target) {
            n = Arrays.binarySearch(nums, target + 1);
            if (n < 0) {
                n = -n - 1;
            }
        }
        if (n == 0) return 0;

        CombineList[][] dp = new CombineList[n][target + 1];
        combinationSum4_2(nums, n - 1, target, dp);
        int count = 0;
        for (int[] indices : dp[n - 1][target].lists) {
            count += combine(indices);
        }
        return count;
    }

    private void combinationSum4_2(int[] nums, int end, int target,
                                   CombineList[][] dp) {
        if (dp[end][target] != null) return;

        dp[end][target] = new CombineList();
        int num = nums[end];
        if (end == 0) {
            if (target % num == 0) {
                dp[end][target].add(target / num);
            }
            return;
        }

        for (int i = target / num; i >= 0; i--) {
            int left = target - num * i;
            combinationSum4_2(nums, end - 1, left, dp);
            dp[end][target].add(dp[end - 1][left], i);
        }
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<int[], Integer, Integer> sum,
              int[] nums, int target, int expected) {
        assertEquals(expected, (int)sum.apply(nums, target));
    }


    void test(int[] nums, int target, int expected) {
        CombinationSum4 sum = new CombinationSum4();
        if (nums.length < 20) {
            test(sum::combinationSum4, nums, target, expected);
        }
        test(sum::combinationSum4_2, nums, target, expected);
    }

    @Test
    public void test1() {
        test(new int[] {9}, 3, 0);
        test(new int[] {1, 2, 3}, 4, 7);
        test(new int[] {1, 2, 3, 4}, 4, 8);
        test(new int[] {1, 50}, 100, 53);
        test(new int[] {1, 50}, 200, 28730);
        // test(new int[] {1, 2, 50}, 200, 1396632869);
        test(new int[] {10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130,
                        140, 150, 160, 170, 180, 190, 200, 210, 220, 230, 240,
                        250, 260, 270, 280, 290, 300, 310, 320, 330, 340, 350,
                        360, 370, 380, 390, 400, 410, 420, 430, 440, 450, 460,
                        470, 480, 490, 500, 510, 520, 530, 540, 550, 560, 570,
                        580, 590, 600, 610, 620, 630, 640, 650, 660, 670, 680,
                        690, 700, 710, 720, 730, 740, 750, 760, 770, 780, 790,
                        800, 810, 820, 830, 840, 850, 860, 870, 880, 890, 900,
                        910, 920, 930, 940, 950, 960, 970, 980, 990, 111},
             999, 1);
        test(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
                        17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
                        31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44,
                        45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58,
                        59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72,
                        73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86,
                        87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100},
             31, 1073741824);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CombinationSum4");
    }
}
