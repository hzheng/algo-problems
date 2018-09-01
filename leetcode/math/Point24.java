import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC679: https://leetcode.com/problems/24-game/
//
// You have 4 cards each containing a number from 1 to 9. You need to judge
// whether they could operated through *, /, +, -, (, ) to get the value of 24.
public class Point24 {
    static final int TARGET = 24;
    private static final double EPSILON = 1E-8;

    // beats 88.26%(8 ms for 70 tests)
    public boolean judgePoint24(int[] nums) {
        for (int i = 0; i < 3; i++) {
            for (int j = i + 1; j < 4; j++) {
                double a = nums[i];
                double b = nums[j];
                double[] cands = new double[3];
                for (int k = 0, index = 1; k < 4; k++) {
                    if (k != i && k != j) {
                        cands[index++] = nums[k];
                    }
                }
                cands[0] = a + b;
                if (judge3(cands)) return true;

                cands[0] = a * b;
                if (judge3(cands)) return true;

                cands[0] = a - b;
                if (judge3(cands)) return true;

                cands[0] = b - a;
                if (judge3(cands)) return true;

                if (Math.abs(a) > EPSILON) {
                    cands[0] = b / a;
                    if (judge3(cands)) return true;
                }
                if (Math.abs(b) > EPSILON) {
                    cands[0] = a / b;
                    if (judge3(cands)) return true;
                }
            }
        }
        return false;
    }

    private boolean judge3(double[] nums) {
        for (int i = 0; i < 3; i++) {
            double a = nums[i];
            double b = nums[(i + 1) % 3];
            double c = nums[(i + 2) % 3];
            if (judge(a + b, c) || judge(a * b, c)) return true;
            if (judge(a - b, c) || judge(b - a, c)) return true;

            if (Math.abs(a) > EPSILON && judge(b / a, c)) return true;
            if (Math.abs(b) > EPSILON && judge(a / b, c)) return true;
        }
        return false;
    }

    private boolean judge(double a, double b) {
        if (Math.abs(a + b - TARGET) < EPSILON) return true;
        if (Math.abs(a * b - TARGET) < EPSILON) return true;
        if (Math.abs(a - b - TARGET) < EPSILON) return true;
        if (Math.abs(b - a - TARGET) < EPSILON) return true;
        if (Math.abs(a) < EPSILON || Math.abs(b) < EPSILON) return false;
        return (Math.abs(a / b - TARGET) < EPSILON) ||
               (Math.abs(b / a - TARGET) < EPSILON);
    }

    // Stack
    // beats 5.45%(56 ms for 70 tests)
    public boolean judgePoint24_2(int[] nums) {
        return check(nums);
    }

    private static final char[] OPERATORS = "+-*/".toCharArray();

    private boolean check(int[] nums) {
        int n = nums.length;
        int[] indices = new int[n];
        for (int i = 0; i < n; i++) {
            indices[i] = i;
        }
        char[] buf = new char[10];
        do {
            buf[0] = (char) ('0' + nums[indices[0]]);
            buf[1] = (char) ('0' + nums[indices[1]]);
            buf[3] = (char) ('0' + nums[indices[2]]);
            buf[6] = (char) ('0' + nums[indices[3]]);
            for (char a : OPERATORS) {
                for (char b : OPERATORS) {
                    for (char c : OPERATORS) {
                        buf[9] = c;

                        buf[2] = a;
                        buf[4] = b;
                        if (check(buf)) return true;

                        buf[4] = 0;
                        buf[7] = b;
                        if (check(buf)) return true;

                        buf[2] = 0;
                        buf[7] = 0;
                        buf[4] = a;
                        buf[5] = b;
                        if (check(buf)) return true;

                        buf[5] = 0;
                        buf[7] = b;
                        if (check(buf)) return true;

                        buf[4] = 0;
                        buf[7] = a;
                        buf[8] = b;
                        if (check(buf)) return true;

                        buf[7] = 0;
                        buf[8] = 0;
                    }
                }
            }
        } while (next(indices));
        return false;
    }

    private boolean check(char[] buf) {
        Stack<Double> stack = new Stack<>();
        for (char c : buf) {
            switch (c) {
            case 0:
                continue;
            case '+':
                stack.push(stack.pop() + stack.pop());
                break;
            case '-':
                stack.push(-stack.pop() + stack.pop());
                break;
            case '*':
                stack.push(stack.pop() * stack.pop());
                break;
            case '/':
                if (Math.abs(stack.peek()) < EPSILON) return false;
                stack.push(1 / stack.pop() * stack.pop());
                break;
            default:
                stack.push((double) (c - '0'));
            }
        }
        return Math.abs(stack.peek() - TARGET) < EPSILON;
    }

    private boolean next(int[] indices) {
        int n = indices.length;
        int i = n - 1;
        for (; i > 0 && indices[i] < indices[i - 1]; i--) ;
        if (i == 0) return false;

        int j = n - 1;
        for (; indices[j] < indices[i - 1]; j--) {}
        swap(indices, i - 1, j);
        for (j = n - 1; j > i; i++, j--) {
            swap(indices, i, j);
        }
        return true;
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    void test(int[] nums, boolean expected) {
        assertEquals(expected, judgePoint24(nums));
        assertEquals(expected, judgePoint24_2(nums));
    }

    @Test
    public void test() {
        test(new int[] { 4, 1, 8, 7 }, true);
        test(new int[] { 1, 2, 1, 2 }, false);
        test(new int[] { 8, 1, 6, 6 }, true);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
