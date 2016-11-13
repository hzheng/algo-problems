import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC018: https://leetcode.com/problems/4sum/
//
//  Find all unique quadruplets in the array which gives the sum of target
public class FourSum {
    // Solution of Choice
    // Sort + Two Pointers
    // time complexity: O(N ^ 3)
    // beats 74.18%(69 ms for 282 tests)
    public List<List<Integer> > fourSum(int[] nums, int target) {
        if (nums == null || nums.length < 4) return Collections.emptyList();

        Arrays.sort(nums);
        List<List<Integer> > result = new ArrayList<>();
        for (int i = 0; i < nums.length - 3; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) continue;

            int tgt = target - nums[i];
            for (int j = i + 1; j < nums.length - 2; j++) {
                if (j == i + 1 || nums[j] > nums[j - 1]) {
                    twoSum(nums, tgt, i, j, result);
                }
            }
        }
        return result;
    }

    private void twoSum(int[] nums, int target, int first, int second,
                        List<List<Integer> > lists) {
        int firstNum = nums[first];
        int secondNum = nums[second];
        target -= secondNum;
        for (int i = second + 1, j = nums.length - 1; j > i; ) {
            int sum = nums[i] + nums[j];
            if (sum < target) {
                i++;
            } else if (sum > target) {
                j--;
            } else {
                List<Integer> l = Arrays.asList(firstNum, secondNum,
                                                nums[i], nums[j]);
                lists.add(l);
                while (++i < j && nums[i] == nums[i - 1]) {}
                while (i < --j && nums[j] == nums[j + 1]) {}
            }
        }
    }

    //https://discuss.leetcode.com/topic/29585/7ms-java-code-win-over-100/
    // beats 95.87%(28 ms for 282 tests)
    public List<List<Integer> > fourSum2(int[] nums, int target) {
        List<List<Integer> > res = new ArrayList<>();
        Arrays.sort(nums);
        int n = nums.length;
        int max = n > 0 ? nums[n - 1] : 0;
        if (n < 4 || 4 * nums[0] > target || 4 * max < target) return res;

        for (int i = 0; i < n; i++) {
            int num = nums[i];
            if (i > 0 && num == nums[i - 1] || num + 3 * max < target) continue;
            if (4 * num > target) break;

            if (4 * num == target) {
                if (i + 3 < n && nums[i + 3] == num) {
                    res.add(Arrays.asList(num, num, num, num));
                }
                break;
            }
            if (i + 3 < n) {
                threeSum(nums, target - num, i + 1, n - 1, num, res);
            }
        }
        return res;
    }

    private void threeSum(int[] nums, int target, int start, int end, int a,
                          List<List<Integer> > res) {
        int max = nums[end];
        if (3 * nums[start] > target || 3 * max < target) return;

        for (int i = start; i < end - 1; i++) {
            int num = nums[i];
            if (i > start && num == nums[i - 1]) continue;
            if (num + 2 * max < target) continue;

            if (3 * num > target) return;

            if (3 * num == target) {
                if (i + 1 < end && nums[i + 2] == num) {
                    res.add(Arrays.asList(a, num, num, num));
                }
                return;
            }
            if (i + 1 < end) {
                twoSum(nums, target - num, i + 1, end, a, num, res);
            }
        }
    }

    private void twoSum(int[] nums, int target, int start, int end, int a, int b,
                        List<List<Integer> > res) {
        if (2 * nums[start] > target || 2 * nums[end] < target) return;

        for (int i = start, j = end; i < j; ) {
            int sum = nums[i] + nums[j];
            if (sum < target) {
                i++;
            } else if (sum > target) {
                j--;
            } else {
                res.add(Arrays.asList(a, b, nums[i], nums[j]));
                for (int c = nums[i++]; i < j && nums[i] == c; i++) {}
                for (int c = nums[j--]; i < j && nums[j] == c; j--) {}
            }
        }
    }

    void test(int[] nums, int target, Integer[][] expected) {
        FourSum sum = new FourSum();
        test(sum::fourSum, "fourSum", nums, target, expected);
        test(sum::fourSum2, "fourSum2", nums, target, expected);
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<int[], Integer, List<List<Integer>>> fourSum,
              String name, int[] nums, int target, Integer[][] expected) {
        List<List<Integer> > res = fourSum.apply(nums, target);
        // System.out.println(res);
        Integer[][] resArray = res.stream().map(
            l -> l.stream().toArray(Integer[]::new)).toArray(Integer[][]::new);
        resArray = sort(resArray);
        expected = sort(expected);
        assertArrayEquals(expected, resArray);
    }

    Integer[][] sort(Integer[][] lists) {
        Arrays.sort(lists, (a, b) -> {
            if (a[0] != b[0]) return a[0] - b[0];
            if (a[1] != b[1]) return a[1] - b[1];
            return a[2] - b[2];
        });
        return lists;
    }

    @Test
    public void test1() {
        test(new int[] {}, 0, new Integer[0][0]);
        test(new int[] {1, 0, -1, 0, -2, 2}, 0,
             new Integer[][] {{-1, 0, 0, 1}, {-2, -1, 1, 2}, {-2, 0, 0, 2}});
        test(new int[] {-3, -2, -1, 0, 0, 1, 2, 3}, 0,
             new Integer[][] {{-3, -2, 2, 3}, {-3, -1, 1, 3}, {-3, 0, 0, 3},
                              {-3, 0, 1, 2}, {-2, -1, 0, 3}, {-2, -1, 1, 2},
                              {-2, 0, 0, 2}, {-1,0,0,1}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FourSum");
    }
}
