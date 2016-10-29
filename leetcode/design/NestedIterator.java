import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.NestedInteger;
import common.NestedIntegerImpl;

// LC341: https://leetcode.com/problems/flatten-nested-list-iterator/
//
// Given a nested list of integers, implement an iterator to flatten it.
// Each element is either an integer, or a list -- whose elements may also be
// integers or other lists.
public class NestedIterator {
    // stack
    // beats 3.13%(16 ms)
    static class NestedIterator1 implements Iterator<Integer> {
        List<NestedInteger> list;
        Stack<NestedInteger> valStack = new Stack<>();
        Stack<Integer> indexStack = new Stack<>();

        public NestedIterator1(List<NestedInteger> nestedList) {
            if (nestedList == null || nestedList.isEmpty()) return;

            list = nestedList;
            indexStack.push(0);
            prepareStack(list.get(0));
        }

        private void prepareStack(NestedInteger nestedInt) {
            while (true) {
                valStack.push(nestedInt);
                List<NestedInteger> nestedIntList = nestedInt.getList();
                if (nestedIntList == null) break;

                if (nestedIntList.isEmpty()) {
                    valStack.pop();
                    prepareNextNext();
                    break;
                }
                indexStack.push(0);
                nestedInt = nestedIntList.get(0);
            }
        }

        private void prepareNextNext() {
            while (!valStack.isEmpty()) {
                List<NestedInteger> parent = valStack.peek().getList();
                int index = indexStack.pop();
                if (++index < parent.size()) {
                    indexStack.push(index);
                    prepareStack(parent.get(index));
                    return;
                }
                valStack.pop();
            }

            int index = indexStack.pop(); // root index
            if (++index < list.size()) {
                indexStack.push(index);
                prepareStack(list.get(index));
            }
        }

        @Override
        public Integer next() {
            Integer nextInt = valStack.pop().getInteger();
            prepareNextNext();
            return nextInt;
        }

        @Override
        public boolean hasNext() {
            return !indexStack.isEmpty();
        }
    }

    // stack
    // https://discuss.leetcode.com/topic/42042/simple-java-solution-using-a-stack-with-explanation
    // the solution in the link has design problem: hasNext shouldn't change state.
    // beats 10.27%(12 ms)
    static class NestedIterator2 implements Iterator<Integer> {
        private Stack<NestedInteger> stack = new Stack<>();

        public NestedIterator2(List<NestedInteger> nestedList) {
            for (int i = nestedList.size() - 1; i >= 0; i--) {
                stack.push(nestedList.get(i));
            }
            prepareNext();
        }

        private void prepareNext() {
            while (!stack.isEmpty()) {
                NestedInteger cur = stack.peek();
                if (cur.isInteger()) return;

                stack.pop();
                for (int i = cur.getList().size() - 1; i >= 0; i--) {
                    stack.push(cur.getList().get(i));
                }
            }
        }

        @Override
        public Integer next() {
            Integer val = stack.pop().getInteger();
            prepareNext();
            return val;
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }
    }

    void testNestedInteger(int expected, int[] indices, Object ... nested) {
        NestedInteger nestedInt = new NestedIntegerImpl(nested);
        for (int i : indices) {
            nestedInt = nestedInt.getList().get(i);
        }
        assertEquals(expected, (int)nestedInt.getInteger());
    }

    @Test
    public void testNestedInteger() {
        testNestedInteger(4, new int[] {1, 1}, new Integer[] {1, 2},
                          new Integer[] {3, 4}, 5, new Integer[] {6, 7});
        testNestedInteger(7, new int[] {3, 1}, new Integer[] {1, 2},
                          new Integer[] {3, 4}, 5, new Integer[] {6, 7});
        testNestedInteger(5, new int[] {2, 0, 1}, new Integer[] {1, 2}, 3,
                          new Object[] {new Integer[] {4, 5}, 6});
    }

    void test1(int[] expected, Object[] ... nested) {
        List<NestedInteger> nestedList = new ArrayList<>();
        for (Object[] n : nested) {
            nestedList.add(new NestedIntegerImpl(n));
        }

        test1(expected, new NestedIterator1(nestedList));
        test1(expected, new NestedIterator2(nestedList));
    }

    void test1(int[] expected, Iterator<Integer> itr) {
        int i = 0;
        while (itr.hasNext()) {
            assertEquals(expected[i++], (int)itr.next());
        }
        assertEquals(expected.length, i);
    }

    @Test
    public void test1() {
        test1(new int[] {}, new Object[] {new Object[0]});
        test1(new int[] {1, 2, 3},
              new Object[] {new Integer[] {1, 2}},
              new Object[] {new Object[0]}, new Object[] {3});
        test1(new int[] {1, 2, 3, 4, 5},
              new Object[] {new Integer[] {1, 2}, 3},
              new Object[] {new Integer[] {4, 5}});
        test1(new int[] {1, 2, 3, 4, 5, 6, 7, 8},
              new Object[] {new Object[] {1, 2}, 3, 4},
              new Object[] {5, new Integer[] {6, 7}, 8});
        test1(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10},
              new Object[] {new Object[] {1, 2}, new Object[] {3, 4, 5}},
              new Object[] {6, 7, new Integer[] {8, 9, 10}});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("NestedIterator");
    }
}
