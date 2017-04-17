import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC551: https://leetcode.com/problems/student-attendance-record-i/
//
// Given a string representing an attendance record for a student. The record only
// contains the following three characters:
// 'A' : Absent. 'L' : Late. 'P' : Present.
// A student could be rewarded if his attendance record doesn't contain more than one 'A'
// or more than two continuous 'L'.
// Return whether the student could be rewarded according to his attendance record.
public class AttendanceRecord {
    // beats N/A(9 ms for 113 tests)
    public boolean checkRecord(String s) {
        for (int i = 0, absent = 0, late = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == 'A') {
                if (++absent > 1) return false;
            } else if (c == 'L') {
                if (i > 0 && s.charAt(i - 1) == 'L') {
                    if (++late > 2) return false;
                } else {
                    late = 1;
                }
            }
        }
        return true;
    }

    // beats N/A(10 ms for 113 tests)
    public boolean checkRecord2(String s) {
        int late = 0;
        int absent = 0;
        for (char c : s.toCharArray()) {
            switch (c) {
            case 'A':
                if (++absent > 1) return false;
                late = 0;
                break;
            case 'L':
                if (++late > 2) return false;
                break;
            default:
                late = 0;
            }
        }
        return true;
    }

    // Regexp
    // beats N/A(17 ms for 113 tests)
    public boolean checkRecord3(String s) {
        return !s.matches(".*(LLL|A.*A).*");
    }

    // beats N/A(9 ms for 113 tests)
    public boolean checkRecord4(String s) {
        int absent = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == 'A') {
                if (++absent > 1) return false;
            }
        }
        return s.indexOf("LLL") < 0;
    }

    void test(String s, boolean expected) {
        assertEquals(expected, checkRecord(s));
        assertEquals(expected, checkRecord2(s));
        assertEquals(expected, checkRecord3(s));
        assertEquals(expected, checkRecord4(s));
    }

    @Test
    public void test() {
        test("LLLALL", false);
        test("PPALLP", true);
        test("PPALLL", false);
    }

    public static void main(String[] args) {
        String clazz = new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
