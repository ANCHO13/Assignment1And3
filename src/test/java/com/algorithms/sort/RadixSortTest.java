package com.algorithms.sort;

import com.algorithms.sort.verify.SortVerifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class RadixSortTest {

    @Test
    void sortsAllRequiredDistributions() {
        SortTestSupport.assertSortsAllDistributions(RadixSort::sort);
    }

    @Test
    void handlesEmptyArray() {
        int[] a = {};
        RadixSort.sort(a);
        assertDoesNotThrow(() -> SortVerifier.assertCorrect(new int[] {}, a));
    }

    @Test
    void handlesSingleElement() {
        int[] a = {7};
        RadixSort.sort(a);
        SortVerifier.assertCorrect(new int[] {7}, a);
    }

    @Test
    void handlesDuplicatesAndNegatives() {
        int[] original = {5, -3, 5, 0, -3, Integer.MIN_VALUE, Integer.MAX_VALUE, -3};
        SortTestSupport.assertSorts(RadixSort::sort, original);
    }

    @Test
    void handlesExtremeIntBoundaries() {
        int[] original = {Integer.MAX_VALUE, Integer.MIN_VALUE, 0, -1, 1, Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1};
        SortTestSupport.assertSorts(RadixSort::sort, original);
    }

    @Test
    void handlesAllNegativeValues() {
        int[] original = {-5, -100, -1, -999999, -2};
        SortTestSupport.assertSorts(RadixSort::sort, original);
    }
}
