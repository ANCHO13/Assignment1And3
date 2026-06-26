package com.algorithms.sort;

/**
 * In-place quicksort for {@code int[]}.
 *
 * <p>Pivot strategy: median-of-three (first, middle, last element of the current range).
 * A naive first-or-last-element pivot degrades to O(n^2) on already-sorted or
 * reverse-sorted input -- exactly two of the distributions this assignment benchmarks --
 * so median-of-three is used to keep those cases close to the average O(n log n) case.
 *
 * <p>After partitioning, the algorithm recurses into the smaller side and loops into the
 * larger side (tail-iteration) so worst-case stack depth stays O(log n) instead of O(n).
 * Subarrays of size 3 or fewer are sorted directly (insertion-sort style) to avoid
 * edge cases in the partition scheme.
 */
public final class QuickSort {

    private static final int SMALL_CUTOFF = 3;

    private QuickSort() {
    }

    public static void sort(int[] a) {
        if (a == null || a.length < 2) {
            return;
        }
        quicksort(a, 0, a.length - 1);
    }

    private static void quicksort(int[] a, int lo, int hi) {
        while (hi - lo >= SMALL_CUTOFF) {
            int p = partition(a, lo, hi);
            if (p - lo < hi - p) {
                quicksort(a, lo, p - 1);
                lo = p + 1;
            } else {
                quicksort(a, p + 1, hi);
                hi = p - 1;
            }
        }
        insertionSort(a, lo, hi);
    }

    private static int partition(int[] a, int lo, int hi) {
        int mid = lo + (hi - lo) / 2;
        medianOfThree(a, lo, mid, hi);
        swap(a, mid, hi - 1);
        int pivot = a[hi - 1];

        int i = lo;
        int j = hi - 1;
        while (true) {
            while (a[++i] < pivot) {
            }
            while (a[--j] > pivot) {
            }
            if (i >= j) {
                break;
            }
            swap(a, i, j);
        }
        swap(a, i, hi - 1);
        return i;
    }

    private static void medianOfThree(int[] a, int lo, int mid, int hi) {
        if (a[mid] < a[lo]) {
            swap(a, lo, mid);
        }
        if (a[hi] < a[lo]) {
            swap(a, lo, hi);
        }
        if (a[hi] < a[mid]) {
            swap(a, mid, hi);
        }
    }

    private static void insertionSort(int[] a, int lo, int hi) {
        for (int i = lo + 1; i <= hi; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= lo && a[j] > key) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = key;
        }
    }

    private static void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
}
