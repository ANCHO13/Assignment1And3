package com.algorithms.sort;

/**
 * LSD (least-significant-digit) radix sort for 32-bit signed integers, base-256
 * (one byte per pass, 4 passes total).
 *
 * <p>Negative numbers are handled by flipping the sign bit of every value before
 * sorting (XOR with {@code 0x80000000}) and flipping it back afterward. Two's-complement
 * negative integers, after this flip, sort correctly when their bit patterns are compared
 * as plain unsigned bytes: the flip maps the signed ordering onto the unsigned bit-pattern
 * ordering that byte-wise counting sort naturally produces.
 */
public final class RadixSort {

    private static final int BITS_PER_PASS = 8;
    private static final int RADIX = 256;
    private static final int PASSES = 4;
    private static final int SIGN_BIT = 0x80000000;

    private RadixSort() {
    }

    public static void sort(int[] a) {
        if (a == null || a.length < 2) {
            return;
        }
        int n = a.length;

        for (int i = 0; i < n; i++) {
            a[i] ^= SIGN_BIT;
        }

        int[] buffer = new int[n];
        int[] src = a;
        int[] dst = buffer;

        for (int pass = 0; pass < PASSES; pass++) {
            int shift = pass * BITS_PER_PASS;
            int[] count = new int[RADIX + 1];

            for (int i = 0; i < n; i++) {
                int b = (src[i] >>> shift) & 0xFF;
                count[b + 1]++;
            }
            for (int i = 0; i < RADIX; i++) {
                count[i + 1] += count[i];
            }
            for (int i = 0; i < n; i++) {
                int b = (src[i] >>> shift) & 0xFF;
                dst[count[b]++] = src[i];
            }

            int[] tmp = src;
            src = dst;
            dst = tmp;
        }

        for (int i = 0; i < n; i++) {
            a[i] = src[i] ^ SIGN_BIT;
        }
    }
}
