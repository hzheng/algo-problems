import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC071: https://leetcode.com/problems/simplify-path/
//
// Given an absolute path for a file (Unix-style), simplify it.
public class SimplifyPath {
    // Solution of Choice
    // beats 30.91%(15 ms)
    public String simplifyPath(String path) {
        if (path.length() == 0) return "";

        String[] dirs = path.split("/");
        Deque<String> names = new ArrayDeque<>();
        for (int i = 0; i < dirs.length; i++) {
            String dir = dirs[i];
            if (dir.equals("..")) {
                if (!names.isEmpty()) {
                    names.removeLast();
                }
            } else if (!dir.isEmpty() && !dir.equals(".")) {
                names.addLast(dir);
            }
        }
        StringBuilder sb = new StringBuilder();
        while (!names.isEmpty()) {
            sb.append("/").append(names.pop());
        }
        return sb.length() == 0 ? "/" : sb.toString();
    }

    void test(String path, String expected) {
        assertEquals(expected, simplifyPath(path));
    }

    @Test
    public void test1() {
        test("/home/", "/home");
        test("/a/./b/../../c/", "/c");
        test("/a/./b/../../c/..", "/");
        test("/", "/");
        test("/a/../../b", "/b");
        test("a/./b", "/a/b"); // ?
        test("a", "/a"); // ?
        test("a/../b", "/b"); // ?
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("SimplifyPath");
    }
}
