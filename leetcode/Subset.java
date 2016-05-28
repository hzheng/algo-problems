import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a set of distinct integers, nums, return all possible subsets.
public class Subset {
    // beats 15.24%
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
            int last = cur.get(count - 1);
            for (int i = 0; i < nums.length; i++) {
                if (nums[i] == last) {
                    nextIndex = i + 1;
                    break;
                }
            }
        }

        for (int i = nextIndex; i < nums.length; i++) {
            cur.add(nums[i]);
            subsets(nums, res, total, cur);
            cur.remove(count);
        }
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
        Subset s = new Subset();
        test(s::subsets, "subsets", nums, expected);
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
