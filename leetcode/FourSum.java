import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

//  Find all unique quadruplets in the array which gives the sum of target
public class FourSum {
    // time complexity: O(N ^ 3)
    // beats 88.26%
    public List<List<Integer> > fourSum(int[] nums, int target) {
        if (nums == null || nums.length < 4) return Collections.emptyList();

        Arrays.sort(nums);
        List<List<Integer> > result = new ArrayList<List<Integer> >();
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
            if (sum == target) {
                List<Integer> l = Arrays.asList(firstNum, secondNum,
                                                nums[i], nums[j]);
                lists.add(l);
                i++;
                while (i < j && nums[i] == nums[i - 1]) i++;
                j--;
                while (i < j && nums[j] == nums[j + 1]) j--;
            } else if (sum < target) {
                i++;
            } else {
                j--;
            }
        }
    }

    void test(int[] nums, int target, Integer[][] expected) {
        FourSum sum = new FourSum();
        test(sum::fourSum, "fourSum", nums, target, expected);
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
