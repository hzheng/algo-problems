import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC157: https://leetcode.com/problems/read-n-characters-given-read4/
//
// The API: int read4(char *buf) reads 4 characters at a time from a file.
// The return value is the actual number of characters read.
// By using the read4 API, implement the function int read(char *buf, int n) that
// reads n characters from the file.
public class ReadNChars {
    private static final String STREAM = "abcdefghijklmnopqrstuvwxyz";
    private static int offset = 0;

    public int read4(char[] buf) {
        int len = STREAM.length();
        for (int i = 0; i < 4; i++, offset++) {
            if (offset < len) {
                buf[i] = STREAM.charAt(offset);
            } else return i;
        }
        return 4;
    }

    // beats 5.35%(2 ms for 49 tests)
    public int read(char[] buf, int n) {
        char[] tmp = new char[4];
        for (int i = 0, nRead = 0; i < n; i += nRead) {
            nRead = read4(tmp);
            if (nRead == 0) return i;

            nRead = Math.min(nRead, n - i);
            System.arraycopy(tmp, 0, buf, i, nRead);
        }
        return n;
    }

    // beats 36.25%(1 ms for 49 tests)
    public int read2(char[] buf, int n) {
        char[] tmp = new char[4];
        for (int i = 0; i < n; i += 4) {
            int nRead = read4(tmp);
            System.arraycopy(tmp, 0, buf, i, Math.min(nRead, n - i));
            if (nRead < 4) return Math.min(i + nRead, n);
        }
        return n;
    }

    @FunctionalInterface
    interface Function<A, B, C> {
        public C apply(A a, B b);
    }

    void test(Function<char[], Integer, Integer> read, char[] buf, int n, int expected) {
        offset = 0;
        assertEquals(expected, (int)read.apply(buf, n));
        assertEquals(STREAM.substring(0, n), new String(buf, 0, n));
    }

    void test(char[] buf, int n, int expected) {
        ReadNChars r = new ReadNChars();
        test(r::read, buf, n, expected);
        test(r::read2, buf, n, expected);
    }

    @Test
    public void test() {
        char[] buf = new char[STREAM.length()];
        test(buf, 6, 6);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("ReadNChars");
    }
}
