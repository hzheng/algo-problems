import java.util.*;
import java.io.*;

// https://codejam.withgoogle.com/2018/challenges/0000000000007765/dashboard/000000000003e0a8
// Round 1C 2018: Problem C - Ant Stack
//
// The ants try to arrange themselves into a vertical stack, with each ant in 
// the stack directly holding the next on its back. Ants are able to carry up to
// 6 times their own weight. Each ant also has a different body length. Each ant
// except for the top ant must be directly below exactly one ant, and each ant
// except for the bottom ant must be directly above exactly one ant. The lengths
// of the ants in the stack must be strictly decreasing from the bottom to the 
// top. For each ant, the sum of the weights of all the ants above it in the
// stack must be no more than 6 times the weight of that ant.
// What is the maximum number of these ants that can form such a stack?
// Input
// The first line of the input gives the number of test cases, T. T test cases 
// follow. Each begins with one line with an integer N: the number of ants in 
// the colony. Then, a second line follows containing N integers W1, W2, ..., 
// WN, where Wi is the weight in milligrams of the i-th ant. The ants are listed
// in strictly increasing order of length.
// Output
// For each test case, output one line containing Case #x: y, where x is the
// test case number and y is the maximum number of the given ants that can form
// a stack that obeys the rules above.
// Limits
// 7 ≤ T ≤ 100.
// Time limit: 15 seconds per test set.
// Memory limit: 1GB.
// Test set 1 (Visible)
// For exactly 6 cases, N = 100; for the other T - 6 cases, 2 ≤ N ≤ 50.
// 1 ≤ Wi ≤ 1000, for all i.
// Test set 2 (Hidden)
// For exactly 6 cases, N = 105; for the other T - 6 cases, 2 ≤ N ≤ 500.
// 1 ≤ Wi ≤ 10^9, for all i.
public class AntStack {
    // Dynamic Programming + Hash Table
    // time complexity: O(N ^ 2), space complexity: O(N)
    // Time Limit Exceeded for large set
    public static int solve(int[] W, int N) {
        Map<Integer, Integer> prev = new HashMap<>();
        prev.put(0, 0);
        prev.put(1, W[0]);
        for (int i = 1; i < N; i++) {
            Map<Integer, Integer> cur = new HashMap<>();
            for (int count : prev.keySet()) {
                int weight = prev.get(count);
                if (W[i] * 6 >= weight) {
                    int old = cur.getOrDefault(count + 1, Integer.MAX_VALUE);
                    cur.put(count + 1, Math.min(old, weight + W[i]));
                }
                int old = cur.getOrDefault(count, Integer.MAX_VALUE);
                cur.put(count, Math.min(old, weight));
            }
            prev = cur;
        }
        int res = 0;
        for (int count : prev.keySet()) {
            res = Math.max(res, count);
        }
        return res;
    }

    // Dynamic Programming
    // time complexity: O(N ^ MAX_W), space complexity: O(N ^ MAX_W)
    // Memeory Limit Exceeded for large set
    public static int solve2(int[] W, int N) {
        int maxW = 0;
        for (int w : W) {
            maxW += w; // not suitable for large set
        }
        int[][] dp = new int[N + 1][maxW + 1];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j <= maxW; j++) {
                if (W[i] > j) {
                    dp[i + 1][j] = dp[i][j];
                } else {
                    dp[i + 1][j] = Math.max(
                        dp[i][j], dp[i][Math.min(6 * W[i], j - W[i])] + 1);
                }
            }
        }
        return dp[N][maxW];
    }

    // Dynamic Programming
    // time complexity: O(N * K), space complexity: O(N * K)
    public static int solve3(int[] W, int N) {
        if (MAX_ANTS == 0) { // =140
            MAX_ANTS = maxAnts(1000_000_000);
        }
        final int k = MAX_ANTS;
        // min sum of the weights of j ants until i-th ant
        long[][] dp = new long[N + 1][k + 1];
        for (int i = 0; i <= N; i++) {
            for (int j = 1; j <= k; j++) {
                dp[i][j] = Long.MAX_VALUE;
            }
        }
        for (int i = 0; i < N; i++) {
            for (int j = 1; j <= k; j++) {
                if (dp[i][j - 1] > W[i] * 6L) {
                    dp[i + 1][j] = dp[i][j];
                } else {
                    dp[i + 1][j] = Math.min(dp[i][j], dp[i][j - 1] + W[i]);
                }
            }
        }
        for (int i = k;; i--) {
            if (dp[N][i] != Long.MAX_VALUE) return i;
        }
    }

    private static int MAX_ANTS;

    private static int maxAnts(long maxWeight) {
        int res = 1;
        for (long bottom = 1, total = 0; bottom <= maxWeight; ) {
            if (bottom * 6 >= total) {
                total += bottom;
                res++;
            } else {
                bottom = (long)Math.ceil(total / 6.0);
            }
        }
        return res;
    }

    // Heap
    // Memeory Limit Exceeded
    public static int solve0(int[] W, int N) {
        PriorityQueue<int[]> cur = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        cur.offer(new int[] {0, 0});
        cur.offer(new int[] {1, W[0]});
        int maxCount = 1;
        for (int i = 1; i < N; i++) {
            PriorityQueue<int[]> next =
                new PriorityQueue<>((a, b) -> a[1] - b[1]);
            while (!cur.isEmpty()) {
                int[] head = cur.peek();
                if (W[i] * 6 < head[1]) break;

                next.offer(cur.poll());
                next.offer(new int[] {head[0] + 1, head[1] + W[i]});
                maxCount = Math.max(maxCount, head[0] + 1);
            }
            if (next.isEmpty()) continue;

            while (!cur.isEmpty()) {
                int[] head = cur.poll();
                if (maxCount - head[0] < N - i - 1) {
                    next.offer(head);
                }
            }
            cur = next;
        }
        return maxCount;
    }

    public static void main(String[] args) {
        Scanner in =
            new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        PrintStream out = System.out;
        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            out.format("Case #%d: ", i);
            printResult(in, out);
        }
    }

    private static void printResult(Scanner in, PrintStream out) {
        int N = in.nextInt();
        int[] W = new int[N];
        for (int i = 0; i < N; i++) {
            W[i] = in.nextInt();
        }
        out.println(solve3(W, N));
    }
}
