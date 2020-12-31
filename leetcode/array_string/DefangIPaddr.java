import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1108: https://leetcode.com/problems/defanging-an-ip-address/
//
// Given a valid (IPv4) IP address, return a defanged version of that IP address.
// A defanged IP address replaces every period "." with "[.]".
//
// Constraints: The given address is a valid IPv4 address.
public class DefangIPaddr {
    // time complexity: O(N), space complexity: O(N)
    // 0 ms(25.17%), 37.4 MB(29.12%) for 62 tests
    public String defangIPaddr(String address) {
        return address.replace(".", "[.]");
    }

    // time complexity: O(N), space complexity: O(N)
    // 3 ms(25.17%), 37.2 MB(52.93%) for 62 tests
    public String defangIPaddr2(String address) {
        return address.replaceAll("\\.", "[.]");
    }

    // time complexity: O(N), space complexity: O(N)
    // 1 ms(32.14%), 36.9 MB(88.21%) for 62 tests
    public String defangIPaddr3(String address) {
        return String.join("[.]", address.split("\\."));
    }

    // time complexity: O(N), space complexity: O(N)
    // 0 ms(25.17%), 37.4 MB(19.81%) for 62 tests
    public String defangIPaddr4(String address) {
        StringBuilder sb = new StringBuilder();
        for (char c : address.toCharArray()) {
            sb.append(c == '.' ? "[.]" : c);
        }
        return sb.toString();
    }

    private void test(String address, String expected) {
        assertEquals(expected, defangIPaddr(address));
        assertEquals(expected, defangIPaddr2(address));
        assertEquals(expected, defangIPaddr3(address));
        assertEquals(expected, defangIPaddr4(address));
    }

    @Test public void test() {
        test("1.1.1.1", "1[.]1[.]1[.]1");
        test("255.100.50.0", "255[.]100[.]50[.]0");
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
