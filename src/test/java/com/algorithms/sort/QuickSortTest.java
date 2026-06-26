package com.algorithms.sort;

import com.algorithms.sort.data.DatasetGenerator;
import com.algorithms.sort.verify.SortVerifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class QuickSortTest {

    @Test
    void sortsAllRequiredDistributions() {
        SortTestSupport.assertSortsAllDistributions(QuickSort::sort);
    }

    @Test
    void handlesEmptyArray() {
        int[] a = {};
        QuickSort.sort(a);
        assertDoesNotThrow(() -> SortVerifier.assertCorrect(new int[] {}, a));
    }

    @Test
    void handlesSingleElement() {
        int[] a = {7};
        QuickSort.sort(a);
        SortVerifier.assertCorrect(new int[] {7}, a);
    }

    @Test
    void handlesDuplicatesAndNegatives() {
        int[] original = {5, -3, 5, 0, -3, Integer.MIN_VALUE, Integer.MAX_VALUE, -3};
        SortTestSupport.assertSorts(QuickSort::sort, original);
    }

    @Test
    void handlesAllEqualElements() {
        int[] original = new int[500];
        java.util.Arrays.fill(original, 42);
        SortTestSupport.assertSorts(QuickSort::sort, original);
    }

    @Test
    void doesNotStackOverflowOnLargeSortedInput() {
        int[] original = DatasetGenerator.sortedAscending(200_000);
        SortTestSupport.assertSorts(QuickSort::sort, original);
    }

    @Test
    void doesNotStackOverflowOnLargeReverseSortedInput() {
        int[] original = DatasetGenerator.reverseSortedDescending(200_000);
        SortTestSupport.assertSorts(QuickSort::sort, original);
    }
}
