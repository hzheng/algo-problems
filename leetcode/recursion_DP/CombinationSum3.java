import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC216: https://leetcode.com/problems/combination-sum-iii/
//
// Find all possible combinations of k numbers that add up to a number n, given
// that only numbers from 1 to 9 can be used and each combination should be a
// unique set of numbers.
public class CombinationSum3 {
    // Backtracking + Recursion
    // beats 65.85%(1 ms)
    public List<List<Integer> > combinationSum3(int k, int n) {
        List<List<Integer> > res = new ArrayList<>();
        combinationSum3(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9}, n, k, 0,
                        new ArrayList<>(), res);
        return res;
    }

    private void combinationSum3(int[] candidates, int target, int k,
                                 int index, List<Integer> path,
                                 List<List<Integer> > res) {
        if (path.size() == k) {
            if (target == 0) {
                res.add(new ArrayList<>(path));
            }
            return;
        }

        for (int i = index; i < candidates.length; i++) {
            int candidate = candidates[i];
            if (candidate > target) break;

            path.add(candidate);
            combinationSum3(candidates, target - candidate, k, i + 1, path, res);
            path.remove(path.size() - 1);
        }
    }

    // Solution of Choice
    // Backtracking + Recursion
    // beats 14.43%(2 ms)
    public List<List<Integer> > combinationSum3_2(int k, int n) {
        List<List<Integer> > res = new ArrayList<>();
        combinationSum3(k, n, 1, new ArrayList<>(), res);
        return res;
    }

    private void combinationSum3(int k, int n, int start,
                                 List<Integer> path, List<List<Integer> > res) {
        if (path.size() == k) {
            if (n == 0) {
                res.add(new ArrayList<>(path));
            }
            return;
        }

        int max = Math.min(n, 9);
        for (int i = start; i <= max; i++) {
            path.add(i);
            combinationSum3(k, n - i, i + 1, path, res);
            path.remove(path.size() - 1);
        }
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<Integer, Integer, List<List<Integer>>> sum,
              int k, int n, int[][] expected) {
        List<List<Integer> > res = sum.apply(k, n);
        // System.out.println(res);
        Integer[][] resArray = res.stream().map(
            a -> a.toArray(new Integer[0])).toArray(Integer[][]::new);
        sort(resArray);
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

    void test(int k, int n, int[][] expected) {
        CombinationSum3 sum = new CombinationSum3();
        test(sum::combinationSum3, k, n, expected);
        test(sum::combinationSum3_2, k, n, expected);
    }

    @Test
    public void test1() {
        test(3, 7, new int[][] {{1, 2, 4}});
        test(3, 9, new int[][] {{1, 2, 6}, {1, 3, 5}, {2, 3, 4}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CombinationSum3");
    }
}
