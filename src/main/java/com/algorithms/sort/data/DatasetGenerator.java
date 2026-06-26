package com.algorithms.sort.data;

import java.util.Random;

/**
 * Generates the four input distributions required for benchmarking: uniform random,
 * already sorted ascending, reverse sorted descending, and nearly sorted (~1% random swaps).
 */
public final class DatasetGenerator {

    private static final double NEARLY_SORTED_SWAP_FRACTION = 0.01;

    private DatasetGenerator() {
    }

    public static int[] uniformRandom(int n, long seed) {
        Random random = new Random(seed);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = random.nextInt();
        }
        return a;
    }

    public static int[] sortedAscending(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i;
        }
        return a;
    }

    public static int[] reverseSortedDescending(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = n - i;
        }
        return a;
    }

    public static int[] nearlySorted(int n, long seed) {
        int[] a = sortedAscending(n);
        Random random = new Random(seed);
        int swaps = Math.max(1, (int) (n * NEARLY_SORTED_SWAP_FRACTION));
        for (int k = 0; k < swaps; k++) {
            int i = random.nextInt(n);
            int j = random.nextInt(n);
            int tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
        }
        return a;
    }
}
