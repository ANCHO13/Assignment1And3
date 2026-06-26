package columns.model;

import columns.model.kernel.Screen;

import java.util.ArrayList;
import java.util.List;

class FakeScreen implements Screen {

    final List<int[]> fillRectCalls = new ArrayList<>();
    final List<int[]> drawRectCalls = new ArrayList<>();
    final List<String> drawStringCalls = new ArrayList<>();
    final List<int[]> clearRectCalls = new ArrayList<>();
    final List<Integer> setColorCalls = new ArrayList<>();

    @Override
    public void setColor(int color) {
        setColorCalls.add(color);
    }

    @Override
    public void fillRect(int x, int y, int width, int height) {
        fillRectCalls.add(new int[] {x, y, width, height});
    }

    @Override
    public void drawRect(int x, int y, int width, int height) {
        drawRectCalls.add(new int[] {x, y, width, height});
    }

    @Override
    public void drawString(String string, int x, int y) {
        drawStringCalls.add(string);
    }

    @Override
    public void clearRect(int x, int y, int width, int height) {
        clearRectCalls.add(new int[] {x, y, width, height});
    }

    @Override
    public int Black() {
        return 0;
    }

    @Override
    public int White() {
        return 8;
    }
}
