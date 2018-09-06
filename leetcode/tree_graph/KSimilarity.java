import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC854: https://leetcode.com/problems/k-similar-strings/
//
// Strings A and B are K-similar if we can swap the positions of two letters in
// A exactly K times so that the resulting string equals B. Given two anagrams A
// and B, return the smallest K for which A and B are K-similar.
// Note:
// 1 <= A.length == B.length <= 20
// A and B contain only lowercase letters from the set {'a', 'b', 'c', 'd', 'e', 'f'}
public class KSimilarity {
    // BFS + Queue + Hash Table
    // beats 32.09%(71 ms for 54 tests)
    public int kSimilarity(String A, String B) {
        Map<String, Integer> dist = new HashMap<>();
        Queue<String> queue = new LinkedList<>();
        dist.put(A, 0);
        for (queue.offer(A); ;) {
            String cur = queue.poll();
            if (cur.equals(B)) return dist.get(cur);

            for (String nei : neighbors(cur, B)) {
                if (!dist.containsKey(nei)) {
                    dist.put(nei, dist.get(cur) + 1);
                    queue.offer(nei);
                }
            }
        }
    }

    private List<String> neighbors(String S, String B) {
        List<String> res = new ArrayList<>();
        int i = 0;
        char[] cs = S.toCharArray();
        for (; i < cs.length; i++) {
            if (S.charAt(i) != B.charAt(i)) break;
        }
        for (int j = i + 1; j < cs.length; j++) {
            if (S.charAt(j) != B.charAt(j) && S.charAt(j) == B.charAt(i)) {
                // at least we have progress
                swap(cs, i, j);
                res.add(new String(cs));
                swap(cs, i, j);
            }
        }
        return res;
    }

    private void swap(char[] cs, int i, int j) {
        char tmp = cs[i];
        cs[i] = cs[j];
        cs[j] = tmp;
    }

    // BFS + Queue + Set + Bit Manipulation
    // beats 47.29%(48 ms for 54 tests)
    public int kSimilarity2(String A, String B) {
        long a = toLong(A);
        long b = toLong(B);
        Queue<Long> queue = new LinkedList<>();
        queue.offer(a);
        Set<Long> visited = new HashSet<>();
        int n = A.length();
        for (int k = 0;; k++) {
            for (int x = queue.size(); x > 0; x--) {
                long cur = queue.poll();
                if (cur == b) return k;

                int i = 0;
                long cc = cur;
                long bb = b;
                for (; (cc & 7) == (bb & 7); i++) {
                    cc >>= 3;
                    bb >>= 3;
                }
                long bbb = bb;
                bb &= 7;
                for (int j = i + 1; j < n; j++) {
                    cc >>= 3;
                    bbb >>= 3;
                    if ((cc & 7) == (bbb & 7) || (cc & 7) != bb) continue;
                    // at least we have progress
                    long next = swap(cur, i, j);
                    if (visited.add(next)) {
                        queue.offer(next);
                    }
                }
            }
        }
    }

    private long toLong(String s) {
        long res = 0;
        int i = -3;
        for (char c : s.toCharArray()) {
            res |= (c - 'a' + 1L) << (i += 3);
        }
        return res;
    }

    private long swap(long a, int i, int j) {
        long mask1 = 7L << (i * 3);
        long mask2 = 7L << (j * 3);
        long res = a & (~mask1) & (~mask2);
        res |= ((a & mask1) << ((j - i) * 3));
        res |= ((a & mask2) >> ((j - i) * 3));
        return res;
    }

    private String toString(long a) {
        String res = "";
        for (; a > 0; a >>= 3) {
            res += (char)((a & 7) + 'a' - 1);
        }
        return res;
    }

    // DFS/Backtracking + Recursion + Hash Table
    // beats 17.02%(96 ms for 54 tests)
    public int kSimilarity3(String A, String B) {
        return minSwap(A.toCharArray(), B, new HashMap<>(), 0);
    }

    private int minSwap(char[] A, String B, Map<String, Integer> dp, int i) {
        String s = new String(A);
        if (s.equals(B)) return 0;
        if (dp.containsKey(s)) return dp.get(s);

        int res = A.length;
        for (; i < A.length && A[i] == B.charAt(i); i++) {}
        for (int j = i + 1; j < B.length(); j++) {
            if (A[j] != B.charAt(i)) continue;

            swap(A, i, j);
            res = Math.min(res, minSwap(A, B, dp, i + 1) + 1);
            swap(A, i, j); // backtrack
        }
        dp.put(s, res);
        return res;
    }

    void test(String A, String B, int expected) {
        assertEquals(expected, kSimilarity(A, B));
        assertEquals(expected, kSimilarity2(A, B));
        assertEquals(expected, kSimilarity3(A, B));
    }

    @Test
    public void test() {
        test("ab", "ba", 1);
        test("abc", "bca", 2);
        test("abac", "baca", 2);
        test("aabc", "abca", 2);
        test("abcceecda", "bcccdeaae", 4);
        test("abcaceedea", "baeccdeaae", 5);
        test("abccaccedea", "bcacccdeaae", 5);
        test("abccacceedea", "bcaceccdeaae", 5);
        test("abccaacceecdeea", "bcaacceeccdeaae", 9);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
