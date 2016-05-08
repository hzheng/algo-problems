import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

//  Find all unique triplets in the array which gives the sum of zero.
public class ThreeSum {
    // time complexity: O(N ^ 2)
    // beats 8.96%
    public List<List<Integer> > threeSum(int[] nums) {
        if (nums == null || nums.length < 3) return Collections.emptyList();

        Set<List<Integer> > result = new HashSet<List<Integer> >();
        Set<Integer> computed = new HashSet<>();
        Map<Integer, Integer> map = new HashMap<>();
        Arrays.sort(nums); // if not sorted, will result in 'Time Limit Exceeded'
        for (int i = 0; i < nums.length - 2; i++) {
            int num = nums[i];
            if (!computed.contains(num)) {
                twoSum(nums, i, result, map);
                computed.add(num);
            }
        }
        return new ArrayList<List<Integer> >(result);
    }

    private void twoSum(int[] nums, int start, Set<List<Integer> > lists,
                        Map<Integer, Integer> map) {
        map.clear();
        int target = -nums[start];
        for (int i = start + 1; i < nums.length; i++) {
            int complement = target - nums[i];
            if (map.containsKey(complement)) {
                List<Integer> l = Arrays.asList(-target, complement, nums[i]);
                // Collections.sort(l);
                lists.add(l);
            } else {
                map.put(nums[i], i);
            }
        }
    }

    void test(int[] nums, int[][] expected) {
        List<List<Integer> > res = threeSum(nums);
        // System.out.println(res);
        Integer[][] lists = res.stream().map(
            l -> l.stream().toArray(Integer[]::new)).toArray(Integer[][]::new);
        assertArrayEquals(expected, lists);
    }

    @Test
    public void test1() {
        test(new int[] {-1, 0, 1, 2, -1, -4},
             new int[][] {{-1, -1, 2}, {-1, 0, 1}});
        //  new int[][] {{1, 0, -1}, {-1, -1, 2}});
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
             new int[][] {
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
