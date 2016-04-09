import java.util.LinkedList;
import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Cracking the Coding Interview(5ed) Problem 3.7:
 * An animal shelter holds only dogs and cats, and operates on a FIFO basis.
 * People must adopt either the "oldest" (based on arrival time) of all animals
 * at the shelter, or they can select whether they would prefer a dog or a cat
 * (and will receive the oldest animal of that type). Create a data structures
 * to maintain this system and implement operations: enqueue, dequeueAny,
 * dequeueDog and dequeueCat.
 */
public class AnimalShelter {
    public abstract class Animal {
        private int id;
        private int order;

        public Animal(int id) {
            this.id = id;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public boolean olderThan(Animal other) {
            return order < other.order;
        }
    }

    public class Cat extends Animal {
        Cat(int id) {
            super(id);
        }
    }

    public class Dog extends Animal {
        Dog(int id) {
            super(id);
        }
    }

    private LinkedList<Dog> dogs = new LinkedList<Dog>();
    private LinkedList<Cat> cats = new LinkedList<Cat>();
    int order = 0;

    public void enqueue(Animal animal) {
        animal.setOrder(++order);
        if (animal instanceof Dog) {
            dogs.addLast((Dog)animal);
        } else if (animal instanceof Cat) {
            cats.addLast((Cat)animal);
        }
    }

    public Animal dequeueAny() {
        if (dogs.size() == 0) {
            return dequeueCat();
        }
        if (cats.size() == 0) {
            return dequeueDog();
        }

        if (dogs.peek().olderThan(cats.peek())) {
            return dequeueDog();
        } else {
            return dequeueCat();
        }
    }

    public Dog dequeueDog() {
        return dogs.poll();
    }

    public Cat dequeueCat() {
        return cats.poll();
    }

    public static void main(String[] args) {
    }
}
