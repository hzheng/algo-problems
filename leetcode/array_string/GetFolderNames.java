import java.util.*;

import org.junit.Test;

import static org.junit.Assert.*;

// LC1487: https://leetcode.com/problems/making-file-names-unique/
//
// Given an array of strings names of size n. You will create n folders in your file system such
// that, at the ith minute, you will create a folder with the name names[i]. Since two files cannot
// have the same name, if you enter a folder name which is previously used, the system will have a
// suffix addition to its name in the form of (k), where, k is the smallest positive integer such
// that the obtained name remains unique. Return an array of strings of length n where ans[i] is the
// actual name the system will assign to the ith folder when you create it.
// Constraints:
// 1 <= names.length <= 5 * 10^4
// 1 <= names[i].length <= 20
// names[i] consists of lower case English letters, digits and/or round brackets.
public class GetFolderNames {
    // Hash Table + Set
    // time complexity: O(N), space complexity: O(N)
    // 100 ms(29.08%), 126 MB(100%) for 33 tests
    public String[] getFolderNames(String[] names) {
        Map<String, Integer> map = new HashMap<>();
        Set<String> set = new HashSet<>();
        int n = names.length;
        String[] res = new String[n];
        int i = 0;
        for (String name : names) {
            if (set.add(name)) {
                res[i] = name;
                map.put(name, 1);
            } else {
                for (int count = map.getOrDefault(name, 1); ; count++) {
                    map.put(name, count + 1);
                    res[i] = name + "(" + count + ")";
                    if (set.add(res[i])) { break; }
                }
            }
            i++;
        }
        return res;
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 72 ms(45.90%), 125.9 MB(100%) for 33 tests
    public String[] getFolderNames2(String[] names) {
        int n = names.length;
        String[] res = new String[n];
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String name = names[i];
            Integer count = map.get(name);
            if (count == null) {
                res[i] = name;
                map.put(name, 1);
            } else {
                for (; ; count++) {
                    res[i] = name + '(' + count + ')';
                    if (!map.containsKey(res[i])) { break; }
                }
                map.put(res[i], 1);
                map.put(name, count + 1);
            }
        }
        return res;
    }

    void test(String[] names, String[] expected) {
        assertArrayEquals(expected, getFolderNames(names));
        assertArrayEquals(expected, getFolderNames2(names));
    }

    @Test public void test() {
        test(new String[] {"pes", "fifa", "gta", "pes(2019)"},
             new String[] {"pes", "fifa", "gta", "pes(2019)"});
        test(new String[] {"gta", "gta(1)", "gta", "avalon"},
             new String[] {"gta", "gta(1)", "gta(2)", "avalon"});
        test(new String[] {"onepiece", "onepiece(1)", "onepiece(2)", "onepiece(3)", "onepiece"},
             new String[] {"onepiece", "onepiece(1)", "onepiece(2)", "onepiece(3)", "onepiece(4)"});
        test(new String[] {"wano", "wano", "wano", "wano"},
             new String[] {"wano", "wano(1)", "wano(2)", "wano(3)"});
        test(new String[] {"kaido", "kaido(1)", "kaido", "kaido(1)"},
             new String[] {"kaido", "kaido(1)", "kaido(2)", "kaido(1)(1)"});
        test(new String[] {"kaido", "kaido(1)", "kaido", "kaido(1)", "kaido(2)"},
             new String[] {"kaido", "kaido(1)", "kaido(2)", "kaido(1)(1)", "kaido(2)(1)"});
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
