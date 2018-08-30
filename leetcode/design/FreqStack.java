import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;
import static org.junit.Assert.*;

// LC895: https://leetcode.com/problems/maximum-frequency-stack/
//
public class FreqStack {
    // Deque + SortedSet + Hash Table
    // beats %(147 ms for 36 tests)
    class FreqStack1 {
        private Map<Integer, Deque<Integer> > map = new HashMap<>();
        private NavigableSet<Deque<Integer> > set = new TreeSet<>(
            new Comparator<Deque<Integer> >() {
            public int compare(Deque<Integer> a, Deque<Integer> b) {
                if (a.size() != b.size()) return a.size() - b.size();
                return a.peekLast() - b.peekLast();
            }
        });
        private int num = 0;

        public FreqStack1() {}

        // time complexity: O(log(N))
        public void push(int x) {
            Deque<Integer> stack = map.get(x);
            if (stack == null) {
                map.put(x, stack = new ArrayDeque<>());
                stack.offerLast(x);
            } else {
                set.remove(stack);
            }
            stack.offerLast(num++);
            set.add(stack);
        }

        // time complexity: O(log(N))
        public int pop() {
            Deque<Integer> stack = set.pollLast();
            stack.pollLast();
            if (stack.size() > 1) {
                set.add(stack);
            }
            return stack.peekFirst();
        }
    }

    // Stack + Hash Table
    // beats %(111 ms for 36 tests)
    class FreqStack2 {
        private Map<Integer, Stack<Integer> > group = new HashMap<>();
        private Map<Integer, Integer> freq = new HashMap<>();
        private int maxFreq;

        public FreqStack2() {}

        // time complexity: O(1)
        public void push(int x) {
            int f = freq.getOrDefault(x, 0) + 1;
            freq.put(x, f);
            maxFreq = Math.max(maxFreq, f);
            group.computeIfAbsent(f, s -> new Stack<>()).push(x);
            // or: 
            // Stack<Integer> stack = group.get(f);
            // if (stack == null) {
            //     group.put(f, stack = new Stack<>());
            // }
            // stack.push(x);
        }
    
        // time complexity: O(1)
        public int pop() {
            Stack<Integer> stack = group.get(maxFreq);
            int x = stack.pop();
            freq.put(x, freq.get(x) - 1);
            if (stack.isEmpty()) {
                maxFreq--;
            }
            return x;
        }
    }

    // Stack + Hash Table
    // beats %(139 ms for 36 tests)
    class FreqStack3 {
        private List<Stack<Integer>> bucket = new ArrayList<>();
        private Map<Integer, Integer> count = new HashMap<>();

        public FreqStack3() {}

        // time complexity: O(1)
        public void push(int x) {
            int freq = count.getOrDefault(x, 0);
            count.put(x, freq + 1);
            if (freq == bucket.size()) {
                bucket.add(new Stack<>());
            }
            bucket.get(freq).add(x);
        }
    
        // time complexity: O(1)
        public int pop() {
            Stack<Integer> stack = bucket.get(bucket.size() - 1);
            int x = stack.pop();
            if (stack.isEmpty()) {
                bucket.remove(bucket.size() - 1);
            }
            count.put(x, count.get(x) - 1);
            return x;
        }
    }

