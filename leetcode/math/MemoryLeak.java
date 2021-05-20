import org.junit.Test;

import static org.junit.Assert.*;

// LC1860: https://leetcode.com/problems/incremental-memory-leak/
//
// You are given two integers memory1 and memory2 representing the available memory in bits on two
// memory sticks. There is currently a faulty program running that consumes an increasing amount of
// memory every second.
// At the ith second (starting from 1), i bits of memory are allocated to the stick with more
// available memory (or from the first memory stick if both have the same available memory). If
// neither stick has at least i bits of available memory, the program crashes.
// Return an array containing [crashTime, memory1crash, memory2crash], where crashTime is the time
// (in seconds) when the program crashed and memory1crash and memory2crash are the available bits
// of memory in the first and second sticks respectively.
//
// Constraints:
// 0 <= memory1, memory2 <= 2^31 - 1
public class MemoryLeak {
    // time complexity: O((M1+M2)^0.5), space complexity: O(1)
    // 4 ms(90.88%), 36.6 MB(85.54%) for 82 tests
    public int[] memLeak(int memory1, int memory2) {
        int time = 1;
        for (int i = 1; Math.max(memory1, memory2) >= i; i++, time++) {
            if (memory1 >= memory2) {
                memory1 -= i;
            } else {
                memory2 -= i;
            }
        }
        return new int[] {time, memory1, memory2};
    }

    // TODO: quadratic equation O(1)

    private void test(int memory1, int memory2, int[] expected) {
        assertArrayEquals(expected, memLeak(memory1, memory2));
    }

    @Test public void test1() {
        test(2, 2, new int[] {3, 1, 0});
        test(8, 11, new int[] {6, 0, 4});
        test(3432, 892332, new int[] {1338, 762, 549});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
