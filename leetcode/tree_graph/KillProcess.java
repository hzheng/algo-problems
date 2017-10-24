import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC582: https://leetcode.com/problems/kill-process/
//
// Given n processes, each process has a unique PID and its PPID. Given the two lists,
// and a PID representing a process to kill, return a list of PIDs of processes that
// will be killed in the end.
public class KillProcess {
    // Recursion + DFS
    // beats 50.00%(88 ms for 166 tests)
    public List<Integer> killProcess(List<Integer> pid, List<Integer> ppid,
                                     int kill) {
        Map<Integer, List<Integer> > map = new HashMap<>();
        for (int i = 0, n = ppid.size(); i < n; i++) {
            int parentId = ppid.get(i);
            List<Integer> children = map.get(parentId);
            if (children == null) {
                map.put(parentId, children = new ArrayList<>());
            }
            children.add(pid.get(i));
        }
        List<Integer> res = new ArrayList<>();
        subprocesses(kill, map, res);
        return res;
    }

    private void subprocesses(int parent, Map<Integer, List<Integer> > map,
                              List<Integer> res) {
        res.add(parent);
        List<Integer> children = map.get(parent);
        if (children != null) {
            for (int child : children) {
                subprocesses(child, map, res);
            }
        }
    }

    // Stack + DFS
    // beats 56.25%(73 ms for 166 tests)
    public List<Integer> killProcess2(List<Integer> pid, List<Integer> ppid,
                                      int kill) {
        Map<Integer, List<Integer> > map = new HashMap<>();
        for (int i = 0, n = ppid.size(); i < n; i++) {
            int parentId = ppid.get(i);
            List<Integer> children = map.get(parentId);
            if (children == null) {
                map.put(parentId, children = new ArrayList<>());
            }
            children.add(pid.get(i));
        }
        List<Integer> res = new ArrayList<>();
        ArrayDeque<Integer> stack = new ArrayDeque<>();
        stack.push(kill);
        while (!stack.isEmpty()) {
            int process = stack.pop();
            res.add(process);
            List<Integer> children = map.get(process);
            if (children != null) {
                stack.addAll(children);
            }
        }
        return res;
    }

    // Queue + BFS
    // beats 31.25%(103 ms for 166 tests)
    public List<Integer> killProcess3(List<Integer> pid, List<Integer> ppid,
                                      int kill) {
        Map<Integer, List<Integer> > map = new HashMap<>();
        for (int i = 0, n = ppid.size(); i < n; i++) {
            int parentId = ppid.get(i);
            List<Integer> children = map.get(parentId);
            if (children == null) {
                map.put(parentId, children = new ArrayList<>());
            }
            children.add(pid.get(i));
        }
        List<Integer> res = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(kill);
        while (!queue.isEmpty()) {
            int process = queue.poll();
            res.add(process);
            List<Integer> children = map.get(process);
            if (children != null) {
                for (int child : children) {
                    queue.offer(child);
                }
            }
        }
        return res;
    }

    void test(Integer[] pid, Integer[] ppid, int kill, Integer[] expected) {
        List<Integer> expectedList = Arrays.asList(expected);
        assertEquals(expectedList,
                     killProcess(Arrays.asList(pid), Arrays.asList(ppid),
                                 kill));
        assertEquals(expectedList,
                     killProcess2(Arrays.asList(pid), Arrays.asList(ppid),
                                  kill));
        assertEquals(expectedList,
                     killProcess3(Arrays.asList(pid), Arrays.asList(ppid),
                                  kill));
    }

    @Test
    public void test() {
        test(new Integer[] {1, 3, 10, 5}, new Integer[] {3, 0, 5, 3}, 5,
             new Integer[] {5, 10});
    }

    public static void main(String[] args) {
        String clazz =
            new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
