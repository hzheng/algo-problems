import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;

import static org.junit.Assert.*;

// LC047: https://leetcode.com/problems/permutations-ii/
//
// Given a collection of numbers might contain duplicates, return all unique permutations.
public class Permutation2 {
    // Recursion
    // beats 5.70%(84 ms)
    public List<List<Integer>> permuteUnique(int[] nums) {
        int n = nums.length;
        Set<List<Integer>> res = new HashSet<>();
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        do {
            List<Integer> perm = new ArrayList<>();
            for (int i : indices) {
                perm.add(nums[i]);
            }
            res.add(perm);
        } while (next(indices));
        return new ArrayList<>(res);
    }

    private boolean next(int[] indices) {
        int n = indices.length;
        int i = n - 1;
        for (; i > 0 && indices[i] < indices[i - 1]; i--) {}
        if (i == 0) { return false; }

        int j = n - 1;
        for (; indices[j] < indices[i - 1]; j--) {}
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

    // Recursion + Backtracking + Set
    // beats 2.07%(140 ms)
    public List<List<Integer>> permuteUnique2(int[] nums) {
        Set<List<Integer>> res = new HashSet<>();
        permute2(res, new ArrayList<>(), nums, new boolean[nums.length]);
        return new ArrayList<>(res);
    }

    private void permute2(Set<List<Integer>> res, List<Integer> cur, int[] nums,
                          boolean[] isVisited) {
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
    // Recursion + Backtracking + Sort + Set
    // 1 ms(98.78%), 39.6 MB(82.06%) for 33 tests
    public List<List<Integer>> permuteUnique3(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(nums);
        permute(res, new ArrayList<>(), nums, new boolean[nums.length]);
        return res;
    }

    private void permute(List<List<Integer>> res, List<Integer> cur, int[] nums,
                         boolean[] visited) {
        int n = nums.length;
        if (cur.size() == n) {
            res.add(new ArrayList<>(cur));
            return;
        }
        for (int i = 0; i < n; i++) {
            if (visited[i]) { continue; }
            if (i > 0 && nums[i] == nums[i - 1] && !visited[i - 1]) { continue; }

            visited[i] = true;
            cur.add(nums[i]);
            permute(res, cur, nums, visited);
            visited[i] = false;
            cur.remove(cur.size() - 1);
            // for (; i < n - 1 && nums[i] == nums[i + 1]; i++) {}
        }
    }

    // Solution of Choice
    // Recursion + Sort
    // beats 79.10%(4 ms for 30 tests)
    // 5 ms(30.34%), 39.7 MB(70.65%) for 33 tests
    public List<List<Integer>> permuteUnique4(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> res = new ArrayList<>();
        dfs(res, nums, 0);
        return res;
    }

    private void dfs(List<List<Integer>> res, int[] nums, int cur) {
        int n = nums.length;
        if (cur == n - 1) {
            res.add(Arrays.stream(nums).boxed().collect(Collectors.toList()));
            return;
        }
        for (int i = cur; i < n; i++) {
            if ((i == cur) || (nums[i] != nums[cur])) {
                swap(nums, i, cur);
                dfs(res, nums.clone(), cur + 1);
            }
        }
    }

    void test(Function<int[], List<List<Integer>>> perm, String name, Integer[][] expected,
              int... nums) {
        System.out.println("test " + name);
        List<List<Integer>> res = perm.apply(nums);
        Integer[][] resArray =
                res.stream().map(a -> a.toArray(new Integer[0])).toArray(Integer[][]::new);
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
            for (; i < len && (a[i] == b[i]); i++) {}
            if (i == len) { return a.length - b.length; }
            return a[i] - b[i];
        });
        return lists;
    }

    void test(Function<int[], List<List<Integer>>> perm, String name, int n, Integer[][] expected) {
        int[] nums = new int[n];
        for (int i = 1; i <= n; i++) {
            nums[i - 1] = i;
        }
        test(perm, name, expected, nums);
    }

    void test(Integer[][] expected, int... nums) {
        Permutation2 p = new Permutation2();
        test(p::permuteUnique, "permuteUnique", expected, nums);
        test(p::permuteUnique2, "permuteUnique2", expected, nums);
        test(p::permuteUnique3, "permuteUnique3", expected, nums);
        test(p::permuteUnique4, "permuteUnique4", expected, nums);
    }

    @Test public void test1() {
        test(new Integer[][] {{1, 1, 2}, {1, 2, 1}, {2, 1, 1}}, 1, 1, 2);
        test(new Integer[][] {{1, 1, 2, 2}, {1, 2, 1, 2}, {1, 2, 2, 1}, {2, 1, 1, 2}, {2, 1, 2, 1},
                              {2, 2, 1, 1}}, 2, 2, 1, 1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
