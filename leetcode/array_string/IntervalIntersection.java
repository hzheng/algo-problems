import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Interval;;

// LC986: https://leetcode.com/problems/interval-list-intersections/
//
// Given two lists of closed intervals, each list of intervals is pairwise
// disjoint and in sorted order.
// Return the intersection of these two interval lists.
public class IntervalIntersection {
    // 6 ms(97.57%), 34.6 MB(%) for tests
    public Interval[] intervalIntersection(Interval[] A, Interval[] B) {
        List<Interval> res = new ArrayList<>();
        for (int i = 0, j = 0; i < A.length && j < B.length;) {
            Interval a = A[i];
            Interval b = B[j];
            if ((b.start >= a.start && b.start <= a.end)
                || (a.start >= b.start && a.start <= b.end)) {
                res.add(new Interval((b.start >= a.start ? b : a).start, Math.min(a.end, b.end)));
                if (a.end > b.end) {
                    j++;
                } else {
                    i++;
                }
            } else if (b.start > a.start) {
                i++;
            } else {
                j++;
            }
        }
        return res.toArray(new Interval[0]);
    }

    // 6 ms(97.57%), 34.6 MB(%) for tests
    public Interval[] intervalIntersection2(Interval[] A, Interval[] B) {
        List<Interval> res = new ArrayList<>();
        for (int i = 0, j = 0; i < A.length && j < B.length;) {
            int lo = Math.max(A[i].start, B[j].start);
            int hi = Math.min(A[i].end, B[j].end);
            if (lo <= hi) {
                res.add(new Interval(lo, hi));
            }
            if (A[i].end < B[j].end) {
                i++;
            } else {
                j++;
            }
        }
        return res.toArray(new Interval[res.size()]);
    }

    void test(int[][] a, int[][] b, int[][] expected) {
        Interval[] A = Interval.array(a);
        Interval[] B = Interval.array(b);
        Interval[] exp = Interval.array(expected);
        assertArrayEquals(exp, intervalIntersection(A, B));
        assertArrayEquals(exp, intervalIntersection2(A, B));
    }

    @Test
    public void test() {
        test(new int[][] {{0, 2}, {5, 10}, {13, 23}, {24, 25}},
             new int[][] {{1, 5}, {8, 12}, {15, 24}, {25, 26}},
             new int[][] {{1, 2}, {5, 5}, {8, 10}, {15, 23}, {24, 24}, {25, 25}});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
