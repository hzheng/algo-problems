import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://code.google.com/codejam/contest/6254486/dashboard#s=p1
// Qualification Round 2016: Problem B - Revenge of the Pancakes
//
// A new kind of pancake has a happy face made of chocolate chips on one side
// (the "happy side"), and nothing on the other side (the "blank side").
// You are the head waiter on duty, and the kitchen has just given you a stack of
// pancakes to serve to a customer. Like any good pancake server, you have X-ray
// pancake vision, and you can see whether each pancake in the stack has the happy
// side up or the blank side up. You think the customer will be happiest if every
// pancake is happy side up when you serve them.
// You know the following maneuver: carefully lift up some number of pancakes
// from the top of the stack, flip that entire group over, and then put the group
// back down on top of any pancakes that you did not lift up. When flipping a group
// of pancakes, you flip the entire group in one motion; you do not individually
// flip each pancake. Formally: if we number the pancakes 1, 2, ..., N from top to bottom,
// you choose the top i pancakes to flip. Then, after the flip, the stack is i, i-1, ..., 2,
// 1, i+1, i+2, ..., N. Pancakes 1, 2, ..., i now have the opposite side up, whereas pancakes
// i+1, i+2, ..., N have the same side up that they had up before.
// You will not serve the customer until every pancake is happy side up, what is the
// smallest number of times you will need to execute the maneuver to get all the pancakes happy
// side up, if you make optimal choices?
// Input
// The first line of the input gives the number of test cases, T. T test cases follow.
// Each consists of one line with a string S, each character of which is either +
// (which represents a pancake that is initially happy side up) or - (which represents
// a pancake that is initially blank side up). The string, when read left to right,
// represents the stack when viewed from top to bottom.
// Output
// For each test case, output one line containing Case #x: y, where x is the test case number
// (starting from 1) and y is the minimum number of times you will need to execute
// the maneuver to get all the pancakes happy side up.
// Limits
// 1 ≤ T ≤ 100.
// Every character in S is either + or -.
// Small dataset
// 1 ≤ length of S ≤ 10.
// Large dataset
// 1 ≤ length of S ≤ 100.
public class PancakesRevenge {
    public static int flip(String pancakes) {
        char[] s = pancakes.toCharArray();
        int n = s.length;
        int count = 0;
        for (int i = 1; i < n; i++) {
            if (s[i] != s[i - 1]) {
                count++;
            }
        }
        return (s[n - 1] == '-') ? count + 1 : count;
    }

    // BFS + Queue
    // Time Limit Exceeded on large test set
    public static int flip2(String pancakes) {
        int len = pancakes.length();
        if (len == 0) return 0;

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.offer(pancakes);
        visited.add(pancakes);
        for (int level = 0; ; level++) {
            for (int count = queue.size(); count > 0; count--) {
                String s = queue.poll();
                boolean ok = true;
                for (int i = 0; i < len; i++) {
                    if (s.charAt(i) != '+') {
                        ok = false;
                        break;
                    }
                }
                if (ok) return level;

                for (int i = 0; i <= len; i++) {
                    StringBuilder sb = new StringBuilder(s.substring(0, i));
                    sb.reverse();
                    for (int j = 0; j < i; j++) {
                        sb.setCharAt(j, (sb.charAt(j) == '+') ? '-' : '+');
                    }
                    sb.append(s.substring(i));
                    String next = sb.toString();
                    if (visited.add(next)) {
                        queue.offer(next);
                    }
                }
            }
        }
    }

    void test(String pancakes, int expected) {
        assertEquals(expected, flip(pancakes));
        assertEquals(expected, flip2(pancakes));
    }

    @Test
    public void test() {
        test("--+-", 3);
        test("-", 1);
        test("-+", 1);
        test("+-", 2);
        test("+++", 0);
        test("--+----", 3);
        test("+-+----", 4);
        test("++-+++----", 4);
        test("++-++++---", 4);
        test("--+--++--", 5);
        test("--++--++--", 5);
        test("-+-+++-+-+", 7);
        test("+-+--+-++-", 8);
        test("+-+-++-+--+--+++", 10);
        // test("+-+-++-+--+--+++--++", 12);
    }

    private static Object getResult(String in) {
        return flip(in);
    }

    public static void main(String[] args) {
        // if (true) { // test without input file
        if (false) {
            org.junit.runner.JUnitCore.main("PancakesRevenge");
            return;
        }

        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        for (int i = 1; i <= t; i++) {
            System.out.format("Case #%d: ", i);
            System.out.println(getResult(in.next()));
        }
    }
}
