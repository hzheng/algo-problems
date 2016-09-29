import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC089: https://leetcode.com/problems/gray-code/
//
// The gray code is a binary numeral system where two successive values differ
// in only one bit. Given a non-negative integer n representing the total number
// of bits in the code, print the sequence of gray code. A gray code sequence
// must begin with 0.
public class GrayCode {
    // Backtracking
    // beats 3.34%(12 ms)
    public List<Integer> grayCode(int n) {
        List<Integer> res = new ArrayList<>(1 << n);
        res.add(0);
        grayCode(n, res);
        return res;
    }

    private boolean grayCode(int n, List<Integer> res) {
        int count = res.size();
        if (count == (1 << n)) return true;

        int last = res.get(count - 1);
        for (int i = 0; i < n; i++) {
            int next = last ^ (1 << i); // flip
            if (!res.contains(next)) {
                res.add(next);
                return grayCode(n, res);
                // if (grayCode(n, res)) return true; // always succeed
                // res.remove(count); // unreachable
            }
        }
        return false;
    }

    // Recursion
    // beats 20.61%(2 ms)
    public List<Integer> grayCode2(int n) {
        List<Integer> res = new ArrayList<>(1 << n);
        if (n == 0) {
            res.add(0);
            return res;
        }

        List<Integer> last = grayCode2(n - 1);
        res.addAll(last);
        for(int i = last.size() - 1, mask = 1 << (n - 1); i >= 0; i--) {
            res.add(last.get(i) | mask);
        }
        return res;
    }

    // Solution of Choice
    // Iteration
    // beats 52.83%(1 ms)
    public List<Integer> grayCode3(int n) {
        List<Integer> res = new ArrayList<>(1 << n);
        res.add(0);
        for (int i = 0; i < n; i++) {
            for (int j = res.size() - 1, mask = 1 << i; j >= 0; j--) {
                res.add(res.get(j) | mask);
            }
        }
        return res;
    }

    // Solution of Choice
    // Bit Manipulation
    // https://en.wikipedia.org/wiki/Gray_code
    // beats 52.83%(1 ms)
    public List<Integer> grayCode4(int n) {
        int size = 1 << n;
        List<Integer> res = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            res.add((i >> 1) ^ i);
        }
        return res;
    }

    // Backtracking
    // https://discuss.leetcode.com/topic/31750/backtracking-c-solution
    // beats 52.83%(1 ms)
    public List<Integer> grayCode5(int n) {
        List<Integer> res = new ArrayList<>(1 << n);
        grayCode5(n, new int[1], res);
        return res;
    }

    private void grayCode5(int n, int[] num, List<Integer> res) {
        if (n == 0) {
            res.add(num[0]);
            return;
        }

        grayCode5(n - 1, num, res);
        num[0] ^= 1 << (n - 1);
        grayCode5(n - 1, num, res);
    }

    void test(int n, Integer ... expected) {
        assertArrayEquals(expected, grayCode(n).toArray(new Integer[0]));
        assertArrayEquals(expected, grayCode2(n).toArray(new Integer[0]));
        assertArrayEquals(expected, grayCode3(n).toArray(new Integer[0]));
        assertArrayEquals(expected, grayCode4(n).toArray(new Integer[0]));
        assertArrayEquals(expected, grayCode5(n).toArray(new Integer[0]));
    }

    @Test
    public void test1() {
        test(1, 0, 1);
        test(2, 0, 1, 3, 2);
        test(3, 0, 1, 3, 2, 6, 7, 5, 4);
        test(4, 0, 1, 3, 2, 6, 7, 5, 4, 12, 13, 15, 14, 10, 11, 9, 8);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("GrayCode");
    }
}
