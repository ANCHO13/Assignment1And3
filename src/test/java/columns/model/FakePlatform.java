package columns.model;

import columns.model.kernel.Platform;
import columns.model.kernel.RandomGenerator;
import columns.model.kernel.Screen;

import java.util.ArrayList;
import java.util.List;

class FakePlatform implements Platform {

    final Screen screen;
    final RandomGenerator randomGenerator;
    final List<Long> delayCalls = new ArrayList<>();

    private long time = 0;
    private long tc = 0;
    private boolean keyPressed = false;
    private GameEvent event = GameEvent.NONE;
    private int autoPressAfterDelays = -1;

    FakePlatform(Screen screen, RandomGenerator randomGenerator) {
        this.screen = screen;
        this.randomGenerator = randomGenerator;
    }

    /**
     * Test-only hook so a pause-style "loop while no key is pressed" can be exercised
     * deterministically: after this many delay() calls, isKeyPressed() starts returning true,
     * ending the loop without any real waiting.
     */
    void setAutoPressAfterDelays(int count) {
        this.autoPressAfterDelays = count;
    }

    @Override
    public void delay(long t) {
        delayCalls.add(t);
        if (autoPressAfterDelays >= 0 && delayCalls.size() >= autoPressAfterDelays) {
            keyPressed = true;
        }
    }

    @Override
    public long currentTime() {
        return time;
    }

    void setCurrentTime(long time) {
        this.time = time;
    }

    @Override
    public boolean isKeyPressed() {
        return keyPressed;
    }

    @Override
    public void setKeyPressed(boolean isKeyPressed) {
        this.keyPressed = isKeyPressed;
    }

    @Override
    public Screen getScreen() {
        return screen;
    }

    @Override
    public long getTc() {
        return tc;
    }

    @Override
    public void setTc(long time) {
        this.tc = time;
    }

    @Override
    public int getKeyPressed() {
        return 0;
    }

    @Override
    public GameEvent getEvent() {
        return event;
    }

    void setEvent(GameEvent event) {
        this.event = event;
    }

    @Override
    public RandomGenerator getRandomGenerator() {
        return randomGenerator;
    }
}
