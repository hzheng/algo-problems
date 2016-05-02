package common;

import java.util.List;
import java.util.ArrayList;

public class Trie<T> {
    private List<Trie<T> > children;
    private char c;
    private T data;

    public Trie() {
    }

    public Trie(char c) {
        this.c = c;
    }

    public Trie(T defaultVal, List<String> keys) {
        for (String key : keys) {
            insert(key, defaultVal);
        }
    }

    public void insert(String key, T val) {
        if (key == null || key.isEmpty() || val == null) return;

        Trie<T> cur = this;
        int i = 0;
        for (; i < key.length(); i++) {
            boolean found = false;
            if (cur.children != null) {
                for (Trie<T> node : cur.children) {
                    if (key.charAt(i) == node.c) {
                        cur = node;
                        found = true;
                        break;
                    }
                }
            }
            if (!found) break;
        }
        for (; i < key.length(); i++) {
            if (cur.children == null) {
                cur.children = new ArrayList<Trie<T>>();
            }
            Trie<T> child = new Trie<T>(key.charAt(i));
            cur.children.add(child);
            cur = child;
        }
        cur.data = val;
    }

    public Trie<T> getNode(String key) {
        if (key == null || key.isEmpty()) return null;

        Trie<T> cur = this;
        for (char c : key.toCharArray()) {
            if (cur.children == null) return null;

            boolean found = false;
            for (Trie<T> node : cur.children) {
                if (c == node.c) {
                    cur = node;
                    found = true;
                    break;
                }
            }
            if (!found) return null;
        }
        return cur;
    }

    public T get(String key) {
        Trie<T> node = getNode(key);
        return (node == null) ? null : node.data;
    }

    public boolean contains(String key, boolean exact) {
        Trie<T> node = getNode(key);
        return (node != null) && (!exact || (node.data != null));
    }
}
