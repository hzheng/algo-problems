import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC386: https://leetcode.com/problems/lexicographical-numbers/
//
// Given an integer n, return 1 - n in lexicographical order.
public class LexicalOrder {
    // naive method, which is mainly for unit test
    public List<Integer> lexicalOrder0(int n) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(new Comparator<Integer>() {
            public int compare(Integer a, Integer b) {
                return a.toString().compareTo(b.toString());
            }
        } );
        for (int i = 1; i <= n; i++) {
            pq.offer(i);
        }
        List<Integer> res = new ArrayList<>();
        while (!pq.isEmpty()) {
            res.add(pq.poll());
        }
        return res;
    }

    // beats N/A(166 ms)
    public List<Integer> lexicalOrder(int n) {
        List<Integer> res = new ArrayList<>();
        for (int i = 1, count = 0, limit = n / 10; count < n; count++) {
            res.add(i);
            if (i <= limit) { // append 0's when possible
                i *= 10;
            } else {
                if (i == n) {
                    i /= 10;
                }
                // remove trailing 0's if any
                for (++i; i % 10 == 0; i /= 10) {}
            }
        }
        return res;
    }

    void test(int n, Integer ... expected) {
        assertArrayEquals(expected, lexicalOrder0(n).toArray(new Integer[0]));
    }

    @Test
    public void test1() {
        test(13, 1, 10, 11, 12, 13, 2, 3, 4, 5, 6, 7, 8, 9);
        test(30, 1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 2, 20, 21, 22, 23,
             24, 25, 26, 27, 28, 29, 3, 30, 4, 5, 6, 7, 8, 9);
    }

    void test(int n) {
        Integer[] res = lexicalOrder0(n).toArray(new Integer[0]);
        assertArrayEquals(res, lexicalOrder(n).toArray(new Integer[0]));
    }

    @Test
    public void test2() {
        test(0);
        test(1);
        test(13);
        test(50);
        test(100);
        test(1000);
        test(23489);
        test(123456);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LexicalOrder");
    }
}
