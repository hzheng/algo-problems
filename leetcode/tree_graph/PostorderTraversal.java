import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC590: https://leetcode.com/problems/n-ary-tree-postorder-traversal/
//
// Given a n-ary tree, return the postorder traversal of its nodes' values.
public class PostorderTraversal {
    class Node {
        public int val;
        public List<Node> children;
    
        public Node() {}
    
        public Node(int _val,List<Node> _children) {
            val = _val;
            children = _children;
        }
    }

    // Recursion
    // beats 98.64%(3 ms for 36 tests)
    public List<Integer> postorder(Node root) {
        List<Integer> res = new ArrayList<>();
        postorder(root, res);
        return res;
    }
    
    private void postorder(Node root, List<Integer> res) {
        if (root == null) return;
        
        for (Node child : root.children) {
            postorder(child, res);
        }
        res.add(root.val);
    }

    // Stack
    // beats 49.75%(5 ms for 36 tests)
    public List<Integer> postorder2(Node root) {
        LinkedList<Integer> res = new LinkedList<>();
        if (root == null) return res;

        ArrayDeque<Node> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node top = stack.pop();
            res.addFirst(top.val);
            for (Node child : top.children) {
                stack.push(child);
            }
        }
        return res;
    }

    // Stack
    // beats 49.75%(5 ms for 36 tests)
    public List<Integer> postorder3(Node root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;
        
        ArrayDeque<Node> stack = new ArrayDeque<>();
        stack.push(root);
        while(!stack.isEmpty()) {
            Node top = stack.pop();
            res.add(top.val);
            for(Node node : top.children) {
                stack.push(node);
            }
        }
        Collections.reverse(res);
        return res;
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
