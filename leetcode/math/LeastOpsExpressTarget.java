import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC964: https://leetcode.com/problems/least-operators-to-express-number/
//
// Given a single positive integer x, we will write an expression of the form x (op1) x (op2) x
// (op3) x ... where each operator op1, op2, etc. is either addition, subtraction, multiplication,
// or division (+, -, *, or /).
// When writing such an expression, we adhere to the following conventions:
// The division operator (/) returns rational numbers.
// There are no parentheses placed anywhere.
// We use the usual order of operations
// It's not allowed to use the unary negation operator (-).
// We would like to write an expression with the least number of operators such that the expression
// equals the given target.  Return the least number of operators used.
//
// Note:
// 2 <= x <= 100
// 1 <= target <= 2 * 10^8
public class LeastOpsExpressTarget {
    // BFS + Queue + Set
    // 172 ms(5.17%), 47.2 MB(5.17%) for 159 tests
    public int leastOpsExpressTarget(int x, int target) {
        if (target == 1) { return 1; } // x/x

        int n = (int)(Math.ceil(Math.log(target) / Math.log(x)));
        long base = (long)Math.pow(x, n);
        Queue<Long> queue = new LinkedList<>();
        queue.offer(((long)(target) << Integer.SIZE));
        int res = Integer.MAX_VALUE;
        Set<String> visited = new HashSet<>();
        for (int pow = n; !queue.isEmpty(); base /= x, pow--) {
            for (int size = queue.size(); size > 0; size--) {
                long cur = queue.poll();
                int cost = (int)(cur & Integer.MAX_VALUE);
                int tgt = (int)(cur >> Integer.SIZE);
                if (cost >= res || !visited.add(cur + "|" + pow)) { continue; }

                if (tgt == 0 || base == 1) {
                    res = Math.min(res, cost + Math.abs(tgt) * 2);
                    continue;
                }
                for (int k = (int)(tgt / base), i = 2; i > 0; i--) {
                    long newTarget = tgt - k * base;
                    int newCost = cost + (k == 0 ? 0 : Math.abs(k) * pow + (cost == 0 ? -1 : 0));
                    queue.offer((newTarget << Integer.SIZE) | newCost);
                    k += (tgt >= 0) ? 1 : -1;
                }
            }
        }
        return res;
    }

    // Recursion + Dynamic Programming(Top-Down) + Hash Table
    // time complexity: O(log(T)), space complexity: O(log(T))
    // 2 ms(86.21%), 36.6 MB(44.83%) for 159 tests
    public int leastOpsExpressTarget2(int x, int target) {
        int n = (int)(Math.ceil(Math.log(target) / Math.log(x)));
        return dfs(x, 0, n, target, new HashMap<>()) - 1;
    }

    private int dfs(int x, int i, int n, int target, Map<Long, Integer> dp) {
        long code = (((long)target) << Integer.SIZE) | i;
        int cached = dp.getOrDefault(code, -1);
        if (cached >= 0) { return dp.get(code); }

        int res;
        if (target == 0) {
            res = 0;
        } else if (target == 1) {
            res = cost(i);
        } else if (i > n) {
            res = Integer.MAX_VALUE / 2;
        } else {
            int remainder = target % x;
            target /= x;
            res = Math.min(remainder * cost(i) + dfs(x, i + 1, n, target, dp),
                           (x - remainder) * cost(i) + dfs(x, i + 1, n, target + 1, dp));
        }
        dp.put(code, res);
        return res;
    }

    private int cost(int i) {
        return i > 0 ? i : 2;
    }

    // Recursion + Dynamic Programming(Top-Down) + Hash Table
    // time complexity: O(log(T)), space complexity: O(log(T))
    // 2 ms(86.21%), 36.6 MB(44.83%) for 159 tests
    public int leastOpsExpressTarget3(int x, int target) {
        return dfs(x, target, new HashMap<>());
    }

    private int dfs(int x, int target, Map<Integer, Integer> dp) {
        if (target == x) { return 0; }
        if (target < x) { return Math.min(target * 2 - 1, (x - target) * 2); }

        int cached = dp.getOrDefault(target, -1);
        if (cached >= 0) { return cached; }

        long pow = x;
        int n = 0;
        for (; pow < target; n++, pow *= x) {}
        /* or: int n = (int)(Math.ceil(Math.log(target) / Math.log(x))) - 1;
               long pow = (long)Math.pow(x, n + 1); */
        int res;
        if (pow == target) {
            res = n;
        } else {
            res = dfs(x, (int)(target - pow / x), dp) + n; // use plus
            if (pow - target < target) { // use minus
                res = Math.min(res, dfs(x, (int)(pow - target), dp) + n + 1);
            }
        }
        dp.put(target, res);
        return res;
    }

    // time complexity: O(log(T)), space complexity: O(1)
    // 0 ms(100.00%), 35.7 MB(93.10%) for 159 tests
    public int leastOpsExpressTarget4(int x, int target) {
        for (int k = 0, plus = 0, minus = 0; ; k++) {
            if (target <= 0) { return Math.min(plus, minus + k) - 1; }

            int remainder = target % x;
            target /= x;
            if (k > 0) {
                int tmp = Math.min(remainder * k + plus, (remainder + 1) * k + minus);
                minus = Math.min((x - remainder) * k + plus, (x - remainder - 1) * k + minus);
                plus = tmp;
            } else {
                plus = remainder * 2;
                minus = (x - remainder) * 2;
            }
        }
    }

    private void test(int x, int target, int expected) {
        assertEquals(expected, leastOpsExpressTarget(x, target));
        assertEquals(expected, leastOpsExpressTarget2(x, target));
        assertEquals(expected, leastOpsExpressTarget3(x, target));
        assertEquals(expected, leastOpsExpressTarget4(x, target));
    }

    @Test public void test() {
        test(3, 19, 5);
        test(5, 501, 8);
        test(100, 100000000, 3);
        test(3, 1, 1);
        test(3, 3, 0);
        test(3, 9, 1);
        test(100, 200000000, 7);
        test(2, 3125029, 83);
        test(2, 1125082, 84);
        test(2, 200000000, 113);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
