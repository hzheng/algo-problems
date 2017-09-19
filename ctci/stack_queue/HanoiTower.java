import java.util.Stack;

/**
 * Cracking the Coding Interview(5ed) Problem 3.4:
 * In the Hanoi Tower problem, you have 3 towers and N disks of different sizes
 * which can slide onto any tower. The puzzle starts with disks sorted in
 * ascending order of size from top to bottom, having the following constraints:
 * (1) Only one disk can be moved at a time.
 * (2) A disk is slid off the top of one tower onto the next rod.
 * (3) A disk can only be placed on top of a larger disk.
 * Problem: Move the disks from the first tower to the last using Stacks.
 */
public class HanoiTower {
    private Stack<Integer> stack = new Stack<Integer>();
    private String label;

    public HanoiTower(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void addDisk(int disk) {
        if (!stack.empty() && stack.peek() < disk) {
            throw new IllegalStateException();
        }
        stack.push(disk);
    }

    private void moveDisk(HanoiTower other) {
        int disk = stack.pop();
        other.addDisk(disk);
        // the following can be generalized as notification
        System.out.println("move " + disk + " from tower "
                           + label + " to tower " + other.label);
    }

    public void move(int n, HanoiTower dest, HanoiTower buffer) {
        if (n <= 0) {
            return;
        }

        move(n - 1, buffer, dest);
        moveDisk(dest);
        buffer.move(n - 1, dest, this);
    }

    public static void testMove(int n) {
        HanoiTower tower1 = new HanoiTower("A");
        HanoiTower tower2 = new HanoiTower("B");
        HanoiTower tower3 = new HanoiTower("C");

        for (int i = n; i > 0; i--) {
            tower1.addDisk(i);
        }
        tower1.move(n, tower3, tower2);
    }

    public static void main(String[] args) {
        System.out.println("===test 3 disks===");
        HanoiTower.testMove(3);
        System.out.println();

        System.out.println("===test 4 disks===");
        HanoiTower.testMove(4);
    }
}
