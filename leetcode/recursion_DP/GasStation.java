import org.junit.Test;
import static org.junit.Assert.*;

// LC134: https://leetcode.com/problems/gas-station/
//
// There are N gas stations along a circular route, where the amount of gas at
// station i is gas[i]. You have a car with an unlimited gas tank and it costs
// cost[i] of gas to travel from station i to its next station (i+1). You begin
// the journey with an empty tank at one of the gas stations. Return the
// starting gas station's index if you can travel around the circuit once,
// otherwise return -1.
// Note: The solution is guaranteed to be unique.
public class GasStation {
    // 2D-Dynamic Programming
    // Memory Limit Exceeded
    public int canCompleteCircuit(int[] gas, int[] cost) {
        int n = gas.length;

        // cost from station i to station j
        int[][] costs = new int[n][n + 1];
        int[][] maxCosts = new int[n][n + 1];
        for (int i = 0; i < n; i++) {
            // maxCosts[i][i] = Integer.MIN_VALUE; // may overflow
            for (int j = i + 1; j <= n; j++) {
                costs[i][j] =  costs[i][j - 1] + cost[j - 1] - gas[j - 1];
                maxCosts[i][j] = Math.max(maxCosts[i][j - 1], costs[i][j]);
            }
        }

        for (int i = 0; i < n; i++) {
            if (maxCosts[i][n] > 0) continue;

            if (costs[i][n] + costs[0][i] <= 0
                && (costs[i][n] + maxCosts[0][i]) <= 0) {
                return i;
            }
        }
        return -1;
    }

    // 1D-Dynamic Programming
    // Time Limit Exceeded
    public int canCompleteCircuit2(int[] gas, int[] cost) {
        int n = gas.length;
        int[] costs0 = new int[n + 1];
        int[] maxCosts0 = new int[n + 1];
        int[] costs = new int[n + 1];
        int[] maxCosts = new int[n + 1];
        for (int j = 1; j <= n; j++) {
            costs0[j] = costs0[j - 1] + cost[j - 1] - gas[j - 1];
            maxCosts0[j] = Math.max(maxCosts0[j - 1], costs0[j]);
        }
        for (int i = 0; i < n; i++) {
            costs[i] = 0;
            maxCosts[i] = 0;
            for (int j = i + 1; j <= n; j++) {
                costs[j] = costs[j - 1] + cost[j - 1] - gas[j - 1];
                maxCosts[j] = Math.max(maxCosts[j - 1], costs[j]);
            }
            if (maxCosts[n] <= 0 && (costs[n] + costs0[i] <= 0)
                && (costs[n] + maxCosts0[i]) <= 0) {
                return i;
            }
        }
        return -1;
    }

    // Greedy(Kadane's algorithm)
    // beats 1.46%(2 ms)
    public int canCompleteCircuit3(int[] gas, int[] cost) {
        int n = gas.length;
        int total = 0;
        for (int i = 0; i < n; i++) {
            total += gas[i] - cost[i];
        }
        if (total < 0) return -1;

        int maxGas = 0;
        int maxIndex = 0;
        int runningGas = 0;
        int start = -1;
        for (int i = 0; i < 2 * n; i++) {
            int index = i < n ? i : i - n;
            int gain = gas[index] - cost[index];
            runningGas += gain;
            if (runningGas > maxGas) {
                maxGas = runningGas;
                maxIndex = start + 1;
            } else if (runningGas < 0) {
                runningGas = 0;
                start = i;
            }
        }
        return maxIndex % n;
    }

    // Solution of Choice
    // Greedy(Kadane's algorithm)
    // Don't have to loop 2 * n times, and no need to find maximal sum.
    // Since the answer is unique, none of other stations could possibly pass
    // that unique station. Hence as long as running gas sum is negative,
    // none of passing stations is the answer, and the desired station must
    // be the lastest start point.
    //
    // beats 87.63%(0 ms)
    public int canCompleteCircuit4(int[] gas, int[] cost) {
        int total = 0;
        int runningGas = 0;
        int start = -1;
        for (int i = 0; i < gas.length; i++) {
            int gain = gas[i] - cost[i];
            total += gain;
            runningGas += gain;
            if (runningGas < 0) {
                runningGas = 0;
                start = i;
            }
        }
        return (total < 0) ? -1 : start + 1;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<int[], int[], Integer> complete, String name,
              int[] gas, int[] cost, int expected) {
        long t1 = System.nanoTime();
        assertEquals(expected, (int)complete.apply(gas, cost));
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
    }

    void test(int[] gas, int[] cost, int expected) {
        GasStation g = new GasStation();
        test(g::canCompleteCircuit, "canCompleteCircuit", gas, cost, expected);
        test(g::canCompleteCircuit2, "canCompleteCircuit2", gas, cost, expected);
        test(g::canCompleteCircuit3, "canCompleteCircuit3", gas, cost, expected);
        test(g::canCompleteCircuit4, "canCompleteCircuit4", gas, cost, expected);
    }

    @Test
    public void test1() {
        test(new int[] {2, 0, 1, 2, 3, 4, 0},
             new int[] {0, 1, 0, 0, 0, 0, 11}, 0);
        test(new int[] {10, 12, 9, 2}, new int[] {10, 10, 12, 1}, 3);
        test(new int[] {2, 4}, new int[] {3, 4}, -1);
        test(new int[] {5}, new int[] {4}, 0);
        test(new int[] {10}, new int[] {20}, -1);
        test(new int[] {10, 12}, new int[] {12, 10}, 1);
        test(new int[] {10, 20}, new int[] {20, 10}, 1);
        test(new int[] {4, 10, 7, 10, 5, 10, 20},
             new int[] {5, 12, 10, 12, 3, 12, 10}, 4);
        test(new int[] {4, 10, 7, 10, 5, 10, 10, 20},
             new int[] {5, 12, 10, 12, 3, 12, 12, 10}, 7);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("GasStation");
    }
}
