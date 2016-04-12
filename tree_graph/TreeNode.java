package tree_graph;

public class TreeNode
{
    public TreeNode left;
    public TreeNode right;
    public int data;
    // needed in some case
    public TreeNode parent;

    public TreeNode() {
    }

    public TreeNode(int data) {
        this.data = data;
    }

    public static void main(String[] args) {
    }
}
