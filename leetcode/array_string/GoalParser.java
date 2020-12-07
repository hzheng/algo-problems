import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1678: https://leetcode.com/problems/goal-parser-interpretation/
//
// You own a Goal Parser that can interpret a string command. The command consists of an alphabet of
// "G", "()" and/or "(al)" in some order. The Goal Parser will interpret "G" as the string "G", "()"
// as the string "o", and "(al)" as the string "al". The interpreted strings are then concatenated
// in the original order.
// Given the string command, return the Goal Parser's interpretation of command.
//
// Constraints:
// 1 <= command.length <= 100
// command consists of "G", "()", and/or "(al)" in some order.
public class GoalParser {
    // time complexity: O(N), space complexity: O(N)
    // 4 ms(25.00%), 38.4 MB(25.00%) for 105 tests
    public String interpret(String command) {
        return command.replaceAll("\\(\\)", "o").replaceAll("\\(al\\)", "al");
    }

    // time complexity: O(N), space complexity: O(1)
    // 0 ms(100.00%), 37.3 MB(75.00%) for 105 tests
    public String interpret2(String command) {
        StringBuilder sb = new StringBuilder();
        for (int n = command.length(), i = 0; i < n; i++) {
            char cur = command.charAt(i);
            if (cur != '(') {
                sb.append(cur);
            } else if (command.charAt(i + 1) == ')') {
                sb.append("o");
                i++;
            } else {
                sb.append("al");
                i += 3;
            }
        }
        return sb.toString();
    }

    private void test(String command, String expected) {
        assertEquals(expected, interpret(command));
        assertEquals(expected, interpret2(command));
    }

    @Test public void test() {
        test("G()(al)", "Goal");
        test("G()()()()(al)", "Gooooal");
        test("(al)G(al)()()G", "alGalooG");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
