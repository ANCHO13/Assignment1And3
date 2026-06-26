# Assignment 3 Test Report: Unit Testing The Columns Game

## Test Framework

JUnit 5 (`junit-jupiter`), already a dependency in this repo's `pom.xml` from Assignment 1. Run with `mvn test`.

## What Was Tested

**Model — `FigureTest`** (6 tests): color assignment from the injected `RandomGenerator`, `moveLeft`/`moveRight`/`moveDown`, and both `rotateUp`/`rotateDown` color-rotation directions.

**Model — `BoardTest`** (18 tests): `initBoard` clearing field/score/level/counter, `pasteFigure` cell writes, `dropFigure` landing position, `canMoveLeft`/`canMoveRight` boundary and blocked-cell cases, `figureMayMoveDown` bottom and blocked-cell cases, `findMatches` for all three match shapes (vertical, horizontal, one diagonal direction) plus a true-negative case (no false match on an almost-matching board), `collapse`'s packing/gravity behavior, `changeLevelIfNeeded` at and below the level-up threshold including the max-level cap, and `isFieldFull` both true and false (row 3).

**Controller/event — `GameControllerTest`** (15 tests): `LEFT`/`RIGHT` movement plus their wall and blocked-cell boundary cases, `UP`/`DOWN` rotation, `DROP` (position change + controller timing reset), `LEVEL_UP`/`LEVEL_DOWN` (normal change, cap at 0/`MAX_LEVEL`, and match-counter reset on both), and `PAUSE` (optional area) — made deterministic via a fake platform that flips "key pressed" after a fixed number of `delay()` calls, so the loop terminates without any real waiting.

Total: 39 tests, all passing (`mvn test`).

## Test Doubles

All hand-written, no mocking library:

- `FakeRandomGenerator` — returns a fixed, cyclic sequence of `int`s, so `Figure` color assignment is deterministic.
- `FakePlatform` — controllable `currentTime()`/`tc`, `keyPressed` flag, recorded `delay()` calls, and a test-only `setAutoPressAfterDelays(n)` hook used solely to make the `PAUSE` loop end deterministically.
- `FakeScreen` — records every `setColor`/`fillRect`/`drawRect`/`drawString`/`clearRect` call instead of touching AWT; `Black()`/`White()` return fixed indices.
- `FakeModelListener` — records `levelHasChanged`, `tripletDetected`, `fieldWasUpdated`, and `scoreUpdated` calls for assertion.

## Production-Code Changes

**None.** `Figure`, `Board`, and `GameController` were already package-private classes with package-private internal methods (`processEvent`, `packField`, `changeLevelIfNeeded`, `checkNeighbours`, etc.), and all real-world boundaries (`Platform`, `RandomGenerator`, `Screen`, `ModelListener`) were already injected through constructors or setters rather than constructed internally. Tests were placed in the same `columns.model` package specifically to use this existing access, per the assignment's guidance — no visibility was widened and no behavior was changed.

## What Was Hard To Test

- **`Figure`'s color mapping isn't the raw random value** — the constructor applies `abs(value) % 7 + 1` to each `RandomGenerator.nextInt()` result. The first test draft assumed the fake's input values would appear unchanged in `figure.c[]`, which failed immediately and made the transformation's existence obvious. Once known, it's trivial to account for, but it's an easy trap for any test that wants specific, predictable colors.
- **Isolating individual match directions in `findMatches`** required care: `checkNeighbours` is called four times per occupied cell (vertical, horizontal, two diagonals) for every cell in the whole field, so a careless test board can trigger more than one direction's match at once, especially near a hand-placed triplet. Each match test here uses the minimum 3 non-zero cells needed for exactly one direction, with everything else left at 0, to keep the assertion meaningful and unambiguous.
- **`GameController.runGameLoop` itself was intentionally not tested directly** (per `testability-issues.md`'s own guidance) — it interleaves figure creation, falling, event polling, match detection, and game-over checking in one loop, which would make a single test brittle and hard to diagnose on failure. Testing `processEvent(...)` directly, with `board.figure` set up by hand, exercises the same logic in isolated, fast, deterministic pieces instead.

## Design Problems The Tests Revealed

- `Board.findMatches`/`collapse`/`changeLevelIfNeeded` call `listener.*` methods unconditionally with no null check — any test (or future caller) that forgets `setModelListener(...)` first gets an immediate `NullPointerException` rather than a clear error. Not fixed here (it's existing behavior, not a testability blocker — `setModelListener` is just always called in `@BeforeEach`), but worth flagging.
- `Board`'s mutable public fields (`newField`, `Score`, `level`, `figuresMatchedCounter`, `figure`) make tests easy to set up directly, but they also mean nothing in the class prevents a test (or production caller) from putting the board into an inconsistent state (e.g. setting `figure` without ever calling `initFields()`). This is a direct trade-off the assignment explicitly allows ("mutable model state... makes setup easy but also makes tests easy to overfit"); the tests here only set the minimum fields needed per behavior to avoid overfitting to internals.
