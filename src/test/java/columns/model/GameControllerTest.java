package columns.model;

import columns.model.kernel.RandomGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameControllerTest {

    private GameController controller;
    private FakePlatform platform;

    @BeforeEach
    void setUp() {
        RandomGenerator randomGenerator = new FakeRandomGenerator(1, 2, 3);
        platform = new FakePlatform(new FakeScreen(), randomGenerator);
        controller = new GameController(platform);
        controller.board.figure = new Figure(randomGenerator);
    }

    @Test
    void leftEventMovesFigureLeftWhenAllowed() {
        int startX = controller.board.figure.x;

        controller.processEvent(GameEvent.LEFT);

        assertEquals(startX - 1, controller.board.figure.x);
    }

    @Test
    void leftEventDoesNotMoveThroughWall() {
        controller.board.figure.x = 1;

        controller.processEvent(GameEvent.LEFT);

        assertEquals(1, controller.board.figure.x);
    }

    @Test
    void leftEventDoesNotMoveThroughBlock() {
        controller.board.figure.x = 4;
        controller.board.figure.y = 5;
        controller.board.newField[3][7] = 9;

        controller.processEvent(GameEvent.LEFT);

        assertEquals(4, controller.board.figure.x);
    }

    @Test
    void rightEventMovesFigureRightWhenAllowed() {
        int startX = controller.board.figure.x;

        controller.processEvent(GameEvent.RIGHT);

        assertEquals(startX + 1, controller.board.figure.x);
    }

    @Test
    void rightEventDoesNotMoveThroughWall() {
        controller.board.figure.x = GameConfig.WIDTH;

        controller.processEvent(GameEvent.RIGHT);

        assertEquals(GameConfig.WIDTH, controller.board.figure.x);
    }

    @Test
    void upEventRotatesFigureColorsUpward() {
        int[] before = controller.board.figure.c.clone();

        controller.processEvent(GameEvent.UP);

        assertEquals(before[2], controller.board.figure.c[1]);
        assertEquals(before[3], controller.board.figure.c[2]);
        assertEquals(before[1], controller.board.figure.c[3]);
    }

    @Test
    void downEventRotatesFigureColorsDownward() {
        int[] before = controller.board.figure.c.clone();

        controller.processEvent(GameEvent.DOWN);

        assertEquals(before[3], controller.board.figure.c[1]);
        assertEquals(before[1], controller.board.figure.c[2]);
        assertEquals(before[2], controller.board.figure.c[3]);
    }

    @Test
    void dropEventMovesFigureToDropPositionAndResetsTiming() {
        controller.board.figure.y = 1;
        platform.setTc(12345);

        controller.processEvent(GameEvent.DROP);

        assertEquals(GameConfig.DEPTH - 2, controller.board.figure.y);
        assertEquals(0, platform.getTc());
    }

    @Test
    void levelUpIncreasesLevel() {
        controller.board.level = 2;

        controller.processEvent(GameEvent.LEVEL_UP);

        assertEquals(3, controller.board.level);
    }

    @Test
    void levelUpDoesNotExceedMaxLevel() {
        controller.board.level = GameConfig.MAX_LEVEL;

        controller.processEvent(GameEvent.LEVEL_UP);

        assertEquals(GameConfig.MAX_LEVEL, controller.board.level);
    }

    @Test
    void levelDownDecreasesLevel() {
        controller.board.level = 2;

        controller.processEvent(GameEvent.LEVEL_DOWN);

        assertEquals(1, controller.board.level);
    }

    @Test
    void levelDownDoesNotGoBelowZero() {
        controller.board.level = 0;

        controller.processEvent(GameEvent.LEVEL_DOWN);

        assertEquals(0, controller.board.level);
    }

    @Test
    void levelUpResetsMatchCounter() {
        controller.board.figuresMatchedCounter = 10;

        controller.processEvent(GameEvent.LEVEL_UP);

        assertEquals(0, controller.board.figuresMatchedCounter);
    }

    @Test
    void levelDownResetsMatchCounter() {
        controller.board.figuresMatchedCounter = 10;

        controller.processEvent(GameEvent.LEVEL_DOWN);

        assertEquals(0, controller.board.figuresMatchedCounter);
    }

    @Test
    void pauseEventEndsLoopOnceKeyIsPressed() {
        // isKeyPressed() starts false (loop runs); the fake platform flips it true after
        // 2 delay() calls so the loop terminates deterministically, never waiting for real input.
        platform.setKeyPressed(false);
        platform.setAutoPressAfterDelays(2);
        platform.setCurrentTime(999);

        controller.processEvent(GameEvent.PAUSE);

        assertTrue(platform.isKeyPressed());
        assertEquals(2, platform.delayCalls.size());
        assertEquals(999, platform.getTc());
    }
}
