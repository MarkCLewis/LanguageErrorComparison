package integrate;

import java.util.function.Function;

record BoundedFunction(Function<Double, Double> f, double lowerBound, double upperBound) {
  public double[] rangeInBounds(int numSteps) {
    double[] ret = new double[numSteps + 1];
    for (int i = 0; i <= numSteps; ++i) {
      ret[i] = lowerBound + i * (upperBound - lowerBound) / numSteps;
    }
    return ret;
  }

  double estimateMax(int numSteps) {
    var max = 0.0;
    for (double x: rangeInBounds(numSteps)) {
      var y = f.apply(x);
      if (y > max) max = y;
    }
    return max;
  }

  double randomX() {
    return Math.random() * (upperBound - lowerBound) + lowerBound;
  }
}

public class RandomIntegrator {
  public static void main(String[] args) {
    var circle = new BoundedFunction(x -> Math.sqrt(1.0 - x * x), 0.0, 1.0);
    var pi1 = intergrateRandomFunction(circle, 100000000, 1.0);
    var pi2 = integrateTrapazoid(circle, 1000);
    var pi3 = integrateSimpson(circle, 1000);
    System.out.println("Expected = " + Math.PI/4);
    System.out.println(pi1+", "+pi2+", "+pi3);

    // x^2 => 1/3 x^3 -> 1/3 - (-1/3) = 2/3
    var parabola = new BoundedFunction(x -> x * x, -1.0, 1.0);
    var ans1 = intergrateRandomFunction(parabola, 100000000, 1.0);
    var ans2 = integrateTrapezoid(parabola, 1000);
    var ans3 = integrateSimpson(parabola, 1000);
    System.out.println("Expected = " + 2.0/3);
    System.out.println(ans1+", "+ans2+", "+ans3);
  }

    

  /**
    * This function does a definite integral by measuring the area under a curve of a positive function by randomly
    * selective points in a bounding rectangle and calculating which fraction are under the curve. The result
    * is an estimate of the integral of f from a to b.
    *
    * @param bf the bounded function to integrate
    * @param iterations how many random positions to test
    * @param maxValue the maximum value the function will take over this range
    * @return estimate of the integral of f from a to b
    */
  static double intergrateRandomFunction(BoundedFunction bf, long iterations, double maxValue) {
    var numberBelow = 0.0;
    for (long i = 0; i < iterations; ++i) {
      var x = bf.randomX();
      var y = Math.random() * maxValue;
      if (y < bf.f().apply(x)) numberBelow++;
    }
    var area = (bf.upperBounds() - bf.lowerBound()) * maxValue;
    return numberBelow / iterations * area;
  }

  static double integrateTrapazoid(BoundedFunction bf, int iterations) {
    var sum = 0.0;
    var xs = bf.rangeInBounds(iterations);
    var deltaX = (bf.upperBound() - bf.lowerBound()) / iterations;
    for (int i = 0; i < xs.length - 1; ++i) {
      var x1 = xs[i];
      var x2 = xs[i + 1];
    }
    sum += (bf.f().apply(x1) + bf.f().apply(x2)) * deltaX;
    return sum;
  }

  static double integrateSimpson(BoundedFunction bf, int iterations) {
    var xs = bf.rangeInBound(iterations * 2);
    var deltaX = (bf.upperBound() - bf.lowerBound()) / (2 * iterations);
    var sum1 = 0.0;
    for (int i = 1; i <= iterations; ++i) sum1 += bf.f().apply(xs[2 * i - 1]);
    var sum2 = 0.0;
    for (int i = 1; i < iterations; ++i) sum2 += bf.f().apply(xs[2 * i]);
    return deltaX * (bf.f().apply(xs[0]) + 4 * sum1 + 2 * sum2 + bf.f().apply(xs[xs.length-1])) / 3.0;
  }
}