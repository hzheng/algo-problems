import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1042: https://leetcode.com/problems/flower-planting-with-no-adjacent/
//
// You have N gardens, labelled 1 to N. In each garden, you want to plant one of 4 types of flowers.
// paths[i] = [x, y] describes the existence of a bidirectional path from garden x to garden y.
// Also, there is no garden that has more than 3 paths coming into or leaving it. Your task is to
// choose a flower type for each garden such that, for any two gardens connected by a path, they
// have different types of flowers. Return any such a choice as an array answer, where answer[i] is
// the type of flower planted in the (i+1)-th garden. The flower types are denoted 1, 2, 3, or 4.
// It is guaranteed an answer exists.
public class GardenNoAdjacent {
    // time complexity: O(N), space complexity: O(N)
    // 17 ms(%), 43.5 MB(100%) for 51 tests
    public int[] gardenNoAdj(int N, int[][] paths) {
        List<Integer>[] graph = new List[N];
        for (int i = 0; i < N; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] edge : paths) {
            int a = edge[0] - 1;
            int b = edge[1] - 1;
            graph[a].add(b);
            graph[b].add(a);
        }
        int[] res = new int[N];
        for (int i = 0; i < N; i++) {
            boolean[] colors = new boolean[5];
            for (int nei : graph[i]) {
                colors[res[nei]] = true;
            }
            for (int j = 1; j <= 4; j++) {
                if (!colors[j]) {
                    res[i] = j;
                    break;
                }
            }
        }
        return res;
    }

    void test(int N, int[][] paths, int[] expected) {
        assertArrayEquals(expected, gardenNoAdj(N, paths));
    }

    @Test
    public void test() {
        test(3, new int[][]{{1, 2}, {2, 3}, {3, 1}}, new int[]{1, 2, 3});
        test(4, new int[][]{{1, 2}, {3, 4}}, new int[]{1, 2, 1, 2});
        test(4, new int[][]{{1, 2}, {2, 3}, {3, 4}, {4, 1}, {1, 3}, {2, 4}}, new int[]{1, 2, 3, 4});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
