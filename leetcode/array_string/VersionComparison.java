import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC165: https://leetcode.com/problems/compare-version-numbers/
//
// Compare two version numbers version1 and version2.
public class VersionComparison {
    // beats 45.72%(3 ms)
    public int compareVersion(String version1, String version2) {
        String[] v1 = version1.split("\\.");
        String[] v2 = version2.split("\\.");
        int i = 0;
        for (; i < v1.length && i < v2.length; i++) {
            int n1 = Integer.parseInt(v1[i]);
            int n2 = Integer.parseInt(v2[i]);
            if (n1 > n2) return 1;

            if (n1 < n2) return -1;
        }

        if (v1.length == v2.length) return 0;

        if (v1.length > v2.length) {
            return isZero(v1, i) ? 0 : 1;
        }

        return isZero(v2, i) ? 0 : -1;
    }

    private boolean isZero(String[] version, int start) {
        for (int i = start; i < version.length; i++) {
            if (Integer.parseInt(version[i]) != 0) return false;
        }
        return true;
    }

    // beats 12.31%(4 ms for 71 tests)
    public int compareVersion2(String version1, String version2) {
        String[] v1 = version1.split("\\.");
        String[] v2 = version2.split("\\.");
        for (int i = 0; i < v1.length || i < v2.length; i++) {
            int num1 = i < v1.length ? Integer.parseInt(v1[i]) : 0;
            int num2 = i < v2.length ? Integer.parseInt(v2[i]) : 0;
            if (num1 < num2) return -1;
            if (num1 > num2) return 1;
        }
        return 0;
    }

    // Solution of Choice
    // beats 91.59%(0 ms)
    public int compareVersion3(String version1, String version2) {
        int len1 = version1.length();
        int len2 = version2.length();
        for (int i = 0, j = 0; i < len1 || j < len2; i++, j++) {
            int num1 = 0;
            while (i < len1 && version1.charAt(i) != '.') {
                num1 = num1 * 10 + version1.charAt(i++) - '0';
            }
            int num2 = 0;
            while (j < len2 && version2.charAt(j) != '.') {
                num2 = num2 * 10 + version2.charAt(j++) - '0';
            }
            if (num1 < num2) return -1;
            if (num1 > num2) return 1;
        }
        return 0;
    }

    void test(int expected, String v1, String v2) {
        assertEquals(expected, compareVersion(v1, v2));
        assertEquals(expected, compareVersion2(v1, v2));
        assertEquals(expected, compareVersion3(v1, v2));
    }

    @Test
    public void test1() {
        test(1, "1.1", "0.1");
        test(-1, "0.9", "0.12");
        test(0, "3.12", "3.12");
        test(1, "0.1", "0.0.1");
        test(0, "0.0.1.0.0", "0.0.1");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("VersionComparison");
    }
}
