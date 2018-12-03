import java.util.*;

import org.junit.Test;
import static org.junit.Assert.*;

// LC950: https://leetcode.com/problems/reveal-cards-in-increasing-order/
//
// In a deck of cards, every card has a unique integer.  You can order the deck
// in any order you want. Initially, all the cards start face down (unrevealed)
// in one deck. Do the following steps repeatedly, until all cards are revealed:
// Take the top card of the deck, reveal it, and take it out of the deck.
// If there are still cards in the deck, put the next top card of the deck at 
// the bottom of the deck. If there are still unrevealed cards, go back to step 
// 1.  Otherwise, stop. Return an ordering of the deck that would reveal the 
// cards in increasing order.
// The first entry in the answer is considered to be the top of the deck.
public class RevealCardsInOrder {
    // Sort + Deque
    // beats %(5 ms for 32 tests)
    public int[] deckRevealedIncreasing(int[] deck) {
        Arrays.sort(deck);
        Deque<Integer> deque = new LinkedList<>();
        for (int i = deck.length - 1; i >= 0; i--) {
            if (!deque.isEmpty()) {
                deque.offerFirst(deque.pollLast());
            }
            deque.offerFirst(deck[i]);
        }
        int[] res = new int[deck.length];
        int i = 0;
        for (int x : deque) {
            res[i++] = x;
        }
        return res;
    }

    // Sort + Queue
    // beats %(5 ms for 32 tests)
    public int[] deckRevealedIncreasing2(int[] deck) {
        int n = deck.length;
        Queue<Integer> index = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            index.offer(i);
        }
        Arrays.sort(deck);
        int[] res = new int[n];
        for (int card : deck) {
            res[index.poll()] = card;
            index.offer(index.poll());
        }
        return res;
    }

    void test(int[] deck, int[] expected) {
        assertArrayEquals(expected, deckRevealedIncreasing(deck));
        assertArrayEquals(expected, deckRevealedIncreasing2(deck));
    }

    @Test
    public void test() {
        test(new int[] {17, 13, 11, 2, 3, 5, 7}, new int[] {2, 13, 3, 11, 5, 17, 7});
    }

    public static void main(String[] args) {
        String clazz = new Object() {}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main(clazz);
    }
}
