import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC682: https://leetcode.com/problems/baseball-game/
//
// You're now a baseball game point recorder.
// Given a list of strings, each string can be one of the 4 following types:
// 1. Integer (one round's score): Directly represents the number of points you
//    get in this round.
// 2. "+" (one round's score): Represents that the points you get in this round
//    are the sum of the last two valid round's points.
// 3. "D" (one round's score): Represents that the points you get in this round
//    are the doubled data of the last valid round's points.
// 4. "C" (an operation, which isn't a round's score): Represents the last valid
//    round's points you get were invalid and should be removed.
// Each round's operation is permanent and could have an impact on the round
// before and the round after.
// You need to return the sum of the points you could get in all the rounds.
public class BaseballGame {
    // Stack
    // beats 46.15%(10 ms for 39 tests)
    public int calPoints(String[] ops) {
        Stack <Integer> stack = new Stack <>();
        for (String op : ops) {
            switch (op) {
            case "+":
                int top = stack.pop();
                int sum = top + stack.peek();
                stack.push(top);
                stack.push(sum);
                break;
            case "D":
                stack.push(stack.peek() * 2);
                break;
            case "C":
                stack.pop();
                break;
            default:
                stack.push(Integer.valueOf(op));
                break;
            }
        }
        int sum = 0;
        for (int v : stack) {
            sum += v;
        }
        return sum;
    }

    // LinkedList
    // beats 74.05%(9 ms for 39 tests)
    public int calPoints2(String[] ops) {
        int sum = 0;
        LinkedList<Integer> list = new LinkedList<>();
        for (String op : ops) {
            if (op.equals("C")) {
                sum -= list.removeLast();
            } else if (op.equals("D")) {
                list.add(list.peekLast() * 2);
                sum += list.peekLast();
            } else if (op.equals("+")) {
                list.add(list.peekLast() + list.get(list.size() - 2));
                sum += list.peekLast();
            } else {
                list.add(Integer.parseInt(op));
                sum += list.peekLast();
            }
        }
        return sum;
    }

    void test(String[] ops, int expected) {
        assertEquals(expected, calPoints(ops));
        assertEquals(expected, calPoints2(ops));
    }

    @Test
    public void test() {
        test(new String[] {"5", "2", "C", "D", "+"}, 30);
        test(new String[] {"5", "-2", "4", "C", "D", "9", "+", "+"}, 27);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
