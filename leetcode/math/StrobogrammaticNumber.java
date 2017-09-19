import org.junit.Test;
import static org.junit.Assert.*;

// LC246: https://leetcode.com/problems/strobogrammatic-number/
//
// A strobogrammatic number is a number that looks the same when rotated 180 degrees.
// Write a function to determine if a number is strobogrammatic.
public class StrobogrammaticNumber {
    // beats 57.40%(0 ms for 47 tests)
    public boolean isStrobogrammatic(String num) {
        char[] s = num.toCharArray();
        char[] map = {'0', '1', 0, 0, 0, 0, '9', 0, '8', '6'};
        for (int i = 0, j = s.length - 1; i <= j; i++, j--) {
            if (map[s[i] - '0'] != s[j]) return false;
        }
        return true;
    }

    // beats 57.40%(0 ms for 47 tests)
    public boolean isStrobogrammatic2(String num) {
        for (int i = 0, j = num.length() - 1; i <= j; i++, j--) {
            if (!"00 11 88 696".contains(num.charAt(i) + "" + num.charAt(j))) return false;
        }
        return true;
    }

    void test(String num, boolean expected) {
        assertEquals(expected, isStrobogrammatic(num));
        assertEquals(expected, isStrobogrammatic2(num));
    }

    @Test
    public void test() {
        test("6", false);
        test("1", true);
        test("0", true);
        test("101", true);
        test("8", true);
        test("9", false);
        test("61", false);
        test("69", true);
        test("96", true);
        test("88", true);
        test("818", true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("StrobogrammaticNumber");
    }
}
