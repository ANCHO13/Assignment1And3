package com.algorithms.sort.bench;

import com.algorithms.sort.BubbleSort;
import com.algorithms.sort.QuickSort;
import com.algorithms.sort.RadixSort;
import com.algorithms.sort.data.DatasetGenerator;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.concurrent.TimeUnit;

/**
 * JMH benchmarks comparing BubbleSort, QuickSort, RadixSort, and Arrays.sort(int[])
 * across the four required input distributions, at N = 1,000,000.
 *
 * <p>Each algorithm sorts in place, so a fresh copy of the dataset is made for every
 * single invocation ({@code @Setup(Level.Invocation)}) and excluded from the measured
 * time -- only the {@code @Benchmark} method body (the sort call itself) is timed.
 *
 * <p>Bubble sort is O(n^2). At N = 1,000,000 its "random" and "reverse" distributions
 * take dramatically longer than the other three algorithms -- that slowdown is the point
 * of the comparison, but it also means a full run (4 algorithms x 4 distributions x
 * warmup + measurement iterations) can take a very long time if bubble sort is included
 * at full scale. To run a subset, pass a regex filter as a benchmark-jar argument, e.g.:
 * {@code java -jar target/benchmarks.jar QuickSort|RadixSort|ArraysSort}
 * to skip BubbleSort, or {@code java -jar target/benchmarks.jar BubbleSort} to run only it.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3)
@Measurement(iterations = 5)
@Fork(1)
public class SortBenchmarks {

    @State(Scope.Thread)
    public static class Dataset {

        @Param({"RANDOM", "SORTED", "REVERSE", "NEARLY_SORTED"})
        public Distribution distribution;

        private static final int SIZE = 1_000_000;
        private static final long SEED = 42L;

        private int[] source;
        public int[] working;

        @Setup(Level.Trial)
        public void setUpSource() {
            source = switch (distribution) {
                case RANDOM -> DatasetGenerator.uniformRandom(SIZE, SEED);
                case SORTED -> DatasetGenerator.sortedAscending(SIZE);
                case REVERSE -> DatasetGenerator.reverseSortedDescending(SIZE);
                case NEARLY_SORTED -> DatasetGenerator.nearlySorted(SIZE, SEED);
            };
        }

        @Setup(Level.Invocation)
        public void copyForInvocation() {
            working = source.clone();
        }
    }

    public enum Distribution {
        RANDOM, SORTED, REVERSE, NEARLY_SORTED
    }

    @Benchmark
    public void bubbleSort(Dataset dataset) {
        BubbleSort.sort(dataset.working);
    }

    @Benchmark
    public void quickSort(Dataset dataset) {
        QuickSort.sort(dataset.working);
    }

    @Benchmark
    public void radixSort(Dataset dataset) {
        RadixSort.sort(dataset.working);
    }

    @Benchmark
    public void arraysSort(Dataset dataset) {
        java.util.Arrays.sort(dataset.working);
    }
}
