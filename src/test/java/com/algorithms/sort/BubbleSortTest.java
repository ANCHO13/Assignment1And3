package com.algorithms.sort;

import com.algorithms.sort.verify.SortVerifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class BubbleSortTest {

    @Test
    void sortsAllRequiredDistributions() {
        SortTestSupport.assertSortsAllDistributions(BubbleSort::sort);
    }

    @Test
    void handlesEmptyArray() {
        int[] a = {};
        BubbleSort.sort(a);
        assertDoesNotThrow(() -> SortVerifier.assertCorrect(new int[] {}, a));
    }

    @Test
    void handlesSingleElement() {
        int[] a = {7};
        BubbleSort.sort(a);
        SortVerifier.assertCorrect(new int[] {7}, a);
    }

    @Test
    void handlesDuplicatesAndNegatives() {
        int[] original = {5, -3, 5, 0, -3, Integer.MIN_VALUE, Integer.MAX_VALUE, -3};
        SortTestSupport.assertSorts(BubbleSort::sort, original);
    }
}
