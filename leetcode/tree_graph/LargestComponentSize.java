import java.util.*;

import org.junit.Test;

import common.DisjointSet;

import static org.junit.Assert.*;

// LC952: https://leetcode.com/problems/largest-component-size-by-common-factor/
//
// Given a non-empty array of unique positive integers A, consider the following
// graph: 
// There are A.length nodes, labelled A[0] to A[A.length - 1];
// There is an edge between A[i] and A[j] if and only if A[i] and A[j] share a 
// common factor greater than 1.
// Return the size of the largest connected component in the graph.
// Note:
// 1 <= A.length <= 20000
// 1 <= A[i] <= 100000
public class LargestComponentSize {
    // Union Find
    // time complexity: O(N ^ 2), space complexity: O(N)
    // Time Limit Exceeded
    public int largestComponentSize(int[] A) {
        int n = A.length;
        DisjointSet ds = new DisjointSet(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (gcd(A[i], A[j]) > 1) {
                    ds.union(i, j);
                }
            }
        }
        int res = 0;
        for (int x : ds.getParent()) {
            if (x < 0 && -x > res) {
                res = -x;
            }
        }
        return res;
    }

    private int gcd(int a, int b) {
        if (a < b) return gcd(b, a);
        if (b == 0) return a;

        if ((a & 1) == 0) {
            if ((b & 1) == 0) return gcd(a >> 1, b >> 1) << 1;

            return gcd(a >> 1, b);
        }

        return ((b & 1) == 0) ? gcd(a, b >> 1) : gcd(a - b, b);
    }

    // Union Find
    // time complexity: O(?), space complexity: O(N)
    // Time Limit Exceeded
    public int largestComponentSize2(int[] A) {
        int n = A.length;
        int max = 1;
        DisjointSet ds = new DisjointSet(n);
        for (int a : A) {
            max = Math.max(max, a);
        }
        int[] primes = new int[max + 1];
        for (int i = 2; i <= max; i++) {
            primes[i] = i;
        }
        for (int factor = 2, m = (int)Math.sqrt(max); factor <= m; factor++) {
            if (primes[factor] == 0) continue;

            for (int j = 2; factor * j <= max; j++) {
                primes[factor * j] = 0;
            }
        }
        for (int i = 2; i <= max; i++) {
            if (primes[i] == 0) continue;

            int root = -1;
            for (int j = 0; j < n; j++) {
                if (A[j] % primes[i] == 0) {
                    if (root < 0) {
                        root = j;
                    } else {
                        ds.union(root, j);
                    }
                }
            }
        }
        int res = 0;
        for (int x : ds.getParent()) {
            if (x < 0 && -x > res) {
                res = -x;
            }
        }
        return res;
    }

    // Union Find + Hash Table + Set
    // time complexity: O(N * (MAX ^ 1/2)), space complexity: O(N)
    // beats %(292 ms for 100 tests)
    public int largestComponentSize3(int[] A) {
        int n = A.length;
        @SuppressWarnings("unchecked")
        List<Integer>[] factors = new List[n];
        for (int i = 0; i < n; i++) {
            List<Integer> factorList = factors[i] = new ArrayList<>();
            int a = A[i];
            for (int factor = 2; factor * factor <= a; factor++) {
                if (a % factor != 0) continue;

                for (; a % factor == 0; a /= factor) {}
                factorList.add(factor);
            }
            if (a > 1 || factorList.isEmpty()) {
                factorList.add(a);
            }
        }
        Set<Integer> primes = new HashSet<>();
        for (List<Integer> factorList : factors) {
            for (int f : factorList) {
                primes.add(f);
            }
        }
        Integer[] primeArray = primes.toArray(new Integer[primes.size()]);
        Map<Integer, Integer> primeMap = new HashMap<>();
        for (int i = 0; i < primeArray.length; i++) {
            primeMap.put(primeArray[i], i);
        }
        DisjointSet ds = new DisjointSet(primeArray.length);
        for (List<Integer> factorList : factors) {
            for (int f : factorList) {
                ds.union(primeMap.get(factorList.get(0)), primeMap.get(f));
            }
        }
        int[] count = new int[primeArray.length];
        for (List<Integer> factorList : factors) {
            count[ds.root(primeMap.get(factorList.get(0)))]++;
        }
        int res = 0;
        for (int x : count) {
            res = Math.max(res, x);
        }
        return res;
    }

    // Union Find
    // time complexity: O(N * (MAX ^ 1/2)), space complexity: O(N)
    // Time Limit Exceeded
    public int largestComponentSize4(int[] A) {
        int n = A.length;
        final int MAX = 100001;
        int res = 0;
        int[] id = new int[n + MAX];
        int[] size = new int[id.length];
        for (int i = 1; i < n + MAX; i++) {
            id[i] = i;
        }
        for (int i = 0; i < n; i++) {
            if (A[i] == 1) continue;

            int repr = i + MAX;
            size[repr] = 1;
            for (int factor = 1; factor * factor <= A[i]; factor++) {
                if (A[i] % factor != 0) continue;

                if (factor != 1) {
                    union(repr, factor, id, size);
                }
                union(repr, A[i] / factor, id, size);
            }
            res = Math.max(res, size[repr]);
        }
        return res;
    }

    // Union Find + Set
    // time complexity: O(N * (#prime < M)), space complexity: O(N)
    // beats %(147 ms for 100 tests)
    public int largestComponentSize5(int[] A) {
        int n = A.length;
        if (n == 1) return 1;

        final int MAX = 100001;
        boolean[] composite = new boolean[MAX];
        Set<Integer> primes = new HashSet<>();
        for (int i = 2; i < MAX; i++) {
            if (composite[i]) continue;

            primes.add(i);
            for (int j = 2; j * i < MAX; j++) {
                composite[j * i] = true;
            }
        }
        int[] size = new int[n];
        int[] id = new int[n];
        int[] primeIndices = new int[MAX];
        Arrays.fill(primeIndices, -1);
        for (int i = 0; i < n; i++) {
            id[i] = i;
            size[i] = 1;
        }
        int res = 0;
        for (int i = 0; i < n; i++) {
            int a = A[i];
            for (int p : primes) {
                if (primes.contains(a)) {
                    p = a;
                }
                if (a % p == 0) {
                    if (primeIndices[p] >= 0) {
                        res = Math.max(res, union(primeIndices[p], i, id, size));
                    }
                    for (primeIndices[p] = i; a % p == 0; a /= p) {}
                }
                if (a == 1) break;
            }
        }
        return res;
    }

    private int root(int x, int[] id) {
        return (id[x] == x) ? x : root(id[x], id);
    }

    private int union(int x, int y, int[] id, int[] size) {
        int rootX = root(x, id);
        int rootY = root(y, id);
        if (rootX == rootY) return 0;

        id[rootY] = rootX;
        return size[rootX] += size[rootY];
    }

    void test(int[] A, int expected) {
        assertEquals(expected, largestComponentSize(A));
        assertEquals(expected, largestComponentSize2(A));
        assertEquals(expected, largestComponentSize3(A));
        assertEquals(expected, largestComponentSize4(A));
        assertEquals(expected, largestComponentSize5(A));
    }

    @Test
    public void test() {
        test(new int[] {4}, 1);
        test(new int[] {4, 6, 15, 35}, 4);
        test(new int[] {20, 50, 9, 63}, 2);
        test(new int[] {2, 3, 6, 7, 4, 12, 21, 39}, 8);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
