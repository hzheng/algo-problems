import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1817: https://leetcode.com/problems/finding-the-users-active-minutes/
//
// You are given the logs for users' actions on LeetCode, and an integer k. The logs are represented
// by a 2D integer array logs where each logs[i] = [IDi, timei] indicates that the user with IDi
// performed an action at the minute timei.
// Multiple users can perform actions simultaneously, and a single user can perform multiple actions
// in the same minute.
// The user active minutes (UAM) for a given user is defined as the number of unique minutes in
// which the user performed an action on LeetCode. A minute can only be counted once, even if
// multiple actions occur during it.
// You are to calculate a 1-indexed array answer of size k such that, for each j (1 <= j <= k),
// answer[j] is the number of users whose UAM equals j.
// Return the array answer as described above.
//
// Constraints:
// 1 <= logs.length <= 10^4
// 0 <= IDi <= 10^9
// 1 <= timei <= 10^5
// k is in the range [The maximum UAM for a user, 10^5].
public class FindingUsersActiveMinutes {
    // time complexity: O(N), space complexity: O(N+K)
    // 13 ms(66.67%), 51.8 MB(33.33%) for 38 tests
    public int[] findingUsersActiveMinutes(int[][] logs, int k) {
        int[] res = new int[k];
        Map<Integer, Set<Integer>> map = new HashMap<>();
        for (int[] log : logs) {
            int id = log[0];
            int time = log[1];
            map.computeIfAbsent(id, x -> new HashSet<>()).add(time);
        }
        for (Set<Integer> set : map.values()) {
            res[set.size() - 1]++;
        }
        return res;
    }

    private void test(int[][] logs, int k, int[] expected) {
        assertArrayEquals(expected, findingUsersActiveMinutes(logs, k));
    }

    @Test public void test() {
        test(new int[][] {{0, 5}, {1, 2}, {0, 2}, {0, 5}, {1, 3}}, 5, new int[] {0, 2, 0, 0, 0});
        test(new int[][] {{1, 1}, {2, 2}, {2, 3}}, 4, new int[] {1, 1, 0, 0});
        test(new int[][] {{305589003, 4136}, {305589004, 4139}, {305589004, 4141},
                          {305589004, 4137}, {305589001, 4139}, {305589001, 4139}}, 6,
             new int[] {2, 0, 1, 0, 0, 0});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
