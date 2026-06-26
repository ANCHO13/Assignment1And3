package columns.model;

import columns.model.kernel.ModelListener;

import java.util.ArrayList;
import java.util.List;

class FakeModelListener implements ModelListener {

    final List<Integer> levelChanges = new ArrayList<>();
    final List<int[]> tripletDetections = new ArrayList<>();
    final List<int[][]> fieldUpdates = new ArrayList<>();
    final List<Long> scoreUpdates = new ArrayList<>();

    @Override
    public void levelHasChanged(int level) {
        levelChanges.add(level);
    }

    @Override
    public void tripletDetected(int a, int b, int c, int d, int i, int j) {
        tripletDetections.add(new int[] {a, b, c, d, i, j});
    }

    @Override
    public void fieldWasUpdated(int[][] newField) {
        fieldUpdates.add(newField);
    }

    @Override
    public void scoreUpdated(long score) {
        scoreUpdates.add(score);
    }
}
