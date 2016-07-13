import java.util.*;

import org.junit.*;
import static org.junit.Assert.*;
import org.junit.rules.*;

// https://leetcode.com/problems/implement-queue-using-stacks/
//
// Implement the following operations of a queue using stacks.
public class PeekingIterator {
    static interface IPeekingIterator extends Iterator<Integer> {
        // Returns the next element in the iteration without advancing the iterator.
        public Integer peek();
    }

    // beats 7.56%(128 ms)
    static class MyIterator implements IPeekingIterator {
        Iterator<Integer> itr;
        Integer first;

        public MyIterator(Iterator<Integer> iterator) {
            itr = iterator;
            updateFirst();
        }

        @Override
        public Integer peek() {
            return first;
        }

        @Override
        public Integer next() {
            if (first == null) throw new NoSuchElementException();

            Integer nextEl = first;
            updateFirst();
            return nextEl;
        }

        @Override
        public boolean hasNext() {
            return first != null;
        }

        private void updateFirst() {
            if (itr.hasNext()) {
                first = itr.next();
            } else {
                first = null;
            }
        }
    }

    // beats 35.46%(116 ms)
    static class MyIterator2 implements IPeekingIterator {
        private Iterator<Integer> itr;
        private boolean peeked;
        private Integer first;

        public MyIterator2(Iterator<Integer> iterator) {
            itr = iterator;
        }

        @Override
        public Integer peek() {
            if (!peeked) {
                first = itr.next();
                peeked = true;
            }
            return first;
        }

        @Override
        public Integer next() {
            if (!peeked) return itr.next();

            Integer nextEl = first;
            peeked = false;
            first = null;
            return nextEl;
        }

        @Override
        public boolean hasNext() {
            return peeked || itr.hasNext();
        }
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    void test1(IPeekingIterator obj) {
        assertEquals(1, (int)obj.peek());
        assertEquals(1, (int)obj.next());
        assertEquals(2, (int)obj.next());
        assertEquals(3, (int)obj.peek());
        assertEquals(true, obj.hasNext());
        assertEquals(3, (int)obj.next());
        assertEquals(false, obj.hasNext());
        try {
            obj.next();
            fail("should throw NoSuchElementException");
        } catch (NoSuchElementException e) {}
    }

    void test(Integer... nums) {
        test1(new MyIterator(Arrays.asList(nums).iterator()));
        test1(new MyIterator2(Arrays.asList(nums).iterator()));
    }

    @Test
    public void test1() {
        test(1, 2, 3);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("PeekingIterator");
    }
}
