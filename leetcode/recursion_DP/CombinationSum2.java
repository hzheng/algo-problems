import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC040: https://leetcode.com/problems/combination-sum-ii/
//
// Given a set of candidate numbers and a target T, find all unique combinations
// where the candidate numbers sums to T.
// The same repeated number may only be chosen once.
// All numbers (including target) will be positive integers.
public class CombinationSum2 {
    // Backtracking
    // beats 75.29%(5 ms)
    public List<List<Integer> > combinationSum2(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer> > res = new ArrayList<>();
        combinationSum2(candidates, target, new ArrayList<>(), 0, res);
        return res;
    }

    private void combinationSum2(int[] candidates, int target,
                                 List<Integer> path, int index,
                                 List<List<Integer> > res) {
        if (target == 0) {
            res.add(new ArrayList<>(path));
            return;
        }

        int last = 0;
        for (int i = index; i < candidates.length; i++) {
            int candidate = candidates[i];
            if (candidate > target) break;

            if (i > index && candidates[i - 1] == candidate) continue;

            path.add(candidate);
            combinationSum2(candidates, target - candidate, path, i + 1, res);
            path.remove(path.size() - 1);
        }
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<int[], Integer, List<List<Integer>>> sum,
              int[][] expected, int target, int ... candidates) {
        List<List<Integer> > res = sum.apply(candidates.clone(), target);
        System.out.println(res);
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

    void test(int[][] expected, int target, int ... candidates) {
        CombinationSum2 sum = new CombinationSum2();
        test(sum::combinationSum2, expected, target, candidates);
    }

    @Test
    public void test1() {
        test(new int[][] { {2, 2, 3}, {7} }, 7, 7, 2, 6, 2, 3);
        test(new int[][] { {2, 2, 6}, {3, 7} }, 10, 7, 2, 6, 2, 3);
        test(new int[][] { {3, 8}, {4, 7} }, 11, 8, 7, 4, 3);
        test(new int[][] { {3} }, 3, 8, 7, 4, 3);
        test(new int[][] { {1, 1, 2, 6}, {1, 2, 7} },
             10, 1, 2, 7, 6, 1, 5);
        test(new int[][] { {1, 1, 6}, {1, 2, 5}, {1, 7}, {2, 6} },
             8, 1, 2, 7, 6, 1, 5);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CombinationSum2");
    }
}
