import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC904: https://leetcode.com/problems/fruit-into-baskets/
//
// In a row of trees, the i-th tree produces fruit with type tree[i]. You start
// at any tree of your choice, then repeatedly perform the following steps:
// Add one piece of fruit from this tree to your baskets. If you cannot, stop.
// Move to the next tree to the right of the current tree. If there is no tree
// to the right, stop.
// Note that you do not have any choice after the initial choice of starting
// tree: you must perform step 1, then step 2, then back to step 1, then step 2,
// and so on until you stop.
// You have two baskets, and each basket can carry any quantity of fruit, but
// you want each basket to only carry one type of fruit each.
// What is the total amount of fruit you can collect with this procedure?
public class FruitIntoBaskets {
    // time complexity: O(N), space complexity: O(1)
    // beats %(15 ms for 90 tests)
    public int totalFruit(int[] tree) {
        int res = 0;
        int[] first = { -1, -1 };
        int[] second = { -1, -1 };
        for (int i = 0, start = -1; i < tree.length; i++) {
            if (first[0] < 0) {
                first[0] = tree[i];
                first[1] = i;
            } else if (tree[i] == first[0]) {
                first[1] = i;
            } else if (second[0] < 0) {
                second[0] = tree[i];
                second[1] = i;
            } else if (tree[i] == second[0]) {
                second[1] = i;
            } else if (first[1] < second[1]) {
                first[0] = tree[i];
                start = first[1];
                first[1] = i;
            } else {
                second[0] = tree[i];
                start = second[1];
                second[1] = i;
            }
            res = Math.max(res, i - start);
        }
        return res;
    }

    // time complexity: O(N), space complexity: O(1)
    // beats %(17 ms for 90 tests)
    public int totalFruit2(int[] tree) {
        int n = tree.length;
        int v1 = tree[0];
        int p1 = 0;
        for (; p1 < n && tree[p1] == v1; p1++) {}

        int p2 = p1--;
        if (p2 == n) return n;

        int res = p2 + 1;
        for (int v2 = tree[p2], cur = p2 + 1, start = -1; cur < n; cur++) {
            int v = tree[cur];
            if (v == v1 || v == v2) {
                if (v == v1) {
                    p1 = cur;
                } else {
                    p2 = cur;
                }
                res = Math.max(res, cur - start);
            } else if (p1 < p2) {
                start = p1;
                p1 = cur;
                v1 = v;
            } else {
                start = p2;
                p2 = cur;
                v2 = v;
            }
        }
        return res;
    }

    // Solution of Choice
    // time complexity: O(N), space complexity: O(1)
    // beats %(17 ms for 90 tests)
    public int totalFruit3(int[] tree) {
        int res = 0;
        int cur = 0;
        int streak = 0;
        int a = -1;
        int b = -1;
        for (int c : tree) {
            cur = (c == a || c == b) ? (cur + 1) : (streak + 1);
            res = Math.max(res, cur);
            if (b == c) {
                streak++;
            } else {
                streak = 1;
                a = b;
                b = c;
            }
        }
        return res;
    }

    // List + Set
    // time complexity: O(N), space complexity: O(N)
    // beats %(69 ms for 90 tests)
    public int totalFruit4(int[] tree) {
        List<Integer> blockStarts = new ArrayList<>();
        for (int i = 0; i <= tree.length; i++) {
            if (i == 0 || i == tree.length || tree[i - 1] != tree[i]) {
                blockStarts.add(i);
            }
        }
        outer : for (int i = 0, res = 0;; ) {
            Set<Integer> types = new HashSet<>();
            for (int j = i, len = 0; j < blockStarts.size() - 1; j++) {
                types.add(tree[blockStarts.get(j)]);
                len += blockStarts.get(j + 1) - blockStarts.get(j);
                if (types.size() >= 3) {
                    i = j - 1;
                    continue outer;
                }
                res = Math.max(res, len);
            }
            return res;
        }
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(1)
    // beats %(58 ms for 90 tests)
    public int totalFruit5(int[] tree) {
        int res = 0;
        Map<Integer, Integer> count = new HashMap<>();
        for (int i = 0, j = 0; j < tree.length; j++) {
            add(count, tree[j], 1);
            for (; count.size() >= 3; i++) {
                add(count, tree[i], -1);
                if (count.get(tree[i]) == 0) {
                    count.remove(tree[i]);
                }
            }
            res = Math.max(res, j - i + 1);
        }
        return res;
    }

    private void add(Map<Integer, Integer> count, int k, int v) {
        count.put(k, count.getOrDefault(k, 0) + v);
    }

    void test(int[] tree, int expected) {
        assertEquals(expected, totalFruit(tree));
        assertEquals(expected, totalFruit2(tree));
        assertEquals(expected, totalFruit3(tree));
        assertEquals(expected, totalFruit4(tree));
        assertEquals(expected, totalFruit5(tree));
    }

    @Test
    public void test() {
        test(new int[] { 1 }, 1);
        test(new int[] { 1, 2, 1 }, 3);
        test(new int[] { 0, 1, 2, 2 }, 3);
        test(new int[] { 1, 2, 3, 2, 2 }, 4);
        test(new int[] { 3, 3, 3, 1, 2, 1, 1, 2, 3, 3, 4 }, 5);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
