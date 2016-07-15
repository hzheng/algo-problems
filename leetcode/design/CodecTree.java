import java.util.*;
import java.io.*;
import java.nio.charset.Charset;

import org.junit.Test;
import static org.junit.Assert.*;

import common.TreeNode;

// https://leetcode.com/problems/serialize-and-deserialize-binary-tree/
//
// Design an algorithm to serialize and deserialize a binary tree.
public class CodecTree {
    static interface Codec {
        // Encodes a tree to a single string.
        public String serialize(TreeNode root);

        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data);
    }

    static class Codec1 implements Codec {
        // important(UTF family doesn't work, UTF-16LE doesn't work for big tree)
        private static final String CHARSET = "ISO-8859-1";

        // beats 57.85%(27 ms)
        public String serialize(TreeNode root) {
            if (root == null) return "";

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
                 DataOutputStream dos = new DataOutputStream(baos)) {
                serialize(root, dos);
                return baos.toString(CHARSET);
                // or : return new String(baos.toByteArray(), CHARSET);
            } catch (IOException e) {
                return null;
            }
        }

        private void serialize(TreeNode root, DataOutputStream dos)
        throws IOException {
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            while (!queue.isEmpty()) {
                for (int i = queue.size() - 1; i >= 0; i--) {
                    TreeNode head = queue.poll();
                    byte childFlag = 0;
                    if (head.left != null) {
                        queue.offer(head.left);
                        childFlag = 1;
                    }
                    if (head.right != null) {
                        queue.offer(head.right);
                        childFlag |= 2;
                    }
                    dos.writeInt(head.val);
                    dos.writeByte(childFlag);
                }
            }
        }

        public TreeNode deserialize(String data) {
            if (data.length() == 0) return null;

            try (ByteArrayInputStream bais
                     = new ByteArrayInputStream(data.getBytes(CHARSET));
                 DataInputStream dis = new DataInputStream(bais)) {
                return deserialize(dis);
            } catch (IOException e) {
                return null;
            }
        }

        private TreeNode deserialize(DataInputStream dis) throws IOException {
            Queue<TreeNode> nodeQueue = new LinkedList<>();
            Queue<Byte> childFlagQueue = new LinkedList<>();
            TreeNode root = new TreeNode(dis.readInt());
            nodeQueue.offer(root);
            childFlagQueue.offer(dis.readByte());
            while (!nodeQueue.isEmpty()) {
                for (int i = nodeQueue.size() - 1; i >= 0; i--) {
                    TreeNode node = nodeQueue.poll();
                    byte childFlag = childFlagQueue.poll();
                    if ((childFlag & 1) != 0) {
                        node.left = new TreeNode(dis.readInt());
                        nodeQueue.offer(node.left);
                        childFlagQueue.offer(dis.readByte());
                    }
                    if ((childFlag & 2) != 0) {
                        node.right = new TreeNode(dis.readInt());
                        nodeQueue.offer(node.right);
                        childFlagQueue.offer(dis.readByte());
                    }
                }
            }
            return root;
        }
    }

    // http://www.geeksforgeeks.org/serialize-deserialize-binary-tree/
    // A simple solution is to store both Inorder and Preorder traversals.
    // This solution requires space twice the size of Binary Tree. We can save
    // space by storing Preorder traversal and a marker for NULL pointers.

    // beats 99.06%(8 ms)
    static class Codec2 implements Codec {
        private static final String NULL = "#";

        public String serialize(TreeNode root) {
            StringBuilder output = new StringBuilder();
            serialize(root, output);
            return output.toString();
        }

        private void serialize(TreeNode root, StringBuilder output) {
            if (root == null) {
                output.append(NULL).append(" ");
                return;
            }

            output.append(root.val).append(" ");
            serialize(root.left, output);
            serialize(root.right, output);
        }

        static class NodeIndex {
            TreeNode node;
            int start;
            NodeIndex(int start) {
                this.start = start;
            }
        }

        public TreeNode deserialize(String data) {
            return deserialize(data, new NodeIndex(0), data.length()).node;
        }

        private NodeIndex deserialize(String data, NodeIndex index, int end) {
            int start = index.start;
            while (index.start < end && data.charAt(index.start) != ' ') {
                index.start++;
            }

            String val = data.substring(start, index.start++);
            if (val.equals(NULL)) return index; //new NodeIndex(index.start);

            index.node = new TreeNode(Integer.parseInt(val));
            NodeIndex left = deserialize(data, new NodeIndex(index.start), end);
            index.node.left = left.node;
            NodeIndex right = deserialize(data, new NodeIndex(left.start), end);
            index.node.right = right.node;
            index.start = right.start;
            return index;
        }
    }

    void test1(String s) {
        Codec[] codecs = {new Codec1(), new Codec2()};
        TreeNode root = TreeNode.of(s);
        for (Codec codec : codecs) {
            String serialized = codec.serialize(root);
            System.out.println(codec.getClass().getName()
                               + ": serialized len="+ serialized.length());
            TreeNode deserialized = codec.deserialize(serialized);

            assertEquals(root.toString(), deserialized.toString());
        }
    }

    @Test
    public void test1() {
        test1("127");
        test1("128");
        test1("-1,0,1");
        test1("1");
        test1("1,2");
        test1("1,2,3");
        test1("1,2,3,4");
        test1("1,2,#,3");
        test1("1,2,3,#,4");
        test1("1,2,3,#,4,5,#,6");
        test1("1,2,3,#,4,5,#,6,7,8,#,#,#,9,#,10");
        test1("1,#,2,#,3,#,4,#,5,#,6,#,7,#,8,#,9,#,10,#,11,#,12,#,13,#,14,#,"
              + "15,#,16,#,17,#,18,#,19,#,20,#,21,#,22,#,23,#,24,#,25,#,26,#,"
              + "27,#,28,#,29,#,30,#,31,#,32,#,33,#,34,#,35,#,36,#,37,#,38,#,"
              + "39,#,40,#,41,#,42,#,43,#,44,#,45,#,46,#,47,#,48,#,49,#,50,#,"
              + "51,#,52,#,53,#,54,#,55,#,56,#,57,#,58,#,59,#,60,#,61,#,62,#,"
              + "63,#,64,#,65,#,66,#,67,#,68,#,69,#,70,#,71,#,72,#,73,#,74,#,"
              + "75,#,76,#,77,#,78,#,79,#,80,#,81,#,82,#,83,#,84,#,85,#,86,#,"
              + "87,#,88,#,89,#,90,#,91,#,92,#,93,#,94,#,95,#,96,#,97,#,98,#,"
              + "99,#,100,#,101,#,102,#,103,#,104,#,105,#,106,#,107,#,108,#,"
              + "109,#,110,#,111,#,112,#,113,#,114,#,115,#,116,#,117,#,118,#,"
              + "119,#,120,#,121,#,122,#,123,#,124,#,125,#,126,#,127,#,128,#,"
              + "129,#,130,#,131,#,132,#,133,#,134,#,135,#,136,#,137,#,138,#,"
              + "139,#,140,#,141,#,142,#,143,#,144,#,145,#,146,#,147,#,148,#,"
              + "149,#,150,#,151,#,152,#,153,#,154,#,155,#,156,#,157,#,158,#,"
              + "159,#,160,#,161,#,162,#,163,#,164,#,165,#,166,#,167,#,168,#,"
              + "169,#,170,#,171,#,172,#,173,#,174,#,175,#,176,#,177,#,178,#,"
              + "179,#,180,#,181,#,182,#,183,#,184,#,185,#,186,#,187,#,188,#,"
              + "189,#,190,#,191,#,192,#,193,#,194,#,195,#,196,#,197,#,198,#,"
              + "199,#,200,#,201,#,202,#,203,#,204,#,205,#,206,#,207,#,208,#,"
              + "209,#,210,#,211,#,212,#,213,#,214,#,215,#,216,#,217,#,218,#,"
              + "219,#,220,#,221,#,222,#,223,#,224,#,225,#,226,#,227,#,228,#,"
              + "229,#,230,#,231,#,232,#,233,#,234,#,235,#,236,#,237,#,238,#,"
              + "239,#,240,#,241,#,242,#,243,#,244,#,245,#,246,#,247,#,248,#,"
              + "249,#,250,#,251,#,252,#,253,#,254,#,255,#,256,#,257,#,258,#,"
              + "259,#,260,#,261,#,262,#,263,#,264,#,265,#,266,#,267,#,268,#,"
              + "269,#,270,#,271,#,272,#,273,#,274,#,275,#,276,#,277,#,278,#");
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("CodecTree");
    }
}
