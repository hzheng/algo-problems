package tree_graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;

// import tree_graph.TreeNode;

public class TreeUtils {
    public static TreeNode createTree(Integer[] dataArray) {
        return createTree(dataArray, false);
    }

    public static TreeNode createTree(Integer[] dataArray, boolean linkParent) {
        if ((dataArray == null) || (dataArray.length == 0)) return null;

        int len = dataArray.length;
        TreeNode root = new TreeNode(dataArray[0]);
        LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
        queue.add(root);
        for (int i = 1; i < len; i++) {
            TreeNode node = queue.removeFirst();
            // add left child
            TreeNode child = null;
            if (node != null && dataArray[i] != null) {
                child = node.left = new TreeNode(dataArray[i]);
                if (linkParent) child.parent = node;
            }
            queue.add(child);

            if (++i >= len) break;

            // add right child
            child = null;
            if (node != null && dataArray[i] != null) {
                child = node.right = new TreeNode(dataArray[i]);
                if (linkParent) child.parent = node;
            }
            queue.add(child);
        }
        return root;
    }

    public static TreeNode findBST(TreeNode root, int n) {
        for (TreeNode node = root; node != null; ) {
            if (node.data == n) return node;

            if (node.data < n) {
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return null;
    }

    public static TreeNode find(TreeNode root, int n) {
        if (root == null) return null;
        if (root.data == n) return root;

        TreeNode node = find(root.left, n);
        return (node != null) ? node : find(root.right, n);
    }

    // https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
    public static void print(TreeNode root) {
        print(Collections.singletonList(root), 1, maxLevel(root));
    }

    private static void print(List<TreeNode> nodes, int level, int maxLevel) {
        if (nodes.isEmpty() || isAllElementsNull(nodes))
            return;

        int floor = maxLevel - level;
        int endgeLines = (int)Math.pow(2, (Math.max(floor - 1, 0)));
        int firstSpaces = (int)Math.pow(2, (floor)) - 1;
        int betweenSpaces = (int)Math.pow(2, (floor + 1)) - 1;

        printWhitespaces(firstSpaces);

        List<TreeNode> newNodes = new ArrayList<TreeNode>();
        for (TreeNode node : nodes) {
            if (node != null) {
                System.out.print(node.data);
                newNodes.add(node.left);
                newNodes.add(node.right);
            } else {
                newNodes.add(null);
                newNodes.add(null);
                System.out.print(" ");
            }

            printWhitespaces(betweenSpaces);
        }
        System.out.println();

        for (int i = 1; i <= endgeLines; i++) {
            for (int j = 0; j < nodes.size(); j++) {
                printWhitespaces(firstSpaces - i);
                if (nodes.get(j) == null) {
                    printWhitespaces(endgeLines + endgeLines + i + 1);
                    continue;
                }

                if (nodes.get(j).left != null)
                    System.out.print("/");
                else
                    printWhitespaces(1);

                printWhitespaces(i + i - 1);

                if (nodes.get(j).right != null)
                    System.out.print("\\");
                else
                    printWhitespaces(1);

                printWhitespaces(endgeLines + endgeLines - i);
            }

            System.out.println();
        }

        print(newNodes, level + 1, maxLevel);
    }

    private static void printWhitespaces(int count) {
        for (int i = 0; i < count; i++)
            System.out.print(" ");
    }

    private static int maxLevel(TreeNode node) {
        if (node == null) return 0;

        return Math.max(maxLevel(node.left), maxLevel(node.right)) + 1;
    }

    private static <T> boolean isAllElementsNull(List<T> list) {
        for (Object object : list) {
            if (object != null) return false;
        }

        return true;
    }

    private static TreeNode test1() {
        TreeNode root = new TreeNode(2);
        TreeNode n11 = new TreeNode(7);
        TreeNode n12 = new TreeNode(5);
        TreeNode n21 = new TreeNode(2);
        TreeNode n22 = new TreeNode(6);
        TreeNode n23 = new TreeNode(3);
        TreeNode n24 = new TreeNode(6);
        TreeNode n31 = new TreeNode(5);
        TreeNode n32 = new TreeNode(8);
        TreeNode n33 = new TreeNode(4);
        TreeNode n34 = new TreeNode(5);
        TreeNode n35 = new TreeNode(8);
        TreeNode n36 = new TreeNode(4);
        TreeNode n37 = new TreeNode(5);
        TreeNode n38 = new TreeNode(8);

        root.left = n11;
        root.right = n12;

        n11.left = n21;
        n11.right = n22;
        n12.left = n23;
        n12.right = n24;

        n21.left = n31;
        n21.right = n32;
        n22.left = n33;
        n22.right = n34;
        n23.left = n35;
        n23.right = n36;
        n24.left = n37;
        n24.right = n38;

        return root;
    }

    private static TreeNode test2() {
        TreeNode root = new TreeNode(2);
        TreeNode n11 = new TreeNode(7);
        TreeNode n12 = new TreeNode(5);
        TreeNode n21 = new TreeNode(2);
        TreeNode n22 = new TreeNode(6);
        TreeNode n23 = new TreeNode(9);
        TreeNode n31 = new TreeNode(5);
        TreeNode n32 = new TreeNode(8);
        TreeNode n33 = new TreeNode(4);

        root.left = n11;
        root.right = n12;

        n11.left = n21;
        n11.right = n22;

        n12.right = n23;
        n22.left = n31;
        n22.right = n32;

        n23.left = n33;

        return root;
    }

    public static void main(String[] args) {
        System.out.println("===test1===");
        print(test1());

        System.out.println("===test1'===");
        Integer[] nodes = new Integer[] {2, 7, 5, 2, 6, 3, 6, 5, 8, 4, 5, 8, 4, 5, 8};
        TreeNode tree = createTree(nodes);
        print(tree);

        System.out.println("===test2===");
        print(test2());

        System.out.println("===test2'===");
        nodes = new Integer[] {2, 7, 5, 2, 6, null, 9, null, null, 5, 8, null, null, 4};
        tree = createTree(nodes);
        print(tree);
    }
}
