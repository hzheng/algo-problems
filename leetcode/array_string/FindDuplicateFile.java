import java.util.*;
import java.util.stream.Collectors;

import org.junit.Test;

import common.Utils;

import static org.junit.Assert.*;

// LC609: https://leetcode.com/problems/find-duplicate-file-in-system/
//
// Given a list of directory info including directory path, and all the files
// with contents in this directory, you need to find out all the groups of
// duplicate files in the file system in terms of their paths.
// A group of duplicate files consists of at least two files that have exactly
// the same content.
// A single directory info string in the input list has the following format:
// "root/d1/d2/.../dm f1.txt(f1_content) f2.txt(f2_content) ... fn.txt(fn_content)"
// It means there are n files (f1.txt, f2.txt ... fn.txt with content f1_content,
// f2_content ... fn_content, respectively) in directory root/d1/d2/.../dm.
// Note that n >= 1 and m >= 0. If m = 0, it means the directory is just the
// root directory.
// The output is a list of group of duplicate file paths. For each group, it
// contains all the file paths of the files that have the same content.
public class FindDuplicateFile {
    // Hash Table
    // beats 99.60%(44 ms for 161 tests)
    public List<List<String> > findDuplicate(String[] paths) {
        Map<String, List<String> > map = new HashMap<>();
        for (String path : paths) {
            String[] info = path.split(" ");
            String dir = info[0];
            for (int i = 1; i < info.length; i++) {
                String fileInfo = info[i];
                int leftParen = fileInfo.lastIndexOf("(");
                String content = fileInfo.substring(leftParen + 1,
                                                    fileInfo.length() - 1);
                String filePath = fileInfo.substring(0, leftParen);
                if (!map.containsKey(content)) {
                    map.put(content, new ArrayList<>());
                }
                map.get(content).add(dir + "/" + filePath);
            }
        }
        List<List<String> > res = new ArrayList<>();
        for (List<String> files : map.values()) {
            if (files.size() > 1) {
                res.add(files);
            }
        }
        return res;
    }

    // Hash Table
    // beats 18.23%(133 ms for 161 tests)
    public static List<List<String> > findDuplicate2(String[] paths) {
        Map<String, List<String> > map = new HashMap<>();
        for (String path : paths) {
            String[] info = path.split(" ");
            String dir = info[0];
            for (int i = 1; i < info.length; i++) {
                String fileInfo = info[i];
                int leftParen = fileInfo.lastIndexOf("(");
                String filePath = fileInfo.substring(0, leftParen);
                String content = fileInfo.substring(leftParen + 1,
                                                    fileInfo.length() - 1);
                map.putIfAbsent(content, new ArrayList<>());
                map.get(content).add(dir + "/" + filePath);
            }
        }
        return map.values().stream().filter(files -> files.size() > 1).collect
                   (Collectors.toList());
    }

    void test(String[] paths, String[][] expected) {
        assertArrayEquals(expected, Utils.toStrArray(findDuplicate(paths)));
        assertArrayEquals(expected, Utils.toStrArray(findDuplicate2(paths)));
    }

    @Test
    public void test() {
        test(new String[] {"root/a 1.txt(abcd) 2.txt(efgh)",
                           "root/c 3.txt(abcd)", "root/c/d 4.txt(efgh)",
                           "root 4.txt(efgh)"},
             new String[][] {{"root/a/2.txt", "root/c/d/4.txt", "root/4.txt"},
                             {"root/a/1.txt", "root/c/3.txt"}});
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
