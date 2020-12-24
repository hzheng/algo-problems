import java.util.*;
import java.util.stream.Collectors;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1024: https://leetcode.com/problems/video-stitching/
//
// You are given a series of video clips from a sporting event that lasted T seconds. These video
// clips can be overlapping with each other and have varied lengths. Each video clip clips[i] is an
// interval: it starts at time clips[i][0] and ends at time clips[i][1]. We can cut these clips into
// segments freely: for example, a clip [0, 7] can be cut into segments [0, 1] + [1, 3] + [3, 7].
// Return the minimum number of clips needed so that we can cut the clips into segments that cover
// the entire sporting event ([0, T]).  If the task is impossible, return -1.
//
// Constraints:
// 1 <= clips.length <= 100
// 0 <= clips[i][0] <= clips[i][1] <= 100
// 0 <= T <= 100
public class VideoStitching {
    // SortedMap
    // time complexity: O(N*log(N)), space complexity: O(N+M)
    // 3 ms(11.00%), 36.6 MB(66.53%) for 55 tests
    public int videoStitching(int[][] clips, int T) {
        SortedMap<Integer, Integer> map = new TreeMap<>();
        for (int[] clip : clips) {
            int left = clip[0];
            map.put(left, Math.max(map.getOrDefault(left, -1), clip[1]));
        }
        int res = 0;
        for (int covered = 0; covered < T; res++) {
            Map<Integer, Integer> candidates = map.headMap(covered + 1);
            if (candidates.isEmpty()) { return -1; }

            for (int right : candidates.values()) {
                covered = Math.max(covered, right);
            }
            candidates.clear();
        }
        return res;
    }

    // Sliding Window
    // time complexity: O(N*T), space complexity: O(T)
    // 0 ms(100.00%), 36.2 MB(97.35%) for 55 tests
    public int videoStitching2(int[][] clips, int T) {
        int[] rightmost = new int[T];
        Arrays.fill(rightmost, -1);
        for (int[] clip : clips) {
            int left = clip[0];
            int right = clip[1];
            for (int t = Math.min(right, T - 1); t >= left; t--) {
                rightmost[t] = Math.max(rightmost[t], right);
            }
        }
        int res = 0;
        for (int covered = 0, prev = -1; covered < T; res++, prev = covered) {
            covered = rightmost[covered];
            if (covered <= prev) { return -1; }
        }
        return res;
    }

    // Dynamic Programming + Sort
    // time complexity: O(N*T), space complexity: O(T)
    // 2 ms(20.20%), 36.7 MB(35.71%) for 55 tests
    public int videoStitching3(int[][] clips, int T) {
        Arrays.sort(clips, Comparator.comparingInt(a -> a[0]));
        int[] dp = new int[T + 1];
        Arrays.fill(dp, T + 1);
        dp[0] = 0;
        for (int[] clip : clips) {
            for (int t = Math.min(T, clip[1]); t > clip[0]; t--) {
                dp[t] = Math.min(dp[t], dp[clip[0]] + 1);
            }
        }
        return dp[T] > T ? -1 : dp[T];
    }

    // Dynamic Programming
    // time complexity: O(N*T), space complexity: O(T)
    // 1 ms(84.29%), 36.2 MB(100.00%) for 55 tests
    public int videoStitching4(int[][] clips, int T) {
        int[] dp = new int[T + 1];
        Arrays.fill(dp, T + 1);
        dp[0] = 0;
        for (int t = 1; t <= T && dp[t - 1] <= T; t++) {
            for (int[] clip : clips) {
                if (t >= clip[0] && t <= clip[1]) {
                    dp[t] = Math.min(dp[t], dp[clip[0]] + 1);
                }
            }
        }
        return dp[T] > T ? -1 : dp[T];
    }

    // Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 2 ms(20.20%), 36.5 MB(66.53%) for 55 tests
    public int videoStitching5(int[][] clips, int T) {
        Arrays.sort(clips, Comparator.comparingInt(a -> a[0]));
        int res = 0;
        for (int winLeft = -1, winRight = 0, i = 0, n = clips.length; winRight < T; i++) {
            if (i == n) { return -1; }

            int[] cur = clips[i];
            int left = cur[0];
            if (left > winRight) { return -1; } // leak

            if (left > winLeft) { // has to add one
                res++;
                winLeft = winRight;
            }
            winRight = Math.max(winRight, cur[1]);
        }
        return res;
    }

    // Sort
    // time complexity: O(N*(T+log(N))), space complexity: O(log(N))
    // 1 ms(84.29%), 36.9 MB(22.65%) for 55 tests
    public int videoStitching6(int[][] clips, int T) {
        Arrays.sort(clips, (a, b) -> (a[1] == b[1] ? a[0] - b[0] : b[1] - a[1]));
        int res = 0;
        for (int next = 0, prev = -1; next < T; res++, prev = next) {
            for (int[] clip : clips) {
                if (clip[0] <= next) {
                    next = clip[1];
                    break;
                }
            }
            if (next == prev) { return -1; }
        }
        return res;
    }

    // Sort
    // time complexity: O(N*log(N)), space complexity: O(log(N))
    // 2 ms(20.20%), 36.5 MB(66.53%) for 55 tests
    public int videoStitching7(int[][] clips, int T) {
        Arrays.sort(clips, Comparator.comparingInt(a -> a[0]));
        int res = 0;
        for (int i = 0, left = 0, right = 0, n = clips.length; left < T; left = right, res++) {
            for (; i < n && clips[i][0] <= left; i++) {
                right = Math.max(right, clips[i][1]);
            }
            if (left == right) { return -1; }
        }
        return res;
    }

    private void test(int[][] clips, int T, int expected) {
        assertEquals(expected, videoStitching(clips, T));
        assertEquals(expected, videoStitching2(clips, T));
        assertEquals(expected, videoStitching3(clips, T));
        assertEquals(expected, videoStitching4(clips, T));
        assertEquals(expected, videoStitching5(clips, T));
        assertEquals(expected, videoStitching6(clips, T));
        assertEquals(expected, videoStitching7(clips, T));
    }

    @Test public void test() {
        test(new int[][] {{0, 4}, {2, 8}}, 5, 2);
        test(new int[][] {{0, 5}, {6, 8}}, 7, -1);
        test(new int[][] {{0, 1}, {1, 2}}, 5, -1);
        test(new int[][] {{0, 1}, {6, 8}, {0, 2}, {5, 6}, {0, 4}, {0, 3}, {6, 7}, {1, 3}, {4, 7},
                          {1, 4}, {2, 5}, {2, 6}, {3, 4}, {4, 5}, {5, 7}, {6, 9}}, 9, 3);
        test(new int[][] {{0, 2}, {4, 6}, {8, 10}, {1, 9}, {1, 5}, {5, 9}}, 10, 3);
        test(new int[][] {{0, 0}, {0, 1}, {1, 2}, {2, 5}}, 5, 3);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
