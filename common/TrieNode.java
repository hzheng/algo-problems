package common;

public class TrieNode {
    private static final int SIZE = 26;

    private boolean endFlag;

    private TrieNode[] children;

    public TrieNode() {
        children = new TrieNode[SIZE];
    }

    public TrieNode getChild(char c) {
        return children[c - 'a'];
    }

    public void setChild(char c, TrieNode child) {
        children[c - 'a'] = child;
    }

    public boolean containsKey(char c) {
        return children[c -'a'] != null;
    }

    public void setEnd() {
        endFlag = true;
    }

    public boolean isEnd() {
        return endFlag;
    }
}
