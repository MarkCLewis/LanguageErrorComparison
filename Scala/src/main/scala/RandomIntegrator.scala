@main def randomIntegrator: Unit = {
  val circle = BoundedFunction(x => math.sqrt(1.0 - x * x), 0.0, 1.0)
  val pi1 = intergrateRandomFunction(circle, 100000000)(1.0)
  val pi2 = integrateTrapazoid(circle, 1000)
  val pi3 = integrateSimpson(circle, 1000)
  println(s"Expected = ${math.Pi/4}")
  println(s"$pi1, $pi2, $pi3")

  // x^2 => 1/3 x^3 -> 1/3 - (-1/3) = 2/3
  val parabola = BoundedFunction(x => x * x, -1.0, 1.0)
  val ans1 = intergrateRandomFunction(parabola, 100000000)(1.0)
  val ans2 = integrateTrapezoid(parabola, 1000)
  val ans3 = integrateSimpson(parabola, 1000)
  println(s"Expected = ${2.0/3}")
  println(s"$ans1, $ans2, $ans3")
}

case class BoundedFunction(f: Double => Double, lowerBound: Double, upperBound: Double) {
  def rangeInBounds(numSteps: Int): IndexedSeq[Double] = (0 to numSteps).map { i =>
    lowerBound + i * (upperBound - lowerBound) / numSteps
  }

  def estimateMax(numSteps: Int): Double = {
    var max = 0.0
    for (x <- rangeInBounds(numSteps)) {
      val y = f(x)
      if (y > max) max = y
    }
    max
  }

  def randomX(): Double = math.random() * (upperBound - lowerBound) + lowerBound
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
def intergrateRandomFunction(bf: BoundedFunction, iterations: Long)(maxValue: Double = bf.estimateMax(10000)): Double = {
  val numberBelow = (1L to iterations).count { i =>
    val x = bf.randomX()
    val y = math.random() * maxValue
    y < bf.f(x)
  }
  val area = (bf.upperBounds - bf.lowerBound) * maxValue
  numberBelow.toDouble / iterations * area
}

def integrateTrapazoid(bf: BoundedFunction, iterations: Int): Double = {
  var sum = 0.0
  val xs = bf.rangeInBounds(iterations)
  val deltaX = (bf.upperBound - bf.lowerBound) / iterations
  for ((x1, x2) <- xs.lazyZip(xs.tail)) {
  }
  sum += (bf.f(x1) + bf.f(x2)) * deltaX
  sum
}

def integrateSimpson(bf: BoundedFunction, iterations: Int): Double = {
  val xs = bf.rangeInBound(iterations * 2)
  val deltaX = (bf.upperBound - bf.lowerBound) / (2 * iterations)
  var sum1 = 0.0
  for (i <- 1 to iterations) sum1 += bf.f(xs(2 * i - 1))
  var sum2 = 0.0
  for (i <- 1 to iterations - 1) sum2 += bf.f(xs(2 * i))
  deltaX * (bf.f(xs(0)) + 4 * sum1 + 2 * sum2 + bf.f(xs.last)) / 3.0
}