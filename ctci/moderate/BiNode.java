import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 17.13:
 * A data structure called BiNode, which has pointers to two other nodes. It
 * could be used to represent both a binary tree or a doubly linked list.
 * Implement a method to convert a binary search tree into a doubly linked list.
 */
public class BiNode {
    private BiNode left;
    private BiNode right;
    private int data;
    private boolean isTree = true;

    public BiNode(int data) {
        this.data = data;
    }

    public void insert(int x) {
        if (isTree) {
            insertTree(x);
        } else {
            insertList(x);
        }
    }

    private void insertTree(int x) {
        if (x <= data) {
            if (left == null) {
                left = new BiNode(x);
            } else {
                left.insert(x);
            }
        } else {
            if (right == null) {
                right = new BiNode(x);
            } else {
                right.insert(x);
            }
        }
    }

    private void insertList(int x) {
        // TODO
    }

    public void convertToList() {
        if (isTree) {
            doConvertToList();
            isTree = false;
        }
    }

    private BiNode doConvertToList() {
        BiNode leftHalf = null;
        if (left != null) {
            BiNode maxNode = left.max();
            leftHalf = left.doConvertToList();
            left = maxNode;
            maxNode.right = this;
        }
        if (right != null) {
            BiNode rightHalf = right.doConvertToList();
            right = rightHalf;
            rightHalf.left = this;
        }
        return (left == null) ? this : leftHalf;
    }

    private BiNode max() {
        BiNode node = this;
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    private BiNode min() {
        BiNode node = this;
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public static void main(String[] args) {
        // org.junit.runner.JUnitCore.main("BiNode");
    }
}
