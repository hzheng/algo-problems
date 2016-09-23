import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

//  Find all unique triplets in the array which gives the sum of zero.
public class ThreeSum {
    // Sort + Hashtable
    // time complexity: O(N ^ 2)
    // beats 12.35%(34 ms)
    public List<List<Integer> > threeSum(int[] nums) {
        if (nums == null || nums.length < 3) return Collections.emptyList();

        Set<List<Integer> > result = new HashSet<List<Integer> >();
        Map<Integer, Integer> map = new HashMap<>();
        Arrays.sort(nums); // if not sorted, will result in 'Time Limit Exceeded'
        for (int i = 0; i < nums.length - 2; i++) {
            if (nums[i] > 0) break;
            if (i == 0 || nums[i] > nums[i - 1]) {
                twoSum(nums, i, result, map);
            }
        }
        return new ArrayList<>(result);
    }

    private void twoSum(int[] nums, int start, Set<List<Integer> > lists,
                        Map<Integer, Integer> map) {
        map.clear();
        int target = -nums[start];
        for (int i = start + 1; i < nums.length; i++) {
            int num = nums[i];
            int complement = target - num;
            if (map.containsKey(complement)) {
                List<Integer> l = Arrays.asList(-target, complement, num);
                // Collections.sort(l);
                lists.add(l);
            } else {
                map.put(num, i);
            }
        }
    }

