import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;
import static org.junit.Assert.*;

// LC900: https://leetcode.com/problems/rle-iterator/
//
// Write an iterator that iterates through a run-length encoded sequence.
// The iterator is initialized by RLEIterator(int[] A), where A is a run-length 
// encoding of some sequence. More specifically, for all even i, A[i] tells us 
// the number of times that the non-negative integer value A[i+1] is repeated.
// The iterator supports one function: next(int n), which exhausts the next n 
// elements (n >= 1) and returns the last element exhausted in this way. If
// there is no element left to exhaust, next returns -1 instead.
public class RLEIterator {
    // Queue
    // beats %(109 ms for 9 tests)
    class RLEIterator1 {
        private Queue<int[]> queue = new LinkedList<>();
        
        public RLEIterator1(int[] A) {
            for (int i = 0; i < A.length; i += 2) {
                queue.offer(new int[]{A[i], A[i + 1]});
            }
        }
        
        public int next(int n) {
            for (int i = n; !queue.isEmpty(); ) {
                int[] cur = queue.peek();
                if (i < cur[0]) {
                    cur[0] -= i;
                    return cur[1];
                }
                if (i == cur[0]) {
                    queue.poll();
                    return cur[1];
                }
                queue.poll();
                i -= cur[0];
            }
            return -1;
        }
    }

    // beats %(109 ms for 9 tests)
    class RLEIterator2 {
        private int[] A;
        private int index;
        private int used;
    
        public RLEIterator2(int[] A) {
            this.A = A;
        }
    
        public int next(int n) {
            for (int i = n; index < A.length; ) {
                if (used + i > A[index]) {
                    i -= A[index] - used;
                    used = 0;
                    index += 2;
                } else {
                    used += i;
                    return A[index + 1];
                }
            }
            return -1;
        }
    }
    
    void test1(String className) throws Exception {
        Object outerObj =
            new Object() {}.getClass().getEnclosingClass().newInstance();
        test(new String[] { className, "next", "next", "next", "next" },
             new Object[][] { new Object[] { outerObj,
                                             new int[] {3, 8, 0, 9, 2, 5}}, 
                              { 2 }, { 1 }, { 1 }, { 2 } },
             new Integer[] { null, 8, 8, 5, -1 });
    }

    void test(String[] methods, Object[][] args,
              Integer[] expected) throws Exception {
        final String name =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        final Object[] VOID = new Object[] {};
        Class<?> clazz = Class.forName(name + "$" + methods[0]);
        Constructor<?> ctor = clazz.getConstructors()[0];
        Object obj = ctor.newInstance(args[0]);
        for (int i = 1; i < methods.length; i++) {
            Object[] arg = args[i];
            Object res = null;
            if (arg.length == 0) {
                res = clazz.getMethod(methods[i]).invoke(obj);
            } else if (arg.length == 1) {
                res = clazz.getMethod(methods[i], int.class).invoke(obj, arg);
            } else if (arg.length == 2) {
                res = clazz.getMethod(methods[i], int.class, int.class).invoke(
                    obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test
    public void test1() {
        try {
            test1("RLEIterator1");
            test1("RLEIterator2");
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
