package ElevatorSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ElevatorSystem {
    enum Command { UP, DOWN, OPEN_DOOR, CLOSE_DOOR };
    private int level;
    private final int min;
    private final int max;

    public ElevatorSystem(int min, int max, int level) {
        this.min = min;
        this.max = max;
        if (!isValid(level)) {
            throw new IllegalArgumentException();
        }
        this.level = level;
    }

    private boolean isValid(int level) {
        return (level >= min && level <= max);
    }

    public int getCurrentLevel() {
        return level;
    }

    public boolean moveUp() {
        return moveBy(1);
    }

    public boolean moveDown() {
        return moveBy(-1);
    }

    private boolean moveBy(int direction) {
        int nextLevel = level + direction;
        boolean valid = isValid(nextLevel);
        if (valid) {
            level = nextLevel;
        }
        return valid;
    }

    public List<Command> getCommands(int target) {
        return getCommands(level, target);
    }

    private List<Command> getCommands(int level, int target) {
        List<ElevatorSystem.Command> commands = new ArrayList<>();
        for (int i = level; i < target; i++) {
            commands.add(Command.UP);
        }
        for (int i = level; i > target; i--) {
            commands.add(Command.DOWN);
        }
        commands.add(Command.OPEN_DOOR);
        commands.add(Command.CLOSE_DOOR);
        return commands;
    }

    public List<Command> getCommands(List<Integer> targets) {
        List<Command> commands = new ArrayList<>();
        if (targets.isEmpty()) {
            return commands;
        }
        int first = targets.get(0);
        targets = targets.stream().sorted().collect(Collectors.toList());
        if (first > level) {
            int curLevel = level;
            for (int target : targets) {
                commands.addAll(getCommands(curLevel, target));
                curLevel = target;
            }
        } else {
            // TODO
        }
        return commands;
    }
}
