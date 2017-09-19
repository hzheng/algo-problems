package common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;
import java.util.Stack;

public class TreeUtils {
    public static TreeNode2 createTree(Integer[] dataArray) {
        return createTree(dataArray, false);
    }

    public static TreeNode2 createTree(Integer[] dataArray, boolean linkParent) {
        if ((dataArray == null) || (dataArray.length == 0)) return null;

        int len = dataArray.length;
        TreeNode2 root = new TreeNode2(dataArray[0]);
        LinkedList<TreeNode2> queue = new LinkedList<TreeNode2>();
        queue.add(root);
        for (int i = 1; i < len; i++) {
            TreeNode2 node = queue.removeFirst();
            // add left child
            TreeNode2 child = null;
            if (node != null && dataArray[i] != null) {
                child = node.left = new TreeNode2(dataArray[i]);
                if (linkParent) child.parent = node;
            }
            queue.add(child);

            if (++i >= len) break;

            // add right child
            child = null;
            if (node != null && dataArray[i] != null) {
                child = node.right = new TreeNode2(dataArray[i]);
                if (linkParent) child.parent = node;
            }
            queue.add(child);
        }
        return root;
    }

    public static TreeNode2 findBST(TreeNode2 root, int n) {
        for (TreeNode2 node = root; node != null; ) {
            if (node.data == n) return node;

            if (node.data < n) {
                node = node.right;
            } else {
                node = node.left;
            }
        }
        return null;
    }

    public static TreeNode2 find(TreeNode2 root, int n) {
        if (root == null) return null;
        if (root.data == n) return root;

        TreeNode2 node = find(root.left, n);
        return (node != null) ? node : find(root.right, n);
    }

    public static List<TreeNode2> bfs(TreeNode2 root, boolean hasParent) {
        if (root == null) return null;

        List<TreeNode2> list = new ArrayList<TreeNode2>();
        LinkedList<TreeNode2> queue = new LinkedList<TreeNode2>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode2 node = queue.removeFirst();
            list.add(node);
            if (node.left != null) {
                queue.add(node.left);
                if (hasParent) {
                    node.left.parent = node;
                }
            }
            if (node.right != null) {
                queue.add(node.right);
                if (hasParent) {
                    node.right.parent = node;
                }
            }
        }
        return list;
    }

    public static List<TreeNode2> dfs(TreeNode2 root) {
        if (root == null) return null;

        Stack<TreeNode2> stack = new Stack<>();
        stack.push(root);
        List<TreeNode2> list = new ArrayList<>();
        while (!stack.empty()) {
            TreeNode2 top = stack.pop();
            list.add(top);
            if (top.left != null) stack.push(top.left);
            if (top.right != null) stack.push(top.right);
        }
        return list;
    }

    public static List<TreeNode2> dfsRecursive(TreeNode2 root) {
        if (root == null) return null;
        List<TreeNode2> list = new ArrayList<>();
        dfsRecursive(root, list);
        return list;
    }

    private static void dfsRecursive(TreeNode2 root, List<TreeNode2> list) {
        if (root == null) return;
        dfsRecursive(root.left, list);
        list.add(root);
        dfsRecursive(root.right, list);
    }

    // https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram
    public static void print(TreeNode2 root) {
        print(Collections.singletonList(root), 1, maxLevel(root));
    }

    private static void print(List<TreeNode2> nodes, int level, int maxLevel) {
        if (nodes.isEmpty() || isAllElementsNull(nodes))
            return;

        int floor = maxLevel - level;
        int endgeLines = (int)Math.pow(2, (Math.max(floor - 1, 0)));
        int firstSpaces = (int)Math.pow(2, (floor)) - 1;
        int betweenSpaces = (int)Math.pow(2, (floor + 1)) - 1;

        printWhitespaces(firstSpaces);

        List<TreeNode2> newNodes = new ArrayList<>();
        for (TreeNode2 node : nodes) {
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

    private static int maxLevel(TreeNode2 node) {
        if (node == null) return 0;

        return Math.max(maxLevel(node.left), maxLevel(node.right)) + 1;
    }

    private static <T> boolean isAllElementsNull(List<T> list) {
        for (Object object : list) {
            if (object != null) return false;
        }

        return true;
    }

    private static TreeNode2 test1() {
        TreeNode2 root = new TreeNode2(2);
        TreeNode2 n11 = new TreeNode2(7);
        TreeNode2 n12 = new TreeNode2(5);
        TreeNode2 n21 = new TreeNode2(2);
        TreeNode2 n22 = new TreeNode2(6);
        TreeNode2 n23 = new TreeNode2(3);
        TreeNode2 n24 = new TreeNode2(6);
        TreeNode2 n31 = new TreeNode2(5);
        TreeNode2 n32 = new TreeNode2(8);
        TreeNode2 n33 = new TreeNode2(4);
        TreeNode2 n34 = new TreeNode2(5);
        TreeNode2 n35 = new TreeNode2(8);
        TreeNode2 n36 = new TreeNode2(4);
        TreeNode2 n37 = new TreeNode2(5);
        TreeNode2 n38 = new TreeNode2(8);

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

    private static TreeNode2 test2() {
        TreeNode2 root = new TreeNode2(2);
        TreeNode2 n11 = new TreeNode2(7);
        TreeNode2 n12 = new TreeNode2(5);
        TreeNode2 n21 = new TreeNode2(2);
        TreeNode2 n22 = new TreeNode2(6);
        TreeNode2 n23 = new TreeNode2(9);
        TreeNode2 n31 = new TreeNode2(5);
        TreeNode2 n32 = new TreeNode2(8);
        TreeNode2 n33 = new TreeNode2(4);

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
        TreeNode2 tree = createTree(nodes);
        print(tree);

        System.out.println("===test2===");
        print(test2());

        System.out.println("===test2'===");
        nodes = new Integer[] {2, 7, 5, 2, 6, null, 9, null, null, 5, 8, null, null, 4};
        tree = createTree(nodes);
        print(tree);
    }
}
