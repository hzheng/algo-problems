import java.util.*;

import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

import common.NestedInteger;
import common.NestedIntegerImpl;

// LC339: https://leetcode.com/problems/nested-list-weight-sum
//
// Given a nested list of integers, return the sum of all integers in the list
// weighted by their depth. Each element is either an integer, or a list --
// whose elements may also be integers or other lists.
public class NestedListWeightSum {
    // DFS + Recursion
    // beats 21.94%(2 ms for 27 tests)
    public int depthSum(List<NestedInteger> nestedList) {
        return depthSum(nestedList, 1);
    }

    private int depthSum(List<NestedInteger> nestedList, int depth) {
        int sum = 0;
        for (NestedInteger ni : nestedList) {
            if (ni.isInteger()) {
                sum += ni.getInteger() * depth;
            } else {
                sum += depthSum(ni.getList(), depth + 1);
            }

        }
        return sum;
    }

    // DFS + Stack
    // beats 3.23%(4 ms for 27 tests)
    public int depthSum2(List<NestedInteger> nestedList) {
        int sum = 0;
        ArrayDeque<NestedInteger> stack = new ArrayDeque<>();
        ArrayDeque<Integer> levels = new ArrayDeque<>();
        int lastLevel = 1;
        for (Iterator<NestedInteger> itr = nestedList.iterator(); itr.hasNext() || !stack.isEmpty(); ) {
            if (itr.hasNext()) {
                stack.push(itr.next());
                levels.push(lastLevel);
                continue;
            }
            int depth = levels.pop();
            NestedInteger ni = stack.pop();
            if (ni.isInteger()) {
                sum += ni.getInteger() * depth;
            } else {
                itr = ni.getList().iterator();
                if (itr.hasNext()) {
                    stack.push(itr.next());
                    levels.push(lastLevel = depth + 1);
                }
            }
        }
        return sum;
    }

    static class LeveledNestedInteger {
        private int level;
        private NestedInteger ni;
        LeveledNestedInteger(int level, NestedInteger ni) {
            this.level = level;
            this.ni = ni;
        }
    }

    // DFS + Stack
    // beats 6.72%(3 ms for 27 tests)
    public int depthSum2_2(List<NestedInteger> nestedList) {
        int sum = 0;
        ArrayDeque<LeveledNestedInteger> stack = new ArrayDeque<>();
        int lastLevel = 1;
        for (Iterator<NestedInteger> itr = nestedList.iterator(); itr.hasNext() || !stack.isEmpty(); ) {
            if (itr.hasNext()) {
                stack.push(new LeveledNestedInteger(lastLevel, itr.next()));
                continue;
            }
            LeveledNestedInteger leveledNi = stack.pop();
            int depth = leveledNi.level;
            NestedInteger ni = leveledNi.ni;
            if (ni.isInteger()) {
                sum += ni.getInteger() * depth;
            } else {
                itr = ni.getList().iterator();
                if (itr.hasNext()) {
                    stack.push(new LeveledNestedInteger(lastLevel = depth + 1, itr.next()));
                }
            }
        }
        return sum;
    }

    // DFS + Stack
    // beats 21.94%(2 ms for 27 tests)
    public int depthSum3(List<NestedInteger> nestedList) {
        int sum = 0;
        ArrayDeque<Iterator<NestedInteger> > stack = new ArrayDeque<>();
        stack.push(nestedList.iterator());
        while (!stack.isEmpty()) {
            Iterator<NestedInteger> itr = stack.peek();
            if (!itr.hasNext()) {
                stack.pop();
                continue;
            }
            while (itr.hasNext()) {
                NestedInteger ni = itr.next();
                if (ni.isInteger()) {
                    sum += ni.getInteger() * stack.size();
                } else {
                    stack.push(ni.getList().iterator());
                    break;
                }
            }
        }
        return sum;
    }

    // BFS + Queue
    // beats 21.94%(2 ms for 27 tests)
    public int depthSum4(List<NestedInteger> nestedList) {
        Queue<NestedInteger> queue = new LinkedList<>(nestedList);
        int sum = 0;
        for (int depth = 1; !queue.isEmpty(); depth++) {
            for (int count = queue.size(); count > 0; count--) {
                NestedInteger ni = queue.poll();
                if (ni.isInteger()) {
                    sum += depth * ni.getInteger();
                } else {
                    queue.addAll(ni.getList());
                }
            }
        }
        return sum;
    }

    // BFS + List
    // beats 21.94%(2 ms for 27 tests)
    public int depthSum4_2(List<NestedInteger> nestedList) {
        int sum = 0;
        int depth = 1;
        for (List<NestedInteger> list = nestedList; !list.isEmpty(); depth++) {
            List<NestedInteger> next = new ArrayList<>();
            for (NestedInteger ni : list) {
                if (ni.isInteger()) {
                    sum += depth * ni.getInteger();
                } else {
                    next.addAll(ni.getList());
                }
            }
            list = next;
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
        assertEquals(expected, depthSum(nestedList));
        assertEquals(expected, depthSum2(nestedList));
        assertEquals(expected, depthSum2_2(nestedList));
        assertEquals(expected, depthSum3(nestedList));
        assertEquals(expected, depthSum4(nestedList));
        assertEquals(expected, depthSum4_2(nestedList));
    }

    @Test
    public void test1() {
        test(new Object[] {new Integer[] {1, 1}, 2, new Integer[] {1, 1}}, 10);
        test(new Object[] {1, new Object[] {4, new Integer[] {6}}}, 27);
        test(new Object[] {1, new Object[] {4, new Integer[] {6}}, new Object[] {}}, 27);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("NestedListWeightSum");
    }
}
