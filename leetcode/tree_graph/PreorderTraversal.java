import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC589: https://leetcode.com/problems/n-ary-tree-preorder-traversal/
//
// Given a n-ary tree, return the preorder traversal of its nodes' values.
public class PreorderTraversal {
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
    // beats 99.09%(3 ms for 36 tests)
    public List<Integer> preorder(Node root) {
        List<Integer> res = new ArrayList<>();
        preorder(root, res);
        return res;
    }
    
    private void preorder(Node root, List<Integer> res) {
        if (root == null) return;
        
        res.add(root.val);
        for (Node child : root.children) {
            preorder(child, res);
        }
    }

    // Stack
    // beats 67.62%(4 ms for 36 tests)
    public List<Integer> preorder2(Node root) {
        List<Integer> res = new ArrayList<>();
        if (root == null) return res;

        ArrayDeque<Node> stack = new ArrayDeque<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            Node top = stack.pop();
            res.add(top.val);
            for (int i = top.children.size() - 1; i >= 0; i--) {
                stack.push(top.children.get(i));
            }
        }
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
