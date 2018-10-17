import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC913: https://leetcode.com/problems/cat-and-mouse/
//
// A game on an undirected graph is played by two players, Mouse and Cat, who
// alternate turns. The graph is given as follows: graph[a] is a list of all
// nodes b such that ab is an edge of the graph. Mouse starts at node 1 and goes
// first, Cat starts at node 2 and goes second, and there is a Hole at node 0.
// During each player's turn, they must travel along one edge of the graph that
// meets where they are.  For example, if the Mouse is at node 1, it must travel
// to any node in graph[1]. Additionally, it is not allowed for the Cat to
// travel to the Hole (node 0.) Then, the game can end in 3 ways:
// If ever the Cat occupies the same node as the Mouse, the Cat wins.
// If ever the Mouse reaches the Hole, the Mouse wins.
// If ever a position is repeated, the game is a draw.
// Given a graph, and assuming both players play optimally, return 1 if the game
// is won by Mouse, 2 if the game is won by Cat, and 0 if the game is a draw.
// Note:

// 3 <= graph.length <= 50
// It is guaranteed that graph[1] is non-empty.
// It is guaranteed that graph[2] contains a non-zero element. 
public class CatMouseGame {
    // DFS + Recursion
    // time complexity: O(N ^ 3), space complexity: O(N ^ 2)
    // beats 100.00%(4 ms for 45 tests)
    public int catMouseGame(int[][] graph) {
        int n = graph.length;
        return dfs(graph, 1, 2, new int[n][n]) - 1;
    }

    private int dfs(int[][] graph, int mouse, int cat, int dp[][]) {
        if (mouse == 0) return 2; // mouse win
        if (mouse == cat) return 3; // cat win

        if (dp[mouse][cat] != 0) return dp[mouse][cat];

        dp[mouse][cat] = 1; // draw if come back
        int mouseBest = 3;
        for (int mouseNext : graph[mouse]) {
            if (mouseNext == cat) continue;

            int catBest = 2;
            for (int catNext : graph[cat]) {
                if (catNext == 0) continue;

                int res = dfs(graph, mouseNext, catNext, dp);
                if (res == 3) {
                    catBest = 3;
                    break;
                } else if (res == 1) {
                    catBest = 1;
                }
            }
            if (catBest == 2) {
                mouseBest = 2;
                break;
            } else if (catBest == 1) {
                mouseBest = 1;
            }
        }
        return dp[mouse][cat] = mouseBest;
    }

    // Topological Sort
    // time complexity: O(N ^ 3), space complexity: O(N ^ 2)
    // beats 21.52%(46 ms for 45 tests)
    public int catMouseGame2(int[][] graph) {
        int n = graph.length;
        final int MOUSE = 1;
        final int CAT = 2;
        int[][][] color = new int[n][n][3];
        int[][][] degree = new int[n][n][3];
        for (int m = 0; m < n; m++) {
            for (int c = 0; c < n; c++) {
                degree[m][c][1] = graph[m].length;
                degree[m][c][2] = graph[c].length;
                for (int x : graph[c]) {
                    if (x == 0) {
                        degree[m][c][2]--;
                        break;
                    }
                }
            }
        }
        Queue<int[]> queue = new LinkedList<>();
        for (int cat = 0; cat < n; cat++) {
            for (int turn = 1; turn <= 2; turn++) {
                color[0][cat][turn] = MOUSE;
                queue.offer(new int[] {0, cat, turn, MOUSE});
                if (cat > 0) {
                    color[cat][cat][turn] = CAT;
                    queue.offer(new int[] {cat, cat, turn, CAT});
                }
            }
        }
        while (!queue.isEmpty()) {
            int[] node = queue.poll();
            int mouse = node[0];
            int cat = node[1];
            int turn = node[2];
            int res = node[3];
            for (int[] parent : parents(graph, mouse, cat, turn)) {
                int preMouse = parent[0];
                int preCat = parent[1];
                int preTurn = parent[2];
                if (color[preMouse][preCat][preTurn] > 0) continue; // visited

                if (preTurn == res) { // one child wins (Immediate coloring)
                    color[preMouse][preCat][preTurn] = res;
                    queue.offer(new int[] {preMouse, preCat, preTurn, res});
                } else if (--degree[preMouse][preCat][preTurn] == 0) { 
                    // all children lose (Eventual coloring)
                    color[preMouse][preCat][preTurn] = 3 - preTurn;
                    queue.offer(new int[] {preMouse, preCat, preTurn, 3 - preTurn});
                }
            }
        }
        return color[1][2][1];
    }

    private List<int[]> parents(int[][] graph, int mouse, int cat, int turn) {
        List<int[]> res = new ArrayList<>();
        if (turn == 2) {
            for (int m2 : graph[mouse]) {
                res.add(new int[] {m2, cat, 3 - turn});
            }
        } else {
            for (int c2 : graph[cat]) {
                if (c2 > 0) {
                    res.add(new int[] {mouse, c2, 3 - turn});
                }
            }
        }
        return res;
    }

    void test(int[][] graph, int expected) {
        assertEquals(expected, catMouseGame(graph));
        assertEquals(expected, catMouseGame2(graph));
    }

    @Test
    public void test() {
        test(new int[][] {{2, 3}, {3, 4}, {0, 4}, {0, 1}, {1, 2}}, 1);
        test(new int[][] {{2, 5}, {3}, {0, 4, 5}, {1, 4, 5}, {2, 3}, {0, 2, 3}}, 0);
        test(new int[][] {{2, 3, 4}, {4}, {0, 3}, {0, 2}, {0, 1}}, 1);
        test(new int[][] {{6}, {4}, {9}, {5}, {1, 5}, {3, 4, 6}, {0, 5, 10}, {8, 9, 10}, {7},
                {2, 7}, {6, 7}}, 1);
        test(new int[][] {{2, 9, 14}, {2, 5, 7}, {0, 1, 3, 8, 9, 11, 14}, {2, 12}, {5, 11, 18},
                {1, 4, 18}, {9, 17, 19}, {1, 11, 12, 13, 14, 17, 19}, {2, 16, 17},
                {0, 2, 6, 12, 14, 18}, {14}, {2, 4, 7}, {3, 7, 9, 13}, {7, 12, 14},
                {0, 2, 7, 9, 10, 13, 17}, {18}, {8, 19}, {6, 7, 8, 14, 19}, {4, 5, 9, 15},
                {6, 7, 16, 17}}, 0);
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
