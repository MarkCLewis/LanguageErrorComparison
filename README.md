# Language Error Comparison Project

## Goals

The goal of this repository is to compare the difficulty of finding certain
types of common errors across languages. The program does numerical integration
using three different approaches and each is applied to two different functions.
These were made to work in each of six languages.

After the code was working, common bugs were introduced into each of them.
The same bugs (mostly typos) were introduced into all six versions.

When you pull this repository down, go into the directories for any languages
you know and see if you can find all the bugs. Note that all three versions of
the integration should produce values that are accurate to a few digits with
the random being the least accurate.

## Performance

Before introducing the bugs, I did some quick performance testing. Note that
the code is fairly idiomatic, and I didn't spend time optimizing it. For
example, rewriting the Scala with while loops would produce perfomance
equivalent to Java. That code would probably be more error prone though, and
error finding is the primary goal of this exercise. The big surprise here is
that JavaScript wound up being the second fastest language for this little
test, with performance slightly better than Golang and the JVM languages.
What isn't surprising is that Rust is a solid 2x faster than the other languages
or that Python is more than 20x slower than Rust.

| Language   | Runtime (secs) |
| ---------- | ------- |
| Rust       | 3.6 +- 0.014 |
| JavaScript | 6.2 +- 0.11 |
| Java       | 7.9 +- 0.01 |
| Scala      | 8.8 +- 0.04 |
| Golang     | 7.3 +- 0.05 |
| Python     | 82 +- 6 |

For each language, 5 timing tests were run. The table has the mean and standard deviation.

Platform: Dell Precision 5530 with Intel Core i7-8850 @ 2.6 GHz running Linux Mint

JVM: Java 17.0.6 with GraalVM EE 22.3.1

Node: v10.19.0

Golang: 1.20.2

Rust: Cargo 1.68.0

Python: 3.8.10 (For Python fans who want to argue that newer versions are faster, remember they are 10-20% faster, not 10-20x faster.)
