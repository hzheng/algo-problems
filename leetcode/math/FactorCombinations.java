import java.util.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Utils;

// LC254: https://leetcode.com/problems/factor-combinations/
//
// Write a function that takes an integer n and return all possible combinations
// of its factors.
public class FactorCombinations {
    // DFS/Backtracking + Recursion
    // beats 75.98%(2 ms for 20 tests)
    public List<List<Integer> > getFactors(int n) {
        List<List<Integer> > res = new ArrayList<>();
        getFactors((int)Math.sqrt(n), n, new ArrayList<>(), res);
        return res;
    }

    private void getFactors(int max, int n, List<Integer> cur, List<List<Integer> > res) {
        int len = cur.size();
        int min = len > 0 ? cur.get(len - 1) : 2;
        if (n < min) return;

        if (len > 0) {
            List<Integer> cloned = new ArrayList<>(cur);
            cloned.add(n);
            res.add(cloned);
        }
        max = Math.min(max, n);
        for (int i = min; i <= max; i++) {
            if (n % i == 0) {
                cur.add(i);
                getFactors(max, n / i, cur, res);
                cur.remove(len);
            }
        }
    }

    // DFS/Backtracking + Recursion
    // beats 75.98%(2 ms for 20 tests)
    public List<List<Integer> > getFactors2(int n) {
        List<List<Integer> > res = new ArrayList<>();
        getFactors2(n, 2, new ArrayList<>(), res);
        return res;
    }

    public void getFactors2(int n, int min, List<Integer> path, List<List<Integer> > res) {
        if (!path.isEmpty()) {
            List<Integer> cloned = new ArrayList<>(path);
            cloned.add(n);
            res.add(cloned);
        }
        int max = (int)Math.sqrt(n);
        for (int i = min; i <= max; i++) {
            if (n % i == 0) {
                path.add(i);
                getFactors2(n / i, i, path, res);
                path.remove(path.size() - 1);
            }
        }
    }

    // DFS/Backtracking + Recursion
    // beats 16.30%(232 ms for 20 tests)
    public List<List<Integer> > getFactors3(int n) {
        List<List<Integer> > res = new ArrayList<>();
        getFactors3(2, n, new ArrayList<>(), res);
        return res;
    }

    private void getFactors3(int start, int n, List<Integer> cur, List<List<Integer> > res) {
        int len = cur.size();
        if (n == 1 && len > 1) {
            res.add(new ArrayList<>(cur));
        }
        for (int i = start; i <= n; i++) {
            if (n % i == 0) {
                cur.add(i);
                getFactors3(i, n / i, cur, res);
                cur.remove(len);
            }
        }
    }

    // beats 75.98%(2 ms for 20 tests)
    // DFS/Backtracking + Recursion
    public List<List<Integer> > getFactors4(int n) {
        List<List<Integer> > res = new ArrayList<>();
        getFactors4(2, n, new ArrayList<>(), res);
        return res;
    }

    private void getFactors4(int min, int max, List<Integer> path, List<List<Integer>> res) {
        int m = (int)Math.sqrt(max);
        for (int i = min; i <= m; i++) {
            if (max % i == 0) {
                path.add(i);
                path.add(max / i);
                res.add(new ArrayList<>(path));
                path.remove(path.size() - 1);
                getFactors4(i, max / i, path, res);
                path.remove(path.size() - 1);
            }
        }
    }

    // Stack(correspond to recursion counterpart getFactors2)
    // beats 75.98%(2 ms for 20 tests)
    public List<List<Integer> > getFactors5(int n) {
        List<List<Integer> > res = new ArrayList<>();
        ArrayDeque<int[]> bounds = new ArrayDeque<>();
        ArrayDeque<List<Integer>> factors = new ArrayDeque<>();
        bounds.push(new int[] {2, n});
        factors.push(new ArrayList<>());
        while (!bounds.isEmpty()) {
            int[] top = bounds.pop();
            int min = top[0];
            int max = top[1];
            List<Integer> path = factors.pop();
            if (!path.isEmpty()) {
                List<Integer> cloned = new ArrayList<>(path);
                cloned.add(max);
                res.add(cloned);
            }
            int m = (int)Math.sqrt(max);
            for (int i = min; i <= m; i++) {
                if (max % i == 0) {
                    List<Integer> cloned = new ArrayList<>(path);
                    cloned.add(i);
                    factors.push(cloned);
                    bounds.push(new int[] {i, max / i});
                }
            }
        }
        return res;
    }

