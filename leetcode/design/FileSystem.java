import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC588: https://leetcode.com/problems/design-in-memory-file-system/
//
// Design an in-memory file system to simulate the following functions:
// ls: Given a path in string format. If it is a file path, return a list that only
// contains this file's name. If it is a directory path, return the list of file and
// directory names in this directory. Your output should in lexicographic order.
// mkdir: Given a directory path that does not exist, you should make a new directory
// according to the path. If the middle directories in the path don't exist either,
// you should create them as well. This function has void return type.
// addContentToFile: Given a file path and file content in string format. If the file
// doesn't exist, you need to create that file containing given content. If the file
// already exists, you need to append given content to original content.
// readContentFromFile: Given a file path, return its content in string format.
public class FileSystem {
    static interface IFileSystem {
        List<String> ls(String path);
        void mkdir(String path);
        void addContentToFile(String filePath, String content);
        String readContentFromFile(String filePath);
    }

    // beats 30.00%(200 ms for 63 tests)
    static class FileSystem1 implements IFileSystem {
        private static class File {
            String name;
            String content = "";
            Map<String, File> children;

            File(String name, boolean isDir) {
                this.name = name;
                if (isDir) {
                    children = new HashMap<>();
                }
            }
        }

        private File root;

        public FileSystem1() {
            root = new File("/", true);
        }

        public List<String> ls(String path) {
            File file = getFile(path, true);
            List<String> files = new ArrayList<>();
            if (file.children == null) {
                files.add(file.name);
            } else {
                for (String child : file.children.keySet()) {
                    files.add(child);
                }
                Collections.sort(files);
            }
            return files;
        }

        public void mkdir(String path) {
            getFile(path, true);
        }

        public void addContentToFile(String filePath, String content) {
            getFile(filePath, false).content += content;
        }

        public String readContentFromFile(String filePath) {
            return getFile(filePath, false).content;
        }

        private File getFile(String path, boolean isDir) {
            String[] paths = path.split("/"); // split("/+")
            File file = root;
            for (int i = 1; i < paths.length; i++) {
                String childName = paths[i];
                File child = file.children.get(childName);
                if (child == null) {
                    child = new File(childName, isDir || i != paths.length - 1);
                    file.children.put(childName, child);
                }
                file = child;
            }
            return file;
        }
    }

    // beats 40.00%(169 ms for 63 tests)
    static class FileSystem2 implements IFileSystem {
        private static class Dir {
            Map<String, Dir> dirs = new HashMap<>();
            Map<String, String> files = new HashMap<>();
        }

        private Dir root;

        public FileSystem2() {
            root = new Dir();
        }

        public List<String> ls(String path) {
            Dir dir = root;
            List<String> files = new ArrayList<>();
            if (!path.equals("/")) {
                String[] paths = path.split("/");
                for (int i = 1; i < paths.length - 1; i++) {
                    dir = dir.dirs.get(paths[i]);
                }
                if (dir.files.containsKey(paths[paths.length - 1])) {
                    files.add(paths[paths.length - 1]);
                    return files;
                }
                dir = dir.dirs.get(paths[paths.length - 1]);
            }
            files.addAll(dir.dirs.keySet());
            files.addAll(dir.files.keySet());
            Collections.sort(files);
            return files;
        }

        public void mkdir(String path) {
            Dir dir = root;
            String[] paths = path.split("/");
            for (int i = 1; i < paths.length; i++) {
                Dir file = dir.dirs.get(paths[i]);
                if (file == null) {
                    dir.dirs.put(paths[i], file = new Dir());
                }
                dir = file;
            }
        }

        public void addContentToFile(String filePath, String content) {
            addOrReadContent(filePath, content);
        }

        public String readContentFromFile(String filePath) {
            return addOrReadContent(filePath, null);
        }

        private String addOrReadContent(String filePath, String content) {
            Dir dir = root;
            String[] paths = filePath.split("/");
            for (int i = 1; i < paths.length - 1; i++) {
                dir = dir.dirs.get(paths[i]);
            }
            String fileContent = dir.files.getOrDefault(
                paths[paths.length - 1], "");
            if (content == null) return fileContent;

            dir.files.put(paths[paths.length - 1],  fileContent += content);
            return fileContent;
        }
    }

    void test1(IFileSystem fs) {
        fs.mkdir("/goowmfn");
        same(fs.ls("/goowmfn"));
        same(fs.ls("/"), "goowmfn");
        fs.mkdir("/z");
        same(fs.ls("/"), "goowmfn", "z");
        fs.addContentToFile("/goowmfn/c", "shetopcy");
        same(fs.ls("/z"));
        same(fs.ls("/goowmfn/c"), "c");
        same(fs.ls("/goowmfn"), "c");
        assertEquals("shetopcy", fs.readContentFromFile("/goowmfn/c"));
        fs.addContentToFile("/goowmfn/c", "+new");
        assertEquals("shetopcy+new", fs.readContentFromFile("/goowmfn/c"));
    }

    void test2(IFileSystem fs) {
        fs.mkdir("/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z");
        same(fs.ls("/a"), "b");
        same(fs.ls("/a/b"), "c");
        same(fs.ls("/a/b/c"), "d");
        same(fs.ls("/a/b/c/d"), "e");
        same(fs.ls("/a/b/c/d/e"), "f");
        same(fs.ls("/a/b/c/d/e/f"), "g");
        same(fs.ls("/a/b/c/d/e/f/g"), "h");
        same(fs.ls("/a/b/c/d/e/f/g/h"), "i");
        same(fs.ls("/a/b/c/d/e/f/g/h/i"), "j");
        same(fs.ls("/a/b/c/d/e/f/g/h/i/j"), "k");
        same(fs.ls("/a/b/c/d/e/f/g/h/i/j/k"), "l");
        same(fs.ls("/a/b/c/d/e/f/g/h/i/j/k/l"), "m");
        same(fs.ls("/a/b/c/d/e/f/g/h/i/j/k/l/m"), "n");
        same(fs.ls("/a/b/c/d/e/f/g/h/i/j/k/l/m/n"), "o");
        same(fs.ls("/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o"), "p");
        same(fs.ls("/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p"), "q");
        same(fs.ls("/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q"), "r");
        same(fs.ls("/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r"), "s");
        same(fs.ls("/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s"), "t");
        same(fs.ls("/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t"), "u");
        same(fs.ls("/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u"), "v");
        same(fs.ls("/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v"), "w");
        same(fs.ls("/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w"), "x");
        same(fs.ls("/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x"), "y");
        same(fs.ls("/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y"), "z");
        same(fs.ls("/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t/u/v/w/x/y/z"));
    }

    void same(List<String> list, String ... expected) {
        assertEquals(Arrays.asList(expected), list);
    }

    @Test
    public void test1() {
        test1(new FileSystem1());
        test1(new FileSystem2());
    }

    @Test
    public void test2() {
        test2(new FileSystem1());
        test2(new FileSystem2());
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
