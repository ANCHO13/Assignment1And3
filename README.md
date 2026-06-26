# Assignment 1: Algorithms & Java Benchmarking

Sorting 1,000,000 integers in Java, benchmarked with JMH (Java Microbenchmark Harness).

## Algorithms

- `BubbleSort` — classic bubble sort with early-exit + shrinking-bound optimization.
- `QuickSort` — in-place, median-of-three pivot, tail-iterates into the larger partition to bound stack depth at O(log n).
- `RadixSort` — LSD radix sort, base-256, 4 passes, handles negative 32-bit integers via a sign-bit flip.
- `Arrays.sort(int[])` — JDK reference sorter (dual-pivot quicksort).

All four live under `src/main/java/com/algorithms/sort/`.

## Dataset Generation

`com.algorithms.sort.data.DatasetGenerator` produces the four required distributions: uniform random, sorted ascending, reverse sorted descending, and nearly sorted (~1% random swaps).

## Build & Test

```bash
mvn clean compile
mvn test
```

Correctness is checked with `com.algorithms.sort.verify.SortVerifier` (provided by the assignment spec), comparing each algorithm's output against `Arrays.sort()` for equality and sortedness, across all four distributions plus edge cases (empty, single-element, duplicates, negative numbers, `Integer.MIN_VALUE`/`MAX_VALUE`).

## Running the Benchmarks

```bash
mvn package
java -jar target/benchmarks.jar
```

This runs all four algorithms across all four distributions at N = 1,000,000, with the warmup/measurement settings configured in `SortBenchmarks` (3 warmup iterations, 5 measurement iterations, 1 fork).

**Bubble sort is O(n²).** At N = 1,000,000, its random/reverse/nearly-sorted cases take on the order of tens of minutes (see `docs/report.md` / `docs/report.pdf` for measured and extrapolated figures) — this is the actual point of the comparison. To skip it and only benchmark the fast algorithms:

```bash
java -jar target/benchmarks.jar "quickSort|radixSort|arraysSort"
```

To run only bubble sort (e.g. just on the sorted distribution, where early-exit makes it fast):

```bash
java -jar target/benchmarks.jar "bubbleSort" -p distribution=SORTED
```

JMH benchmark-jar arguments accept a regex filter on benchmark name plus standard flags (`-wi`, `-i`, `-f`, `-w`, `-r`, `-p`, `-rf`, `-rff`, etc.) — see `java -jar target/benchmarks.jar -h`.

## Report

See [docs/report.md](docs/report.md) (source) and [docs/report.pdf](docs/report.pdf) (deliverable) for the analytical write-up: measured results, the bubble-sort extrapolation methodology, and discussion.
