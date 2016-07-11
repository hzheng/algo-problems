import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/ugly-number-ii/
//
// Ugly numbers are positive numbers whose prime factors only include 2, 3, 5.
// Note that 1 is typically treated as an ugly number.
// Write a program to find the n-th ugly number.
public class UglyNumber2 {
    // beats 0.09%(601 ms)
    public int nthUglyNumber(int n) {
        if (n <= 0) return 0;

        List<Integer> oldUglyNums = new LinkedList<>();
        oldUglyNums.add(1);
        int lastUgly = 1;
        for (int i = n - 1; i > 0; i--) {
            int smallest = oldUglyNums.get(0);
            int nextUgly = smallest * 2;
            if (nextUgly > lastUgly) {
                lastUgly = nextUgly;
                oldUglyNums.add(nextUgly);
                continue;
            }

            nextUgly = smallest * 3;
            int secondSmallest = oldUglyNums.get(1);
            int secondUgly = secondSmallest * 2;
            if (nextUgly > lastUgly) {
                if (secondUgly > lastUgly && secondUgly < nextUgly) {
                    nextUgly = secondUgly;
                }
                lastUgly = nextUgly;
                oldUglyNums.add(nextUgly);
                continue;
            }

            nextUgly = smallest * 5;
            if (nextUgly <= lastUgly) {
                oldUglyNums.remove(0);
                i++;
                continue;
            }

            for (int j = 1; j < oldUglyNums.size(); j++) {
                int oldUgly = oldUglyNums.get(j);
                if (oldUgly * 2 > lastUgly) {
                    nextUgly = Math.min(nextUgly, oldUgly * 2);
                    break;
                }
                if (oldUgly * 3 > lastUgly) {
                    nextUgly = Math.min(nextUgly, oldUgly * 3);
                }
            }
            lastUgly = nextUgly;
            oldUglyNums.add(nextUgly);
        }
        return lastUgly;
    }

    // beats 25.23%(46 ms)
    public int nthUglyNumber2(int n) {
        if (n <= 0) return 0;

        Queue<Integer> ugly2 = new LinkedList<>();
        Queue<Integer> ugly3 = new LinkedList<>();
        Queue<Integer> ugly5 = new LinkedList<>();

        int res = 1;
        for (int i = n - 1; i > 0; i--) {
            ugly2.offer(res * 2);
            ugly3.offer(res * 3);
            ugly5.offer(res * 5);

            int cand2 = ugly2.peek();
            int cand3 = ugly3.peek();
            int cand5 = ugly5.peek();
            res = Math.min(cand2, Math.min(cand3, cand5));

            if (cand2 == res) {
                ugly2.poll();
            }
            if (cand3 == res) {
                ugly3.poll();
            }
            if (cand5 == res) {
                ugly5.poll();
            }
        }
        return res;
    }

    void test(int... expected) {
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], nthUglyNumber(i + 1));
            assertEquals(expected[i], nthUglyNumber2(i + 1));
        }
    }

    @Test
    public void test1() {
        test(1, 2, 3, 4, 5, 6, 8, 9, 10, 12, 15, 16, 18, 20, 24, 25, 27, 30,
             32, 36, 40, 45, 48, 50, 54, 60, 64, 72, 75, 80, 81, 90, 96, 100,
             108, 120, 125, 128, 135, 144, 150, 160, 162, 180, 192, 200);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("UglyNumber2");
    }
}
