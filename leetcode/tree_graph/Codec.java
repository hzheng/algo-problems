import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC449: http://www.10jqka.com.cn/
//
// Design an algorithm to serialize and deserialize a binary search tree.
// The encoded string should be as compact as possible.
public class Codec {
    // BFS + Queue + Recursion
    // beats 80.03%(15 ms for 62 tests)
    public String serialize(TreeNode root) {
        if (root == null) return null;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            TreeNode front = queue.poll();
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(front.val);
            if (front.left != null) {
                queue.offer(front.left);
            }
            if (front.right != null) {
                queue.offer(front.right);
            }
        }
        return sb.toString();
    }

    public TreeNode deserialize(String data) {
        if (data == null) return null;

        String[] nodes = data.split(",");
        if (nodes.length == 0) return null;

        TreeNode root = new TreeNode(Integer.parseInt(nodes[0]));
        int n = nodes.length;
        for (int i = 1; i < n; i++) {
            insert(root, Integer.parseInt(nodes[i]));
        }
        return root;
    }

    private void insert(TreeNode root, int val) {
        if (val < root.val) {
            if (root.left == null) {
                root.left = new TreeNode(val);
            } else {
                insert(root.left, val);
            }
        } else {
            if (root.right == null) {
                root.right = new TreeNode(val);
            } else {
                insert(root.right, val);
            }
        }
    }

    // TODO: serialize in preorder and reuse LC105(inorder comes from sorting)

    // TODO: https://discuss.leetcode.com/topic/66495/using-lower-bound-and-upper-bound-to-deserialize-bst

    void test(String rootStr) {
        TreeNode root = TreeNode.of(rootStr);
        TreeNode deserialized = deserialize(serialize(root));
        // assertEquals("{" + rootStr + "}", deserialized.toString());
        assertEquals(root, deserialized);
    }

    @Test
    public void test1() {
        test("5,3,6,2,4,#,#,1");
        test("5,3,8,2,#,#,9");
        test("5,3,8,2,#,7");
        test("4,#,8,5,#,#,7");
        test("6,4,8,3,5,7,9,2");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Codec");
    }
}
