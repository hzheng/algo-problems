import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a collection of integers that might contain duplicates, return all
// possible subsets.
public class Subset2 {
    // beats 41.85%
    public List<List<Integer> > subsets(int[] nums) {
        List<List<Integer> > res = new ArrayList<>();
        Arrays.sort(nums);
        subsets(nums, res, 0, new ArrayList<>());
        return res;
    }

    private void subsets(int[] nums, List<List<Integer> > res, int start,
                         List<Integer> cur) {
        int count = cur.size();
        res.add(new ArrayList<>(cur));
        for (int i = start; i < nums.length; i++) {
            if (i == start || nums[i - 1] != nums[i]) {
                cur.add(nums[i]);
                subsets(nums, res, i + 1, cur);
                cur.remove(count);
            }
        }
    }

    // beats 13.21%
    public List<List<Integer> > subsets2(int[] nums) {
        Arrays.sort(nums);
        Set<List<Integer> > res = new HashSet<>();
        int n = nums.length;
        for (int i = ((1 << n) - 1); i >= 0; i--) {
            List<Integer> subset = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) > 0) {
                    subset.add(nums[j]);
                }
            }
            res.add(subset);
        }
        return new ArrayList<>(res);
    }

    void test(Function<int[], List<List<Integer>>> subsets,
              String name, int[] nums, Integer[][] expected) {
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
        test(s::subsets2, "subsets2", nums, expected);
    }

    @Test
    public void test1() {
        test(new int[] {1, 2, 2}, new Integer[][] {
            {}, {1}, {2}, {1, 2}, {2, 2}, {1, 2, 2}
        });
        test(new int[] {4, 2, 3}, new Integer[][] {
            {}, {4}, {2}, {3}, {4, 2}, {4, 3}, {2, 3}, {4, 2, 3}
        });
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Subset2");
    }
}
