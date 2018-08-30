import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC678: https://leetcode.com/problems/valid-parenthesis-string/
//
// Given a string containing only three types of characters: '(', ')' and '*', 
// check whether this string is valid.
// Any left parenthesis '(' must have a corresponding right parenthesis ')'.
// Any right parenthesis ')' must have a corresponding left parenthesis '('.
// Left parenthesis '(' must go before the corresponding right parenthesis ')'.
// '*' could be treated as a single right parenthesis ')' or a single left 
// parenthesis '(' or an empty string.
// An empty string is also valid.
public class ValidParenthesisString {
    // DFS + Recursion
    // beats 10.01%(318 ms for 58 tests)
    public boolean checkValidString(String s) {
        return check(s.toCharArray(), 0, 0);
    }
    
    private boolean check(char[] s, int index, int left) {
        if (index == s.length) return left == 0;
       
        if (s[index] != ')' && check(s, index + 1, left + 1)) return true;
        
        if (s[index] == '*' && check(s, index + 1, left)) return true;
 
        return (s[index] != '(' && left > 0 && check(s, index + 1, left - 1));
    }
    
    // DFS + Recursion + Dynamic Programming(Top-Down)
    // time complexity: O(N ^ 2), space complexity: O(N ^ 2)
    // beats 51.69%(4 ms for 58 tests)
    public boolean checkValidString2(String s) {
        int n = s.length();
        return check(s.toCharArray(), 0, 0, new Boolean[n][n]);
    }
    
    private boolean check(char[] s, int index, int bal, Boolean[][] memo) {
        if (index == s.length) return bal == 0;
      
        Boolean res = memo[index][bal];
        if (res != null) return res;

        res = false;
        if (s[index] != ')' && check(s, index + 1, bal + 1, memo)) {
            res = true;
        } else if (s[index] == '*' && check(s, index + 1, bal, memo)) {
            res = true;
        } else if (s[index] != '(' && bal > 0) {
            res = check(s, index + 1, bal - 1, memo);
        }
        return memo[index][bal] = res;
    }
    
    // Dynamic Programming(Bottom-Up)
    // time complexity: O(N ^ 3), space complexity: O(N ^ 2)
    // beats 30.04%(12 ms for 58 tests)
    public boolean checkValidString3(String s) {
        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        for (int len = 0; len < n; len++) {
            for (int i = 0, j = len; j < n; i++, j++) {
                if (s.charAt(i) == '*' && (i == j || dp[i + 1][j])) {
                    dp[i][j] = true;
                } else if (s.charAt(i) != ')') {
                    for (int k = i + 1; k <= j; k++) {
                        if (s.charAt(k) == '(') continue;

                        if (i + 1 < k && !dp[i + 1][k - 1]) continue;

                        if (k + 1 > j || dp[k + 1][j]) {
                            dp[i][j] = true;
                            break;
                        }
                    }
                }
            }
        }
        return n == 0 || dp[0][n - 1];
    }

    // Greedy
    // time complexity: O(N), space complexity: O(1)
    // beats 51.69%(4 ms for 58 tests)
    public boolean checkValidString4(String s) {
        int low = 0;
        int high = 0;
        for (char c : s.toCharArray()) {
            high += (c == ')') ? -1 : 1;
            if (high < 0) return false;

            low += (c == '(') ? 1 : -1;
            low = Math.max(low, 0);
        }
        return low == 0; 
    }

    // Stack
    // time complexity: O(N), space complexity: O(N)
    // beats 39.18%(5 ms for 58 tests)
    public boolean checkValidString5(String s) {
        Stack<Integer> leftStack = new Stack<>();
        Stack<Integer> starStack = new Stack<>();
        int i = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') {
                leftStack.push(i);
            } else if (c == '*') {
                starStack.push(i);
            } else {
                if (!leftStack.isEmpty()) {
                    leftStack.pop();
                } else if (!starStack.isEmpty()) {
                    starStack.pop();
                } else return false;
            }
            i++;
        }
        while (!leftStack.isEmpty() && !starStack.isEmpty()) {
            if (leftStack.pop() > starStack.pop()) return false;
        }
        return leftStack.isEmpty();
    }

    void test(String s, boolean expected) {
        assertEquals(expected, checkValidString(s));
        assertEquals(expected, checkValidString2(s));
        assertEquals(expected, checkValidString3(s));
        assertEquals(expected, checkValidString4(s));
        assertEquals(expected, checkValidString5(s));
    }

    @Test
    public void test() {
        test("", true);
        test("((", false);
        test("()", true);
        test("(*)", true);
        test("(*))", true);
        test("((((*)))*(***)))", true);
        test("(*)))", false);
        test(")(*", false);
        test("(*()", true);
        test("**((*", false);
        test("(())((())()()(*)(*()(())())())()()((()())((()))(*", false);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
