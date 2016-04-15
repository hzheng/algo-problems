package bit_manipulation;

// https://ctci.googlecode.com/svn/trunk/Java/Chapter%205/Question5_7/BitInteger.java

public class BitInteger {
    public static int INTEGER_SIZE = 31;

    private boolean[] bits;
    public BitInteger() {
        bits = new boolean[INTEGER_SIZE];
    }

    /* Creates a number equal to given value. */
    public BitInteger(int value){
        bits = new boolean[INTEGER_SIZE];
        for (int j = 0; j < INTEGER_SIZE; j++) {
            // bits[INTEGER_SIZE - 1 - j] = (((value >> j) & 1) == 1);
            bits[j] = (((value >> j) & 1) == 1);
        }
    }

    /** Returns k-th most-significant bit. */
    public int get(int k){
        return bits[k] ? 1 : 0;
    }

    /** Sets k-th most-significant bit. */
    public void set(int k, int bitValue){
        bits[k] = (bitValue != 0);
    }

    /** Sets k-th most-significant bit. */
    public void set(int k, boolean bitValue){
        bits[k] = bitValue;
    }

    public void swapValues(BitInteger number) {
        for (int i = 0; i < INTEGER_SIZE; i++) {
            int temp = number.get(i);
            number.set(i, get(i));
            set(i, temp);
        }
    }

    public int toInt() {
        int val = 0;
        for (int j = 0; j < INTEGER_SIZE; j++) {
            val |= (get(j) << j);
        }
        return val;
    }

    public String toString() {
        return Integer.toString(toInt());
    }

    public static void main(String[] args) {
        // org.junit.runner.JUnitCore.main("BitInteger");
        BitInteger i = new BitInteger(1024);
        System.out.println(i);
    }
}
