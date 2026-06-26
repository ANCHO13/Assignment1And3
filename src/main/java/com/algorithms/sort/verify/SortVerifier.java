package com.algorithms.sort.verify;

import java.util.Arrays;

public final class SortVerifier {

    private SortVerifier() {
    }

    public static boolean isSorted(int[] a) {
        for (int i = 1; i < a.length; i++) {
            if (a[i - 1] > a[i]) {
                return false;
            }
        }
        return true;
    }

    public static void assertCorrect(int[] original, int[] actual) {
        int[] expected = Arrays.copyOf(original, original.length);
        Arrays.sort(expected);

        if (!isSorted(actual)) {
            throw new AssertionError("Array is not sorted");
        }

        if (!Arrays.equals(actual, expected)) {
            throw new AssertionError("Sorted result differs from reference output");
        }
    }
}
