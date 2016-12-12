import java.util.*;
import java.util.regex.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC468: https://leetcode.com/problems/validate-ip-address/
//
// Write a function to check whether a input string is a valid IPv4 address or
// IPv6 address or neither.
public class ValidIP {
    // beats N/A(16 ms for 95 tests)
    public String validIPAddress(String IP) {
        if (IP.contains(".")) {
            if (isIPv4(IP)) return "IPv4";
        } else if (isIPv6(IP)) return "IPv6";
        return "Neither";
    }

    private boolean isIPv4(String s) {
        String[] segments = s.split("\\.");
        if (segments.length != 4 || s.endsWith(".")) return false;

        for (String seg : segments) {
            try {
                int i = Integer.parseInt(seg);
                if (i < 0 || i > 255 || (i == 0 && seg.length() > 1)
                    || (i > 0 && seg.charAt(0) == '0')) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    private boolean isIPv6(String s) {
        String[] segments = s.split(":");
        if (segments.length != 8 || s.endsWith(":")) return false;

        for (String seg : segments) {
            if (!seg.matches("[0-9a-fA-F]{1,4}")) return false;
        }
        return true;
    }

    // beats N/A(21 ms for 95 tests)
    public String validIPAddress2(String IP) {
        if (IP.contains(".")) {
            if (isIPv4_2(IP)) return "IPv4";
        } else if (isIPv6(IP)) return "IPv6";
        return "Neither";
    }

    private boolean isIPv4_2(String s) {
        if (!s.matches("((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)")) {
            return false;
        }
        return !s.matches("^0[^.].*") && !s.matches(".*\\.0[^.].*");
    }

    // beats N/A(23 ms for 95 tests)
    public String validIPAddress3(String IP) {
        if (IP.contains(".")) {
            if (("." + IP).matches("(\\.(([1-9]?|1\\d|2[0-4])\\d|25[0-5])){4}")) return "IPv4";
        } else if ((":" + IP).matches("(:[0-9a-fA-F]{1,4}){8}")) return "IPv6";
        return "Neither";
    }

    // beats N/A(14 ms for 95 tests)
    public String validIPAddress4(String IP) {
        if (IP.contains(".")) {
            if (isIPv4_4(IP)) return "IPv4";
        } else if (IP.matches("[0-9a-fA-F]{1,4}(:[0-9a-fA-F]{1,4}){7}")) return "IPv6";
        return "Neither";
    }

    private static final Pattern IPV4_PAT = Pattern.compile("\\.([1-9]\\d{0,2}|0)");

    private boolean isIPv4_4(String s) {
        Matcher m = IPV4_PAT.matcher("." + s);
        int groups = 0;
        int end = 0;
        for (; m.find(); groups++) {
            if (m.start() != end) return false;

            end = m.end();
            int val = Integer.parseInt(m.group(1));
            if (val > 255) return false;
        }
        return groups == 4 && end == s.length() + 1;
    }

    void test(String IP, String expected) {
        assertEquals(expected, validIPAddress(IP));
        assertEquals(expected, validIPAddress2(IP));
        assertEquals(expected, validIPAddress3(IP));
        assertEquals(expected, validIPAddress4(IP));
    }

    @Test
    public void test() {
        test("172.16.254.1", "IPv4");
        test("100.16.254.1", "IPv4");
        test("172.16.254.01", "Neither");
        test("172.16.254.3.", "Neither");
        test("172.16.254", "Neither");
        test("2001:0db8:85a3:0:0:8A2E:0370:7334", "IPv6");
        test("255.256.254.253", "Neither");
        test("256.256.256.256", "Neither");
        test("2001:0db8:85a3:0:0:8A2E:0370:7334:", "Neither");
        test("2001:0db8:85a3:0:0:8A2E:0370:7334:1111", "Neither");
        test("00.0.0.0", "Neither");
        test("01.0.0.0", "Neither");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ValidIP");
    }
}