    // Sort + Binary Search
    // time complexity: O(N ^ 2 * log(N))
    // beats 18.12%(29 ms)
    public List<List<Integer> > threeSum2(int[] nums) {
        if (nums == null || nums.length < 3) return Collections.emptyList();

        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2 && nums[i] <= 0; i++) {
            if (i == 0 || nums[i] > nums[i - 1]) {
                twoSum2(nums, i, res);
            }
        }
        return res;
    }

    private void twoSum2(int[] nums, int start, List<List<Integer> > lists) {
        int target = nums[start];
        int lastNum = target - 1; // make it different
        for (int i = start + 1; i < nums.length; i++) {
            int num = nums[i];
            if (num == lastNum) continue;

            lastNum = num;
            int complement = -target - num;
            if (Arrays.binarySearch(nums, i + 1, nums.length, complement) >= 0) {
            // if (find(nums, i + 1, complement)) {
                List<Integer> l = Arrays.asList(target, num, complement);
                lists.add(l);
            }
        }
    }

    private boolean find(int[] nums, int start, int target) {
        for (int end = nums.length - 1; end >= start; ) {
            int mid = (start + end) / 2;
            if (nums[mid] == target) return true;

            if (nums[mid] < target) {
                start = mid + 1;
            } else {
                end = mid - 1;
            }
        }
        return false;
    }

    // Sort + Two Pointers
    // time complexity: O(N)
    // beats 21.95%(26 ms)
    public List<List<Integer> > threeSum3(int[] nums) {
        List<List<Integer> > res = new ArrayList<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2 && nums[i] <= 0; i++) {
            if (i == 0 || nums[i] > nums[i - 1]) {
                twoSum3(nums, i, res);
            }
        }
        return res;
    }

    private void twoSum3(int[] nums, int start, List<List<Integer> > lists) {
        int target = -nums[start];
        for (int i = start + 1, j = nums.length - 1; j > i; ) {
            int sum = nums[i] + nums[j];
            if (sum < target) {
                i++;
            } else if (sum > target) {
                j--;
            } else {
                lists.add(Arrays.asList(-target, nums[i], nums[j]));
                while (i < j && nums[i] == nums[i + 1]) i++;
                i++;
                while (i < j && nums[j] == nums[j - 1]) j--;
                j--;
            }
        }
    }

    void test(int[] nums, Integer[][] expected) {
        ThreeSum sum = new ThreeSum();
        test(sum::threeSum, "threeSum", nums, expected);
        test(sum::threeSum2, "threeSum2", nums, expected);
        test(sum::threeSum3, "threeSum3", nums, expected);
    }

    void test(Function<int[], List<List<Integer>>> threeSum, String name,
              int[] nums, Integer[][] expected) {
        List<List<Integer> > res = threeSum.apply(nums.clone());
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
        test(new int[] {-1, 0, 1, 2, -1, -4},
             new Integer[][] {{-1, -1, 2}, {-1, 0, 1}});
    }

    @Test
    public void test2() {
        test(new int[] {
            12, 0, 3, -14, 5, -11, 11, -5, -2, -1, 6, -7, -10, 1, 4, 1, 1, 9,
            -3, 6, -15, 0, 6, 1, 6, -12, 3, 7, 11, -6, -8, 0, 9, 3, -7, -1, 7,
            -10, 1, 13, -4, -7, -9, -7, 9, 3, 1, -13, -3, 13, 8, -11, -9, -8,
            -3, 4, -13, 7, -11, 5, -14, -4, -9,10, 6, -9, -6, -9, -12, 11, -11,
            -9, 11, -5, 0, -3, 13, -14, -1, -13, 7, -7, 14, 5, 0, -4, -6, -6,
            -11, -2, 14, -10, 2, 12, 8, -7, -11, -13, -9, 14, 5, -5, -9, 1, -2,
            6, 5, -12, -1, -10, -9, -9, -10, 12, 11
        },
             new Integer[][] {
            {-11, -1, 12}, {-7, -1, 8}, {-3, -1, 4}, {-8, -3, 11},
            {-4, -3, 7}, {-9, -5, 14}, {-5, -5, 10}, {-12, 6, 6},
            {-13, 4, 9}, {-9, 4, 5}, {-14, 2, 12}, {-10, 2, 8}, {-6, 2, 4},
            {-11, 0, 11}, {-7, 0, 7}, {-3, 0, 3}, {-12, -2, 14},
            {-8, -2, 10}, {-4, -2, 6}, {-9, -4, 13}, {-5, -4, 9},
            {-6, -6, 12}, {-13, 5, 8}, {-14, 3, 11}, {-10, 3, 7},
            {-6, 3, 3}, {-15, 1, 14}, {-11, 1, 10}, {-7, 1, 6}, {-3, 1, 2},
            {-12, -1, 13}, {-8, -1, 9}, {-4, -1, 5}, {-9, -3, 12},
            {-5, -3, 8}, {-6, -5, 11}, {-7, -7, 14}, {-13, 6, 7},
            {-14, 4, 10}, {-10, 4, 6}, {-15, 2, 13}, {-11, 2, 9},
            {-7, 2, 5}, {-12, 0, 12}, {-8, 0, 8}, {-4, 0, 4}, {0, 0, 0},
            {-9, -2, 11}, {-5, -2, 7}, {-10, -4, 14}, {-6, -4, 10},
            {-7, -6, 13}, {-14, 5, 9}, {-10, 5, 5}, {-15, 3, 12},
            {-11, 3, 8}, {-7, 3, 4}, {-12, 1, 11}, {-8, 1, 7}, {-4, 1, 3},
            {-13, -1, 14}, {-9, -1, 10}, {-5, -1, 6}, {-1, -1, 2},
            {-10, -3, 13}, {-6, -3, 9}, {-7, -5, 12}, {-14, 6, 8},
            {-15, 4, 11}, {-11, 4, 7}, {-12, 2, 10}, {-8, 2, 6},
            {-13, 0, 13}, {-9, 0, 9}, {-5, 0, 5}, {-1, 0, 1}, {-10, -2, 12},
            {-6, -2, 8}, {-2, -2, 4}, {-7, -4, 11}, {-8, -6, 14},
            {-14, 7, 7}, {-15, 5, 10}, {-11, 5, 6}, {-12, 3, 9}, {-8, 3, 5},
            {-13, 1, 12}, {-9, 1, 8}, {-5, 1, 4}, {-10, -1, 11}, {-6, -1, 7},
            {-2, -1, 3}, {-11, -3, 14}, {-7, -3, 10}, {-3, -3, 6},
            {-8, -5, 13}, {-15, 6, 9}, {-12, 4, 8}, {-8, 4, 4}, {-13, 2, 11},
            {-9, 2, 7}, {-5, 2, 3}, {-14, 0, 14}, {-10, 0, 10}, {-6, 0, 6},
            {-2, 0, 2}, {-11, -2, 13}, {-7, -2, 9}, {-3, -2, 5},
            {-8, -4, 12}, {-4, -4, 8}, {-15, 7, 8}, {-12, 5, 7},
            {-13, 3, 10}, {-9, 3, 6}, {-14, 1, 13}, {-10, 1, 9},
            {-6, 1, 5}, {-2, 1, 1}
        });
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ThreeSum");
    }
}
