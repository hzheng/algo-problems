import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC444: https://leetcode.com/problems/sequence-reconstruction
//
// Check whether the original sequence org can be uniquely reconstructed from
// the sequences in seqs. The org sequence is a permutation of the integers from
// 1 to n, with 1 ≤ n ≤ 104. Reconstruction means building a shortest common
// supersequence of the sequences in seqs (i.e., a shortest sequence so that all
// sequences in seqs are subsequences of it). Determine whether there is only
// one sequence that can be reconstructed from seqs and it is the org sequence.
public class SeqReconstruction {
    // Heap + Hash Table
    // beats N/A(27 ms for 106 tests)
    public boolean sequenceReconstruction(int[] org, int[][] seqs) {
        final int size = 10001;
        int n = org.length;
        int[] order = new int[size];
        for (int i = 0; i < n; i++) {
            order[org[i]] = i + 1;
        }
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                for (int i = 0; i < a.length && i < b.length; i++) {
                    if (a[i] >= size || a[i] <= 0) return -1;
                    if (b[i] >= size || b[i] <= 0) return 1;
                    if (a[i] != b[i]) return order[a[i]] - order[b[i]];
                }
                return 0;
            }
        });
        for (int[] seq : seqs) {
            if (seq.length > 0) {
                pq.offer(seq);
            }
        }
        int cur = 0;
        while (!pq.isEmpty()) {
            int[] seq = pq.poll();
            for (int i = 0, linkFlag = 1; i < seq.length; i++) {
                if (seq[i] <= 0 || seq[i] >= size) return false;

                int pos = order[seq[i]];
                if (pos == 0 || i > 0 && pos <= order[seq[i - 1]]) return false;

                if (linkFlag > 0) {
                    if (pos <= cur) {
                        linkFlag = cur - pos;
                        continue;
                    } else if (cur == 0) {
                        linkFlag = 0;
                    } else {
                        if (i == 0) return false;
                        linkFlag = -1;
                    }
                }
                if (linkFlag < 0 || i > 0 && pos - order[seq[i - 1]] != 1) {
                    if (i + 1 < seq.length) {
                        pq.offer(Arrays.copyOfRange(seq, i, seq.length));
                    }
                    break;
                }
                if (pos == cur + 1) {
                    cur++;
                }
            }
        }
        return cur == n;
    }

    // Hash Table
    // beats N/A(103 ms for 106 tests)
    public boolean sequenceReconstruction2(int[] org, int[][] seqs) {
        Map<Integer, Integer> order = new HashMap<>();
        for (int i = 0; i < org.length; i++) {
            order.put(org[i], i);
        }
        Set<Long> consecutives = new HashSet<>();
        for (int[] seq : seqs) {
            if (seq.length > 0 && !order.containsKey(seq[0])) return false;

            for (int i = 1; i < seq.length; i++) {
                if (order.get(seq[i - 1]) > order.getOrDefault(seq[i], -1)) return false;

                consecutives.add((long)seq[i - 1] << 32 | seq[i]);
            }
        }
        for (int i = 1; i < org.length; i++) {
            if (!consecutives.contains((long)org[i - 1] << 32 | org[i])) return false;
        }
        return seqs.length > 0;
    }

    // Topological sorting
    // beats N/A(45 ms for 106 tests)
    @SuppressWarnings("unchecked")
    public boolean sequenceReconstruction3(int[] org, int[][] seqs) {
        int n = org.length;
        int[] indegree = new int[n];
        Arrays.fill(indegree, -1);
        for (int[] seq : seqs) {
            for (int k : seq) {
                if (k > n || k < 1) return false;

                indegree[k - 1] = 0;
            }
        }
        for (int i = 0; i < n; i++) {
            if (indegree[i] != 0) return false;
        }
        List<Integer>[] edges = new List[n];
        for (int i = 0; i < n; i++) {
            edges[i] = new ArrayList<>();
        }
        for (int[] seq : seqs) {
            for (int i = 1; i < seq.length; i++) {
                indegree[seq[i] - 1]++;
                edges[seq[i - 1] - 1].add(seq[i] - 1);
            }
        }
        int next = -1; // reduced Topological Sort(queue size is at most 1)
        for (int i = 0; i < n; ++i) {
            if (indegree[i] == 0) {
                if (next < 0) {
                    next = i;
                } else return false;
            }
        }
        for (int i = 0; ; i++) {
            if (next < 0) return i == n;
            if (next + 1 != org[i]) return false;

            List<Integer> edge = edges[next];
            next = -1;
            for (int k : edge) {
                if (--indegree[k] == 0) {
                    if (next >= 0) return false;
                    next = k;
                }
            }
        }
    }

    void test(int[] org, int[][] seqs, boolean expected) {
        assertEquals(expected, sequenceReconstruction(org, seqs));
        assertEquals(expected, sequenceReconstruction2(org, seqs));
        assertEquals(expected, sequenceReconstruction3(org, seqs));
    }

    @Test
    public void test() {
        test(new int[] {1}, new int[][] {}, false);
        test(new int[] {1, 4, 2, 3}, new int[][] {{1, 2}, {2, 3}}, false);
        test(new int[] {1, 4, 2, 3}, new int[][] {{1, 2}, {1, 3}, {2, 3}}, false);
        test(new int[] {1}, new int[][] {{1}, {1}, {1}}, true);
        test(new int[] {1, 2, 3}, new int[][] {{1, 2}, {2, 3}}, true);
        test(new int[] {1, 2, 3}, new int[][] {{1, 2}, {1, 3}}, false);
        test(new int[] {1, 2, 3}, new int[][] {{1, 2, 3}}, true);
        test(new int[] {1, 2, 3}, new int[][] {{1, 2}}, false);
        test(new int[] {1, 2, 3}, new int[][] {{1, 2}, {1, 3}, {2, 3}}, true);
        test(new int[] {1, 2}, new int[][] {{1, 2}, {1, 2}}, true);
        test(new int[] {1, 2}, new int[][] {{1, 2}, {2, 1}}, false);
        test(new int[] {5, 3, 2, 4, 1},
             new int[][] {{5, 3, 2, 4}, {4, 1}, {1}, {3}, {2, 4}, {1000000000}}, false);
        test(new int[] {4, 1, 5, 2, 6, 3}, new int[][] {{5, 2, 6, 3}, {4, 1, 5, 2}}, true);
        test(new int[] {1, 4, 2, 3}, new int[][] {{1, 2}, {1, 3}, {2, 3}, {4, 2}}, false);
        test(new int[] {1}, new int[][] {{1, -9}, {-9, -8}, {-8, -9}}, false);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SeqReconstruction");
    }
}
