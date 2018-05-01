import java.awt.JobAttributes;
import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC826: https://leetcode.com/problems/most-profit-assigning-work/
//
// We have jobs: difficulty[i] is the difficulty of the ith job, and profit[i]
// is the profit of the ith job. Now we have some workers. worker[i] is the
// ability of the ith worker, which means that this worker can only complete a
// job with difficulty at most worker[i]. Every worker can be assigned at most
// one job, but one job can be completed multiple times.
// What is the most profit we can make?
// Notes:
// 1 <= difficulty.length = profit.length <= 10000
// 1 <= worker.length <= 10000
// difficulty[i], profit[i], worker[i]  are in range [1, 10^5]
public class MaxProfitAssignment {
    // NavigableSet
    // time complexity: O((N + W)* log(N)), space complexity: O(N)
    // beats %(70 ms for 57 tests)
    public int maxProfitAssignment(int[] difficulty, int[] profit,
                                   int[] worker) {
        NavigableSet<Job> jobs = new TreeSet<>();
        for (int i = difficulty.length - 1; i >= 0; i--) {
            jobs.add(new Job(difficulty[i], profit[i]));
        }
        int max = 0;
        for (Job job : jobs) {
            job.profit = max = Math.max(max, job.profit);
        }
        int res = 0;
        for (int w : worker) {
            Job job = jobs.lower(new Job(w + 1, 0));
            // or: Job job = jobs.floor(new Job(w, Integer.MAX_VALUE));
            if (job != null) {
                res += job.profit;
            }
        }
        return res;
    }

    private static class Job implements Comparable<Job> {
        int difficulty;
        int profit;
        Job(int difficulty, int profit) {
            this.difficulty = difficulty;
            this.profit = profit;
        }

        public int compareTo(Job other) {
            return difficulty == other.difficulty
                   ? profit - other.profit : difficulty - other.difficulty;
        }
    }

    // NavigableMap
    // time complexity: O((N + W)* log(N)), space complexity: O(N)
    // beats %(118 ms for 57 tests)
    public int maxProfitAssignment2(int[] difficulty, int[] profit,
                                    int[] worker) {
        NavigableMap<Integer, Integer> jobs = new TreeMap<>();
        for (int i = difficulty.length - 1; i >= 0; i--) {
            jobs.put(difficulty[i],
                     Math.max(profit[i], jobs.getOrDefault(difficulty[i], 0)));
        }
        int max = 0;
        for (int d : jobs.keySet()) {
            max = Math.max(jobs.get(d), max);
            jobs.put(d, max);
        }
        int res = 0;
        for (int w : worker) {
            Map.Entry<Integer, Integer> job = jobs.floorEntry(w);
            if (job != null) {
                res += job.getValue();
            }
        }
        return res;
    }

