import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;
import common.Utils;

// LC655: https://leetcode.com/problems/print-binary-tree/
//
// Print a binary tree in an m*n 2D string array following these rules:
// The row number m should be equal to the height of the given binary tree.
// The column number n should always be an odd number.
// The root node's value should be put in the exactly middle of the first row it
// can be put. The column and the row where the root node belongs will separate
// the rest space into two parts (left-bottom part and right-bottom part). You
// should print the left subtree in the left-bottom part and print the right
// subtree in the right-bottom part. The left-bottom part and the right-bottom
// part should have the same size. Even if one subtree is none while the other
// is not, you don't need to print anything for the none subtree but still need
// to leave the space as large as that for the other subtree. However, if two
// subtrees are none, then you don't need to leave space for both of them.
// Each unused space should contain an empty string "".
// Print the subtrees following the same rules.
public class PrintBinaryTree {
    // beats 9.26%(7 ms for 73 tests)
    public List<List<String> > printTree(TreeNode root) {
        int h = getHeight(root);
        String[][] output = new String[h][(1 << h) - 1];
        for (String[] a : output) {
            Arrays.fill(a, "");
        }
        fill(output, root, 0, 0, output[0].length);
        List<List<String>> res = new ArrayList<>();
        for (String[] a : output) {
            res.add(Arrays.asList(a));
        }
        return res;
    }

    private void fill(String[][] res, TreeNode root, int i, int l, int r) {
        Queue<NodePos> q = new LinkedList<>();
        for (q.offer(new NodePos(root, 0, 0, res[0].length)); !q.isEmpty(); ) {
            NodePos p = q.poll();
            res[p.i][(p.l + p.r) / 2] = "" + p.root.val;
            if (p.root.left != null) {
                q.offer(new NodePos(p.root.left, p.i + 1, p.l, (p.l + p.r) / 2));
            }
            if (p.root.right != null) {
                q.offer(new NodePos(p.root.right, p.i + 1, (p.l + p.r + 1) / 2, p.r));
            }
        }
    }

    private static class NodePos {
        TreeNode root;
        int i, l, r;
        NodePos(TreeNode n, int index, int left, int right) {
            root = n;
            i = index;
            l = left;
            r = right;
        }
    }

    private int getHeight(TreeNode root) {
        Queue<TreeNode> queue = new LinkedList<>();
        int h = 0;
        for (queue.offer(root); !queue.isEmpty(); h++) {
            for (int i = queue.size(); i > 0; i--) {
                TreeNode node = queue.poll();
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
        }
        return h;
    }

    // Recursion
    // beats 100.00%(4 ms for 73 tests)
    public List<List<String>> printTree2(TreeNode root) {
        int rows = getHeight2(root);
        int cols = (1 << rows) - 1;
        List<String> row = new ArrayList<>();
        for (int i = 0; i < cols; i++) {
            row.add("");
        }
        List<List<String>> res = new ArrayList<>();
        for(int i = 0; i < rows; i++) {
            res.add(new ArrayList<>(row));
        }
        fill(res, root, 0, 0, cols);
        return res;
    }

    private void fill(List<List<String>> res, TreeNode root, int i, int l, int r) {
        if (root == null) return;

        res.get(i).set((l + r) / 2, "" + root.val);
        fill(res, root.left, i + 1, l, (l + r) / 2 - 1);
        fill(res, root.right, i + 1, (l + r) / 2 + 1, r);
    }

    private int getHeight2(TreeNode root) {
        if (root == null) return 0;

        return 1 + Math.max(getHeight2(root.left), getHeight2(root.right));
    }

    void test(String root, String[][] expected) {
        assertEquals(Utils.toList(expected), printTree(TreeNode.of(root)));
        assertEquals(Utils.toList(expected), printTree2(TreeNode.of(root)));
    }

    @Test
    public void test() {
        test("1,2,3,#,4",
             new String[][] { { "", "", "", "1", "", "", "" }, 
                              { "", "2", "", "", "", "3", "" },
                              { "", "", "4", "", "", "", "" } });
        test("1,2,5,3,#,#,#,4,#",
             new String[][] {
            {"",  "",  "", "",  "", "", "", "1", "",  "",  "",  "",  "", "", ""},
            {"",  "",  "", "2", "", "", "", "",  "",  "",  "",  "5", "", "", ""},
            {"",  "3", "", "",  "", "", "", "",  "",  "",  "",  "",  "", "", ""},
            {"4", "",  "", "",  "", "", "", "",  "",  "",  "",  "",  "", "", ""}});  
    }

    public static void main(String[] args) {
        String clazz =
            new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
