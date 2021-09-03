import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1235: https://leetcode.com/problems/maximum-profit-in-job-scheduling/
//
// We have n jobs, where every job is scheduled to be done from startTime[i] to endTime[i],
// obtaining a profit of profit[i]. You're given the startTime, endTime and profit arrays, return
// the maximum profit you can take such that there are no two jobs in the subset with overlapping
// time range.
// If you choose a job that ends at time X you can start another job that starts at time X.
//
// Constraints:
// 1 <= startTime.length == endTime.length == profit.length <= 5 * 10^4
// 1 <= startTime[i] < endTime[i] <= 10^9
// 1 <= profit[i] <= 10^4
public class JobScheduling {
    // 1D-Dynamic Programming(Bottom-Up) + Sort
    // time complexity: O(N^2), space complexity: O(N)
    // 27 ms(74.31%), 71 MB(27.87%) for 27 tests
    public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
        int n = startTime.length;
        int[][] jobs = new int[n][];
        for (int i = 0; i < n; i++) {
            jobs[i] = new int[] {startTime[i], endTime[i], profit[i]};
        }
        Arrays.sort(jobs, Comparator.comparingInt(a -> a[0]));
        int[] dp = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            int j = i + 1;
            for (int start = jobs[i][1]; j < n; j++) {
                if (jobs[j][0] >= start) {
                    break;
                }
            }
            dp[i] = Math.max(dp[i + 1], jobs[i][2] + dp[j]);
        }
        return dp[0];
    }

    static class Job implements Comparable<Job> {
        int start;
        int end;
        int profit;

        Job(int start, int end, int profit) {
            this.start = start;
            this.end = end;
            this.profit = profit;
        }

        public int compareTo(Job other) {
            return Integer.compare(start, other.start);
        }
        public String toString() {
            return start+"-"+end+";"+profit;
        }
    }

    // 1D-Dynamic Programming(Bottom-Up) + Sort + Binary Search
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 31 ms(65.89%), 73.3 MB(22.92%) for 27 tests
    public int jobScheduling2(int[] startTime, int[] endTime, int[] profit) {
        int n = startTime.length;
        Job[] jobs = new Job[n];
        for (int i = 0; i < n; i++) {
            jobs[i] = new Job(startTime[i], endTime[i], profit[i]);
        }
        Arrays.sort(jobs);
        int[] dp = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            int key = jobs[i].end;
            int j = Arrays.binarySearch(jobs, i + 1, n, new Job(key, 0, 0));
            if (j < 0) {
                j = -j - 1;
            } else { // go to the leftmost (performance may be degraded)
                for (; j > 0 && jobs[j - 1].start == key; j--) {}
            }
            dp[i] = Math.max(dp[i + 1], jobs[i].profit + dp[j]);
        }
        return dp[0];
    }

    // 1D-Dynamic Programming(Bottom-Up) + Sort + Binary Search
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 16 ms(89.83%), 47.9 MB(85.04%) for 27 tests
    public int jobScheduling2_2(int[] startTime, int[] endTime, int[] profit) {
        int n = startTime.length;
        int[][] jobs = new int[n][];
        for (int i = 0; i < n; i++) {
            jobs[i] = new int[]{startTime[i], endTime[i], profit[i]};
        }
        Arrays.sort(jobs, Comparator.comparingInt(a -> a[0]));
        int[] dp = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            int j = searchLeftmost(jobs, i + 1, n, jobs[i][1]);
            dp[i] = Math.max(dp[i + 1], jobs[i][2] + dp[j]);
        }
        return dp[0];
    }

    private int searchLeftmost(int[][] jobs, int start, int end, int key) {
        int low = start;
        for (int high = end; low < high; ) {
            int mid = (low + high) >>> 1;
            if (jobs[mid][0] < key) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    // 1D-Dynamic Programming(Bottom-Up) + Sort + SortedMap
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 38 ms(58.27%), 48.4 MB(71.57%) for 27 tests
    public int jobScheduling3(int[] startTime, int[] endTime, int[] profit) {
        int n = startTime.length;
        int[][] jobs = new int[n][];
        for (int i = 0; i < n; i++) {
            jobs[i] = new int[] {startTime[i], endTime[i], profit[i]};
        }
        Arrays.sort(jobs, Comparator.comparingInt(a -> a[1]));
        TreeMap<Integer, Integer> dp = new TreeMap<>();
        dp.put(0, 0);
        for (int[] job : jobs) {
            int curProfit = dp.floorEntry(job[0]).getValue() + job[2];
            if (curProfit > dp.lastEntry().getValue()) {
                dp.put(job[1], curProfit);
            }
        }
        return dp.lastEntry().getValue();
    }

    private void test(int[] startTime, int[] endTime, int[] profit, int expected) {
        assertEquals(expected, jobScheduling(startTime, endTime, profit));
        assertEquals(expected, jobScheduling2(startTime, endTime, profit));
        assertEquals(expected, jobScheduling2_2(startTime, endTime, profit));
        assertEquals(expected, jobScheduling3(startTime, endTime, profit));
    }

    @Test public void test() {
        test(new int[] {1, 1, 1}, new int[] {2, 3, 4}, new int[] {5, 6, 4}, 6);
        test(new int[] {1, 2, 3, 3}, new int[] {3, 4, 5, 6}, new int[] {50, 10, 40, 70}, 120);
        test(new int[] {1, 2, 3, 4, 6}, new int[] {3, 5, 10, 6, 9}, new int[] {20, 20, 100, 70, 60},
             150);
        test(new int[] {4, 2, 4, 8, 2}, new int[] {5, 5, 5, 10, 8}, new int[] {1, 2, 8, 10, 4}, 18);
        test(new int[] {1, 2, 2, 3}, new int[] {2, 5, 3, 4}, new int[] {3, 4, 1, 2}, 7);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
