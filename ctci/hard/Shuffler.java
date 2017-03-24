import java.util.Arrays;
import java.util.stream.IntStream;

import java.util.concurrent.ThreadLocalRandom;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 18.2:
 * Write a method to shuffle a deck of cards, each of the 52! permutations of
 * the deck has to be equally likely. Assume that you are given a random number
 * generator which is perfect.
 */
public class Shuffler {
    private static int rand(int n) {
        return ThreadLocalRandom.current().nextInt(0, n);
    }

    public static void shuffle(int[] cards) {
        for (int i = cards.length - 1; i > 0; i--) {
            int chosen = rand(i);
            int tmp = cards[chosen];
            cards[chosen] = cards[i];
            cards[i] = tmp;
        }
    }

    // from the book
    private static int rand(int lower, int higher) {
        return lower + (int)(Math.random() * (higher - lower + 1));
    }

    public static void shuffleRecursive(int[] cards) {
        shuffleRecursive(cards, cards.length - 1);
    }

    private static void shuffleRecursive(int[] cards, int i) {
        if (i < 1) return;

        shuffleRecursive(cards, i - 1); // Shuffle earlier part
        int k = rand(0, i); // Pick random index to swap with
        int temp = cards[k];
        cards[k] = cards[i];
        cards[i] = temp;
    }

    public static void shuffle2(int[] cards) {
        for (int i = 0; i < cards.length; i++) {
            int k = rand(0, i);
            int temp = cards[k];
            cards[k] = cards[i];
            cards[i] = temp;
        }
    }

    @FunctionalInterface
    interface Function<A> {
        public void apply(A a);
    }

    private void test(Function<int[]> shuffle, String name, int[] cards) {
        long t1 = System.nanoTime();
        cards = Arrays.copyOf(cards, cards.length);
        shuffle.apply(cards);
        System.out.format("%s: %.3f ms\n", name, (System.nanoTime() - t1) * 1e-6);
        System.out.println(Arrays.toString(cards));
    }

    private void test() {
        int[] cards = IntStream.range(1, 53).toArray();
        test(Shuffler::shuffle, "shuffle", cards);
        test(Shuffler::shuffleRecursive, "shuffleRecursive", cards);
        test(Shuffler::shuffle2, "shuffle2", cards);
    }

    @Test
    public void test1() {
        test();
    }

    public static void main(String[] args) {
        org.junit.runner.JUnitCore.main("Shuffler");
    }
}
