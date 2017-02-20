import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Utils;

// LC251: https://leetcode.com/problems/flatten-2d-vector/?tab=Description
//
// Implement an iterator to flatten a 2d vector.
public class Vector2D {
    // beats 44.54%(4 ms for 17 tests)
    static class Vector2D1 implements Iterator<Integer> {
        private Iterator<List<Integer> > itr;
        private List<Integer> list;
        private int index = -1;

        // time complexity: O(1)
        public Vector2D1(List<List<Integer> > vec2d) {
            itr = vec2d.iterator();
            prefetch();
        }

        private void prefetch() {
            while (itr.hasNext()) {
                list = itr.next();
                if (!list.isEmpty()) {
                    index = 0;
                    return;
                }
            }
            index = -1;
        }

        // time complexity: O(1)
        public Integer next() {
            int res = list.get(index++);
            if (index == list.size()) {
                prefetch();
            }
            return res;
        }

        // time complexity: O(1)
        public boolean hasNext() {
            return index >= 0;
        }
    }

    // beats 6.27%(7 ms for 17 tests)
    static class Vector2D2 implements Iterator<Integer> {
        private Iterator<List<Integer> > itr;
        private Iterator<Integer> row;

        public Vector2D2(List<List<Integer> > vec2d) {
            if (!vec2d.isEmpty()) {
                itr = vec2d.iterator();
                row = itr.next().iterator();
                prefetch();
            }
        }

        private void prefetch() {
            while (!row.hasNext() && itr.hasNext()) {
                row = itr.next().iterator();
            }
        }

        public Integer next() {
            int res = row.next();
            prefetch();
            return res;
        }

        public boolean hasNext() {
            return row != null && row.hasNext();
        }
    }

    // beats 44.54%(4 ms for 17 tests)
    static class Vector2D3 implements Iterator<Integer> {
        private List<List<Integer> > vec2d;
        private int x = -1;
        private int y = -1;

        // time complexity: O(1)
        public Vector2D3(List<List<Integer> > vec2d) {
            this.vec2d = vec2d;
            prefetch();
        }

        private void prefetch() {
            for (++x; x < vec2d.size(); x++) {
                if (!vec2d.get(x).isEmpty()) {
                    y = 0;
                    return;
                }
            }
            y = -1;
        }

        // time complexity: O(1)
        public Integer next() {
            List<Integer> list = vec2d.get(x);
            int res = list.get(y++);
            if (y == list.size()) {
                prefetch();
            }
            return res;
        }

        // time complexity: O(1)
        public boolean hasNext() {
            return y >= 0;
        }
    }

    void test1(Iterator<Integer> obj, int expected) {
        int i = 1;
        while (obj.hasNext()) {
            assertEquals(i++, (int)obj.next());
        }
        assertEquals(expected, i);
    }

    void test(Integer[][] input, int expected) {
        List<List<Integer> > vec2d = Utils.toList(input);
        test1(new Vector2D1(vec2d), expected);
        test1(new Vector2D2(vec2d), expected);
        test1(new Vector2D3(vec2d), expected);
    }

    @Test
    public void test() {
        test(new Integer[][] {}, 1);
        test(new Integer[][] {{}}, 1);
        test(new Integer[][] {{}, {1}}, 2);
        test(new Integer[][] {{1, 2}, {3}, {4, 5, 6}}, 7);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Vector2D");
    }
}
