import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// Given a collection of distinct numbers, return all possible permutations.
public class Permutation {
    // beats 13.69%
    public List<List<Integer> > permute(int[] nums) {
        int n = nums.length;
        if (n == 0) return Collections.emptyList();

        if (n == 1) {
            List<List<Integer> > res = new ArrayList<>();
            List<Integer> perm = new ArrayList<>();
            perm.add(nums[0]);
            res.add(perm);
            return res;
        }

        List<List<Integer> > res = new ArrayList<>();
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
        for (; perm.get(j) < m; j--);
        List<Integer> nextPerm = new ArrayList<>();
        for (int k = 0; k + 1 < i; k++) {
            nextPerm.add(perm.get(k));
        }
        nextPerm.add(perm.get(j));
        for (int k = n - 1; k >= i; k--) {
            if (k == j) {
                nextPerm.add(perm.get(k));
            } else {
                nextPerm.add(perm.get(k));
            }
        }
        nextPerm.set(n - 1 + i - j, m);
        return nextPerm;
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
              int[][] expected, int ... nums) {
        System.out.println("test " + name);
        List<List<Integer> > res = perm.apply(nums);
        Integer[][] resArray = res.stream().map(
            a -> a.toArray(new Integer[0])).toArray(Integer[][]::new);

        assertArrayEquals(expected, resArray);
    }

    void test(Function<int[], List<List<Integer>>> perm, String name,
              int n, int[][] expected) {
        int[] nums = new int[n];
        for (int i = 1; i <= n; i++) {
            nums[i - 1] = i;
        }
        test(perm, name, expected, nums);
    }

    void test(int n, int[][] expected) {
        Permutation p = new Permutation();
        test(p::permute, "permute", n, expected);
        test(p::permute2, "permute2", n, expected);
    }

    void test(int[][] expected, int ... nums) {
        Permutation p = new Permutation();
        test(p::permute, "permute", expected, nums);
        test(p::permute2, "permute2", expected, nums);
    }

    @Test
    public void test1() {
        test(3, new int[][] {
            {1, 2, 3}, {1, 3, 2}, {2, 1, 3}, {2, 3, 1}, {3, 1, 2}, {3, 2, 1}
        });
        test(new int[][] {
            {4, 2, 3}, {4, 3, 2}, {2, 4, 3}, {2, 3, 4}, {3, 4, 2}, {3, 2, 4}},
            4, 2, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Permutation");
    }
}