    // Heap + Binary Search
    // time complexity: O((N + W)* log(N)), space complexity: O(N)
    // beats %(83 ms for 57 tests)
    public int maxProfitAssignment3(int[] difficulty, int[] profit,
                                    int[] worker) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return (a[0] != b[0]) ? a[0] - b[0] : a[1] - b[1];
            }
        });
        for (int i = difficulty.length - 1; i >= 0; i--) {
            pq.offer(new int[] {difficulty[i], profit[i]});
        }
        List<Integer> dList = new ArrayList<>();
        List<Integer> pList = new ArrayList<>();
        for (int i = 0, max = 0; !pq.isEmpty(); i++) {
            int[] cur = pq.poll();
            cur[1] = max = Math.max(max, cur[1]);
            if (i == 0 || cur[0] != dList.get(i - 1)) {
                dList.add(cur[0]);
                pList.add(cur[1]);
            } else {
                pList.set(--i, max);
            }
        }
        int res = 0;
        for (int w : worker) {
            int index = Collections.binarySearch(dList, w);
            if (index < 0) {
                if ((index = -index - 2) < 0) continue;
            }
            res += pList.get(index);
        }
        return res;
    }

    // Sort + Binary Search
    // time complexity: O((N + W)* log(N)), space complexity: O(N)
    // beats %(43 ms for 57 tests)
    public int maxProfitAssignment4(int[] difficulty, int[] profit,
                                    int[] worker) {
        int n = difficulty.length;
        int[][] jobs = new int[n][2];
        for (int i = 0; i < n; ++i) {
            jobs[i] = new int[] {difficulty[i], profit[i]};
        }
        Arrays.sort(jobs, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return (a[0] != b[0]) ? a[0] - b[0] : b[1] - a[1];
            }
        });
        int[] level = new int[n];
        for (int i = 0; i < n; i++) {
            level[i] = jobs[i][0];
        }
        for (int i = 1; i < n; i++) {
            jobs[i][1] = Math.max(jobs[i][1], jobs[i - 1][1]);
        }
        int res = 0;
        for (int w : worker) {
            int index = Arrays.binarySearch(level, w);
            if (index < 0) {
                index = -index - 2;
            }
            if (index >= 0) {
                res += jobs[index][1];
            }
        }
        return res;
    }

    // Sort + Two Pointers
    // time complexity: O(N * log(N) + W * log(W)), space complexity: O(N)
    // beats %(40 ms for 57 tests)
    public int maxProfitAssignment5(int[] difficulty, int[] profit,
                                    int[] worker) {
        int n = difficulty.length;
        int[][] jobs = new int[n][2];
        for (int i = 0; i < n; ++i) {
            jobs[i] = new int[] {difficulty[i], profit[i]};
        }
        Arrays.sort(jobs, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[0] - b[0];
            }
        });
        Arrays.sort(worker);
        int res = 0;
        int i = 0;
        int max = 0;
        for (int w : worker) {
            while (i < n && w >= jobs[i][0]) {
                max = Math.max(max, jobs[i++][1]);
            }
            res += max;
        }
        return res;
    }

    // Hash Table + Bucket
    // time complexity: O(N + MAX_DIFF), space complexity: O(N + MAX_DIFF)
    // beats %(76 ms for 57 tests)
    public int maxProfitAssignment6(int[] difficulty, int[] profit,
                                    int[] worker) {
        Map<Integer, Integer> map = new HashMap<>();
        int maxLevel = 0;
        for (int i = difficulty.length - 1; i >= 0; i--) {
            map.put(difficulty[i],
                    Math.max(map.getOrDefault(difficulty[i], 0), profit[i]));
            maxLevel = Math.max(maxLevel, difficulty[i]);
        }
        int[] profits = new int[maxLevel + 1];
        for (int i = 1; i < profits.length; i++) {
            profits[i] = Math.max(profits[i - 1], map.getOrDefault(i, 0));
        }
        int res = 0;
        for (int w : worker) {
            res += (w > maxLevel) ? profits[maxLevel] : profits[w];
        }
        return res;
    }

    // Bucket Sort
    // time complexity: O(N + MAX_DIFF), space complexity: O(N + MAX_DIFF)
    // beats %(27 ms for 57 tests)
    public int maxProfitAssignment7(int[] difficulty, int[] profit,
                                    int[] worker) {
        int[] p = new int[100001];
        for (int i = difficulty.length - 1; i >= 0; i--) {
            p[difficulty[i]] = Math.max(p[difficulty[i]], profit[i]);
        }
        for (int i = 1; i < p.length; i++) {
            p[i] = Math.max(p[i], p[i - 1]);
        }
        int res = 0;
        for (int w : worker) {
            res += p[w];
        }
        return res;
    }

    void test(int[] difficulty, int[] profit, int[] worker, int expected) {
        assertEquals(expected, maxProfitAssignment(difficulty, profit, worker));
        assertEquals(expected,
                     maxProfitAssignment2(difficulty, profit, worker));
        assertEquals(expected,
                     maxProfitAssignment3(difficulty, profit, worker));
        assertEquals(expected,
                     maxProfitAssignment4(difficulty, profit, worker));
        assertEquals(expected,
                     maxProfitAssignment5(difficulty, profit, worker));
        assertEquals(expected,
                     maxProfitAssignment6(difficulty, profit, worker));
        assertEquals(expected,
                     maxProfitAssignment7(difficulty, profit, worker));
    }

    @Test
    public void test() {
        test(new int[] {2, 4, 6, 8, 10}, new int[] {10, 20, 30, 40, 50},
             new int[] {4, 5, 6, 7}, 100);
        test(new int[] {68, 35, 52, 47, 86}, new int[] {67, 17, 1, 81, 3},
             new int[] {92, 10, 85, 84, 82}, 324);
        test(new int[] {49, 49, 76, 88, 100}, new int[] {5, 8, 75, 89, 94},
             new int[] {98, 72, 16, 27, 76}, 172);
        test(new int[] {23, 30, 35, 35, 43, 46, 47, 81, 83, 98},
             new int[] { 8, 11, 11, 20, 33, 37, 60, 72, 87, 95},
             new int[] {95, 46, 47, 97, 11, 35, 99, 56, 41, 92}, 553);
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
