package columns.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardTest {

    private Board board;
    private FakeModelListener listener;

    @BeforeEach
    void setUp() {
        board = new Board();
        board.initFields();
        board.initBoard();
        listener = new FakeModelListener();
        board.setModelListener(listener);
    }

    @Test
    void initBoardClearsFieldScoreLevelAndCounter() {
        board.level = 5;
        board.Score = 100;
        board.figuresMatchedCounter = 10;
        board.newField[3][3] = 9;

        board.initBoard();

        assertEquals(0, board.level);
        assertEquals(0, board.Score);
        assertEquals(0, board.figuresMatchedCounter);
        assertEquals(0, board.newField[3][3]);
    }

    @Test
    void pasteFigureWritesColorsIntoExpectedCells() {
        Figure figure = new Figure(new FakeRandomGenerator(1, 2, 3));
        figure.x = 4;
        figure.y = 5;

        board.pasteFigure(figure);

        assertEquals(figure.c[1], board.newField[4][5]);
        assertEquals(figure.c[2], board.newField[4][6]);
        assertEquals(figure.c[3], board.newField[4][7]);
    }

    @Test
    void dropFigureMovesFigureToLowestAvailablePosition() {
        Figure figure = new Figure(new FakeRandomGenerator(1, 2, 3));
        figure.x = 4;
        figure.y = 1;

        board.dropFigure(figure);

        // With an empty column, the figure should drop to the bottom (DEPTH - 2).
        assertEquals(GameConfig.DEPTH - 2, figure.y);
    }

    @Test
    void canMoveLeftFalseAtLeftEdge() {
        Figure figure = new Figure(new FakeRandomGenerator(1, 2, 3));
        figure.x = 1;
        board.figure = figure;

        assertFalse(board.canMoveLeft());
    }

    @Test
    void canMoveLeftFalseWhenBlockingCellExists() {
        Figure figure = new Figure(new FakeRandomGenerator(1, 2, 3));
        figure.x = 4;
        figure.y = 5;
        board.figure = figure;
        board.newField[3][7] = 9;

        assertFalse(board.canMoveLeft());
    }

    @Test
    void canMoveRightFalseAtRightEdge() {
        Figure figure = new Figure(new FakeRandomGenerator(1, 2, 3));
        figure.x = GameConfig.WIDTH;
        board.figure = figure;

        assertFalse(board.canMoveRight());
    }

    @Test
    void canMoveRightFalseWhenBlockingCellExists() {
        Figure figure = new Figure(new FakeRandomGenerator(1, 2, 3));
        figure.x = 4;
        figure.y = 5;
        board.figure = figure;
        board.newField[5][7] = 9;

        assertFalse(board.canMoveRight());
    }

    @Test
    void figureMayMoveDownFalseAtBottom() {
        Figure figure = new Figure(new FakeRandomGenerator(1, 2, 3));
        figure.x = 4;
        figure.y = GameConfig.DEPTH - 2;
        board.figure = figure;

        assertFalse(board.figureMayMoveDown());
    }

    @Test
    void figureMayMoveDownFalseWhenCellBelowOccupied() {
        Figure figure = new Figure(new FakeRandomGenerator(1, 2, 3));
        figure.x = 4;
        figure.y = 5;
        board.figure = figure;
        board.newField[4][8] = 9;

        assertFalse(board.figureMayMoveDown());
    }

    @Test
    void findMatchesDetectsVerticalMatch() {
        board.newField[4][4] = 2;
        board.newField[4][5] = 2;
        board.newField[4][6] = 2;

        board.findMatches();

        assertFalse(board.noChanges);
        assertEquals(10, board.Score);
        assertEquals(1, board.figuresMatchedCounter);
        assertEquals(1, listener.tripletDetections.size());
    }

    @Test
    void findMatchesDetectsHorizontalMatch() {
        board.newField[2][5] = 3;
        board.newField[3][5] = 3;
        board.newField[4][5] = 3;

        board.findMatches();

        assertFalse(board.noChanges);
        assertEquals(10, board.Score);
        assertEquals(1, board.figuresMatchedCounter);
        assertEquals(1, listener.tripletDetections.size());
    }

    @Test
    void findMatchesDetectsDiagonalMatch() {
        board.newField[3][4] = 4;
        board.newField[4][5] = 4;
        board.newField[5][6] = 4;

        board.findMatches();

        assertFalse(board.noChanges);
        assertEquals(10, board.Score);
        assertEquals(1, board.figuresMatchedCounter);
        assertEquals(1, listener.tripletDetections.size());
    }

    @Test
    void findMatchesDoesNotFireWhenNoTripletExists() {
        board.newField[4][4] = 2;
        board.newField[4][5] = 3;
        board.newField[4][6] = 2;

        board.findMatches();

        assertTrue(board.noChanges);
        assertEquals(0, board.Score);
        assertEquals(0, board.figuresMatchedCounter);
        assertTrue(listener.tripletDetections.isEmpty());
    }

    @Test
    void collapsePacksRemainingCellsDownward() {
        board.oldField[3][15] = 0;
        board.oldField[3][14] = 7;
        board.oldField[3][13] = 0;
        board.oldField[3][12] = 9;

        board.collapse();

        assertEquals(7, board.newField[3][15]);
        assertEquals(9, board.newField[3][14]);
        assertEquals(0, board.newField[3][13]);
        assertEquals(0, board.newField[3][12]);
        assertEquals(1, listener.fieldUpdates.size());
    }

    @Test
    void levelIncreasesAfterMatchThreshold() {
        board.figuresMatchedCounter = GameConfig.NEXT_LEVEL_THRESHOLD;

        board.changeLevelIfNeeded();

        assertEquals(1, board.level);
        assertEquals(0, board.figuresMatchedCounter);
        assertEquals(1, listener.levelChanges.size());
    }

    @Test
    void levelDoesNotExceedMaxLevel() {
        board.level = GameConfig.MAX_LEVEL;
        board.figuresMatchedCounter = GameConfig.NEXT_LEVEL_THRESHOLD;

        board.changeLevelIfNeeded();

        assertEquals(GameConfig.MAX_LEVEL, board.level);
    }

    @Test
    void isFieldFullDetectsBlocksInRow3() {
        board.newField[4][3] = 5;

        assertTrue(board.isFieldFull());
    }

    @Test
    void isFieldFullFalseWhenRow3Empty() {
        assertFalse(board.isFieldFull());
    }
}
