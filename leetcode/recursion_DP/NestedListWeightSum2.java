import java.util.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.NestedInteger;
import common.NestedIntegerImpl;

// LC364: https://leetcode.com/problems/nested-list-weight-sum-ii
//
// Given a nested list of integers, return the sum of all integers in the list
// weighted by their depth. Each element is either an integer, or a list --
// whose elements may also be integers or other lists.
// Different from the previous question(LC339) where weight is increasing from
// root to leaf, now the weight is defined from bottom up.
public class NestedListWeightSum2 {
    // DFS + Recursion + BFS + Queue
    // beats 26.44%(6 ms for 27 tests)
    public int depthSumInverse(List<NestedInteger> nestedList) {
        int max = depth(nestedList);
        int sum = 0;
        Queue<NestedInteger> queue = new LinkedList<>(nestedList);
        for (int depth = 0; !queue.isEmpty(); depth++) {
            for (int count = queue.size(); count > 0; count--) {
                NestedInteger ni = queue.poll();
                if (ni.isInteger()) {
                    sum += (max - depth) * ni.getInteger();
                } else {
                    queue.addAll(ni.getList());
                }
            }
        }
        return sum;
    }

    private int depth(List<NestedInteger> nestedList) {
        int max = 0;
        for (NestedInteger ni : nestedList) {
            max = Math.max(max, ni.isInteger() ? 1 : depth(ni.getList()) + 1);
        }
        return max;
    }

    // DFS + Recursion
    // beats 51.59%(5 ms for 27 tests)
    public int depthSumInverse2(List<NestedInteger> nestedList) {
        int[] res = new int[2];
        return dfs(nestedList, 0, res) + res[0] * (res[1] + 1);
    }

    public int dfs(List<NestedInteger> nestedList, int depth, int[] res) {
        int sum = 0;
        for (NestedInteger ni : nestedList) {
            if (ni.isInteger()) {
                sum -= ni.getInteger() * depth;
                res[0] += ni.getInteger();
                res[1] = Math.max(res[1], depth);
            } else {
                sum += dfs(ni.getList(), depth + 1, res);
            }

        }
        return sum;
    }

    // DFS + Recursion
    // beats 87.80%(4 ms for 27 tests)
    public int depthSumInverse3(List<NestedInteger> nestedList) {
        return dfs3(nestedList, 0);
    }

    private int dfs3(List<NestedInteger> nestedList, int prevSum) {
        int sum = prevSum;
        List<NestedInteger> nextLevel = new ArrayList<>();
        for (NestedInteger ni : nestedList) {
            if (ni.isInteger()) {
                sum += ni.getInteger();
            } else {
                nextLevel.addAll(ni.getList());
            }
        }
        return sum + (nextLevel.isEmpty() ? 0 : dfs3(nextLevel, sum));
    }

    // BFS + Queue
    // beats 51.59%(5 ms for 27 tests)
    public int depthSumInverse4(List<NestedInteger> nestedList) {
        int sum = 0;
        int total = 0;
        int depth = 0;
        Queue<NestedInteger> queue = new LinkedList<>(nestedList);
        for (; !queue.isEmpty(); depth++) {
            for (int count = queue.size(); count > 0; count--) {
                NestedInteger ni = queue.poll();
                if (ni.isInteger()) {
                    sum -= depth * ni.getInteger();
                    total += ni.getInteger();
                } else {
                    queue.addAll(ni.getList());
                }
            }
        }
        return sum + total * depth;
    }

    // BFS + Queue
    // beats 87.80%(4 ms for 27 tests)
    public int depthSumInverse5(List<NestedInteger> nestedList) {
        int sum = 0;
        Queue<NestedInteger> queue = new LinkedList<>(nestedList);
        for (int prevSum = 0; !queue.isEmpty(); ) {
            for (int i = queue.size(); i > 0; i--) {
                NestedInteger ni = queue.poll();
                if (ni.isInteger()) {
                    prevSum += ni.getInteger();
                } else {
                    queue.addAll(ni.getList());
                }
            }
            sum += prevSum;
        }
        return sum;
    }

    // BFS + List
    // beats 51.59%(5 ms for 27 tests)
    public int depthSumInverse6(List<NestedInteger> nestedList) {
        int sum = 0;
        for (int total = 0; !nestedList.isEmpty(); sum += total) {
            List<NestedInteger> next = new LinkedList<>();
            for (NestedInteger ni : nestedList) {
                if (ni.isInteger()) {
                    total += ni.getInteger();
                } else {
                    next.addAll(ni.getList());
                }
            }
            nestedList = next;
        }
        return sum;
    }

    void test(Object[] list, int expected) {
        List<NestedInteger> nestedList = new ArrayList<>();
        for (Object ni : list) {
            if (ni instanceof Object[]) {
                nestedList.add(new NestedIntegerImpl((Object[])ni));
            } else if (ni instanceof Integer) {
                nestedList.add(new NestedIntegerImpl((Integer)ni));
            }
        }
        assertEquals(expected, depthSumInverse(nestedList));
        assertEquals(expected, depthSumInverse2(nestedList));
        assertEquals(expected, depthSumInverse3(nestedList));
        assertEquals(expected, depthSumInverse4(nestedList));
        assertEquals(expected, depthSumInverse5(nestedList));
        assertEquals(expected, depthSumInverse6(nestedList));
    }

    @Test
    public void test1() {
        test(new Object[] {new Integer[] {1, 1}, 2, new Integer[] {1, 1}}, 8);
        test(new Object[] {1, new Object[] {4, new Integer[] {6}}}, 17);
        test(new Object[] {1, new Object[] {4, new Integer[] {6}}, new Object[] {}}, 17);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("NestedListWeightSum2");
    }
}
