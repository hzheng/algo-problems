import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;
import static org.junit.Assert.*;

// LC703: https://leetcode.com/problems/kth-largest-element-in-a-stream/
//
// Design a class to find the kth largest element in a stream. Note that it is 
// the kth largest element in the sorted order, not the kth distinct element.
public class KthLargestInStream {
    // Heap
    // beats 26.50%(117 ms for 10 tests)
    class KthLargest {
        private int k;
        private PriorityQueue<Integer> pq = new PriorityQueue<>();

        public KthLargest(int k, int[] nums) {
            this.k = k;
            for (int num : nums) {
                add(num);
            }
        }

        public int add(int val) {
            pq.offer(val);
            while (pq.size() > k) {
                pq.poll();
            }
            return pq.peek();
        }
    }

    // Heap
    // beats 51.98%(89 ms for 10 tests)
    class KthLargest2 {
        private int k;
        private PriorityQueue<Integer> pq = new PriorityQueue<>();

        public KthLargest2(int k, int[] nums) {
            this.k = k;
            for (int num : nums) {
                add(num);
            }
        }

        public int add(int val) {
            if (pq.size() < k) {
                pq.offer(val);
            } else if (pq.peek() < val) {
                pq.poll();
                pq.offer(val);
            }
            return pq.peek();
        }
    }

    void test1(String className) throws Exception {
        Object outerObj = new Object() {}.getClass().getEnclosingClass().newInstance();
        test(new String[] { className, "add", "add", "add", "add", "add" },
                new Object[][] { new Object[] { outerObj, 3,
                                 new int[] { 4, 5, 8, 2 } },
                                 new Integer[] { 3 }, new Integer[] { 5 }, 
                                 new Integer[] { 10 }, new Integer[] { 9 }, 
                                 new Integer[] { 4 } },
                new Integer[] { null, 4, 5, 5, 8, 8 });
    }

    void test(String[] methods, Object[][] args, Integer[] expected) 
        throws Exception {
        final String name = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        final Object[] VOID = new Object[]{};
        Class<?> clazz = Class.forName(name + "$" + methods[0]);
        Constructor<?> ctor = clazz.getConstructors()[0];
        Object obj = ctor.newInstance(args[0]);
        for (int i = 1; i < methods.length; i++) {
            Object[] arg = args[i];
            Object res = null;
            if (arg.length == 0) {
                res = clazz.getMethod(methods[i]).invoke(obj);
            } else {
                res = clazz.getMethod(methods[i], int.class).invoke(obj, arg[0]);
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test
    public void test1() {
        try {
            test1("KthLargest");
            test1("KthLargest2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
