import java.util.*;

import java.lang.reflect.Constructor;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// LC919: https://leetcode.com/problems/complete-binary-tree-inserter/
//
// A complete binary tree is a binary tree in which every level, except possibly
// the last, is completely filled, and all nodes are as far left as possible.
// Write a data structure CBTInserter that is initialized with a complete binary
// tree and supports the following operations:
// CBTInserter(TreeNode root) initializes the data structure on a given tree
// with head node root;
// CBTInserter.insert(int v) will insert a TreeNode into the tree with value
// node.val = v so that the tree remains complete, and returns the value of the
// parent of the inserted TreeNode;
// CBTInserter.get_root() will return the head node of the tree.
public class CBTInserter {
    // BFS + Queue
    // beats 34.80%(118 ms for 83 tests)
    class CBTInserter1 {
        private TreeNode root;
        private List<List<TreeNode>> levels = new ArrayList<>();

        public CBTInserter1(TreeNode root) {
            this.root = root;
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            while (!queue.isEmpty()) {
                List<TreeNode> level = new ArrayList<>();
                for (int i = queue.size(); i > 0; i--) {
                    TreeNode cur = queue.poll();
                    level.add(cur);
                    if (cur.left != null) {
                        queue.offer(cur.left);
                    }
                    if (cur.right != null) {
                        queue.offer(cur.right);
                    }
                }
                levels.add(level);
            }
        }

        public int insert(int v) {
            int last = levels.size() - 1;
            List<TreeNode> lastLevel = levels.get(last);
            int n = lastLevel.size();
            TreeNode node = new TreeNode(v);
            TreeNode parent = null;
            if (n < (1 << last)) {
                parent = levels.get(last - 1).get(n / 2);
            } else {
                lastLevel = new ArrayList<>();
                parent = levels.get(last).get(0);
                levels.add(lastLevel);
            }
            if (parent.left == null) {
                parent.left = node;
            } else {
                parent.right = node;
            }
            lastLevel.add(node);
            return parent.val;
        }

        public TreeNode get_root() {
            return root;
        }
    }

    // BFS + Queue + Deque
    // beats 68.45%(91 ms for 83 tests)
    class CBTInserter2 {
        private TreeNode root;
        private Deque<TreeNode> deque;

        public CBTInserter2(TreeNode root) {
            this.root = root;
            deque = new LinkedList<>();
            Queue<TreeNode> queue = new LinkedList<>();
            for (queue.offer(root); !queue.isEmpty(); ) {
                TreeNode node = queue.poll();
                if (node.left == null || node.right == null) {
                    deque.offerLast(node);
                }
                if (node.left != null) {
                    queue.offer(node.left);
                    if (node.right != null) {
                        queue.offer(node.right);
                    }
                }
            }
        }

        public int insert(int v) {
            TreeNode node = deque.peekFirst();
            deque.offerLast(new TreeNode(v));
            if (node.left == null) {
                node.left = deque.peekLast();
            } else {
                node.right = deque.peekLast();
                deque.pollFirst();
            }
            return node.val;
        }

        public TreeNode get_root() {
            return root;
        }
    }

    // BFS + Queue + Deque
    // beats 60.09%(99 ms for 83 tests)
    class CBTInserter3 {
        private TreeNode root;
        private Queue<TreeNode> queue = new LinkedList<>();

        public CBTInserter3(TreeNode root) {
            this.root = root;
            for (queue.offer(root); ; ) {
                TreeNode head = queue.peek();
                if (head.right == null) break;

                queue.offer(head.left);
                queue.offer(queue.poll().right);
            }
        }

        public int insert(int v) {
            TreeNode parent = queue.peek();
            if (parent.left == null) {
                parent.left = new TreeNode(v);
            } else {
                parent.right = new TreeNode(v);
                queue.offer(parent.left);
                queue.offer(parent.right);
                queue.poll();
            }
            return parent.val;
        }

        public TreeNode get_root() {
            return root;
        }
    }

    // BFS + List
    // beats 27.49%(126 ms for 83 tests)
    class CBTInserter4 {
        private List<TreeNode> tree = new ArrayList<>();

        public CBTInserter4(TreeNode root) {
            tree.add(root);
            for (int i = 0; i < tree.size(); i++) {
                if (tree.get(i).left != null) {
                    tree.add(tree.get(i).left);
                }
                if (tree.get(i).right != null) {
                    tree.add(tree.get(i).right);
                }
            }
        }

        public int insert(int v) {
            TreeNode node = new TreeNode(v);
            tree.add(node);
            int n = tree.size();
            TreeNode parent = tree.get(n / 2 - 1);
            if (n % 2 == 0) {
                parent.left = node;
            } else {
                parent.right = node;
            }
            return parent.val;
        }

        public TreeNode get_root() {
            return tree.get(0);
        }
    }

    static final Object[] VOID = new Object[] {};

    void test1(String className) throws Exception {
        Object outerObj = new Object() {}.getClass().getEnclosingClass().newInstance();
        test(
                new String[] {className, "insert", "get_root"},
                new Object[][] {new Object[] {outerObj, TreeNode.of(1)}, {2}, VOID},
                new Object[] {null, 1, TreeNode.of(1, 2)});

        test(
                new String[] {className, "insert", "insert", "get_root"},
                new Object[][] {
                    new Object[] {outerObj, TreeNode.of(1, 2, 3, 4, 5, 6)}, {7}, {8}, VOID
                },
                new Object[] {null, 3, 4, TreeNode.of(1, 2, 3, 4, 5, 6, 7, 8)});
    }

    void test(String[] methods, Object[][] args, Object[] expected) throws Exception {
        final String name = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        Class<?> clazz = Class.forName(name + "$" + methods[0]);
        Constructor<?> ctor = clazz.getConstructors()[0];
        Object obj = ctor.newInstance(args[0]);
        for (int i = 1; i < methods.length; i++) {
            Object[] arg = args[i];
            Object res = null;
            if (arg.length == 0) {
                res = clazz.getMethod(methods[i]).invoke(obj);
            } else if (arg.length == 1) {
                res = clazz.getMethod(methods[i], int.class).invoke(obj, arg);
            } else if (arg.length == 2) {
                res = clazz.getMethod(methods[i], int.class, int.class).invoke(obj, arg);
            }
            if (expected[i] != null) {
                assertEquals(expected[i], res);
            }
        }
    }

    @Test
    public void test1() {
        try {
            test1("CBTInserter1");
            test1("CBTInserter2");
            test1("CBTInserter3");
            test1("CBTInserter4");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
