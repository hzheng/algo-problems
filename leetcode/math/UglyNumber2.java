import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC264: https://leetcode.com/problems/ugly-number-ii/
//
// Ugly numbers are positive numbers whose prime factors only include 2, 3, 5.
// Note that 1 is typically treated as an ugly number.
// Write a program to find the n-th ugly number.
public class UglyNumber2 {
    // time complexity: O(N ^ 2)(?), space complexity: O(1)
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

    // Queue
    // time complexity: O(N), space complexity: O(N)
    // beats 32.87%(48 ms for 596 tests)
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

    // Time Limit Exceeded
    public int nthUglyNumber3(int n) {
        if (n <= 0) return 0;

        int res = 1;
        for (int count = 1; count < n; ) {
            if (isUgly(++res)) {
                count++;
            }
        }
        return res;
    }

    private boolean isUgly(int num) {
        if (num <= 0) return false;

        int x = divideRepeately(num, 2);
        x = divideRepeately(x, 3);
        return divideRepeately(x, 5) == 1;
    }

    private int divideRepeately(int num, int divisor) {
        int x = num;
        while (x % divisor == 0) {
            x /= divisor;
        }
        return x;
    }

    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // beats 82.68%(9 ms for 596 tests)
    public int nthUglyNumber4(int n) {
        int[] ugly = new int[n];
        ugly[0] = 1;
        int i2 = 0;
        int i3 = 0;
        int i5 = 0;
        int next2 = 2;
        int next3 = 3;
        int next5 = 5;
        int res = 1;
        for (int i = 1; i < n; i++) {
            res = Math.min(next2, Math.min(next3, next5));
            ugly[i] = res;
            if (res == next2) {
                next2 = ugly[++i2] * 2;
            }
            if (res == next3) {
                next3 = ugly[++i3] * 3;
            }
            if (res == next5) {
                next5 = ugly[++i5] * 5;
            }
        }
        return res;
    }

    // Solution of Choice
    // Dynamic Programming
    // time complexity: O(N), space complexity: O(N)
    // beats 73.33%(10 ms for 596 tests)
    public int nthUglyNumber4_2(int n) {
        int[] dp = new int[n];
        dp[0] = 1;
        for (int i = 1, i2 = 0, i3 = 0, i5 = 0; i < n; i++) {
            dp[i] = Math.min(dp[i2] * 2, Math.min(dp[i3] * 3, dp[i5] * 5));
            if (dp[i] == dp[i2] * 2) {
                i2++;
            }
            if (dp[i] == dp[i3] * 3) {
                i3++;
            }
            if (dp[i] == dp[i5] * 5) {
                i5++;
            }
        }
        return dp[n - 1];
    }

    // SortedSet
    // https://stackoverflow.com/questions/4600048/nth-ugly-number
    // beats 7.86%(127 ms)
    public int nthUglyNumber5(int n) {
        SortedSet<Long> next = new TreeSet<>();  // may overflow if not use long
        next.add(1L);
        for (int i = 1;; i++) {
            long res = next.first();
            if (i >= n) return (int)res;

            next.remove(res);
            next.add(res * 2);
            next.add(res * 3);
            next.add(res * 5);
        }
    }

    // Solution of Choice
    // Heap
    // beats 39.24%(36 ms for 596 tests)
    public int nthUglyNumber6(int n) {
        Queue<Long> pq = new PriorityQueue<>();
        pq.offer(1L);
        for (int i = 1; i < n; i++) {
            long cur = pq.poll();
            pq.offer(cur * 2);
            if (cur % 2 > 0) {
                pq.offer(cur * 3);
                if (cur % 3 > 0) {
                    pq.offer(cur * 5);
                }
            }
        }
        return pq.peek().intValue();
    }

    // Heap
    // beats 24.57%(84 ms for 596 tests)
    public int nthUglyNumber6_2(int n) {
        Queue<Long> pq = new PriorityQueue<>();
        pq.offer(1L);
        int i = 0;
        for (long last = 0, cur = 0; ; last = cur) {
            cur = pq.poll();
            if (cur == last) continue;
            if (++i >= n) return (int)cur;

            pq.offer(cur * 2);
            pq.offer(cur * 3);
            pq.offer(cur * 5);
        }
    }

    // TODO: https://stackoverflow.com/questions/4600048/nth-ugly-number
    // time complexity: O(N ^ (2/3)), space complexity: O(N)

    void test(int ... expected) {
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], nthUglyNumber(i + 1));
            assertEquals(expected[i], nthUglyNumber2(i + 1));
            assertEquals(expected[i], nthUglyNumber3(i + 1));
            assertEquals(expected[i], nthUglyNumber4(i + 1));
            assertEquals(expected[i], nthUglyNumber4_2(i + 1));
            assertEquals(expected[i], nthUglyNumber5(i + 1));
            assertEquals(expected[i], nthUglyNumber6(i + 1));
            assertEquals(expected[i], nthUglyNumber6_2(i + 1));
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
