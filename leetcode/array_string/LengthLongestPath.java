import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC388: https://leetcode.com/problems/longest-absolute-file-path/
//
// Suppose we abstract our file system by a string in the following manner:
// The string "dir\n\tsubdir1\n\tsubdir2\n\t\tfile.ext" represents:
// dir
//     subdir1
//     subdir2
//         file.ext
// Given a string representing the file system in the above format, return the
// length of the longest absolute path to file in the abstracted file system.
// If there is no file in the system, return 0.
// Note:
// The name of a file contains at least a '.' and an extension.
// The name of a directory or sub-directory will not contain a '.'.
public class LengthLongestPath {
    // Stack
    // beats 61.18%(4 ms for 25 tests)
    public int lengthLongestPath(String input) {
        Stack<Integer> dirs = new Stack<>();
        int maxPath = 0;
        for (String line : input.split("\n")) {
            int tabs = line.lastIndexOf('\t') + 1;
            while (tabs < dirs.size()) { // invalid if tabs > dirs.size()
                dirs.pop();
            }
            int path = (dirs.isEmpty() ? 0 : dirs.peek()) + line.length() - tabs;
            if (line.indexOf('.') < 0) {
                dirs.push(path + 1); // add "/"
            } else {
                maxPath = Math.max(maxPath, path);
            }
        }
        return maxPath;
    }

    // Solution of Choice
    // Stack(Array)
    // beats 61.18%(4 ms for 25 tests)
    public int lengthLongestPath2(String input) {
        String[] paths = input.split("\n");
        int[] stack = new int[paths.length + 1];
        int maxPath = 0;
        for (String s : paths) {
            int level = s.lastIndexOf('\t') + 1;
            stack[level + 1] = stack[level] + s.length() - level + 1;
            if (s.contains(".")) {
                maxPath = Math.max(maxPath, stack[level + 1] - 1);
            }
        }
        return maxPath;
    }

    void test(String input, int expected) {
        assertEquals(expected, lengthLongestPath(input));
        assertEquals(expected, lengthLongestPath2(input));
    }

    @Test
    public void test1() {
        test("a\n\tb\n\t\tc.txt\n\taaaa.txt", 10);
        test("dir\n\tsubdir1\n\tsubdir2\n\t\tfile.ext", 20);
        test("dir\n\tsubdir1\n\t\tfile1.ext\n\t\tsubsubdir1\n\tsubdir2\n\t\t"
             + "subsubdir2\n\t\t\tfile2.ext", 32);
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("LengthLongestPath");
    }
}
