import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.Utils;

// LC841: https://leetcode.com/problems/keys-and-rooms/
//
// There are N rooms and you start in room 0. Each room has a distinct number in
// 0, 1, 2, ..., N-1, and each room may have some keys to access the next room.
// Formally, each room i has a list of keys rooms[i], and each key rooms[i][j]
// is an integer in [0, 1, ..., N-1].length.  A key rooms[i][j] = v opens the
// room with number v.
// Initially, all the rooms start locked (except for room 0).
// You can walk back and forth between rooms freely.
// Return true if and only if you can enter every room.
public class KeysAndRooms {
    // Queue + BFS
    // beats %(12 ms for 66 tests)
    public boolean canVisitAllRooms(List<List<Integer> > rooms) {
        int n = rooms.size();
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();
        for (queue.offer(0); !queue.isEmpty(); ) {
            int room = queue.poll();
            visited[room] = true;
            for (int key : rooms.get(room)) {
                if (!visited[key]) {
                    queue.offer(key);
                }
            }
        }
        for (boolean v : visited) {
            if (!v) return false;
        }
        return true;
    }

    // Stack + DFS
    // beats %(11 ms for 66 tests)
    public boolean canVisitAllRooms2(List<List<Integer>> rooms) {
        int n = rooms.size();
        boolean[] visited = new boolean[n];
        visited[0] = true;
        Stack<Integer> stack = new Stack<>();
        for (stack.push(0); !stack.isEmpty(); ) {
            int room = stack.pop();
            for (int key : rooms.get(room)) {
                if (!visited[key]) {
                    visited[key] = true;
                    stack.push(key);
                }
            }
        }
        for (boolean v : visited) {
            if (!v) return false;
        }
        return true;
    }

    // Recursion + DFS
    // beats %(8 ms for 66 tests)
    public boolean canVisitAllRooms3(List<List<Integer> > rooms) {
        int n = rooms.size();
        boolean[] visited = new boolean[n];
        dfs(rooms, 0, visited);
        for (boolean v : visited) {
            if (!v) return false;
        }
        return true;
    }

    private void dfs(List<List<Integer>> rooms, int index, boolean[] visited) {
        visited[index] = true;
        for (int key : rooms.get(index)) {
            if (!visited[key]) {
                dfs(rooms, key, visited);
            }
        }
    }

    void test(Integer[][] rooms, boolean expected) {
        assertEquals(expected, canVisitAllRooms(Utils.toList(rooms)));
        assertEquals(expected, canVisitAllRooms2(Utils.toList(rooms)));
        assertEquals(expected, canVisitAllRooms3(Utils.toList(rooms)));
    }

    @Test
    public void test() {
        test(new Integer[][] { { 1 }, { 2 }, { 3 }, {} }, true);
        test(new Integer[][] { { 1, 3 }, { 3, 0, 1 }, { 2 }, { 0 } }, false);
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