    void test1(String className) throws Exception {
        final FreqStack outerObj = new FreqStack();
        final Object[] VOID = new Object[]{};
        test(new String[] { className, "push", "push", "push", "push",
                                 "push", "push", "pop", "pop", "pop", "pop"},
             new Object[][] { new Object[]{ outerObj }, 
                           new Integer[]{5}, new Integer[]{7}, new Integer[]{5},
                           new Integer[]{7}, new Integer[]{4}, new Integer[]{5},
                           VOID, VOID, VOID, VOID},
             new Integer[] { null, null, null, null, null, null, null,
                            5, 7, 5, 4 });
        test(new String[] { className, "push", "push", "push", "push", "push",
                            "push", "push", "push", "push", "push", "push", 
                            "push", "push", "push", "push", "push", "push",
                            "push", "push", "push", "push", "push", "push", 
                            "push", "push", "push", "pop", "push", "pop", "push",
                            "pop", "push", "pop", "push", "pop", "push", "pop", 
                            "push", "pop", "push", "pop", "push", "pop", "push",
                            "pop", "push", "pop", "push", "pop", "push", "pop",
                            "push", "pop", "push", "pop", "push", "pop", "push",
                            "pop", "push", "pop", "push", "pop", "push", "pop",
                            "push", "pop", "push", "pop", "push", "pop", "push",
                            "pop", "push", "pop", "pop", "pop", "pop", "pop",
                            "pop", "pop", "pop", "pop", "pop", "pop", "pop",
                            "pop", "pop", "pop", "pop", "pop", "pop", "pop",
                            "pop", "pop", "pop", "pop", "pop", "pop", "pop" },
             new Object[][] { new Object[] {outerObj},
                           new Integer[] { 30 }, new Integer[] { 40 },
                           new Integer[] { 4 }, new Integer[] { 25 },
                           new Integer[] { 20 }, new Integer[] { 40 }, new Integer[] { 48 },
                           new Integer[] { 21 }, new Integer[] { 12 },
                           new Integer[] { 44 }, new Integer[] { 1 }, new Integer[] { 16 },
                           new Integer[] { 20 }, new Integer[] { 9 },
                           new Integer[] { 34 }, new Integer[] { 26 }, new Integer[] { 12 },
                           new Integer[] { 21 }, new Integer[] { 35 },
                           new Integer[] { 16 }, new Integer[] { 3 }, new Integer[] { 23 },
                           new Integer[] { 9 }, new Integer[] { 31 },
                           new Integer[] { 10 }, new Integer[] { 6 }, VOID,
                           new Integer[] { 45 }, VOID,
                           new Integer[] { 16 }, VOID, new Integer[] { 14 },
                           VOID, new Integer[] { 27 }, VOID,
                           new Integer[] { 35 }, VOID, new Integer[] { 34 },
                           VOID, new Integer[] { 40 }, VOID,
                           new Integer[] { 13 }, VOID, new Integer[] { 21 },
                           VOID, new Integer[] { 18 }, VOID,
                           new Integer[] { 26 }, VOID, new Integer[] { 29 },
                           VOID, new Integer[] { 32 }, VOID,
                           new Integer[] { 3 }, VOID, new Integer[] { 18 },
                           VOID, new Integer[] { 36 }, VOID,
                           new Integer[] { 1 }, VOID, new Integer[] { 38 },
                           VOID, new Integer[] { 34 }, VOID,
                           new Integer[] { 20 }, new Integer[] {}, new Integer[] { 22 },
                           VOID, new Integer[] { 13 }, VOID,
                           new Integer[] { 37 }, VOID, new Integer[] { 24 },
                           VOID, VOID, VOID, VOID, VOID, VOID, VOID, VOID, VOID,
                           VOID, VOID, VOID, VOID, VOID, VOID, VOID, VOID, VOID,
                           VOID, VOID, VOID, VOID, VOID, VOID, VOID, VOID },
             new Integer[] { null, null, null, null, null, null, null, null, 
                             null, null, null, null, null, null, null, null,
                             null, null, null, null, null, null, null, null,
                             null, null, null, 9, null, 16, null, 16, null, 21,
                             null, 12, null, 35, null, 34, null, 40, null, 20,
                             null, 21, null, 40, null, 26, null, 29, null, 32, 
                             null, 3, null, 18, null, 36, null, 1, null, 38,
                             null, 34, null, 20, null, 22, null, 13, null, 37,
                             null, 24, 18, 13, 27, 14, 45, 6, 10, 31, 23, 3, 35,
                             26, 34, 9, 16, 1, 44, 12, 21, 48, 20, 25, 4, 40, 30 });
    }

    void test(String[] methods, Object[][] args, Integer[] expected)
        throws Exception {
        String name = new Object() {}.getClass().getEnclosingClass().getSimpleName();
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
            test1("FreqStack1");
            test1("FreqStack2");
            test1("FreqStack3");
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
