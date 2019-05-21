import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1046: https://leetcode.com/problems/last-stone-weight/
//
// We have a collection of rocks, each rock has a positive integer weight. Each turn, we choose the
// two heaviest rocks and smash them together.  Suppose the stones have weights x and y with x <= y.
// The result of this smash is:
// If x == y, both stones are totally destroyed;
// If x != y, the stone of weight x is totally destroyed, and the one of weight y has weight y-x.
// At the end, there is at most 1 stone left.  Return the weight of this stone (or 0 if there are no
// stones left.)
// Note:
// 1 <= stones.length <= 30
// 1 <= stones[i] <= 1000
public class LastStoneWeight {
    // Heap
    // time complexity: O(N * log(N)), space complexity: O(N)
    // 1 ms(98.44%), 33.3 MB(100%) for 70 tests
    public int lastStoneWeight(int[] stones) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
        for (int s : stones) {
            pq.offer(s);
        }
        int cur = pq.poll();
        for (; !pq.isEmpty(); cur = pq.poll()) {
            cur -= pq.poll();
            if (cur > 0) {
                pq.offer(cur);
            }
            if (pq.isEmpty()) {
                break;
            }
        }
        return cur;
    }

    // Heap
    // time complexity: O(N * log(N)), space complexity: O(N)
    // 1 ms(98.44%), 33.3 MB(100%) for 70 tests
    public int lastStoneWeight2(int[] stones) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
        for (int s : stones) {
            pq.offer(s);
        }
        pq.offer(0);
        for (int cur = pq.poll(); ; cur = pq.poll()) {
            int next = pq.poll();
            if (next == 0) {
                return cur;
            }
            pq.offer(cur - next);
        }
    }

    // Heap
    // time complexity: O(N * log(N)), space complexity: O(N)
    // 1 ms(98.44%), 33.3 MB(100%) for 70 tests
    public int lastStoneWeight3(int[] stones) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Comparator.reverseOrder());
        for (int s : stones) {
            pq.offer(s);
        }
        for (int i = stones.length - 1; i > 0; i--) { // or: while (pq.size() > 1) {
            pq.offer(pq.poll() - pq.poll());
        }
        return pq.peek();
    }

    void test(int[] stones, int expected) {
        assertEquals(expected, lastStoneWeight(stones));
        assertEquals(expected, lastStoneWeight2(stones));
        assertEquals(expected, lastStoneWeight3(stones));
    }

    @Test
    public void test() {
        test(new int[]{2, 7, 4, 1, 8, 1}, 1);
        test(new int[]{2, 2}, 0);
        test(new int[]{3}, 3);
        test(new int[]{11, 97, 31, 26, 33, 21, 40, 72, 63}, 6);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
