import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;
import common.Utils;

// LC675: https://leetcode.com/problems/cut-off-trees-for-golf-event/
//
// A forest is represented as a non-negative 2D map, in this map:
// 0 represents the obstacle can't be reached.
// 1 represents the ground can be walked through.
// The place with number bigger than 1 represents a tree can be walked through, 
// and this positive number represents the tree's height. You are asked to cut 
// off all the trees in this forest in the order of tree's height. And after 
// cutting, the original place has the tree will become a grass (value 1).
// You will start from the point (0, 0) and you should output the minimum steps 
// you need to walk to cut off all the trees. If you can't cut off all the 
// trees, output -1 in that situation.
// No 2 trees have the same height and at least one needs to be cut off.
public class CutOffTree {
    private static final int[][] MOVES
        = new int[][] { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

    // Heap + BFS + Queue
    // time complexity: O((M * N) ^ 2), space complexity: O(M * N)
    // beats 96.98%(229 ms for 53 tests)
    public int cutOffTree(List<List<Integer>> forest) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return forest.get(a[0]).get(a[1]) - forest.get(b[0]).get(b[1]);
            }
        });
        int i = 0;
        for (List<Integer> row : forest) {
            for (int j = forest.get(0).size() - 1; j >= 0; j--) {
                if (row.get(j) > 1) {
                    pq.offer(new int[] { i, j });
                }
            }
            i++;
        }
        int res = 0;
        for (int[] prev = new int[2], cur = null; !pq.isEmpty(); prev = cur) {
            cur = pq.poll();
            int steps = distance(forest, prev, cur);
            if (steps < 0) return -1;

            res += steps;
        }
        return res;
    }

    private int distance(List<List<Integer>> forest, int[] start, int[] end) {
        int m = forest.size();
        int n = forest.get(0).size();
        boolean[][] visited = new boolean[m][n];
        visited[start[0]][start[1]] = true;
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(start);
        for (int level = 0; !queue.isEmpty(); level++) {
            for (int i = queue.size(); i > 0; i--) {
                int[] cur = queue.poll();
                int x = cur[0];
                int y = cur[1];
                if (x == end[0] && y == end[1]) return level;

                for (int[] move : MOVES) {
                    int nx = x + move[0];
                    int ny = y + move[1];
                    if (nx >= 0 && nx < m && ny >= 0 && ny < n && !visited[nx][ny]) {
                        if (forest.get(nx).get(ny) > 0) {
                            queue.offer(new int[] { nx, ny });
                            visited[nx][ny] = true;
                        }
                    }
                }
            }
        }
        return -1;
    }

    // Sort + Heap + A* Search
    // time complexity: O((M * N) ^ 2), space complexity: O(M * N)
    // beats 93.75%(241 ms for 53 tests)
    public int cutOffTree2(List<List<Integer>> forest) {
        List<int[]> trees = new ArrayList<>();
        for (int i = forest.size() - 1; i >= 0; i--) {
            for (int j = forest.get(0).size() - 1; j >= 0; j--) {
                int v = forest.get(i).get(j);
                if (v > 1) trees.add(new int[]{i, j});
            }
        }
        Collections.sort(trees, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return forest.get(a[0]).get(a[1]) - forest.get(b[0]).get(b[1]);
            }
        });
        int res = 0;
        int[] start = new int[2];
        for (int[] tree : trees) {
            int d = dist(forest, start, tree);
            if (d < 0) return -1;

            res += d;
            start = tree;
        }
        return res;
    }

    private int dist(List<List<Integer>> forest, int[] start, int[] end) {
        int m = forest.size();
        int n = forest.get(0).size();
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return a[0] - b[0];
            }
        });
        int sx = start[0];
        int sy = start[1];
        pq.offer(new int[]{0, 0, sx, sy});
        Map<Integer, Integer> costs = new HashMap<>();
        costs.put(sx * n + sy, 0);
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int d = cur[1];
            int x = cur[2];
            int y = cur[3];
            if (x == end[0] && y == end[1]) return d;

            for (int[] move : MOVES) {
                int nx = x + move[0];
                int ny = y + move[1];
                if (nx < 0 || nx >= m || ny < 0 || ny >= n 
                    || forest.get(nx).get(ny) == 0) continue;

                int c = d + 1 + Math.abs(nx - end[0]) + Math.abs(ny - end[1]);
                if (c < costs.getOrDefault(nx * n + ny, Integer.MAX_VALUE)) {
                    costs.put(nx * n + ny, c);
                    pq.offer(new int[]{c, d + 1, nx, ny});
                }
            }
        }
        return -1;
    }

    // Sort + Set + Deque
    // Hadlock's Algorithm
    // time complexity: O((M * N) ^ 2), space complexity: O(M * N)
    // beats 100%(99 ms for 53 tests)
    public int cutOffTree3(List<List<Integer>> forest) {
        List<int[]> trees = new ArrayList<>();
        for (int i = forest.size() - 1; i >= 0; i--) {
            for (int j = forest.get(0).size() - 1; j >= 0; j--) {
                int v = forest.get(i).get(j);
                if (v > 1) trees.add(new int[]{i, j});
            }
        }
        Collections.sort(trees, new Comparator<int[]>() {
            public int compare(int[] a, int[] b) {
                return forest.get(a[0]).get(a[1]) - forest.get(b[0]).get(b[1]);
            }
        });
        int res = 0;
        int[] start = new int[2];
        for (int[] tree : trees) {
            int d = dist3(forest, start, tree);
            if (d < 0) return -1;

            res += d;
            start = tree;
        }
        return res;
    }

    private static final int[] dx = {-1, 1, 0, 0};
    private static final int[] dy = {0, 0, -1, 1};

    private int dist3(List<List<Integer>> forest, int[] start, int[] end) {
        int m = forest.size();
        int n = forest.get(0).size();
        Set<Integer> visited = new HashSet<>();
        Deque<int[]> deque = new ArrayDeque<>();
        deque.offerFirst(new int[]{0, start[0], start[1]});
        for (int endX = end[0], endY = end[1]; !deque.isEmpty(); ) {
            int[] cur = deque.pollFirst();
            int detours = cur[0];
            int x = cur[1];
            int y = cur[2];
            if (!visited.add(x * n + y)) continue;

            if (x == end[0] && y == end[1]) {
                return Math.abs(start[0] - end[0])
                       + Math.abs(start[1] - end[1]) + 2 * detours;
            }
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if (nx < 0 || nx >= m || ny < 0 || ny >= n 
                    || forest.get(nx).get(ny) <= 0) continue;

                boolean closer;
                if (i <= 1) {
                    closer = (i == 0) ? (x > endX) : (x < endX);
                } else {
                    closer = (i == 2) ? (y > endY) : (y < endY);
                }
                if (closer) {
                    deque.offerFirst(new int[]{detours, nx, ny});
                } else {
                    deque.offerLast(new int[]{detours + 1, nx, ny});
                }
            }
        }
        return -1;
    }

    void test(Integer[][] forest, int expected) {
        assertEquals(expected, cutOffTree(Utils.toList(forest)));
        assertEquals(expected, cutOffTree2(Utils.toList(forest)));
        assertEquals(expected, cutOffTree3(Utils.toList(forest)));
    }

    @Test
    public void test() {
        test(new Integer[][] { { 1, 2, 3 }, { 0, 0, 4 }, { 7, 6, 5 } }, 6);
        test(new Integer[][] { { 69438, 55243, 0, 43779, 5241, 93591, 73380 },
                { 847, 49990, 53242, 21837, 89404, 63929, 48214 }, { 90332, 49751, 0, 3088, 16374, 70121, 25385 },
                { 14694, 4338, 87873, 86281, 5204, 84169, 5024 }, { 31711, 47313, 1885, 28332, 11646, 42583, 31460 },
                { 59845, 94855, 29286, 53221, 9803, 41305, 60749 }, { 95077, 50343, 27947, 92852, 0, 0, 19731 },
                { 86158, 63553, 56822, 90251, 0, 23826, 17478 }, { 60387, 23279, 78048, 78835, 5310, 99720, 0 },
                { 74799, 48845, 60658, 29773, 96129, 90443, 14391 }, { 65448, 63358, 78089, 93914, 7931, 68804, 72633 },
                { 93431, 90868, 55280, 30860, 59354, 62083, 47669 },
                { 81064, 93220, 22386, 22341, 95485, 20696, 13436 }, { 50083, 0, 89399, 43882, 0, 13593, 27847 },
                { 0, 12256, 33652, 69301, 73395, 93440, 0 }, { 42818, 87197, 81249, 33936, 7027, 5744, 64710 },
                { 35843, 0, 99746, 52442, 17494, 49407, 63016 }, { 86042, 44524, 0, 0, 26787, 97651, 28572 },
                { 54183, 83466, 96754, 89861, 84143, 13413, 72921 }, { 89405, 52305, 39907, 27366, 14603, 0, 14104 },
                { 70909, 61104, 70236, 30365, 0, 30944, 98378 }, { 20124, 87188, 6515, 98319, 78146, 99325, 88919 },
                { 89669, 0, 64218, 85795, 2449, 48939, 12869 }, { 93539, 28909, 90973, 77642, 0, 72170, 98359 },
                { 88628, 16422, 80512, 0, 38651, 50854, 55768 }, { 13639, 2889, 74835, 80416, 26051, 78859, 25721 },
                { 90182, 23154, 16586, 0, 27459, 3272, 84893 }, { 2480, 33654, 87321, 93272, 93079, 0, 38394 },
                { 34676, 72427, 95024, 12240, 72012, 0, 57763 }, { 97957, 56, 83817, 45472, 0, 24087, 90245 },
                { 32056, 0, 92049, 21380, 4980, 38458, 3490 }, { 21509, 76628, 0, 90430, 10113, 76264, 45840 },
                { 97192, 58807, 74165, 65921, 45726, 47265, 56084 }, { 16276, 27751, 37985, 47944, 54895, 80706, 2372 },
                { 28438, 53073, 0, 67255, 38416, 63354, 69262 }, { 23926, 75497, 91347, 58436, 73946, 39565, 10841 },
                { 34372, 69647, 44093, 62680, 32424, 69858, 68719 }, { 24425, 4014, 94871, 1031, 99852, 88692, 31503 },
                { 24475, 12295, 33326, 37771, 37883, 74568, 25163 }, { 0, 18411, 88185, 60924, 29028, 69789, 0 },
                { 34697, 75631, 7636, 16190, 60178, 39082, 7052 }, { 24876, 9570, 53630, 98605, 22331, 79320, 88317 },
                { 27204, 89103, 15221, 91346, 35428, 94251, 62745 }, { 26636, 28759, 12998, 58412, 38113, 14678, 0 },
                { 80871, 79706, 45325, 3861, 12504, 0, 4872 }, { 79662, 15626, 995, 80546, 64775, 0, 68820 },
                { 25160, 82123, 81706, 21494, 92958, 33594, 5243 } }, 5637);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
