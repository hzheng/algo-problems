package ElevatorSystem;

import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class ElevatorSystemTest {
    @Test
    public void testLevel() {
        ElevatorSystem system = new ElevatorSystem(0, 10, 1);
        assertEquals(1, system.getCurrentLevel());

    }

    @Test
    public void testMoveUp() {
        ElevatorSystem system = new ElevatorSystem(0, 10, 1);
        system.moveUp();
        assertEquals(2, system.getCurrentLevel());
    }

    @Test
    public void testMoveDown() {
        ElevatorSystem system = new ElevatorSystem(0, 10, 1);
        system.moveDown();
        assertEquals(0, system.getCurrentLevel());
    }

    @Test
    public void testMoveUpOverMax() {
        ElevatorSystem system = new ElevatorSystem(0, 10, 10);
        assertFalse(system.moveUp());
        assertEquals(10, system.getCurrentLevel());
    }

    @Test
    public void testMoveDownOverMin() {
        ElevatorSystem system = new ElevatorSystem(0, 10, 0);
        assertFalse(system.moveDown());
        assertEquals(0, system.getCurrentLevel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testElevatorTooHigh() {
        ElevatorSystem system = new ElevatorSystem(0, 10, 11);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testElevatorTooLow() {
        ElevatorSystem system = new ElevatorSystem(0, 10, -1);
    }

    @Test
    public void testGetUpCommands() {
        ElevatorSystem system = new ElevatorSystem(0, 10, 0);
        List<ElevatorSystem.Command> commands = system.getCommands(2);
        List<ElevatorSystem.Command> expectedCommands = List.of(
                ElevatorSystem.Command.UP,
                ElevatorSystem.Command.UP,
                ElevatorSystem.Command.OPEN_DOOR,
                ElevatorSystem.Command.CLOSE_DOOR
        );
        assertEquals(expectedCommands, commands);
    }

    @Test
    public void testGetDownCommands() {
        ElevatorSystem system = new ElevatorSystem(0, 10, 2);
        List<ElevatorSystem.Command> commands = system.getCommands(0);
        List<ElevatorSystem.Command> expectedCommands = List.of(
                ElevatorSystem.Command.DOWN,
                ElevatorSystem.Command.DOWN,
                ElevatorSystem.Command.OPEN_DOOR,
                ElevatorSystem.Command.CLOSE_DOOR
        );
        assertEquals(expectedCommands, commands);
    }

    @Test
    public void testGetMultipleTargets() {
        ElevatorSystem system = new ElevatorSystem(0, 10, 1);
        List<ElevatorSystem.Command> commands = system.getCommands(List.of(5, 3));
        List<ElevatorSystem.Command> expectedCommands = List.of(
                ElevatorSystem.Command.UP,
                ElevatorSystem.Command.UP,
                ElevatorSystem.Command.OPEN_DOOR,
                ElevatorSystem.Command.CLOSE_DOOR,
                ElevatorSystem.Command.UP,
                ElevatorSystem.Command.UP,
                ElevatorSystem.Command.OPEN_DOOR,
                ElevatorSystem.Command.CLOSE_DOOR
        );
        assertEquals(expectedCommands, commands);
    }

    public static void main(String[] args) {
        String clazz =
                new Object(){}.getClass().getEnclosingClass().getSimpleName();
        org.junit.runner.JUnitCore.main("ElevatorSystem." + clazz);
    }
}