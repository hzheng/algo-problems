import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC039: https://leetcode.com/problems/combination-sum/
//
// Given a set of candidate numbers and a target T, find all unique combinations
// where the candidate numbers sums to T.
// The same repeated number may be chosen unlimited number of times.
// All numbers (including target) will be positive integers.
public class CombinationSum {
    private int[] preprocess(int[] candidates) {
        if (candidates == null || candidates.length == 0) return null;

        SortedSet<Integer> candidateSet = new TreeSet<>();
        for (int i : candidates) {
            candidateSet.add(i);
        }
        candidates = candidateSet.stream().mapToInt(i->i).toArray();
        if (candidates[0] <= 0) return null;
        return candidates;
    }

    // beats 16.98%(16 ms)
    public List<List<Integer> > combinationSum(int[] candidates, int target) {
        // candidates = preprocess(candidates); // only beats 0.68% if add this
        // if (candidates == null) return null;

        List<List<Integer> > res = new ArrayList<>();
        combinationSum(candidates, target, 0, Collections.emptyList(), res);
        return res;
    }

    private void combinationSum(int[] candidates, int target,
                                int index, List<Integer> preselected,
                                List<List<Integer> > res) {
        if (target == 0) {
            res.add(preselected);
            return;
        }

        if (index >= candidates.length) return;

        int first = candidates[index];
        for (int times = target / first; times >= 0; times--) {
            List<Integer> combination = new ArrayList<>(preselected);
            for (int i = times; i > 0; i--) {
                combination.add(first);
            }
            combinationSum(candidates, target - times * first, index + 1,
                           combination, res);
        }
    }

    // beats 44.62%(11 ms)
    public List<List<Integer> > combinationSum2(int[] candidates, int target) {
        // candidates = preprocess(candidates); // beats 0.97% if add this
        // if (candidates == null) return null;
        List<List<Integer> > res = new ArrayList<>();
        combinationSum2(candidates, target, 0, Collections.emptyList(), res);
        return res;
    }

    private void combinationSum2(int[] candidates, int target,
                                 int index, List<Integer> preselected,
                                 List<List<Integer> > res) {
        int first = candidates[index];
        for (int times = target / first; times >= 0; times--) {
            List<Integer> combination = new ArrayList<>(preselected);
            for (int i = times; i > 0; i--) {
                combination.add(first);
            }
            int newTarget = target - times * first;
            if (newTarget == 0) {
                res.add(combination);
            } else if (index + 1 < candidates.length) {
                combinationSum2(candidates, newTarget, index + 1,
                                combination, res);
            }
        }
    }

    // beats 57.38%(7 ms)
    public List<List<Integer> > combinationSum3(int[] candidates, int target) {
        // candidates = preprocess(candidates); // beats 0.68% if add this
        // if (candidates == null) return null;
        Arrays.sort(candidates);
        return combinationSum3(candidates, target, candidates.length - 1);
    }

    private List<List<Integer> > combinationSum3(int[] candidates, int target,
                                                 int index) {
        int last = candidates[index];
        if (index == 0) {
            List<List<Integer> > res = new ArrayList<>();
            if (target % last == 0) {
                List<Integer> r = new ArrayList<>();
                for (int times = target / last; times > 0; times--) {
                    r.add(last);
                }
                res.add(r);
            }
            return res;
        }

        List<List<Integer> > res = combinationSum3(candidates, target, index - 1);
        for (int times = 1; times <= target / last; times++) {
            int tgt = target - times * last;
            for (List<Integer> r : combinationSum3(candidates, tgt, index - 1)) {
                for (int i = times; i > 0; i--) {
                    r.add(last);
                }
                res.add(r);
            }
        }
        return res;
    }

    // DFS + Recursion + Backtracking
    // beats 74.42%(5 ms)
    public List<List<Integer> > combinationSum4(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer> > res = new ArrayList<>();
        combinationSum4(candidates, target, new ArrayList<>(), 0, res);
        return res;
    }

    private void combinationSum4(int[] candidates, int target,
                                 List<Integer> path, int index,
                                 List<List<Integer> > res) {
        if (target == 0) {
            res.add(new ArrayList<>(path));
            return;
        }

        for (int i = index; i < candidates.length; i++) {
            int candidate = candidates[i];
            if (candidate > target) break;

            path.add(candidate);
            combinationSum4(candidates, target - candidate, path, i, res);
            path.remove(path.size() - 1);
        }
    }

    // Solution of Choice
    // DFS + Recursion + Backtracking
    // 2 ms(98.31%), 38.9 MB(95.85%) for 170 tests
    public List<List<Integer>> combinationSum5(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> res = new ArrayList<>();
        dfs(candidates, target, 0, new ArrayList<>(), res);
        return res;
    }

    private void dfs(int[] candidates, int target, int cur, List<Integer> path, List<List<Integer>> res) {
        if (target == 0) {
            res.add(new ArrayList<>(path));
            return;
        }
        if (cur >= candidates.length || candidates[cur] > target) { return; }

        // skip the current number
        dfs(candidates, target, cur + 1, path, res);
        // use the current number
        path.add(candidates[cur]);
        dfs(candidates, target - candidates[cur], cur, path, res);
        path.remove(path.size() - 1);
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        C apply(A a, B b);
    }

    void test(Function<int[], Integer, List<List<Integer>>> sum,
              int[][] expected, int target, int ... candidates) {
        List<List<Integer> > res = sum.apply(candidates.clone(), target);
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

    void test(int[][] expected, int target, int ... candidates) {
        CombinationSum sum = new CombinationSum();
        test(sum::combinationSum, expected, target, candidates);
        test(sum::combinationSum2, expected, target, candidates);
        test(sum::combinationSum3, expected, target, candidates);
        test(sum::combinationSum4, expected, target, candidates);
        test(sum::combinationSum5, expected, target, candidates);
    }

    @Test
    public void test1() {
        // test(new int[][] { {2, 2, 3}, {7} }, 7, 7, 2, 2, 6, 3); // no repeat
        test(new int[][] { {2, 2, 3}, {7} }, 7, 7, 2, 6, 3);
        // test(new int[][] { {2, 2, 2, 2, 2}, {2, 2, 3, 3}, {2, 2, 6}, {3, 7} },
        //      10, 7, 2, 2, 6, 3);
        test(new int[][] { {2, 2, 2, 2, 2}, {2, 2, 3, 3}, {2, 2, 6}, {3, 7} },
             10, 7, 2, 6, 3);
        test(new int[][] { {3, 4, 4}, {3, 8}, {4, 7} }, 11, 8, 7, 4, 3);
        test(new int[][] { {3} }, 3, 8, 7, 4, 3);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
