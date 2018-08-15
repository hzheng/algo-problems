import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC559: https://leetcode.com/problems/maximum-depth-of-n-ary-tree/
//
// Given a n-ary tree, find its maximum depth.
public class MaxDepthOfTree {
    class Node {
        public int val;
        public List<Node> children;
    
        public Node() {}
    
        public Node(int _val,List<Node> _children) {
            val = _val;
            children = _children;
        }
    }

    // DFS + Recursion
    // beats 92.71%(3 ms for 36 tests)
    public int maxDepth(Node root) {
        int[] res = new int[1];
        dfs(root, 0, res);
        return res[0];
    }
    
    private void dfs(Node root, int cur, int[] res) {
        if (root == null) return;
        
        res[0] = Math.max(res[0], cur + 1);
        if (root.children == null) return;

        for (Node child : root.children) {
            dfs(child, cur + 1, res);
        }
    }

    // DFS + Recursion
    // beats 92.71%(3 ms for 36 tests)
    public int maxDepth2(Node root) {
        if (root == null) return 0;

        int res = 0;
        for (Node child : root.children) {
            res = Math.max(res, maxDepth2(child));
        }
        return res + 1;        
    }

    // DFS + Stack
    // beats 8.37%(6 ms for 36 tests)
    public int maxDepth3(Node root) {
        if (root == null) return 0;

        int res = 0;
        ArrayDeque<Node> stack = new ArrayDeque<>();
        ArrayDeque<Integer> depth = new ArrayDeque<>();
        stack.push(root);
        depth.push(1);
        while (!stack.isEmpty()) {
            Node top = stack.pop();
            int h = depth.pop();
            res = Math.max(res, h);
            if (top.children == null) continue;

            for (Node child : top.children) {
                stack.push(child);
                depth.push(h + 1);
            }
        }
        return res;
    }

    // BFS + Queue
    // beats 18.58%(5 ms for 36 tests)
    public int maxDepth4(Node root) {
        if (root == null) return 0;

        Queue<Node> q = new LinkedList<>();
        q.offer(root);
        for (int level = 0; ; level++) {
            if (q.isEmpty()) return level;

            for (int i = q.size(); i > 0; i--) {
                Node node = q.poll();
                for (Node child : node.children) {
                    q.offer(child);
                }
            }
        }
    }

    @Test
    public void test() {
        // test();
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
