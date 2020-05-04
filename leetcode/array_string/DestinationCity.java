import common.Utils;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

// LC1436: https://leetcode.com/problems/destination-city/
//
// You are given the array paths, where paths[i] = [cityAi, cityBi] means there exists a direct path
// going from cityAi to cityBi. Return the destination city, that is, the city without any path
// outgoing to another city. It is guaranteed that the graph of paths forms a line without any loop,
// therefore, there will be exactly one destination city.
public class DestinationCity {
    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(100%), 39.4 MB(100%) for 103 tests
    public String destCity(List<List<String>> paths) {
        Map<String, String> map = new HashMap<>();
        for (List<String> path : paths) {
            map.put(path.get(0), path.get(1));
        }
        for (String start = paths.get(0).get(0), next; ; start = next) {
            next = map.get(start);
            if (next == null) { return start; }
        }
    }

    // Hash Table
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(100%), 39 MB(100%) for 103 tests
    public String destCity2(List<List<String>> paths) {
        Map<String, String> map = new HashMap<>();
        for (List<String> path : paths) {
            map.put(path.get(0), path.get(1));
        }
        for (String dest : map.values()) {
            if (!map.containsKey(dest)) { return dest; }
        }
        return null;
    }

    // Set
    // time complexity: O(N), space complexity: O(N)
    // 1 ms(100%), 39.4 MB(100%) for 103 tests
    public String destCity3(List<List<String>> paths) {
        Set<String> origins = new HashSet<>();
        for (List<String> path : paths) {
            origins.add(path.get(0));
        }
        for (List<String> path : paths) {
            String dest = path.get(1);
            if (!origins.contains(dest)) { return dest; }
        }
        return null;
    }

    // Set
    // time complexity: O(N), space complexity: O(N)
    // 2 ms(95.20%), 39 MB(100%) for 103 tests
    public String destCity4(List<List<String>> paths) {
        Set<String> dests = new HashSet<>();
        for (List<String> path : paths) {
            dests.add(path.get(1));
        }
        for (List<String> path : paths) {
            dests.remove(path.get(0));
        }
        return dests.iterator().next();
    }

    @Test public void test() {
        test(new String[][] {{"London", "New York"}, {"New York", "Lima"}, {"Lima", "Sao Paulo"}},
             "Sao Paulo");
        test(new String[][] {{"B", "C"}, {"D", "B"}, {"C", "A"}}, "A");
        test(new String[][] {{"A", "Z"}}, "Z");
    }

    private void test(String[][] paths, String expected) {
        List<List<String>> pathList = Utils.toList(paths);
        assertEquals(expected, destCity(pathList));
        assertEquals(expected, destCity2(pathList));
        assertEquals(expected, destCity3(pathList));
        assertEquals(expected, destCity4(pathList));
    }

    public static void main(String[] args) {
        String clazz = new Object() {
        }.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
