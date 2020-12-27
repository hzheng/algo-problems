import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1036: https://leetcode.com/problems/escape-a-large-maze/
//
// In a 1 million by 1 million grid, the coordinates of each grid square are (x, y). We start at the
// source square and want to reach the target square.  Each move, we can walk to a 4-directionally
// adjacent square in the grid that isn't in the given list of blocked squares. Return true if and
// only if it is possible to reach the target square through a sequence of moves.
//
// Constraints:
// 0 <= blocked.length <= 200
// blocked[i].length == 2
// 0 <= blocked[i][j] < 10^6
// source.length == target.length == 2
// 0 <= source[i][j], target[i][j] < 10^6
// source != target
public class EscapeMaze {
    private static final int[][] MOVES = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    private static final int MAX = 1000_000;

    // BFS + Queue + Set
    // time complexity: O(B^2), space complexity: O(B^2)
    // 242 ms(48.92%), 46.7 MB(71.94%) for 31 tests
    public boolean isEscapePossible(int[][] blocked, int[] source, int[] target) {
        Set<Long> blockedSet = new HashSet<>();
        for (int[] b : blocked) {
            blockedSet.add(getKey(b));
        }
        long src = getKey(source);
        long tgt = getKey(target);
        return bfs(src, tgt, blockedSet) && bfs(tgt, src, blockedSet);
    }

    private boolean bfs(long src, long tgt, Set<Long> blocked) {
        Set<Long> visited = new HashSet<>();
        int blockedSize = blocked.size();
        int maxSteps = blockedSize * (blockedSize - 1) / 2; // maximal area
        Queue<Long> queue = new LinkedList<>();
        for (queue.offer(src); !queue.isEmpty(); ) {
            long cur = queue.poll();
            int x = (int)(cur >>> Integer.SIZE);
            int y = (int)(cur & Integer.MAX_VALUE);
            for (int[] move : MOVES) {
                int nx = x + move[0];
                int ny = y + move[1];
                if (nx < 0 || ny < 0 || nx >= MAX || ny >= MAX) { continue; }

                long nextKey = getKey(new int[] {nx, ny});
                if (!blocked.contains(nextKey) && visited.add(nextKey)) {
                    if (nextKey == tgt) { return true; }

                    queue.offer(nextKey);
                }
            }
            if (visited.size() > maxSteps) { return true; } // exceed enclosed block's area
        }
        return false;
    }

    // DFS + Recursion + Set
    // time complexity: O(B^2), space complexity: O(B^2)
    // 77 ms(63.31%), 47 MB(69.79%) for 31 tests
    public boolean isEscapePossible2(int[][] blocked, int[] source, int[] target) {
        Set<Long> blockedSet = new HashSet<>();
        for (int[] b : blocked) {
            blockedSet.add(getKey(b));
        }
        int blockedSize = blockedSet.size();
        int maxSteps = blockedSize * (blockedSize - 1) / 2; // maximal area
        long src = getKey(source);
        long tgt = getKey(target);
        return dfs(source[0], source[1], tgt, blockedSet, new HashSet<>(), -maxSteps)
               && dfs(target[0], target[1], src, blockedSet, new HashSet<>(), -maxSteps);
    }

    private boolean dfs(int x, int y, long tgt, Set<Long> blocked, Set<Long> visited,
                        int maxSteps) {
        if (x < 0 || y < 0 || x >= MAX || y >= MAX) { return false; }

        long key = getKey(new int[] {x, y});
        if (key == tgt) { return true; }
        if (maxSteps > 0 && blocked.contains(key) || !visited.add(key)) { return false; }

        if (maxSteps < 0) { // initial start may be in blocked area
            maxSteps = -maxSteps;
        }
        if (visited.size() > maxSteps) { return true; } // exceed enclosed block's area

        return dfs(x + 1, y, tgt, blocked, visited, maxSteps)
               || dfs(x - 1, y, tgt, blocked, visited, maxSteps)
               || dfs(x, y + 1, tgt, blocked, visited, maxSteps)
               || dfs(x, y - 1, tgt, blocked, visited, maxSteps);
    }

    private long getKey(int[] point) {
        return (((long)point[0]) << Integer.SIZE) | point[1];
    }

    private void test(int[][] blocked, int[] source, int[] target, boolean expected) {
        assertEquals(expected, isEscapePossible(blocked, source, target));
        assertEquals(expected, isEscapePossible2(blocked, source, target));
    }

    @Test public void test() {
        test(new int[][] {}, new int[] {0, 0}, new int[] {999999, 999999}, true);
        test(new int[][] {{0, 1}, {1, 0}}, new int[] {0, 0}, new int[] {0, 2}, false);
        test(new int[][] {{0, 3}, {1, 0}, {1, 1}, {1, 2}, {1, 3}}, new int[] {0, 0},
             new int[] {0, 2}, true);
        test(new int[][] {{10, 9}, {9, 10}, {10, 11}, {11, 10}}, new int[] {0, 0},
             new int[] {10, 10}, false);
        test(new int[][] {{5, 20}, {10, 10}, {15, 10}, {10, 30}, {15, 30}, {20, 30}},
             new int[] {10, 20}, new int[] {20, 30}, true);
        test(new int[][] {{859360, 747558}, {767839, 414499}, {957307, 53074}, {680104, 950973},
                          {777486, 371611}, {400205, 636022}, {480897, 491725}, {599558, 635067},
                          {604926, 321303}, {146897, 874078}, {69813, 369743}, {240303, 869910},
                          {297688, 930476}, {819428, 899389}, {528032, 699402}, {442, 726800},
                          {42886, 994090}, {222527, 521523}, {227403, 25583}, {542274, 603306},
                          {428336, 730145}, {647514, 773345}, {783807, 944686}, {284767, 243365},
                          {191104, 475620}, {133935, 672361}, {751876, 609273}, {162090, 772994},
                          {953887, 863427}, {918711, 683947}, {68517, 745792}, {281213, 901250},
                          {649332, 565904}, {211498, 873161}, {472298, 576437}, {657648, 423014},
                          {322155, 223386}, {104451, 548963}, {832668, 155145}, {310940, 588911},
                          {102891, 729651}, {893369, 81819}, {60100, 810221}, {586906, 722845},
                          {184989, 88941}, {312646, 650448}, {621355, 557009}, {787268, 773811},
                          {978155, 882776}, {775843, 758630}, {63834, 988711}, {345650, 156464},
                          {890173, 358341}, {640840, 107643}, {155546, 506015}, {415037, 470166},
                          {783677, 827973}, {70511, 424498}, {726709, 165867}, {315711, 921062},
                          {38804, 847785}, {295889, 787820}, {953842, 224828}, {768592, 129889},
                          {283946, 933122}, {26535, 656712}, {98331, 673090}, {389065, 728072},
                          {676328, 741858}, {626345, 724074}, {992313, 344431}, {759811, 831836},
                          {700408, 665625}, {877522, 468336}, {292117, 463603}, {74647, 814621},
                          {707459, 101239}, {621145, 461629}, {750776, 755457}, {413855, 870990},
                          {680497, 421583}}, new int[] {182699, 459911}, new int[] {925304, 621233},
             true);
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
