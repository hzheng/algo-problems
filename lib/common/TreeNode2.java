package common;

public class TreeNode2
{
    public TreeNode2 left;
    public TreeNode2 right;
    public int data;
    // needed in some case
    public TreeNode2 parent;

    public TreeNode2() {
    }

    public TreeNode2(int data) {
        this.data = data;
    }

    public String toString() {
        return "(" + data + ")";
    }

    public static void main(String[] args) {
    }
}
