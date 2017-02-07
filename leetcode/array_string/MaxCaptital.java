import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC502: https://leetcode.com/problems/ipo/?tab=Description
//
// You are given several projects. For each project i, it has a pure profit Pi
// and a minimum capital of Ci is needed to start the corresponding project.
// Initially, you have W capital. When you finish a project, you will obtain its
// pure profit and the profit will be added to your total capital.
// To sum up, pick a list of at most k distinct projects from given projects to
// maximize your final capital, and output your final maximized capital.
public class MaxCaptital {
    // Heap
    // beats 95.35%(38 ms for 31 tests)
    public int findMaximizedCapital(int k, int W, int[] Profits, int[] Capital) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) { return b[0] - a[0]; }
        });
        for (int i = 0; i < Profits.length; i++) {
            pq.offer(new int[] {Profits[i], Capital[i]});
        }
        int capital = W;
        for (int i = 0; i < k; i++) {
            List<int[]> tmp = new ArrayList<>();
            while (!pq.isEmpty()) {
                int[] prj = pq.poll();
                if (capital >= prj[1]) {
                    capital += prj[0];
                    break;
                }
                tmp.add(prj);
            }
            for (int[] p : tmp) { // add back
                pq.offer(p);
            }
        }
        return capital;
    }

    // Two Heaps or Heap + Sort
    // beats 89.15%(48 ms for 31 tests)
    public int findMaximizedCapital2(int k, int W, int[] Profits, int[] Capital) {
        // int n = Profits.length;
        // int[][] projects = new int[n][2];
        // for (int i = 0; i < n; i++) {
        //     projects[i] = new int[] {Profits[i], Capital[i]};
        // }
        // Arrays.sort(projects, new Comparator<int[]>() {
        //     public int compare(int[] a, int[] b) { return a[1] - b[1]; }
        // });
        PriorityQueue<int[]> prjs = new PriorityQueue<>(new Comparator<int[]>() {
            public int compare(int[] a, int[] b) { return a[1] - b[1]; }
        });
        for (int i = 0; i < Profits.length; i++) {
            prjs.offer(new int[] {Profits[i], Capital[i]});
        }
        PriorityQueue<Integer> profits = new PriorityQueue<>(new Comparator<Integer>() {
            public int compare(Integer a, Integer b) { return b - a; }
        });
        for (int count = k; count > 0; count--, W += profits.poll()) {
            while (!prjs.isEmpty() && prjs.peek()[1] <= W) {
                profits.offer(prjs.poll()[0]);
            }
            if (profits.isEmpty()) break;
        }
        return W;
    }

    void test(int k, int W, int[] Profits, int[] Capital, int expected) {
        assertEquals(expected, findMaximizedCapital(k, W, Profits, Capital));
        assertEquals(expected, findMaximizedCapital2(k, W, Profits, Capital));
    }

    @Test
    public void test() {
        test(2, 0, new int[] {1, 2, 3}, new int[] {0, 1, 1}, 4);
        test(10, 0, new int[] {1, 2, 3}, new int[] {0, 1, 2}, 6);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("MaxCaptital");
    }
}
