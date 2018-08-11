import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC429: https://leetcode.com/problems/n-ary-tree-level-order-traversal/
//
// Given an n-ary tree, return the level order traversal of its nodes' values.
public class NaryTreeLevelOrderTraversal {
    class Node {
        public int val;
        public List<Node> children;
    
        public Node() {}
    
        public Node(int _val,List<Node> _children) {
            val = _val;
            children = _children;
        }
    };

    // BFS + Queue
    // beats 32.63%(36 ms for 7 tests)
    public List<List<Integer>> levelOrder(Node root) {
        List<List<Integer>> res = new ArrayList<>();
        if (root == null) return res;
        
        Queue<Node> q = new LinkedList<>();
        for (q.offer(root); !q.isEmpty(); ) {
            List<Integer> level = new LinkedList<>();
            for (int i = q.size(); i > 0; i--) {
                Node node = q.poll();
                level.add(node.val);
                // if (node.children == null) continue;
                for (Node child : node.children) {
                    q.offer(child);
                }
            }
            res.add(level);
        }
        return res;
    }

    @Test
    public void test() {
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
