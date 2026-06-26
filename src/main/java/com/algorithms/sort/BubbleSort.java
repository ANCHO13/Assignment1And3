package com.algorithms.sort;

/**
 * Classic bubble sort with two combined optimizations:
 * - early exit: stop entirely once a full pass makes no swaps (best case O(n) on sorted input).
 * - shrinking bound: each pass guarantees the last element of that pass is in its final
 *   position, so the next pass need not re-scan it.
 */
public final class BubbleSort {

    private BubbleSort() {
    }

    public static void sort(int[] a) {
        if (a == null || a.length < 2) {
            return;
        }
        int n = a.length;
        boolean swapped;
        do {
            swapped = false;
            for (int i = 1; i < n; i++) {
                if (a[i - 1] > a[i]) {
                    int tmp = a[i - 1];
                    a[i - 1] = a[i];
                    a[i] = tmp;
                    swapped = true;
                }
            }
            n--;
        } while (swapped && n > 1);
    }
}
