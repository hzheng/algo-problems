import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// https://leetcode.com/problems/compare-version-numbers/
//
// Compare two version numbers version1 and version2.
public class VersionComparison {
    // beats 45.72%
    public int compareVersion(String version1, String version2) {
        String[] v1 = version1.split("\\.");
        String[] v2 = version2.split("\\.");
        int i = 0;
        for ( ; i < v1.length && i < v2.length; i++) {
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

    void test(int expected, String v1, String v2) {
        assertEquals(expected, compareVersion(v1, v2));
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
