import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC090: https://leetcode.com/problems/subsets-ii/
//
// Given a collection of integers that might contain duplicates, return all
// possible subsets.
public class Subset2 {
    // Solution of Choice
    // Backtracking
    // beats 76.22%(2 ms)
    public List<List<Integer> > subsets(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer> > res = new ArrayList<>();
        subsets(nums, res, 0, new ArrayList<>());
        return res;
    }

    private void subsets(int[] nums, List<List<Integer> > res, int start,
                         List<Integer> cur) {
        res.add(new ArrayList<>(cur));
        int count = cur.size();
        for (int i = start; i < nums.length; i++) {
            if (i == start || nums[i - 1] != nums[i]) {
                cur.add(nums[i]);
                subsets(nums, res, i + 1, cur);
                cur.remove(count);
            }
        }
    }

    // Backtracking
    // beats 29.78%(4 ms)
    public List<List<Integer> > subsets1(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer> > res = new ArrayList<>();
        subsets1(nums, 0, new ArrayList<>(), res);
        return res;
    }

    private void subsets1(int[] nums, int start, List<Integer> path,
                          List<List<Integer> > res) {
        res.add(path);
        for (int i = start; i < nums.length; i++) {
            if (i == start || nums[i - 1] != nums[i]) {
                List<Integer> cloned = new ArrayList<>(path);
                cloned.add(nums[i]);
                subsets1(nums, i + 1, cloned, res);
            }
        }
    }

    // Bit Manipulation + Set
    // beats 18.67%(9 ms)
    public List<List<Integer> > subsets2(int[] nums) {
        Arrays.sort(nums); // cannot be omitted
        Set<List<Integer> > res = new HashSet<>();
        int n = nums.length;
        for (int i = ((1 << n) - 1); i >= 0; i--) {
            List<Integer> subset = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) != 0) {
                    subset.add(nums[j]);
                }
            }
            res.add(subset);
        }
        return new ArrayList<>(res);
    }

    // Solution of Choice
    // Bit Manipulation
    // beats 29.78%(4 ms)
    public List<List<Integer> > subsets3(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer> > res = new ArrayList<>();
outer:
        for (int n = nums.length, i = ((1 << n) - 1); i >= 0; i--) {
            List<Integer> subset = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) == 0) continue;

                if (j > 0 && nums[j] == nums[j - 1] && (i & (1 << (j - 1))) == 0) {
                    continue outer;
                }
                subset.add(nums[j]);
            }
            res.add(subset);
        }
        return res;
    }

    // Solution of Choice
    // Iteration
    // beats 40.11%(3 ms)
    public List<List<Integer> > subsets4(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer> > res = new ArrayList<>(Arrays.asList(new ArrayList<>()));
        for (int i = 0, n = nums.length, size = 0; i < n; i++) {
            int start = (i > 0 && nums[i] == nums[i - 1]) ? size : 0;
            size = res.size();
            for (int j = start; j < size; j++) {
                List<Integer> cloned = new ArrayList<>(res.get(j));
                cloned.add(nums[i]);
                res.add(cloned);
            }
        }
        return res;
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
        Subset2 s = new Subset2();
        test(s::subsets, "subsets", nums, expected);
        test(s::subsets1, "subsets1", nums, expected);
        test(s::subsets2, "subsets2", nums, expected);
        test(s::subsets3, "subsets3", nums, expected);
        test(s::subsets4, "subsets4", nums, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1, 2, 2}, new Integer[][] {
            {}, {1}, {2}, {1, 2}, {2, 2}, {1, 2, 2}
        });
        test(new int[] {4, 2, 3}, new Integer[][] {
            {}, {4}, {2}, {3}, {4, 2}, {4, 3}, {2, 3}, {4, 2, 3}
        });
        test(new int[] {4, 4, 4, 1, 4}, new Integer[][] {
            {}, {1}, {4}, {1, 4}, {1, 4, 4}, {1, 4, 4, 4},
            {1, 4, 4, 4, 4}, {4, 4}, {4, 4, 4}, {4, 4, 4, 4}
        });
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Subset2");
    }
}
