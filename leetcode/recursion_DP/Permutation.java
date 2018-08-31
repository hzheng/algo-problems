import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;
import static org.junit.Assert.*;

// LC046: https://leetcode.com/problems/permutations/
//
// Given a collection of distinct numbers, return all possible permutations.
public class Permutation {
    // Recursion
    // beats 13.69%(6 ms)
    public List<List<Integer> > permute(int[] nums) {
        int n = nums.length;
        if (n == 0) return Collections.emptyList();

        List<List<Integer> > res = new ArrayList<>();
        if (n == 1) {
            List<Integer> perm = new ArrayList<>();
            perm.add(nums[0]);
            res.add(perm);
            return res;
        }

        for (int i = 0; i < n; i++) {
            int[] partialNums = new int[n - 1];
            for (int j = 0; j < n - 1; j++) {
                if (j < i) {
                    partialNums[j] = nums[j];
                } else {
                    partialNums[j] = nums[j + 1];
                }
            }
            for (List<Integer> p : permute(partialNums)) {
                List<Integer> perm = new ArrayList<>();
                perm.add(nums[i]);
                perm.addAll(p);
                res.add(perm);
            }
        }
        return res;
    }

    // Iteration
    // beats 7.7%
    public List<List<Integer> > permute2(int[] nums) {
        List<List<Integer> > res = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            indices.add(i);
        }
        while (indices != null) {
            List<Integer> perm = new ArrayList<>();
            for (int i : indices) {
                perm.add(nums[i]);
            }
            res.add(perm);
            indices = next(indices);
        }
        return res;
    }

    private List<Integer> next(List<Integer> perm) {
        int n = perm.size();
        int i = n - 1;
        for (; i > 0 && perm.get(i) < perm.get(i - 1); i--);
        if (i == 0) return null;

        int m = perm.get(i - 1);
        int j = n - 1;
        for (; perm.get(j) < m; j--) {}
        List<Integer> nextPerm = new ArrayList<>();
        for (int k = 0; k + 1 < i; k++) {
            nextPerm.add(perm.get(k));
        }
        nextPerm.add(perm.get(j));
        for (int k = n - 1; k >= i; k--) {
            nextPerm.add(perm.get(k));
        }
        nextPerm.set(n - 1 + i - j, m);
        return nextPerm;
    }

    // Solution of Choice
    // Iteration
    // beats 79.05%(3 ms for 25 tests)
    public List<List<Integer> > permute3(int[] nums) {
        int n = nums.length;
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        List<List<Integer> > res = new ArrayList<>();
        do {
            List<Integer> perm = new ArrayList<>();
            for (int i : indices) {
                perm.add(nums[i]);
            }
            res.add(perm);
        } while (next(indices));
        return res;
    }

    private boolean next(int[] indices) {
        int n = indices.length;
        int i = n - 1;
        for (; i > 0 && indices[i] < indices[i - 1]; i--);
        if (i == 0) return false;

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

    // Backtracking
    // beats 13.69%(6 ms)
    public List<List<Integer> > permute4(int[] nums) {
        List<List<Integer> > res = new ArrayList<>();
        permute4(res, new ArrayList<>(), nums);
        return res;
    }

    private void permute4(List<List<Integer> > res, List<Integer> cur, int[] nums) {
        int n = nums.length;
        if (cur.size() == n) {
            res.add(new ArrayList<>(cur));
            return;
        }

        for (int i = 0; i < n; i++) {
            if (cur.contains(nums[i])) continue;

            cur.add(nums[i]);
            permute4(res, cur, nums);
            cur.remove(cur.size() - 1);
        }
    }

    // Backtracking(modified input)
    // http://www.programcreek.com/2013/02/leetcode-permutations-java/
    // beats 0.29%(103 ms)
    public List<List<Integer> > permute5(int[] nums) {
        List<List<Integer> > res = new ArrayList<>();
        permute5(nums, 0, res);
        return res;
    }

    private void permute5(int[] nums, int start, List<List<Integer> > res) {
        if (start >= nums.length) {
            res.add(Arrays.stream(nums).boxed().collect(Collectors.toList()));
            return;
        }

        for (int i = start; i < nums.length; i++) {
            swap(nums, start, i);
            permute5(nums, start + 1, res);
            swap(nums, start, i);
        }
    }

    // Solution of Choice
    // Backtracking
    // similar to permute4, but add visited flags
    // beats 100.00%(2 ms for 25 tests)
    public List<List<Integer> > permute6(int[] nums) {
        List<List<Integer> > res = new ArrayList<>();
        permute(nums, new ArrayList<>(), res, new boolean[nums.length]);
        return res;
    }

    private void permute(int[] nums, List<Integer> cur, 
                         List<List<Integer> > res, boolean[] visited) {
        int n = nums.length;
        if (cur.size() == n) {
            res.add(new ArrayList<>(cur));
            return;
        }

        for (int i = 0; i < n; i++) {
            if (visited[i]) continue;

            visited[i] = true;
            cur.add(nums[i]);
            permute(nums, cur, res, visited);
            visited[i] = false;
            cur.remove(cur.size() - 1);
        }
    }

    void testNext(Integer[] expected, Integer[] nums) {
        List<Integer> res = next(Arrays.asList(nums));
        if (expected == null) {
            assertNull(res);
        } else {
            Integer[] resArray = res.stream().toArray(Integer[]::new);
            assertArrayEquals(expected, resArray);
        }
    }

    @Test
    public void testNext() {
        testNext(new Integer[] {6, 3, 1, 2, 4, 5},
                 new Integer[] {6, 2, 5, 4, 3, 1});
        testNext(new Integer[] {6, 3, 2, 4, 5},
                 new Integer[] {6, 2, 5, 4, 3});
        testNext(null, new Integer[] {5, 4, 3});
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

    void test(int n, Integer[][] expected) {
        Permutation p = new Permutation();
        test(p::permute, "permute", n, expected);
        test(p::permute2, "permute2", n, expected);
        test(p::permute3, "permute3", n, expected);
        test(p::permute4, "permute4", n, expected);
        test(p::permute5, "permute5", n, expected);
        test(p::permute6, "permute6", n, expected);
    }

    void test(Integer[][] expected, int ... nums) {
        Permutation p = new Permutation();
        test(p::permute, "permute", expected, nums);
        test(p::permute2, "permute2", expected, nums);
        test(p::permute3, "permute3", expected, nums);
        test(p::permute4, "permute4", expected, nums);
        test(p::permute5, "permute5", expected, nums);
        test(p::permute6, "permute6", expected, nums);
    }

    @Test
    public void test1() {
        test(3, new Integer[][] {
            {1, 2, 3}, {1, 3, 2}, {2, 1, 3}, {2, 3, 1}, {3, 1, 2}, {3, 2, 1}
        });
        test(new Integer[][] {
            {4, 2, 3}, {4, 3, 2}, {2, 4, 3}, {2, 3, 4}, {3, 4, 2}, {3, 2, 4}
        },
             4, 2, 3);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
