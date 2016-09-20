import java.util.*;
import java.util.function.Function;

import org.junit.Test;
import static org.junit.Assert.*;

// LC401: https://leetcode.com/problems/binary-watch/
//
// A binary watch has 4 LEDs on the top which represent the hours (0-11), and
// the 6 LEDs on the bottom represent the minutes (0-59).
// Each LED represents a zero or one, with the least significant bit on the right.
// Given a non-negative integer n which represents the number of LEDs that are
// currently on, return all possible times the watch could represent.
// Note:
// The order of output does not matter.
// The hour must not contain a leading zero
// The minute must be consist of two digits and may contain a leading zero
public class BinaryWatch {
    // Backtracking + Bit Manipulation
    // beats 18.58%(31 ms)
    public List<String> readBinaryWatch(int num) {
        List<String> res = new ArrayList<>();
        readBinaryWatch(num, 0, 10, res);
        return res;
    }

    private void readBinaryWatch(int num, int bits, int start, List<String> res) {
        if (start < num) return;

        if (num == 0) {
            String time = convertTime(bits);
            if (time != null) {
                res.add(time);
            }
            return;
        }

        readBinaryWatch(num - 1, bits | (1 << (start - 1)), start - 1, res);
        readBinaryWatch(num, bits, start - 1, res);
    }

    private String convertTime(int n) {
        int hour = n >> 6;
        if (hour > 11) return null;

        int min = n & 0x3F;
        if (min > 59) return null;

        return String.format("%d:%02d", hour, min);
    }

    // beats 16.70%(32 ms)
    public List<String> readBinaryWatch2(int num) {
        List<String> res = new ArrayList<>();
        for (int h = 0; h < 12; h++) {
            for (int m = 0; m < 60; m++) {
                if (Integer.bitCount((h << 6) + m) == num) {
                    res.add(String.format("%d:%02d", h, m));
                }
            }
        }
        return res;
    }

    void test(Function<Integer, List<String> > readBinaryWatch, int num, String ... expected) {
        Arrays.sort(expected);
        String[] res = readBinaryWatch.apply(num).toArray(new String[0]);
        Arrays.sort(res);
        assertArrayEquals(expected, res);
    }

    void test(int num, String ... expected) {
        BinaryWatch b = new BinaryWatch();
        test(b::readBinaryWatch, num, expected);
        test(b::readBinaryWatch2, num, expected);
    }

    @Test
    public void test1() {
        test(1, "1:00", "2:00", "4:00", "8:00", "0:01", "0:02", "0:04", "0:08",
             "0:16", "0:32");
        test(2, "0:03", "0:05", "0:06", "0:09", "0:10", "0:12", "0:17", "0:18",
             "0:20", "0:24", "0:33", "0:34", "0:36", "0:40", "0:48", "1:01",
             "1:02", "1:04", "1:08", "1:16", "1:32", "2:01", "2:02", "2:04",
             "2:08", "2:16", "2:32", "3:00", "4:01", "4:02", "4:04", "4:08",
             "4:16", "4:32", "5:00", "6:00", "8:01", "8:02", "8:04", "8:08",
             "8:16", "8:32", "9:00", "10:00");
        test(3, "0:07", "0:11", "0:13", "0:14", "0:19", "0:21", "0:22", "0:25",
             "0:26", "0:28", "0:35", "0:37", "0:38", "0:41", "0:42", "0:44",
             "0:49", "0:50", "0:52", "0:56", "1:03", "1:05", "1:06", "1:09",
             "1:10", "1:12", "1:17", "1:18", "1:20", "1:24", "1:33", "1:34",
             "1:36", "1:40", "1:48", "2:03", "2:05", "2:06", "2:09", "2:10",
             "2:12", "2:17", "2:18", "2:20", "2:24", "2:33", "2:34", "2:36",
             "2:40", "2:48", "3:01", "3:02", "3:04", "3:08", "3:16", "3:32",
             "4:03", "4:05", "4:06", "4:09", "4:10", "4:12", "4:17", "4:18",
             "4:20", "4:24", "4:33", "4:34", "4:36", "4:40", "4:48", "5:01",
             "5:02", "5:04", "5:08", "5:16", "5:32", "6:01", "6:02", "6:04",
             "6:08", "6:16", "6:32", "7:00", "8:03", "8:05", "8:06", "8:09",
             "8:10", "8:12", "8:17", "8:18", "8:20", "8:24", "8:33", "8:34",
             "8:36", "8:40", "8:48", "9:01", "9:02", "9:04", "9:08", "9:16",
             "9:32", "10:01", "10:02", "10:04", "10:08", "10:16", "10:32", "11:00");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("BinaryWatch");
    }
}
