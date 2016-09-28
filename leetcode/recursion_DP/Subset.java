import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC078: https://leetcode.com/problems/subsets/
// Cracking the Coding Interview(5ed) Problem 9.4
//
// Given a set of distinct integers, nums, return all possible subsets.
public class Subset {
    // Backtracking + Binary Search
    // beats 8.05%(3 ms)
    public List<List<Integer> > subsets(int[] nums) {
        List<List<Integer> > res = new ArrayList<>();
        Arrays.sort(nums);
        for (int i = 0; i <= nums.length; i++) {
            subsets(nums, res, i, new ArrayList<Integer>());
        }
        return res;
    }

    private void subsets(int[] nums, List<List<Integer> > res, int total,
                         List<Integer> cur) {
        int count = cur.size();
        if (count == total) {
            res.add(new ArrayList<Integer>(cur));
            return;
        }

        int nextIndex = 0;
        if (count > 0) {
            nextIndex = Arrays.binarySearch(
                nums, count - 1, nums.length, cur.get(count - 1)) + 1;
        }

        for (int i = nextIndex; i < nums.length; i++) {
            cur.add(nums[i]);
            subsets(nums, res, total, cur);
            cur.remove(count);
        }
    }

    // Solution of Choice
    // Backtracking
    // beats 26.55%(2 ms)
    public List<List<Integer> > subsets2(int[] nums) {
        List<List<Integer> > res = new ArrayList<>();
        subsets2(nums, 0, new ArrayList<>(), res);
        return res;
    }

    private void subsets2(int[] nums, int start, List<Integer> buf,
                          List<List<Integer>> res) {
        res.add(new ArrayList<>(buf));
        for (int i = start, count = buf.size(); i < nums.length; i++) {
            buf.add(nums[i]);
            subsets2(nums, i + 1, buf, res);
            buf.remove(count);
        }
    }

    // Solution of Choice
    // Bit Manipulation
    // beats 91.45%(1 ms)
    public List<List<Integer> > subsets3(int[] nums) {
        List<List<Integer> > res = new ArrayList<>();
        int n = nums.length;
        for (int i = ((1 << n) - 1); i >= 0; i--) {
            List<Integer> subset = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) > 0) {
                    subset.add(nums[j]);
                }
            }
            // or: beats 26.55%(2 ms)
            // for (int j = i, index = 0; j > 0; j >>= 1, index++) {
            //     if ((j & 1) > 0) {
            //         subset.add(nums[index]);
            //     }
            // }
            res.add(subset);
        }
        return res;
    }

    // Recursion
    // From Cracking the Coding Interview
    // beats 26.55%(2 ms)
    public List<List<Integer> > subsets4(int[] nums) {
        return subsets4(nums, 0);
    }

    private List<List<Integer> > subsets4(int[] nums, int index) {
        if (nums.length == index) { // Base case - add empty set
            return new ArrayList<>(Arrays.asList(new ArrayList<>()));
        }

        List<List<Integer>> allSubsets = subsets4(nums, index + 1);
        int item = nums[index];
        List<List<Integer> > moreSubsets = new ArrayList<>();
        for (List<Integer> subset : allSubsets) {
            List<Integer> cloned = new ArrayList<>(subset);
            cloned.add(item);
            moreSubsets.add(cloned);
        }
        allSubsets.addAll(moreSubsets);
        return allSubsets;
    }

    // Solution of Choice
    // Iteration version of last method
    // beats 26.55%(2 ms)
    public List<List<Integer> > subsets5(int[] nums) {
        List<List<Integer> > allSubsets = new ArrayList<>(Arrays.asList(new ArrayList<>()));
        for (int i : nums) {
            List<List<Integer>> moreSubsets = new ArrayList<>();
            for (List<Integer> set : allSubsets) {
                List<Integer> cloned = new ArrayList<>(set);
                cloned.add(i);
                moreSubsets.add(cloned);
            }
            allSubsets.addAll(moreSubsets);
        }
        return allSubsets;
    }

    void test(Function<int[], List<List<Integer>>> subsets,
              String name, int[] nums, Integer[][] expected) {
        nums = nums.clone();
        List<List<Integer> > res = subsets.apply(nums);
        Integer[][] resArray = res.stream().map(
            a -> a.toArray(new Integer[0])).toArray(Integer[][]::new);
        sort(resArray);
        sort(expected);
        assertArrayEquals(expected, resArray);
    }

    Integer[][] sort(Integer[][] lists) {
        Arrays.sort(lists, (a, b) -> {
            Arrays.sort(a);
            Arrays.sort(b);
            int len = Math.min(a.length, b.length);
            int i = 0;
            for (; i < len && (a[i] == b[i]); i++);
            if (i == len) return a.length - b.length;
            return a[i] - b[i];
        });
        return lists;
    }

    void test(int[] nums, Integer[][] expected) {
        Subset s = new Subset();
        test(s::subsets, "subsets", nums, expected);
        test(s::subsets2, "subsets2", nums, expected);
        test(s::subsets3, "subsets3", nums, expected);
        test(s::subsets4, "subsets4", nums, expected);
        test(s::subsets5, "subsets5", nums, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1, 2, 3}, new Integer[][] {
            {}, {1}, {2}, {3}, {1, 2}, {1, 3}, {2, 3}, {1, 2, 3}
        });
        test(new int[] {4, 2, 3}, new Integer[][] {
            {}, {4}, {2}, {3}, {4, 2}, {4, 3}, {2, 3}, {4, 2, 3}
        });
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Subset");
    }
}
