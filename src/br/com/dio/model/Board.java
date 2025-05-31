package br.com.dio.model; 

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static br.com.dio.model.GameStatusEnum.*; 

public class Board {
    private final Space[][] spaces;
    private boolean isDirty = true;
    private boolean cachedHasErrors;
    private GameStatusEnum cachedStatus;

    public Board(List<List<Space>> spaces) {
        this.spaces = spaces.stream()
                            .map(row -> row.toArray(new Space[0]))
                            .toArray(Space[][]::new);
    }

    public static Space[][] initBoard(Map<String, String> gameConfig) {
        final int BOARD_LIMIT = 9;

        return IntStream.range(0, BOARD_LIMIT).parallel()
            .mapToObj(i -> IntStream.range(0, BOARD_LIMIT).parallel()
                .mapToObj(j -> {
                    String key = i + "," + j;
                    String[] config = gameConfig.get(key).split(",");
                    int expected = Integer.parseInt(config[0]);
                    boolean fixed = Boolean.parseBoolean(config[1]);
                    return new Space(expected, fixed);
                }).toArray(Space[]::new))
            .toArray(Space[][]::new);
    }

    public Space[][] getSpaces(){
        return spaces;
    }

    public GameStatusEnum getStatus() {
        if (isDirty) {
            cachedStatus = calculateStatus();
            isDirty = false;
        }
        return cachedStatus;
    }

    private GameStatusEnum calculateStatus() {
        boolean hasInput = Arrays.stream(spaces)
                                 .flatMap(Arrays::stream)
                                 .anyMatch(s -> !s.isFixed() && s.getActual() != null);

        if (!hasInput) return NON_STARTED;

        boolean isComplete = Arrays.stream(spaces)
                                   .flatMap(Arrays::stream)
                                   .allMatch(s -> s.getActual() != null);

        return isComplete ? COMPLETE : INCOMPLETE;
    }

    public boolean hasErrors() {
        if (isDirty) {
            cachedHasErrors = checkForErrors();
            isDirty = false;
        }
        return cachedHasErrors;
    }

    private boolean checkForErrors() {
        return IntStream.range(0, 9).parallel()
                                    .anyMatch(i -> checkRow(i) || checkColumn(i) || checkQuadrant(i));
    }

    private boolean checkRow(int row) {
        int mask = 0;
        for (Space space : spaces[row]) {
            Integer val = space.getActual();
            if (val != null && (mask & (1 << val)) !=0) return true;
            if (val !=null) mask |= (1<<val);
        }
        return false;
    }

    private boolean checkColumn(int col) {
        int mask = 0;
        for (int row = 0; row < 9; row++) {
            Integer val = spaces[row][col].getActual();
            if (val != null && (mask & (1 << val)) != 0) return true;
            if (val != null) mask |= (1 << val);
        }
        return false;
    }

    private boolean checkQuadrant(int index) {
        int mask = 0;
        int startRow = (index / 3) * 3;
        int startCol = (index % 3) * 3;

        for (int row = startRow; row < startRow + 3; row++) {
            for (int col = startCol; col < startCol + 3; col++) {
                Integer val = spaces[row][col].getActual();
                if (val != null && (mask & (1 << val)) != 0) return true;
                if (val != null) mask |= (1 << val);
            }
        }
        return false;
    }

    public boolean changeValue(int col, int row, int value) {
        Space space = spaces[col][row];
        if (space.isFixed()) return false;
        isDirty = true;
        return true;
    }

    public boolean clearValue(int col, int row) {
        Space space = spaces[col][row];
        if (space.isFixed()) return false;
        space.clearSpace();
        isDirty = true;
        return true;
    }

    public void reset() {
        Arrays.stream(spaces).forEach(row ->
            Arrays.stream(row).forEach(Space::clearSpace)
        );
        isDirty = true;
    }

    public boolean gameIsFinished() {
        return !hasErrors() && getStatus() == COMPLETE;
    }
}