    // Stack(correspond to recursion counterpart getFactors4)
    // beats 69.11%(3 ms for 20 tests)
    public List<List<Integer> > getFactors6(int n) {
        List<List<Integer> > res = new ArrayList<>();
        ArrayDeque<int[]> bounds = new ArrayDeque<>();
        ArrayDeque<List<Integer>> factors = new ArrayDeque<>();
        bounds.push(new int[] {2, n});
        factors.push(new ArrayList<>());
        while (!bounds.isEmpty()) {
            int[] top = bounds.pop();
            int min = top[0];
            int max = top[1];
            List<Integer> path = factors.pop();
            int m = (int)Math.sqrt(max);
            for (int i = min; i <= m; i++) {
                if (max % i == 0) {
                    path.add(i);
                    factors.push(new ArrayList<>(path));
                    bounds.push(new int[] {i, max / i});
                    path.add(max / i);
                    res.add(new ArrayList<>(path));
                    path.remove(path.size() - 1);
                    path.remove(path.size() - 1);
                }
            }
        }
        return res;
    }

    void test(Function<Integer, List<List<Integer>>> getFactors,
              int n, int[][] expected) {
        List<List<Integer> > res = getFactors.apply(n);

        assertArrayEquals(expected, Utils.toSortedInts(res));
    }

    void test(int n, int[][] expected) {
        FactorCombinations f = new FactorCombinations();
        test(f::getFactors, n, Utils.sort(expected));
        test(f::getFactors2, n, Utils.sort(expected));
        test(f::getFactors3, n, Utils.sort(expected));
        test(f::getFactors4, n, Utils.sort(expected));
        test(f::getFactors5, n, Utils.sort(expected));
        test(f::getFactors6, n, Utils.sort(expected));
    }

    @Test
    public void test() {
        test(32, new int[][] {{2, 16}, {2, 2, 8},
                              {2, 2, 2, 4}, {2, 2, 2, 2, 2}, {2, 4, 4}, {4, 8}});
        test(23848713,
             new int[][] {{3, 7949571}, {3, 3, 2649857}, {3, 3, 7, 378551},
                          {3, 7, 1135653}, {3, 21, 378551}, {7, 3406959}, {7, 9, 378551},
                          {9, 2649857}, {21, 1135653}, {63, 378551}});
        test(177174,
             new int[][] {{2, 88587}, {2, 3, 29529}, {2, 3, 3, 9843}, {2, 3, 3, 3, 3281},
                          {2, 3, 3, 3, 17, 193}, {2, 3, 3, 17, 579}, {2, 3, 3, 51, 193}, {2, 3, 9, 3281},
                          {2, 3, 9, 17, 193}, {2, 3, 17, 1737}, {2, 3, 51, 579}, {2, 3, 153, 193},
                          {2, 9, 9843}, {2, 9, 17, 579}, {2, 9, 51, 193}, {2, 17, 5211}, {2, 17, 27, 193},
                          {2, 27, 3281}, {2, 51, 1737}, {2, 153, 579}, {2, 193, 459}, {3, 59058}, {3, 3, 19686},
                          {3, 3, 3, 6562}, {3, 3, 3, 17, 386}, {3, 3, 3, 34, 193}, {3, 3, 6, 3281},
                          {3, 3, 6, 17, 193}, {3, 3, 17, 1158}, {3, 3, 34, 579}, {3, 3, 51, 386},
                          {3, 3, 102, 193}, {3, 6, 9843}, {3, 6, 17, 579}, {3, 6, 51, 193}, {3, 9, 6562},
                          {3, 9, 17, 386}, {3, 9, 34, 193}, {3, 17, 3474}, {3, 17, 18, 193}, {3, 18, 3281},
                          {3, 34, 1737}, {3, 51, 1158}, {3, 102, 579}, {3, 153, 386}, {3, 193, 306}, {6, 29529},
                          {6, 9, 3281}, {6, 9, 17, 193}, {6, 17, 1737}, {6, 51, 579}, {6, 153, 193}, {9, 19686},
                          {9, 17, 1158}, {9, 34, 579}, {9, 51, 386}, {9, 102, 193}, {17, 10422}, {17, 18, 579},
                          {17, 27, 386}, {17, 54, 193}, {18, 9843}, {18, 51, 193}, {27, 6562}, {27, 34, 193},
                          {34, 5211}, {51, 3474}, {54, 3281}, {102, 1737}, {153, 1158}, {193, 918}, {306, 579},
                          {386, 459}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("FactorCombinations");
    }
}
