import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 1.8:
 * Assume you have a method isSubstring which checks if one word is a substring
 * of another. Given two strings, s1 and s2, write code to check If s2 is a
 * rotation of s1 using only one call to isSubstring
 * (e.g., "waterbottLe" is a rotation of "erbottLewat").
 */
public class RotationCheck {
    boolean isRotation(String s1, String s2) {
        if (s1 == null || s2 == null
            || s1.length() != s2.length()) {
            return false;
        }
        return isSubstring(s2, s1 + s1);
    }

    boolean isSubstring(String s1, String s2) {
        return s2.indexOf(s1) >= 0;
    }

    void test(String s1, String s2, boolean expected) {
        assertEquals(expected, isRotation(s1, s2));
    }

    @Test
    public void test1() {
        test("a", "a", true);
    }

    @Test
    public void test2() {
        test("a", "", false);
    }

    @Test
    public void test3() {
        test("waterbottLe", "erbottLewat", true);
    }

    @Test
    public void test4() {
        test("erbottLewat", "waterbottLe", true);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("RotationCheck");
    }
}
