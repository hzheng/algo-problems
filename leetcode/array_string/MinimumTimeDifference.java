import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC539: https://leetcode.com/problems/minimum-time-difference
//
// Given a list of 24-hour clock time points in "Hour:Minutes" format, find the
// minimum minutes difference between any two time points in the list.
public class MinimumTimeDifference {
    // Sort
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats N/A(50 ms for 112 tests)
    public int findMinDifference(List<String> timePoints) {
        Collections.sort(timePoints);
        int min = Integer.MAX_VALUE;
        int n = timePoints.size();
        int first = convert(timePoints.get(0));
        int cur = 0;
        for (int i = 1, prev = first; i < n; i++, prev = cur) {
            cur = convert(timePoints.get(i));
            min = Math.min(min, cur - prev);
        }
        return Math.min(min, convert("24:00") - cur + first);
    }

    private int convert(String time) {
        int hour = Integer.parseInt(time.substring(0, 2));
        int min = Integer.parseInt(time.substring(3, 5));
        return hour * 60 + min;
    }

    // Heap
    // time complexity: O(N * log(N)), space complexity: O(N)
    // beats N/A(41 ms for 112 tests)
    public int findMinDifference2(List<String> timePoints) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (String time : timePoints) {
            pq.offer(convert(time));
        }
        int min = Integer.MAX_VALUE;
        int first = pq.poll();
        int cur = 0;
        for (int prev = first; !pq.isEmpty(); prev = cur) {
            cur = pq.poll();
            min = Math.min(min, cur - prev);
        }
        return Math.min(min, convert("24:00") - cur + first);
    }

    // time complexity: O(N), space complexity: O(1)
    // beats N/A(19 ms for 112 tests)
    public int findMinDifference3(List<String> timePoints) {
        final int MAX = 24 * 60;
        boolean[] times = new boolean[MAX];
        for (String time : timePoints) {
            int mins = convert(time);

            if (times[mins]) return 0;
            times[mins] = true;
        }
        int first = -1;
        int prev = 0;
        int min = MAX;
        for (int cur = 0; cur < MAX; cur++) {
            if (!times[cur]) continue;

            if (first < 0) {
                first = cur;
            } else {
                min = Math.min(min, cur - prev);
            }
            prev = cur;
        }
        return Math.min(min, MAX - prev + first);
    }

    void test(String[] times, int expected) {
        assertEquals(expected, findMinDifference(Arrays.asList(times)));
        assertEquals(expected, findMinDifference2(Arrays.asList(times)));
        assertEquals(expected, findMinDifference3(Arrays.asList(times)));
    }

    @Test
    public void test() {
        test(new String[] {"23:59", "00:00"}, 1);
        test(new String[] {"23:59", "02:00"}, 121);
        test(new String[] {"01:11", "12:01", "13:59", "02:00", "09:18"}, 49);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MinimumTimeDifference");
    }
}
