import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC923: https://leetcode.com/problems/3sum-with-multiplicity/
//
// Given an integer array A, and an integer target, return the number of tuples
// i, j, k  such that i < j < k and A[i] + A[j] + A[k] == target.
// As the answer can be very large, return it modulo 10^9 + 7.
// Note:
// 3 <= A.length <= 3000
// 0 <= A[i] <= 100
// 0 <= target <= 300
public class ThreeSumMulti {
    // Sort + Hash Table + Set
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 70.79%(23 ms for 70 tests)
    public int threeSumMulti(int[] A, int target) {
        int MOD = 1000_000_000 + 7;
        Map<Integer, Integer> count = new HashMap<>();
        for (int a : A) {
            count.put(a, count.getOrDefault(a, 0) + 1);
        }
        Set<Integer> set = count.keySet();
        Integer[] nums = set.toArray(new Integer[0]);
        Arrays.sort(nums);
        int n = nums.length;
        long res = 0;
        for (int i = 0; i < n; i++) {
            int a = nums[i];
            long cnt = count.get(a);
            if (cnt >= 3 && a * 3 == target) {
                res += cnt * (cnt - 1) * (cnt - 2) / 6;
                res %= MOD;
            }
            if (cnt >= 2) {
                int c = target - 2 * a;
                if (c > a && set.contains(c)) {
                    res += cnt * (cnt - 1) / 2 * count.get(c);
                    res %= MOD;
                }
            }
            for (int j = i + 1; j < n; j++) {
                int b = nums[j];
                int c = target - a - b;
                if (c < b) break;

                if (!set.contains(c)) continue;

                long cnt2 = count.get(b);
                if (c == b) {
                    res += cnt * cnt2 * (cnt2 - 1) / 2;
                } else {
                    res += cnt * cnt2 * count.get(c);
                }
                res %= MOD;
            }
        }
        return (int)res;
    }

    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 100.00%(5 ms for 70 tests)
    public int threeSumMulti2(int[] A, int target) {
        int MOD = 1_000_000_007;
        int MAX = 101;
        long[] count = new long[MAX];
        for (int a : A) {
            count[a]++;
        }
        long res = 0;
        // x < y < z
        for (int x = 0; x < MAX; x++) {
            for (int y = x + 1; y < MAX; ++y) {
                int z = target - x - y;
                if (y < z && z < MAX) {
                    res += count[x] * count[y] * count[z];
                    res %= MOD;
                }
            }
        }
        // x == y != z
        for (int x = 0; x < MAX; x++) {
            int z = target - 2 * x;
            if (x < z && z < MAX) {
                res += count[x] * (count[x] - 1) / 2 * count[z];
                res %= MOD;
            }
        }
        // x != y == z
        for (int x = 0; x < MAX; x++) {
            if (target % 2 == x % 2) {
                int y = (target - x) / 2;
                if (x < y && y < MAX) {
                    res += count[x] * count[y] * (count[y] - 1) / 2;
                    res %= MOD;
                }
            }
        }
        // x == y == z
        if (target % 3 == 0) {
            int x = target / 3;
            if (x >= 0 && x < MAX) {
                res += count[x] * (count[x] - 1) * (count[x] - 2) / 6;
                res %= MOD;
            }
        }
        return (int)res;
    }

    // Sort + Three Pointer
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 49.02%(65 ms for 70 tests)
    public int threeSumMulti3(int[] A, int target) {
        int MOD = 1_000_000_007;
        long res = 0;
        Arrays.sort(A);
        for (int i = 0, n = A.length; i < n; ++i) {
            int tgt = target - A[i];
            for (int j = i + 1, k = n - 1; j < k;) {
                if (A[j] + A[k] < tgt) {
                    j++;
                } else if (A[j] + A[k] > tgt) {
                    k--;
                } else if (A[j] != A[k]) {
                    // Let's count "left": the number of A[j] == A[j+1] == A[j+2] == ...
                    int left = 1;
                    int right = 1;
                    for (; j + 1 < k && A[j] == A[j + 1]; j++, left++) {}
                    for (; k - 1 > j && A[k] == A[k - 1]; k--, right++) {}
                    res += left * right;
                    res %= MOD;
                    j++;
                    k--;
                } else {
                    res += (k - j + 1) * (k - j) / 2;
                    res %= MOD;
                    break;
                }
            }
        }
        return (int)res;
    }

