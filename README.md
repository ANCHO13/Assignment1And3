# Assignment 1 & 3

## Build Note: JDK Version

This repo targets Java 17 (`maven.compiler.source/target = 17`), but `columns.Columns` extends the legacy `java.applet.Applet`. That package was deprecated for removal since Java 9 and is **gone entirely** on newer JDKs (confirmed missing on a Java 26 toolchain). If `mvn compile`/`mvn test`/`mvn package` fail with `package java.applet does not exist`, your `mvn` is running on a JDK that has actually dropped it — point `JAVA_HOME` at an older JDK that still has it (JDK 17–21 all still include it, just deprecated) before running Maven, e.g.:

```bash
export JAVA_HOME=/path/to/jdk-17-or-newer-but-not-too-new
mvn clean test
```

## Assignment 1: Algorithms & Java Benchmarking

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

## Assignment 3: Unit Testing The Columns Game

Unit tests for a Columns-style falling-block game, copied from `Java2026/src/columns` in the `KIUJava2026` repo (excluding the untested legacy `columns/original`) into `src/main/java/columns/`.

- `FigureTest`, `BoardTest`, `GameControllerTest` — 39 tests under `src/test/java/columns/model/`, package `columns.model` (same package as the package-private production classes, per the assignment's testing guidance — no production visibility was widened).
- `FakeRandomGenerator`, `FakePlatform`, `FakeScreen`, `FakeModelListener` — hand-written test doubles, also in `src/test/java/columns/model/`.
- No production-code changes were needed — see [docs/test-report.md](docs/test-report.md) for why.

Run with:

```bash
mvn test
```

See [docs/test-report.md](docs/test-report.md) for the required test report: behavior tested, framework, test doubles, what was hard to test, and design problems the tests revealed.
