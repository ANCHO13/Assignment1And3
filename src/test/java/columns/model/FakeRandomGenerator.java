package columns.model;

import columns.model.kernel.RandomGenerator;

class FakeRandomGenerator implements RandomGenerator {

    private final int[] values;
    private int index = 0;

    FakeRandomGenerator(int... values) {
        this.values = values;
    }

    @Override
    public int nextInt() {
        int value = values[index % values.length];
        index++;
        return value;
    }
}
