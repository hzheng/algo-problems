import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC047: https://leetcode.com/problems/permutations-ii/
//
// Given a collection of numbers might contain duplicates, return all unique permutations.
public class Permutation2 {
    // Recursion
    // beats 5.70%(84 ms)
    public List<List<Integer> > permuteUnique(int[] nums) {
        int n = nums.length;
        Set<List<Integer> > res = new HashSet<>();
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        while (true) {
            List<Integer> perm = new ArrayList<>();
            for (int i : indices) {
                perm.add(nums[i]);
            }
            res.add(perm);
            if (!next(indices)) break;
        }
        return new ArrayList<>(res);
    }

    private boolean next(int[] indices) {
        int n = indices.length;
        int i = n - 1;
        for (; i > 0 && indices[i] < indices[i - 1]; i--);
        if (i == 0) return false;

        int j = n - 1;
        for (; indices[j] < indices[i - 1]; j--);
        swap(indices, i - 1, j);
        for (j = n - 1; j > i; i++, j--) {
            swap(indices, i, j);
        }
        return true;
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    // Backtracking
    // beats 2.07%(140 ms)
    public List<List<Integer> > permuteUnique2(int[] nums) {
        Set<List<Integer> > res = new HashSet<>();
        permute2(res, new ArrayList<>(), nums, new boolean[nums.length]);
        return new ArrayList<>(res);
    }

    private void permute2(Set<List<Integer> > res, List<Integer> cur,
                          int[] nums, boolean[] isVisited) {
        int n = nums.length;
        if (cur.size() == n) {
            res.add(new ArrayList<>(cur));
            return;
        }

        for (int i = 0; i < nums.length; ++i) {
            if (!isVisited[i]) {
                isVisited[i] = true;
                cur.add(nums[i]);
                permute2(res, cur, nums, isVisited);
                isVisited[i] = false;
                cur.remove(cur.size() - 1);
            }
        }
    }

    // Solution of Choice
    // beats 97.22%(3 ms)
    public List<List<Integer> > permuteUnique3(int[] nums) {
        List<List<Integer> > res = new ArrayList<>();
        Arrays.sort(nums);
        permute3(res, new ArrayList<>(), nums, new boolean[nums.length]);
        return res;
    }

    private void permute3(List<List<Integer> > res, List<Integer> cur,
                          int[] nums, boolean[] isVisited) {
        int n = nums.length;
        if (cur.size() == n) {
            res.add(new ArrayList<>(cur));
            return;
        }

        for (int i = 0; i < nums.length; ++i) {
            if (!isVisited[i]) {
                isVisited[i] = true;
                cur.add(nums[i]);
                permute3(res, cur, nums, isVisited);
                isVisited[i] = false;
                cur.remove(cur.size() - 1);
                for (; i < n - 1 && nums[i] == nums[i + 1]; ++i) {}
            }
        }
    }

    void test(Function<int[], List<List<Integer>>> perm, String name,
              Integer[][] expected, int ... nums) {
        System.out.println("test " + name);
        List<List<Integer> > res = perm.apply(nums);
        Integer[][] resArray = res.stream().map(
            a -> a.toArray(new Integer[0])).toArray(Integer[][]::new);
        sort(resArray);
        sort(expected);
        assertArrayEquals(expected, resArray);
    }

    Integer[][] sort(Integer[][] lists) {
        Arrays.sort(lists, (a, b) -> {
            // Arrays.sort(a);
            // Arrays.sort(b);
            int len = Math.min(a.length, b.length);
            int i = 0;
            for (; i < len && (a[i] == b[i]); i++);
            if (i == len) return a.length - b.length;
            return a[i] - b[i];
        });
        return lists;
    }

    void test(Function<int[], List<List<Integer>>> perm, String name,
              int n, Integer[][] expected) {
        int[] nums = new int[n];
        for (int i = 1; i <= n; i++) {
            nums[i - 1] = i;
        }
        test(perm, name, expected, nums);
    }

    void test(Integer[][] expected, int ... nums) {
        Permutation2 p = new Permutation2();
        test(p::permuteUnique, "permuteUnique", expected, nums);
        test(p::permuteUnique2, "permuteUnique2", expected, nums);
        test(p::permuteUnique3, "permuteUnique3", expected, nums);
    }

    @Test
    public void test1() {
        test(new Integer[][] { {1, 1, 2}, {1, 2, 1}, {2, 1, 1}}, 1, 1, 2);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Permutation2");
    }
}
