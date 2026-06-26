package com.algorithms.sort;

import com.algorithms.sort.data.DatasetGenerator;
import com.algorithms.sort.verify.SortVerifier;

import java.util.function.Consumer;

final class SortTestSupport {

    private static final int N = 2_000;
    private static final long SEED = 42L;

    private SortTestSupport() {
    }

    static void assertSortsAllDistributions(Consumer<int[]> sortFn) {
        assertSorts(sortFn, DatasetGenerator.uniformRandom(N, SEED));
        assertSorts(sortFn, DatasetGenerator.sortedAscending(N));
        assertSorts(sortFn, DatasetGenerator.reverseSortedDescending(N));
        assertSorts(sortFn, DatasetGenerator.nearlySorted(N, SEED));
    }

    static void assertSorts(Consumer<int[]> sortFn, int[] original) {
        int[] actual = original.clone();
        sortFn.accept(actual);
        SortVerifier.assertCorrect(original, actual);
    }
}
