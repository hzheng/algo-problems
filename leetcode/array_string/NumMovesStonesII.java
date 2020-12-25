import java.util.*;
import java.util.stream.Collectors;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1040: https://leetcode.com/problems/moving-stones-until-consecutive-ii/
//
// On an infinite number line, the position of the i-th stone is given by stones[i]. Call a stone
// an endpoint stone if it has the smallest or largest position.
// Each turn, you pick up an endpoint stone and move it to an unoccupied position so that it is no
// longer an endpoint stone.
// In particular, if the stones are at say, stones = [1,2,5], you cannot move the endpoint stone at
// position 5, since moving it to any position (such as 0, or 3) will still keep that stone as an
// endpoint stone.
// The game ends when you cannot make any more moves, ie. the stones are in consecutive positions.
// When the game ends, what is the minimum and maximum number of moves that you could have made?
// Return the answer as an length 2 array: answer = [minimum_moves, maximum_moves]
//
// Note:
// 3 <= stones.length <= 10^4
// 1 <= stones[i] <= 10^9
// stones[i] have distinct values.
public class NumMovesStonesII {
    // Sort + SortedMap
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 34 ms(6.15%), 39.9 MB(58.46%) for 25 tests
    public int[] numMovesStonesII(int[] stones) {
        Arrays.sort(stones);
        return new int[] {minMoves(stones), maxMoves(stones)};
    }

    private int maxMoves(int[] stones) {
        int n = stones.length;
        int left = stones[1] - stones[0];
        int right = stones[n - 1] - stones[n - 2];
        int range = stones[n - 1] - stones[0] - Math.min(left, right);
        return range - n + 2;
    }

    private int minMoves(int[] stones) {
        int n = stones.length;
        NavigableSet<Integer> set = new TreeSet<>();
        for (int stone : stones) {
            set.add(stone);
        }
        int res = Integer.MAX_VALUE;
        for (int left : stones) {
            int end = left + n - 1;
            Integer right = set.ceiling(end);
            if (right == null) { break; }

            int min = n - set.subSet(left, end + 1).size();
            if (min == 1 && right > end + 1) {
                min++;
            }
            res = Math.min(res, min);
        }
        return res;
    }

    // Sort + Sliding Window
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 5 ms(69.23%), 39.7 MB(64.62%) for 25 tests
    public int[] numMovesStonesII2(int[] stones) {
        Arrays.sort(stones);
        return new int[] {minMoves2(stones), maxMoves(stones)};
    }

    private int minMoves2(int[] stones) {
        for (int i = 0, j = i, n = stones.length, res = n; ; i++) {
            int left = stones[i];
            int end = left + n - 1;
            for (; j < n && stones[j] < end; j++) {}
            if (j == n) { return res; }

            int right = stones[j];
            int moves = n - (j - i + ((right == end) ? 1 : 0));
            if (right > end + 1 && moves == 1) {
                moves++;
            }
            res = Math.min(res, moves);
        }
    }

    // Sort + Sliding Window
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 5 ms(69.23%), 39.9 MB(52.31%) for 25 tests
    public int[] numMovesStonesII3(int[] stones) {
        Arrays.sort(stones);
        int n = stones.length;
        int min = n;
        for (int i = 0, j = 0; j < n; j++) {
            for (; stones[j] - stones[i] >= n; i++) {}
            if (j - i + 1 == n - 1 && stones[j] - stones[i] == n - 2) {
                min = Math.min(min, 2);
            } else {
                min = Math.min(min, n - (j - i + 1));
            }
        }
        int max = Math.max(stones[n - 1] - stones[1], stones[n - 2] - stones[0]) - n + 2;
        return new int[] {min, max};
    }

    private void test(int[] stones, int[] expected) {
        assertArrayEquals(expected, numMovesStonesII(stones.clone()));
        assertArrayEquals(expected, numMovesStonesII2(stones.clone()));
        assertArrayEquals(expected, numMovesStonesII3(stones.clone()));
    }

    @Test public void test() {
        test(new int[] {7, 4, 9}, new int[] {1, 2});
        test(new int[] {6, 5, 4, 3, 10}, new int[] {2, 3});
        test(new int[] {6, 5, 4, 3, 8}, new int[] {1, 1});
        test(new int[] {6, 5, 4, 3, 8}, new int[] {1, 1});
        test(new int[] {1, 500000000, 1000000000}, new int[] {2, 499999999});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
