import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1233: https://leetcode.com/problems/remove-sub-folders-from-the-filesystem/
//
// Given a list of folders, remove all sub-folders in those folders and return in any order the
// folders after removing. If a folder[i] is located within another folder[j], it is called a
// sub-folder of it. The format of a path is one or more concatenated strings of the form:
// / followed by one or more lowercase English letters. For example, /leetcode and
// /leetcode/problems are valid paths while an empty string and / are not.
//
// Constraints:
// 1 <= folder.length <= 4 * 10^4
// 2 <= folder[i].length <= 100
// folder[i] contains only lowercase letters and '/'
// folder[i] always starts with character '/'
// Each folder name is unique.
public class RemoveSubfolders {
    // Sort
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 43 ms(85.43%), 49.8 MB(38.03%) for 31 tests
    public List<String> removeSubfolders(String[] folder) {
        List<String> res = new ArrayList<>();
        Arrays.sort(folder);
        String prev = folder[0];
        res.add(prev);
        for (int n = folder.length, i = 1; i < n; i++) {
            String cur = folder[i];
            if (cur.indexOf(prev) != 0 || cur.charAt(prev.length()) != '/') {
                res.add(cur);
                prev = cur;
            }
        }
        return res;
    }

    // Sort
    // time complexity: O(N*log(N)), space complexity: O(N)
    // 49 ms(61.97%), 48.8 MB(56.07%) for 31 tests
    public List<String> removeSubfolders2(String[] folder) {
        Arrays.sort(folder);
        LinkedList<String> res = new LinkedList<>();
        for (String f : folder) {
            if (res.isEmpty() || !f.startsWith(res.peekLast() + "/")) {
                res.add(f);
            }
        }
        return res;
    }

    // TODO: Trie

    private void test(String[] folder, String[] expected) {
        List<String> expectedList = Arrays.asList(expected);
        assertEquals(expectedList, removeSubfolders(folder));
        assertEquals(expectedList, removeSubfolders2(folder));
    }

    @Test public void test() {
        test(new String[] {"/a", "/a/b", "/c/d", "/c/d/e", "/c/f"},
             new String[] {"/a", "/c/d", "/c/f"});
        test(new String[] {"/a", "/a/b/c", "/a/b/d"}, new String[] {"/a"});
        test(new String[] {"/a/b/c", "/a/b/ca", "/a/b/d"},
             new String[] {"/a/b/c", "/a/b/ca", "/a/b/d"});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
