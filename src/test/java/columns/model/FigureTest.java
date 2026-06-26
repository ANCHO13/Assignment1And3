package columns.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FigureTest {

    @Test
    void constructorAssignsColorsFromRandomGenerator() {
        // Figure's constructor maps each random value through abs(value) % 7 + 1,
        // so raw values 1, 2, 3 become colors 2, 3, 4.
        Figure figure = new Figure(new FakeRandomGenerator(1, 2, 3));

        assertEquals(0, figure.c[0]);
        assertEquals(2, figure.c[1]);
        assertEquals(3, figure.c[2]);
        assertEquals(4, figure.c[3]);
    }

    @Test
    void moveRightIncreasesX() {
        Figure figure = new Figure(new FakeRandomGenerator(1, 2, 3));
        int startX = figure.x;

        figure.moveRight();

        assertEquals(startX + 1, figure.x);
    }

    @Test
    void moveLeftDecreasesX() {
        Figure figure = new Figure(new FakeRandomGenerator(1, 2, 3));
        int startX = figure.x;

        figure.moveLeft();

        assertEquals(startX - 1, figure.x);
    }

    @Test
    void moveDownIncreasesY() {
        Figure figure = new Figure(new FakeRandomGenerator(1, 2, 3));
        int startY = figure.y;

        figure.moveDown();

        assertEquals(startY + 1, figure.y);
    }

    @Test
    void rotateUpRotatesColorsInExpectedOrder() {
        // Colors start as [_, 2, 3, 4] (see constructorAssignsColorsFromRandomGenerator).
        Figure figure = new Figure(new FakeRandomGenerator(1, 2, 3));

        figure.rotateUp();

        assertEquals(3, figure.c[1]);
        assertEquals(4, figure.c[2]);
        assertEquals(2, figure.c[3]);
    }

    @Test
    void rotateDownRotatesColorsInExpectedOrder() {
        // Colors start as [_, 2, 3, 4] (see constructorAssignsColorsFromRandomGenerator).
        Figure figure = new Figure(new FakeRandomGenerator(1, 2, 3));

        figure.rotateDown();

        assertEquals(4, figure.c[1]);
        assertEquals(2, figure.c[2]);
        assertEquals(3, figure.c[3]);
    }
}
