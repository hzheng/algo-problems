import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC752: https://leetcode.com/problems/open-the-lock/
//
// You have a lock in front of you with 4 circular wheels. Each wheel has 10 slots: '0', '1', '2',
// '3', '4', '5', '6', '7', '8', '9'. The wheels can rotate freely and wrap around. Each move
// consists of turning one wheel one slot. The lock initially starts at '0000', a string
// representing the state of the 4 wheels. You are given a list of deadends dead ends, meaning if
// the lock displays any of these codes, the wheels of the lock will stop turning and you will be
// unable to open it. Given a target representing the value of the wheels that will unlock the lock,
// return the minimum total number of turns required to open the lock, or -1 if it is impossible.
//
// Constraints:
// 1 <= deadends.length <= 500
// deadends[i].length == 4
// target.length == 4
// target will not be in the list deadends.
// target and deadends[i] consist of digits only.
public class OpenLock {
    private static final String START = "0000";
    private static final int[] CHANGES = {-1, 1};

    // BFS + Queue
    // time complexity: O(N^4), space complexity: O(1)
    // 75 ms(73.98%), 47.2 MB(6.24%) for 46 tests
    public int openLock(String[] deadends, String target) {
        Set<String> blocked = new HashSet<>(Arrays.asList(deadends));
        Queue<String> queue = new LinkedList<>();
        queue.offer(START);
        for (int res = 0, n = START.length(); !queue.isEmpty(); res++) {
            for (int size = queue.size(); size > 0; size--) {
                String cur = queue.poll();
                if (cur.equals(target)) { return res; }
                if (!blocked.add(cur)) { continue; }

                for (int i = 0; i < n; i++) {
                    for (int change : CHANGES) {
                        char[] next = cur.toCharArray();
                        next[i] = (char)((next[i] - '0' + change + 10) % 10 + '0');
                        queue.offer(String.valueOf(next));
                    }
                }
            }
        }
        return -1;
    }

    // Bidirectional BFS + Set
    // time complexity: O(N^4), space complexity: O(1)
    // 18 ms(98.02%), 39.7 MB(6.24%) for 46 tests
    public int openLock2(String[] deadends, String target) {
        Set<String> blocked = new HashSet<>(Arrays.asList(deadends));
        Set<String> start = new HashSet<>(Arrays.asList(START));
        Set<String> end = new HashSet<>(Arrays.asList(target));
        Set<String> nextStart;
        for (int res = 0, n = START.length(); ; start = nextStart, res++) {
            if (start.isEmpty() || end.isEmpty()) { return -1; }

            if (start.size() > end.size()) { // performance enhancing
                nextStart = start;
                start = end;
                end = nextStart;
            }
            nextStart = new HashSet<>();
            for (String cur : start) {
                if (end.contains(cur)) { return res; }
                if (!blocked.add(cur)) { continue; }

                for (int i = 0; i < n; i++) {
                    for (int change : CHANGES) {
                        char[] next = cur.toCharArray();
                        next[i] = (char)((next[i] - '0' + change + 10) % 10 + '0');
                        String nextStr = String.valueOf(next);
                        if (!blocked.contains(nextStr)) {
                            nextStart.add(nextStr);
                        }
                    }
                }
            }
        }
    }

    private void test(String[] deadends, String target, int expected) {
        assertEquals(expected, openLock(deadends, target));
        assertEquals(expected, openLock2(deadends, target));
    }

    @Test public void test() {
        test(new String[] {"0201", "0101", "0102", "1212", "2002"}, "0202", 6);
        test(new String[] {"8888"}, "0009", 1);
        test(new String[] {"8887", "8889", "8878", "8898", "8788", "8988", "7888", "9888"}, "8888",
             -1);
        test(new String[] {"0000"}, "8888", -1);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
