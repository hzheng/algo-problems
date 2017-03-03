import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC281: https://leetcode.com/problems/zigzag-iterator/
//
// Given two 1d vectors, implement an iterator to return their elements alternately.
// Follow up: What if you are given k 1d vectors? How well can your code be
// extended to such cases?
public class ZigzagIterator {
    static interface IZigzagIterator {
        public int next();

        public boolean hasNext();
    }

    // beats 53.27%(5ms for 125 tests)
    static class ZigzagIterator1 implements IZigzagIterator {
        @SuppressWarnings("unchecked")
        private Iterator<Integer>[] itrs = new Iterator[2];
        private int nextIndex;
        private Integer nextVal;

        public ZigzagIterator1(List<Integer> v1, List<Integer> v2) {
            itrs[0] = v1.iterator();
            itrs[1] = v2.iterator();
            prefetch();
        }

        private void prefetch() {
            if (itrs[nextIndex].hasNext()) {
                nextVal = itrs[nextIndex].next();
                nextIndex = 1 - nextIndex;
            } else if (itrs[1 - nextIndex].hasNext()) {
                nextVal = itrs[1 - nextIndex].next();
                nextIndex = 1 - nextIndex;
            } else {
                nextVal = null;
            }
        }

        public int next() {
            int res = nextVal;
            prefetch();
            return res;
        }

        public boolean hasNext() {
            return nextVal != null;
        }
    }

    // beats 53.27%(5ms for 125 tests)
    static class ZigzagIterator2 implements IZigzagIterator {
        @SuppressWarnings("unchecked")
        private int total = 2; // could extend to any k
        private Iterator<Integer>[] itrs = new Iterator[total];
        private int[] indices = new int[total];
        private int nextIndex;
        private Integer nextVal;

        public ZigzagIterator2(List<Integer> v1, List<Integer> v2) {
            itrs[0] = v1.iterator();
            itrs[1] = v2.iterator();
            for (int i = 0; i < total; i++) {
                indices[i] = i;
            }
            prefetch();
        }

        private void prefetch() {
            for (; total > 0; total--) {
                int index = indices[nextIndex];
                if (itrs[index].hasNext()) {
                    nextVal = itrs[index].next();
                    nextIndex = (nextIndex + 1) % total;
                    return;
                }
                for (int i = nextIndex + 1; i < total; i++) {
                    indices[i - 1] = indices[i];
                }
                if (nextIndex == total - 1) {
                    nextIndex--;
                }
            }
            nextVal = null;
        }

        public int next() {
            int res = nextVal;
            prefetch();
            return res;
        }

        public boolean hasNext() {
            return nextVal != null;
        }
    }

    // beats 53.27%(5ms for 125 tests)
    static class ZigzagIterator3 implements IZigzagIterator {
        private Iterator<Integer> itr1;
        private Iterator<Integer> itr2;

        public ZigzagIterator3(List<Integer> v1, List<Integer> v2) {
            itr1 = v1.iterator();
            itr2 = v2.iterator();
        }

        public int next() {
            if (itr1.hasNext()) {
                Iterator<Integer> tmp = itr1;
                itr1 = itr2;
                itr2 = tmp;
            }
            return itr2.next();
        }

        public boolean hasNext() {
            return itr1.hasNext() || itr2.hasNext();
        }
    }

    // beats 12.67%(7ms for 125 tests)
    static class ZigzagIterator4 implements IZigzagIterator {
        private Queue<Iterator<Integer>> itrs = new LinkedList<>();

        public ZigzagIterator4(List<Integer> v1, List<Integer> v2) {
            if (!v1.isEmpty()) {
                itrs.offer(v1.iterator());
            }
            if (!v2.isEmpty()) {
                itrs.offer(v2.iterator());
            }
        }

        public int next() {
            Iterator<Integer> head = itrs.poll();
            int res = head.next();
            if (head.hasNext()) {
                itrs.offer(head);
            }
            return res;
        }

        public boolean hasNext() {
            return !itrs.isEmpty();
        }
    }

    void test1(IZigzagIterator obj, Integer[] expected) {
        Iterator<Integer> itr = Arrays.asList(expected).iterator();
        while (itr.hasNext()) {
            assertTrue(obj.hasNext());
            assertTrue(itr.next() == obj.next());
        }
        assertFalse(obj.hasNext());
    }

    void test(Integer[] v1, Integer[] v2, Integer[] expected) {
        test1(new ZigzagIterator1(Arrays.asList(v1), Arrays.asList(v2)), expected);
        test1(new ZigzagIterator2(Arrays.asList(v1), Arrays.asList(v2)), expected);
        test1(new ZigzagIterator3(Arrays.asList(v1), Arrays.asList(v2)), expected);
        test1(new ZigzagIterator4(Arrays.asList(v1), Arrays.asList(v2)), expected);
    }

    @Test
    public void test1() {
        test(new Integer[] {1, 2}, new Integer[] {}, new Integer[] {1, 2});
        test(new Integer[] {}, new Integer[] {1, 2}, new Integer[] {1, 2});
        test(new Integer[] {1, 2}, new Integer[] {3, 4, 5, 6}, new Integer[] {1, 3, 2, 4, 5, 6});
        test(new Integer[] {}, new Integer[] {}, new Integer[] {});
        test(new Integer[] {1}, new Integer[] {}, new Integer[] {1});
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ZigzagIterator");
    }
}
