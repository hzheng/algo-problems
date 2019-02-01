import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC967: https://leetcode.com/problems/numbers-with-same-consecutive-differences/
//
// Return all non-negative integers of length N such that the absolute 
// difference between every two consecutive digits is K.
// Note that every number in the answer must not have leading zeros except for 
// the number 0 itself.
// You may return the answer in any order.
// Note:
// 1 <= N <= 9
// 0 <= K <= 9
public class NumsSameConsecDiff {
    // Recursion + DFS + Set
    // beats 72.92%(8 ms for 92 tests)
    public int[] numsSameConsecDiff(int N, int K) {
        Set<String> res = new HashSet<>();
        dfs(K, N, 0, new char[N], res);
        int[] ans = new int[res.size()];
        int i = 0;
        for (String r : res) {
            ans[i++] = Integer.valueOf(r);
        }
        return ans;
    }

    private void dfs(int K, int N, int cur, char[] buf, Set<String> res) {
        if (cur == N) {
            res.add(String.valueOf(buf));
            return;
        }
        if (cur == 0) {
            for (char c = (N == 1) ? '0' : '1'; c <= '9'; c++) {
                buf[cur] = c;
                dfs(K, N, cur + 1, buf, res);
            }
        } else {
            int prev = buf[cur - 1] - '0';
            if (prev + K <= 9) {
                buf[cur] = (char)('0' + (prev + K));
                dfs(K, N, cur + 1, buf, res);
            }
            if (prev >= K) {
                buf[cur] = (char)('0' + (prev - K));
                dfs(K, N, cur + 1, buf, res);
            }
        }
    }

    // Recursion + DFS
    // beats 35.42%(45 ms for 92 tests)
    public int[] numsSameConsecDiff2(int N, int K) {
        List<Integer> res = new ArrayList<>();
        if (N == 1) {
            res.add(0);
        }
        dfs(N, K, 0, res);
        return res.stream().mapToInt(i -> i).toArray();
    }

    public void dfs(int N, int K, int cur, List<Integer> res) {
        if (N == 0) {
            res.add(cur);
            return;
        }
        for (int i = 0; i < 10; i++) {
            if (i == 0 && cur == 0) continue;

            if (cur == 0) {
                dfs(N - 1, K, i, res);
            } else if (Math.abs((cur % 10) - i) == K) {
                dfs(N - 1, K, cur * 10 + i, res);
            }
        }
    }

    // BFS + Queue
    // time complexity: O(2 ^ N), space complexity: O(2 ^ N)
    // beats 22.92%(47 ms for 92 tests)
    public int[] numsSameConsecDiff3(int N, int K) {
        Queue<Integer> q = new LinkedList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        if (N == 1) {
            q.offer(0);
        }
        for (int i = 1; i < N; i++) {
            for (int size = q.size(); size > 0; size--) {
                int num = q.poll();
                int digit1 = num % 10 - K;
                if (digit1 >= 0) {
                    q.offer(num * 10 + digit1);
                }
                int digit2 = num % 10 + K;
                if (digit2 < 10 && digit1 != digit2) {
                    q.offer(num * 10 + digit2);
                }
            }
        }
        return q.stream().mapToInt(i -> i).toArray();
    }

    // Set
    // time complexity: O(2 ^ N), space complexity: O(2 ^ N)
    // beats 72.92%(8 ms for 92 tests)
    public int[] numsSameConsecDiff4(int N, int K) {
        Set<Integer> cur = new HashSet<>();
        for (int i = 1; i <= 9; i++) {
            cur.add(i);
        }
        for (int i = 1; i < N; i++) {
            Set<Integer> next = new HashSet<>();
            for (int x : cur) {
                int d = x % 10;
                if (d - K >= 0) {
                    next.add(10 * x + (d - K));
                }
                if (d + K <= 9) {
                    next.add(10 * x + (d + K));
                }
            }
            cur = next;
        }
        if (N == 1) {
            cur.add(0);
        }
        int[] res = new int[cur.size()];
        int t = 0;
        for (int x : cur) {
            res[t++] = x;
        }
        return res;
    }

    // Set
    // time complexity: O(2 ^ N), space complexity: O(2 ^ N)
    // beats 34.38%(46 ms for 92 tests)
    public int[] numsSameConsecDiff4_2(int N, int K) {
        List<Integer> cur = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        for (int i = 2; i <= N; ++i) {
            List<Integer> next = new ArrayList<>();
            for (int x : cur) {
                int y = x % 10;
                if (x > 0 && y + K < 10) {
                    next.add(x * 10 + y + K);
                }
                if (x > 0 && K > 0 && y >= K) {
                    next.add(x * 10 + y - K);
                }
            }
            cur = next;
        }
        return cur.stream().mapToInt(j -> j).toArray();
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(int N, int K, int[] expected, Function<Integer, Integer, int[]> f) {
        int[] res = f.apply(N, K);
        Arrays.sort(res);
        assertArrayEquals(expected, res);
    }

    void test(int N, int K, int[] expected) {
        NumsSameConsecDiff n = new NumsSameConsecDiff();
        Arrays.sort(expected);
        test(N, K, expected, n::numsSameConsecDiff);
        test(N, K, expected, n::numsSameConsecDiff2);
        test(N, K, expected, n::numsSameConsecDiff3);
        test(N, K, expected, n::numsSameConsecDiff4);
        test(N, K, expected, n::numsSameConsecDiff4_2);
    }

    @Test
    public void test() {
        test(3, 7, new int[] {181, 292, 707, 818, 929});
        test(2, 1, new int[] {10, 12, 21, 23, 32, 34, 43, 45, 54, 56, 65, 67, 76, 78, 87, 89, 98});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
