import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC547: https://leetcode.com/problems/friend-circles
//
// There are N students in a class. Some of them are friends, while some are not.
// Their friendship is transitive in nature. And we defined a friend circle is a group
// of students who are direct or indirect friends.
// Given a N*N matrix M representing the friend relationship between students in the class.
// If M[i][j] = 1, then the ith and jth students are direct friends with each other,
// otherwise not. And you have to output the total number of friend circles among all the students.
public class FriendCircles {
    // Union Find
    // beats N/A(12 ms for 113 tests)
    // time complexity: O(N ^ 2), space complexity: O(N)
    public int findCircleNum(int[][] M) {
        int n = M.length;
        int[] id = new int[n];
        for (int i = 0; i < n; i++) {
            id[i] = i;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (M[i][j] == 0) continue;

                int p = i;
                for (; id[p] != p; p = id[p] = id[id[p]]) {}
                int q = j;
                for (; id[q] != q; q = id[q] = id[id[q]]) {}
                id[q] = p;
            }
        }
        int count = 0;
        for (int i = 0; i < n; i++) {
            count += (id[i] == i) ? 1 : 0;
        }
        return count;
    }

    // Union Find
    // beats N/A(28 ms for 113 tests)
    // time complexity: O(N ^ 2), space complexity: O(N)
    public int findCircleNum_2(int[][] M) {
        int n = M.length;
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (M[i][j] == 1 && i != j) {
                    union(parent, i, j);
                }
            }
        }
        int count = 0;
        for (int i = 0; i < n; i++) {
            count += (parent[i] == -1) ? 1 : 0;
        }
        return count;
    }

    private int find(int parent[], int i) {
        return parent[i] == -1 ? i : find(parent, parent[i]);
    }

    private void union(int parent[], int x, int y) {
        int setX = find(parent, x);
        int setY = find(parent, y);
        if (setX != setY) {
            parent[setX] = setY;
        }
    }

    // Recursion + DFS + Hash Table
    // time complexity: O(N), space complexity: O(N)
    // beats N/A(11 ms for 113 tests)
    public int findCircleNum2(int[][] M) {
        int n = M.length;
        int count = 0;
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                count++;
                dfs(M, visited, i);
            }
        }
        return count;
    }

    private void dfs(int[][] M, boolean[] visited, int i) {
        int n = M.length;
        visited[i] = true;
        for (int j = 0; j < n; j++) {
            if (!visited[j] && M[i][j] == 1) {
                dfs(M, visited, j);
            }
        }
    }

    // Queue + BFS + Hash Table
    // time complexity: O(N), space complexity: O(N)
    // beats N/A(19 ms for 113 tests)
    public int findCircleNum3(int[][] M) {
        int count = 0;
        int n = M.length;
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (visited[i]) continue;

            count++;
            for (queue.offer(i); !queue.isEmpty(); ) {
                int cur = queue.poll();
                for (int j = 0; j < n; j++) {
                    if (!visited[j] && M[cur][j] == 1) {
                        visited[j] = true;
                        queue.offer(j);
                    }
                }
            }
        }
        return count;
    }

    void test(int[][] M, int expected) {
        assertEquals(expected, findCircleNum(M));
        assertEquals(expected, findCircleNum_2(M));
        assertEquals(expected, findCircleNum2(M));
        assertEquals(expected, findCircleNum3(M));
    }

    @Test
    public void test() {
        test(new int[][] {{1, 1, 0}, {1, 1, 0}, {0, 0, 1}}, 2);
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
