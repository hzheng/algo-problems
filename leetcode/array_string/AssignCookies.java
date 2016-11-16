import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC455: https://leetcode.com/problems/assign-cookies/
//
// Assume you are an awesome parent and want to give your children some cookies.
// But, you should give each child at most one cookie. Each child i has a greed
// factor gi, which is the minimum size of a cookie that the child will be
// content with; and each cookie j has a size sj. If sj >= gi, we can assign the
// cookie j to the child i, and the child i will be content. Your goal is to
// maximize the number of your content children and output the maximum number.
public class AssignCookies {
    // Heap
    // beats N/A(67 ms for 21 tests)
    public int findContentChildren(int[] g, int[] s) {
        PriorityQueue<Integer> greeds = new PriorityQueue<>();
        for (int greed : g) {
            greeds.offer(greed);
        }
        PriorityQueue<Integer> sizes = new PriorityQueue<>();
        for (int size : s) {
            sizes.offer(size);
        }
        int count = 0;
        while (!greeds.isEmpty() && !sizes.isEmpty()) {
            int greed = greeds.poll();
            while (!sizes.isEmpty()) {
                if (sizes.poll() >= greed) {
                    count++;
                    break;
                }
            }
        }
        return count;
    }

    // Sort + Two Pointers
    // beats N/A(19 ms for 21 tests)
    public int findContentChildren2(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);
        int count = 0;
        for (int i = 0; count < g.length && i < s.length; i++) {
            if (g[count] <= s[i]) {
                count++;
            }
        }
        return count;
    }

    void test(int[] g, int[] s, int expected) {
        assertEquals(expected, findContentChildren(g, s));
        assertEquals(expected, findContentChildren2(g, s));
    }

    @Test
    public void test() {
        test(new int[] {1, 2, 3}, new int[] {1, 1}, 1);
        test(new int[] {1, 2}, new int[] {1, 2, 3}, 2);
        test(new int[] {5, 4}, new int[] {7, 4, 3}, 2);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("AssignCookies");
    }
}