    // Three Pointer
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 99.16%(6 ms for 70 tests)
    public int threeSumMulti4(int[] A, int target) {
        int MOD = 1_000_000_007;
        int MAX = 101;
        long[] count = new long[MAX];
        int uniq = 0;
        for (int a : A) {
            if (++count[a] == 1) {
                uniq++;
            }
        }
        int[] keys = new int[uniq];
        for (int i = 0, j = 0; i < MAX; i++) {
            if (count[i] > 0) {
                keys[j++] = i;
            }
        }
        long res = 0;
        for (int i = 0; i < uniq; i++) {
            int x = keys[i];
            int tgt = target - x;
            for (int j = i, k = uniq - 1; j <= k;) {
                int y = keys[j];
                int z = keys[k];
                if (y + z < tgt) {
                    j++;
                } else if (y + z > tgt) {
                    k--;
                } else {
                    if (i < j && j < k) {
                        res += count[x] * count[y] * count[z];
                    } else if (i == j && j < k) {
                        res += count[x] * (count[x] - 1) / 2 * count[z];
                    } else if (i < j && j == k) {
                        res += count[x] * count[y] * (count[y] - 1) / 2;
                    } else { // i == j == k
                        res += count[x] * (count[x] - 1) * (count[x] - 2) / 6;
                    }
                    res %= MOD;
                    j++;
                    k--;
                }
            }
        }
        return (int)res;
    }

    // Three Pointer
    // time complexity: O(MAX ^ 2), space complexity: O(MAX)
    // beats 99.16%(6 ms for 70 tests)
    public int threeSumMulti5(int[] A, int target) {
        int MAX = 101;
        long[] count = new long[MAX];
        for (int a : A) {
            count[a]++;
        }
        long res = 0;
        for (int i = 0; i < MAX; i++) {
            for (int j = i; j < MAX; j++) {
                int k = target - i - j;
                if (k >= MAX || k < 0) continue;

                if (i == j && j == k) {
                    res += count[i] * (count[i] - 1) * (count[i] - 2) / 6;
                } else if (i == j && j != k) {
                    res += count[i] * (count[i] - 1) / 2 * count[k];
                } else if (j < k) {
                    res += count[i] * count[j] * count[k];
                }
            }
        }
        return (int)(res % (1E9 + 7));
    }

    // Hash Table
    // time complexity: O(N ^ 2), space complexity: O(N)
    // beats 10.40%(713 ms for 70 tests)
    public int threeSumMulti6(int[] A, int target) {
        int MOD = 1000_000_000 + 7;
        Map<Integer, Integer> map = new HashMap<>();
        int res = 0;
        for (int i = 0; i < A.length; i++) {
            res = (res + map.getOrDefault(target - A[i], 0)) % MOD;
            for (int j = 0; j < i; j++) {
                int sum = A[i] + A[j];
                map.put(sum, map.getOrDefault(sum, 0) + 1);
            }
        }
        return res;
    }

    void test(int[] A, int target, int expected) {
        assertEquals(expected, threeSumMulti(A, target));
        assertEquals(expected, threeSumMulti2(A, target));
        assertEquals(expected, threeSumMulti3(A, target));
        assertEquals(expected, threeSumMulti4(A, target));
        assertEquals(expected, threeSumMulti5(A, target));
        assertEquals(expected, threeSumMulti6(A, target));
    }

    @Test
    public void test() {
        test(new int[] {1, 1, 2, 2, 3, 3, 4, 4, 5, 5}, 8, 20);
        test(new int[] {0, 2, 0}, 2, 1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
