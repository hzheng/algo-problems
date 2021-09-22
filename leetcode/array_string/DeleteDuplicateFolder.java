import java.util.*;
import java.util.function.Function;

import common.Utils;
import org.junit.Test;

import static org.junit.Assert.*;

// LC1948: https://leetcode.com/problems/delete-duplicate-folders-in-system/
//
// Due to a bug, there are many duplicate folders in a file system. You are given a 2D array paths,
// where paths[i] is an array representing an absolute path to the ith folder in the file system.
// For example, ["one", "two", "three"] represents the path "/one/two/three".
// Two folders (not necessarily on the same level) are identical if they contain the same non-empty
// set of identical subfolders and underlying subfolder structure. The folders do not need to be at
// the root level to be identical. If two or more folders are identical, then mark the folders as
// well as all their subfolders.
// For example, folders "/a" and "/b" in the file structure below are identical. They (as well as
// their subfolders) should all be marked:
// /a
// /a/x
// /a/x/y
// /a/z
// /b
// /b/x
// /b/x/y
// /b/z
// However, if the file structure also included the path "/b/w", then the folders "/a" and "/b"
// would not be identical. Note that "/a/x" and "/b/x" would still be considered identical even with
// the added folder. Once all the identical folders and their subfolders have been marked, the file
// system will delete all of them. The file system only runs the deletion once, so any folders that
// become identical after the initial deletion are not deleted.
// Return the 2D array ans containing the paths of the remaining folders after deleting all the
// marked folders. The paths may be returned in any order.
//
// Constraints:
// 1 <= paths.length <= 2 * 10^4
// 1 <= paths[i].length <= 500
// 1 <= paths[i][j].length <= 10
// 1 <= sum(paths[i][j].length) <= 2 * 10^5
// path[i][j] consists of lowercase English letters.
// No two paths lead to the same folder.
// For any folder not at the root level, its parent folder will also be in the input.
public class DeleteDuplicateFolder {
    // DFS + Recursion + Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 117 ms(55.11%), 68.3 MB(65.98%) for 108 tests
    public List<List<String>> deleteDuplicateFolder(List<List<String>> paths) {
        Directory root = new Directory("");
        for (List<String> path : paths) {
            addPath(root, path);
        }
        Map<String, Integer> dirCount = new HashMap<>();
        for (Directory child : root.children.values()) {
            count(dirCount, child);
        }
        for (Directory child : root.children.values()) {
            markDeletion(dirCount, child);
        }
        List<List<String>> res = new ArrayList<>();
        for (List<String> path : paths) {
            if (isValid(root, path)) {
                res.add(path);
            }
        }
        return res;
    }

    private boolean isValid(Directory root, List<String> path) {
        Directory cur = root;
        for (String p : path) {
            cur = cur.children.get(p);
            if (cur.toBeDeleted) {return false;}
        }
        return true;
    }

    private void markDeletion(Map<String, Integer> dirCount, Directory dir) {
        if (dirCount.getOrDefault(dir.key, 0) > 1) {
            dir.toBeDeleted = true;
            return;
        }
        for (Directory child : dir.children.values()) {
            markDeletion(dirCount, child);
        }
    }

    private String count(Map<String, Integer> dirCount, Directory dir) {
        if (dir.children.isEmpty()) {return "";}

        StringBuilder sb = new StringBuilder();
        for (Directory child : dir.children.values()) {
            sb.append('(');
            sb.append(child.name).append(count(dirCount, child));
            sb.append(')');
        }
        String key = sb.toString();
        dir.key = key;
        dirCount.put(key, dirCount.getOrDefault(key, 0) + 1);
        return key;
    }

    private void addPath(Directory root, List<String> path) {
        Directory cur = root;
        for (String p : path) {
            if (!cur.children.containsKey(p)) {
                cur.children.put(p, new Directory(p));
            }
            cur = cur.children.get(p);
        }
    }

    private static class Directory {
        String name;
        SortedMap<String, Directory> children = new TreeMap<>();
        String key = "";
        boolean toBeDeleted;

        Directory(String name) {
            this.name = name;
        }
    }

    // TODO: Trie
    // TODO: Hash

    private void test(Function<List<List<String>>, List<List<String>>> deleteDuplicateFolder,
                      String[][] paths, String[][] expected) {
        List<List<String>> pathList = Utils.toList(paths);
        List<List<String>> expectedList = Utils.toList(expected);
        Comparator<List<String>> comparator = (a, b) -> {
            if (a.size() != b.size()) {return a.size() - b.size();}

            for (int i = 0; i < a.size(); i++) {
                int res = a.get(i).compareTo(b.get(i));
                if (res != 0) {return res;}
            }
            return 0;
        };
        expectedList.sort(comparator);
        List<List<String>> res = deleteDuplicateFolder.apply(pathList);
        res.sort(comparator);
        assertEquals(expectedList, res);
    }

    private void test(String[][] paths, String[][] expected) {
        DeleteDuplicateFolder d = new DeleteDuplicateFolder();
        test(d::deleteDuplicateFolder, paths, expected);
    }

    @Test public void test() {
        test(new String[][] {{"a"}, {"c"}, {"d"}, {"a", "b"}, {"c", "b"}, {"d", "a"}},
             new String[][] {{"d"}, {"d", "a"}});
        test(new String[][] {{"a"}, {"c"}, {"a", "b"}, {"c", "b"}, {"a", "b", "x"},
                             {"a", "b", "x", "y"}, {"w"}, {"w", "y"}},
             new String[][] {{"c"}, {"c", "b"}, {"a"}, {"a", "b"}});
        test(new String[][] {{"a", "b"}, {"c", "d"}, {"c"}, {"a"}},
             new String[][] {{"c"}, {"c", "d"}, {"a"}, {"a", "b"}});
        test(new String[][] {{"a"}, {"a", "x"}, {"a", "x", "y"}, {"a", "z"}, {"b"}, {"b", "x"},
                             {"b", "x", "y"}, {"b", "z"}}, new String[][] {});
        test(new String[][] {{"a"}, {"a", "x"}, {"a", "x", "y"}, {"a", "z"}, {"b"}, {"b", "x"},
                             {"b", "x", "y"}, {"b", "z"}, {"b", "w"}},
             new String[][] {{"b"}, {"b", "w"}, {"b", "z"}, {"a"}, {"a", "z"}});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
