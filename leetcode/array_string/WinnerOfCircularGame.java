import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1823: https://leetcode.com/problems/find-the-winner-of-the-circular-game/
//
// There are n friends that are playing a game. The friends are sitting in a circle and are numbered
// from 1 to n in clockwise order. More formally, moving clockwise from the ith friend brings you to
// the (i+1)th friend for 1 <= i < n, and moving clockwise from the nth friend brings you to the 1st
// friend.
// The rules of the game are as follows:
// Start at the 1st friend.
// Count the next k friends in the clockwise direction including the friend you started at. The
// counting wraps around the circle and may count some friends more than once.
// The last friend you counted leaves the circle and loses the game.
// If there is still more than one friend in the circle, go back to step 2 starting from the friend
// immediately clockwise of the friend who just lost and repeat.
// Else, the last friend in the circle wins the game.
// Given the number of friends, n, and an integer k, return the winner of the game.
//
// Constraints:
// 1 <= k <= n <= 500
public class WinnerOfCircularGame {
    // time complexity: O(N*K), space complexity: O(N)
    // 135 ms(33.33%), 35.8 MB(33.33%) for 95 tests
    public int findTheWinner(int n, int k) {
        boolean[] leave = new boolean[n];
        for (int i = 0, count = n; count > 1; ) {
            for (int j = k; j > 0; i = (i + 1) % n) {
                if (leave[i]) { continue; }

                if (--j == 0) {
                    leave[i] = true;
                    count--;
                }
            }
        }
        for (int i = 0;  ; i++) {
            if (!leave[i]) { return i + 1; }
        }
    }

    // Queue
    // time complexity: O(N*K), space complexity: O(N)
    // 37 ms(33.33%), 38.2 MB(33.33%) for 95 tests
    public int findTheWinner2(int n, int k) {
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 1; i <= n; i++) {
            queue.offer(i);
        }
        for (int count = n; count > 1; count--) {
            for (int i = k - 1; i > 0; i--) {
                queue.offer(queue.poll());
            }
            queue.poll();
        }
        return queue.peek();
    }

    // https://en.wikipedia.org/wiki/Josephus_problem#The_general_case
    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 35.5 MB(100.00%) for 95 tests
    public int findTheWinner3(int n, int k) {
        int res = 0;
        for (int i = 2; i <= n; i++) {
            res = (res + k) % i;
        }
        return res + 1;
    }

    private void test(int n, int k, int expected) {
        assertEquals(expected, findTheWinner(n, k));
        assertEquals(expected, findTheWinner2(n, k));
        assertEquals(expected, findTheWinner3(n, k));
    }

    @Test public void test() {
        test(5, 2, 3);
        test(6, 5, 1);
        test(1, 1, 1);
        test(16, 7, 12);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
