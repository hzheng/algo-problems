package common;

import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class TreeNode {
    public TreeNode left;
    public TreeNode right;
    public int val;

    public TreeNode(int x) {
        val = x;
    }

    public TreeNode (Integer[] vals) {
        build(this, vals);
    }

    public static TreeNode of(String s) {
        if (s == null) {
            return null;
        }
        List<Integer> vals = new ArrayList<>();
        for (String v : s.split(",")) {
            vals.add(v.equals("#") ? null : Integer.parseInt(v));
        }
        return TreeNode.of(vals.toArray(new Integer[0]));
    }

    public static TreeNode of(Integer ... vals) {
        TreeNode root = new TreeNode(vals[0]);
        return build(root, vals);
    }

    private static TreeNode build(TreeNode root, Integer ... vals) {
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int len = vals.length;
        for (int i = 1; i < len && !queue.isEmpty(); i++) {
            TreeNode node = queue.poll();
            Integer val = vals[i];
            if (val != null) {
                node.left = new TreeNode(val);
                queue.offer(node.left);
            }
            if (++i >= len) break;

            val = vals[i];
            if (val != null) {
                node.right = new TreeNode(val);
                queue.offer(node.right);
            }
        }
        return root;
    }

    public Integer[] toArray() {
        List<Integer> list = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(this);
        list.add(val);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            // if (node.left == null && node.right == null) continue;

            if (node.left == null) {
                list.add(null);
            } else {
                queue.offer(node.left);
                list.add(node.left.val);
            }
            if (node.right == null) {
                list.add(null);
            } else {
                queue.offer(node.right);
                list.add(node.right.val);
            }
        }
        while (true) {
            int last = list.size() - 1;
            if (list.get(last) != null) break;

            list.remove(last);
        }
        return list.stream().toArray(Integer[]::new);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(toArray());
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TreeNode)) return false;

        TreeNode o = (TreeNode)other;
        return Arrays.equals(toArray(), o.toArray());
    }

    @Override
    public String toString() {
        return toString(true);
    }

    public String toString(boolean includeBrackets) {
        StringBuilder sb = new StringBuilder();
        if (includeBrackets) {
            sb.append("{");
        }
        Integer[] arr = toArray();
        for (Integer x : arr) {
            sb.append(x == null ? "#" : String.valueOf(x));
            sb.append(",");
        }
        if (arr.length > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        if (includeBrackets) {
            sb.append("}");
        }
        return sb.toString();
    }
}
