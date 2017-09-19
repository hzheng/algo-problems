package common;

public class TrieNode {
    private static final int SIZE = 26;

    private boolean endFlag;

    private TrieNode[] children;

    public TrieNode() {
        children = new TrieNode[SIZE + 1];
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

    // Support macro character
    public TrieNode get(char c) {
        int index = c - 'a';
        return children[index < 0 ? SIZE : index];
    }

    // Support macro character
    public void set(char c, TrieNode child) {
        int index = c - 'a';
        children[index < 0 ? SIZE : index] = child;
    }

    public void setEnd() {
        endFlag = true;
    }

    public boolean isEnd() {
        return endFlag;
    }

    // Delete a word
    public void clearEnd() {
        endFlag = false;
    }
}
