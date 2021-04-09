import java.util.*;
import java.util.stream.LongStream;

import java.lang.reflect.Constructor;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1622: https://leetcode.com/problems/fancy-sequence/
//
// Write an API that generates fancy sequences using the append, addAll, and multAll operations.
// Implement the Fancy class:
// Fancy() Initializes the object with an empty sequence.
// void append(val) Appends an integer val to the end of the sequence.
// void addAll(inc) Increments all existing values in the sequence by an integer inc.
// void multAll(m) Multiplies all existing values in the sequence by an integer m.
// int getIndex(idx) Gets the current value at index idx (0-indexed) of the sequence modulo 10^9 + 7.
// If the index is greater or equal than the length of the sequence, return -1.
//
// Constraints:
// 1 <= val, inc, m <= 100
// 0 <= idx <= 10^5
// At most 10^5 calls total will be made to append, addAll, multAll, and getIndex.
public class Fancy {
    private static final int MOD = 1_000_000_007;

    private static long modInverse(long x) {
        long res = 1; // a^(-1) = a^(m-2) % (mod m)
        for (int n = MOD - 2; n > 0; n >>= 1) {
            if ((n & 1) != 0) {
                res = (res * x) % MOD;
            }
            x = (x * x) % MOD;
        }
        return res;
    }

    // Math
    // time complexity: O(1), space complexity: O(N)
    // 74 ms(75.48%), 95.2 MB(30.32%) for 107 tests
    static class Fancy1 {
        private List<Long> nums = new ArrayList<>();
        private long multiplier = 1;
        private long adder = 0;
        // we can cache inverses' to improve speed
        private static final long[] INVERSES =
                LongStream.range(0, 101).map(Fancy::modInverse).toArray();

        public Fancy1() {
        }

        public void append(int val) {
            long v = (((val - adder + MOD) % MOD) * modInverse(multiplier)) % MOD;
            nums.add(v);
        }

        public void addAll(int inc) {
            adder = (adder + inc % MOD) % MOD;
        }

        public void multAll(int m) {
            multiplier = multiplier * m % MOD;
            adder = adder * m % MOD;
        }

        public int getIndex(int idx) {
            if (idx >= nums.size()) { return -1; }

            return (int)(((nums.get(idx) * multiplier) % MOD + adder) % MOD);
        }
    }

    // Math
    // time complexity: O(1), space complexity: O(N)
    // 85 ms(68.39%), 74.5 MB(99.35%) for 107 tests
    static class Fancy2 {
        private static final int SIZE = 100001;

        private final int[] seq = new int[SIZE];
        private final long[] adders = new long[SIZE];
        private final long[] multipliers = new long[SIZE];
        private int size = 0;

        public Fancy2() {
            multipliers[0] = 1;
        }

        public void append(int val) {
            seq[size] = val;
            adders[size + 1] = adders[size];
            multipliers[size + 1] = multipliers[size++];
        }

        public void addAll(int inc) {
            adders[size] += inc;
            adders[size] %= MOD;
        }

        public void multAll(int m) {
            multipliers[size] *= m;
            multipliers[size] %= MOD;
            adders[size] *= m;
            adders[size] %= MOD;
        }

        public int getIndex(int idx) {
            if (idx >= size) { return -1; }

            long mult = multipliers[size] * modInverse(multipliers[idx]) % MOD;
            long inc = (adders[size] - adders[idx] * mult % MOD + MOD) % MOD;
            return (int)((seq[idx] * mult % MOD + inc) % MOD);
        }
    }

    // TODO: Segment Tree

    void test1(String className) throws Exception {
        test(new String[] {className, "append", "addAll", "append", "multAll", "getIndex", "addAll",
                           "append", "multAll", "getIndex", "getIndex", "getIndex"},
             new Object[][] {{}, {2}, {3}, {7}, {2}, {0}, {3}, {10}, {2}, {0}, {1}, {2}},
             new Object[] {null, null, null, null, null, 10, null, null, null, 26, 34, 20});
        test(new String[] {className, "append", "getIndex", "multAll", "multAll", "getIndex",
                           "addAll", "append", "append", "getIndex", "append", "append", "addAll",
                           "getIndex", "multAll", "addAll", "append", "addAll", "getIndex",
                           "getIndex", "multAll", "multAll", "multAll", "append", "addAll",
                           "getIndex", "getIndex", "getIndex", "append", "getIndex", "addAll",
                           "multAll", "append", "multAll", "addAll", "getIndex", "append", "append",
                           "addAll", "getIndex", "multAll", "getIndex", "addAll", "getIndex",
                           "multAll", "addAll", "getIndex", "addAll", "append", "append", "append",
                           "multAll", "multAll", "append", "multAll", "addAll", "getIndex",
                           "addAll", "multAll", "multAll", "multAll", "append", "multAll", "append",
                           "multAll", "addAll", "append", "append", "getIndex", "getIndex",
                           "getIndex", "addAll", "multAll", "multAll", "append", "append",
                           "getIndex", "append", "append", "append", "getIndex", "getIndex"},
             new Object[][] {{}, {5}, {0}, {14}, {10}, {0}, {12}, {10}, {4}, {2}, {4}, {2}, {1},
                             {1}, {8}, {11}, {15}, {12}, {0}, {3}, {4}, {11}, {11}, {10}, {8}, {2},
                             {3}, {0}, {7}, {3}, {2}, {6}, {10}, {6}, {8}, {7}, {9}, {9}, {12}, {0},
                             {13}, {7}, {3}, {4}, {8}, {14}, {2}, {9}, {9}, {9}, {7}, {5}, {12},
                             {9}, {3}, {8}, {10}, {14}, {14}, {14}, {6}, {1}, {3}, {11}, {12}, {6},
                             {7}, {13}, {12}, {5}, {6}, {1}, {11}, {11}, {4}, {9}, {7}, {11}, {1},
                             {3}, {1}, {0}},
             new Object[] {null, null, 5, null, null, 700, null, null, null, 4, null, null, null,
                           11, null, null, null, null, 5727, 63, null, null, null, null, null,
                           30500, 30500, 2771876, null, 30500, null, null, null, null, null, 332,
                           null, null, null, 99787628, null, 4472, null, 10651007, null, null,
                           114201606, null, null, null, null, null, null, null, null, null, 401588,
                           null, null, null, null, null, null, null, null, null, null, null,
                           69515718, 633655703, 831230656, null, null, null, null, null, 715527902,
                           null, null, null, 728131107, 601045500});
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
            } else if (arg.length == 1) {
                res = clazz.getMethod(methods[i], int.class).invoke(obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test public void test1() {
        try {
            test1("Fancy1");
            test1("Fancy2");
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
