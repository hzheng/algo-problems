import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1286: https://leetcode.com/problems/iterator-for-combination/
//
// Design the CombinationIterator class:
// CombinationIterator(string characters, int combinationLength) Initializes the object with a
// string characters of sorted distinct lowercase English letters and a number combinationLength as
// arguments.
// next() Returns the next combination of length combinationLength in lexicographical order.
// hasNext() Returns true if and only if there exists a next combination.
//
// Constraints:
// 1 <= combinationLength <= characters.length <= 15
// All the characters of characters are unique.
// At most 10^4 calls will be made to next and hasNext.
// It's guaranteed that all calls of the function next are valid.
public class CombinationIterator {
    // 10 ms(99.62%), 40.5 MB(93.93%) for 16 tests
    static class CombinationIterator1 {
        private int len;
        private char[] s;
        private int[] next;

        public CombinationIterator1(String characters, int combinationLength) {
            s = characters.toCharArray();
            len = combinationLength;
            next = new int[len];
            for (int i = 0; i < len; i++) {
                next[i] = i;
            }
        }

        public String next() {
            char[] res = new char[len];
            for (int i = 0; i < len; i++) {
                res[i] = s[next[i]];
            }
            for (int i = len - 1, max = s.length - 1; ; i--, max--) {
                if (i < 0) {
                    next = null;
                    break;
                }
                if (next[i] >= max) { continue; }

                for (int j = i + 1, k = ++next[i] + 1; j < len; j++, k++) {
                    next[j] = k;
                }
                break;
            }
            return String.valueOf(res);
        }

        public boolean hasNext() {
            return next != null;
        }
    }

    // Bit Manipulation
    // 13 ms(83.49%), 40.5 MB(93.93%) for 16 tests
    static class CombinationIterator2 {
        private int len;
        private char[] s;
        private final int minMask;
        private int mask;

        public CombinationIterator2(String characters, int combinationLength) {
            s = characters.toCharArray();
            len = combinationLength;
            minMask = (1 << len) - 1;
            mask = minMask << (s.length - len);
        }

        public String next() {
            for (; Integer.bitCount(mask) != len; mask--) {}
            char[] res = new char[len];
            for (int i = s.length - 1, k = 0, j = 0, m = mask--; j < len; i--, k++) {
                if (((m >> i) & 1) != 0) {
                    res[j++] = s[k];
                }
            }
            return String.valueOf(res);
        }

        public boolean hasNext() {
            return mask >= minMask;
        }
    }

    // Recursion + Backtracking + Queue
    // 13 ms(83.49%), 40.5 MB(93.93%) for 16 tests
    static class CombinationIterator3 {
        private Queue<String> queue = new LinkedList<>();

        public CombinationIterator3(String s, int k) {
            generate(s.toCharArray(), k, 0, new StringBuilder());
        }

        private void generate(char[] s, int len, int start, StringBuilder sb) {
            if (len == 0) {
                queue.offer(sb.toString());
                return;
            }
            for (int i = start; i <= s.length - len; i++) {
                sb.append(s[i]);
                generate(s, len - 1, i + 1, sb);
                sb.deleteCharAt(sb.length() - 1);
            }
        }

        public String next() {
            return queue.poll();
        }

        public boolean hasNext() {
            return !queue.isEmpty();
        }
    }

    void test1(String className) throws Exception {
        test(new String[] {className, "next", "hasNext", "next", "hasNext", "next", "hasNext"},
             new Object[][] {{"abc", 2}, {}, {}, {}, {}, {}, {}},
             new Object[] {null, "ab", true, "ac", true, "bc", false});
        test(new String[] {className, "next", "hasNext", "next", "hasNext", "next", "hasNext",
                           "next", "hasNext"},
             new Object[][] {{"abcd", 3}, {}, {}, {}, {}, {}, {}, {}, {}},
             new Object[] {null, "abc", true, "abd", true, "acd", true, "bcd", false});
        test(new String[] {className, "hasNext", "hasNext", "next", "next", "hasNext", "hasNext",
                           "next", "hasNext", "hasNext", "hasNext"},
             new Object[][] {{"bvwz", 2}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}},
             new Object[] {null, true, true, "bv", "bw", true, true, "bz", true, true, true});
    }

    void test(String[] methods, Object[][] args, Object[] expected) throws Exception {
        final String name = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        Class<?> clazz = Class.forName(name + "$" + methods[0]);
        Constructor<?> ctor = clazz.getConstructors()[0];
        Object obj = ctor.newInstance(args[0]);
        for (int i = 1; i < methods.length; i++) {
            Object[] arg = args[i];
            Object res = null;
            if (arg.length == 0) {
                res = clazz.getMethod(methods[i]).invoke(obj);
            } else if (arg.length == 3) {
                res = clazz.getMethod(methods[i], int.class, int.class, int.class).invoke(obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test public void test1() {
        try {
            test1("CombinationIterator1");
            test1("CombinationIterator2");
            test1("CombinationIterator3");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
