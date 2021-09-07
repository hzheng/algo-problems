import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC071: https://leetcode.com/problems/simplify-path/
//
// Given a string path, which is an absolute path (starting with a slash '/') to a file or directory
// in a Unix-style file system, convert it to the simplified canonical path.
//
// The canonical path should have the following format:
// The path starts with a single slash '/'.
// Any two directories are separated by a single slash '/'.
// The path does not end with a trailing '/'.
// The path only contains the directories on the path from the root directory to the target file or
// directory (i.e., no period '.' or double period '..')
// Return the simplified canonical path.
//
// Constraints:
// 1 <= path.length <= 3000
// path consists of English letters, digits, period '.', slash '/' or '_'.
// path is a valid absolute Unix path.
public class SimplifyPath {
    // Solution of Choice
    // time complexity: O(N), space complexity: O(N)
    // 3 ms(96.08%), 38.9 MB(79.62%) for 256 tests
    public String simplifyPath(String path) {
        Deque<String> names = new ArrayDeque<>();
        for (String dir : path.split("/")) {
            if (dir.equals("..")) {
                if (!names.isEmpty()) {
                    names.pollLast();
                }
            } else if (!dir.isEmpty() && !dir.equals(".")) {
                names.offerLast(dir);
            }
        }
        StringBuilder sb = new StringBuilder();
        while (!names.isEmpty()) {
            sb.append("/").append(names.pollFirst());
        }
        return sb.length() == 0 ? "/" : sb.toString();
    }

    void test(String path, String expected) {
        assertEquals(expected, simplifyPath(path));
    }

    @Test public void test1() {
        test("/home/", "/home");
        test("/home//foo", "/home/foo");
        test("/a/./b/../../c/", "/c");
        test("/a/./b/../../c/..", "/");
        test("/", "/");
        test("/a/../../b", "/b");
        test("a/./b", "/a/b"); // ?
        test("a", "/a"); // ?
        test("a/../b", "/b"); // ?
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
