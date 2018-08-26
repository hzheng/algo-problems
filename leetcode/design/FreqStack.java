import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC895: https://leetcode.com/problems/maximum-frequency-stack/
//
public class FreqStack {
    static interface IFreqStack {
        public void push(int x);

        public int pop();
    }

    // Deque + SortedSet + Hash Table
    // beats %(147 ms for 36 tests)
    class FreqStack1 implements IFreqStack {
        private Map<Integer, Deque<Integer> > map = new HashMap<>();
        private NavigableSet<Deque<Integer> > set = new TreeSet<>(
            new Comparator<Deque<Integer> >() {
            public int compare(Deque<Integer> a, Deque<Integer> b) {
                if (a.size() != b.size()) return a.size() - b.size();
                return a.peekLast() - b.peekLast();
            }
        });
        private int num = 0;

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
    class FreqStack2 implements IFreqStack {
        private Map<Integer, Stack<Integer> > group = new HashMap<>();
        private Map<Integer, Integer> freq = new HashMap<>();
        private int maxFreq;

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
    class FreqStack3 implements IFreqStack {
        private List<Stack<Integer>> bucket = new ArrayList<>();
        private Map<Integer, Integer> count = new HashMap<>();

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

    void test1(IFreqStack obj) {
        test(obj, new String[] { "FreqStack", "push", "push", "push", "push",
                                 "push", "push", "pop", "pop", "pop", "pop"},
             new int[][] { new int[]{}, new int[]{5}, new int[]{7}, new int[]{5},
                           new int[]{7}, new int[]{4}, new int[]{5},
                           new int[]{}, new int[]{}, new int[]{}, new int[]{}},
             new Integer[] { null, null, null, null, null, null, null,
                            5, 7, 5, 4 });
        test(obj, new String[] { "FreqStack", "push", "push", "push", "push", "push",
                                 "push", "push", "push", "push", "push",
                                 "push", "push", "push", "push", "push", "push",
                                 "push", "push", "push", "push", "push", "push",
                                 "push",
                                 "push", "push", "push", "pop", "push", "pop",
                                 "push", "pop", "push", "pop", "push", "pop",
                                 "push",
                                 "pop", "push", "pop", "push", "pop", "push",
                                 "pop", "push", "pop", "push", "pop", "push",
                                 "pop", "push",
                                 "pop", "push", "pop", "push", "pop", "push",
                                 "pop", "push", "pop", "push", "pop", "push",
                                 "pop", "push",
                                 "pop", "push", "pop", "push", "pop", "push",
                                 "pop", "push", "pop", "push", "pop", "pop",
                                 "pop", "pop",
                                 "pop", "pop", "pop", "pop", "pop", "pop",
                                 "pop", "pop", "pop", "pop", "pop", "pop",
                                 "pop", "pop", "pop",
                                 "pop", "pop", "pop", "pop", "pop", "pop",
                                 "pop" },
             new int[][] { new int[] {}, new int[] { 30 }, new int[] { 40 },
                           new int[] { 4 }, new int[] { 25 },
                           new int[] { 20 }, new int[] { 40 }, new int[] { 48 },
                           new int[] { 21 }, new int[] { 12 },
                           new int[] { 44 }, new int[] { 1 }, new int[] { 16 },
                           new int[] { 20 }, new int[] { 9 },
                           new int[] { 34 }, new int[] { 26 }, new int[] { 12 },
                           new int[] { 21 }, new int[] { 35 },
                           new int[] { 16 }, new int[] { 3 }, new int[] { 23 },
                           new int[] { 9 }, new int[] { 31 },
                           new int[] { 10 }, new int[] { 6 }, new int[] {},
                           new int[] { 45 }, new int[] {},
                           new int[] { 16 }, new int[] {}, new int[] { 14 },
                           new int[] {}, new int[] { 27 }, new int[] {},
                           new int[] { 35 }, new int[] {}, new int[] { 34 },
                           new int[] {}, new int[] { 40 }, new int[] {},
                           new int[] { 13 }, new int[] {}, new int[] { 21 },
                           new int[] {}, new int[] { 18 }, new int[] {},
                           new int[] { 26 }, new int[] {}, new int[] { 29 },
                           new int[] {}, new int[] { 32 }, new int[] {},
                           new int[] { 3 }, new int[] {}, new int[] { 18 },
                           new int[] {}, new int[] { 36 }, new int[] {},
                           new int[] { 1 }, new int[] {}, new int[] { 38 },
                           new int[] {}, new int[] { 34 }, new int[] {},
                           new int[] { 20 }, new int[] {}, new int[] { 22 },
                           new int[] {}, new int[] { 13 }, new int[] {},
                           new int[] { 37 }, new int[] {}, new int[] { 24 },
                           new int[] {}, new int[] {}, new int[] {},
                           new int[] {}, new int[] {}, new int[] {},
                           new int[] {}, new int[] {}, new int[] {},
                           new int[] {}, new int[] {}, new int[] {},
                           new int[] {}, new int[] {}, new int[] {},
                           new int[] {}, new int[] {}, new int[] {},
             new int[] {}, new int[] {}, new int[] {},
                           new int[] {}, new int[] {}, new int[] {}, new int[] {}, new int[] {} },
             new Integer[] { null, null, null, null, null, null, null, null, null, null, null, null, null, null,
                             null, null, null, null, null, null, null, null, null, null, null, null, null, 9, null, 16, null,
                             16, null, 21, null, 12, null, 35, null, 34, null, 40, null, 20, null, 21, null, 40, null, 26,
                             null, 29, null, 32, null, 3, null, 18, null, 36, null, 1, null, 38, null, 34, null, 20, null,
                             22, null, 13, null, 37, null, 24, 18, 13, 27, 14, 45, 6, 10, 31, 23, 3, 35, 26, 34, 9, 16, 1,
                             44, 12, 21, 48, 20, 25, 4, 40, 30 });
    }

    void test(IFreqStack obj, String[] methods, int[][] args, Integer[] expected) {
        try {
            Class<?> clazz = obj.getClass();
            for (int i = 1; i < methods.length; i++) {
                int[] arg = args[i];
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1() {
        test1(new FreqStack1());
        test1(new FreqStack2());
        test1(new FreqStack3());
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
