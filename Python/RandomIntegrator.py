
import math
import random

class BoundedFunction:
  def __init__(self, f, lowerBound, upperBound):
    self.f = f
    self.lowerBound = lowerBound
    self.upperBound = upperBound
    
  def rangeInBounds(self, numSteps):
    i = 0
    while i <= numSteps:
      yield self.lowerBound + i * (self.upperBound - self.lowerBound) / numSteps
      i += 1
    return

  def estimateMax(self, numSteps):
    max = 0.0
    for x in self.rangeInBounds(numSteps):
      y = self.f(x)
      if y > max:
        max = y
    return max

  def randomX(self):
    return random.random() * (self.upperBound - self.lowerBound) + self.lowerBound


def randomIntegrator():
  circle = BoundedFunction(lambda x: math.sqrt(1.0 - x * x), 0.0, 1.0)
  pi1 = intergrateRandomFunction(circle, 100000000, 1.0)
  pi2 = integrateTrapazoid(circle, 1000)
  pi3 = integrateSimpson(circle, 1000)
  print(f"Expected = {math.pi/4}")
  print(f"{pi1}, {pi2}, {pi3}")

  # x^2 => 1/3 x^3 -> 1/3 - (-1/3) = 2/3
  parabola = BoundedFunction(lambda x: x * x, -1.0, 1.0)
  ans1 = intergrateRandomFunction(parabola, 100000000, 1.0)
  ans2 = integrateTrapezoid(parabola, 1000)
  ans3 = integrateSimpson(parabola, 1000)
  print(f"Expected = {2.0/3}")
  print(f"{ans1}, {ans2}, {ans3}")

"""
  * This function does a definite integral by measuring the area under a curve of a positive function by randomly
  * selective points in a bounding rectangle and calculating which fraction are under the curve. The result
  * is an estimate of the integral of f from a to b.
  *
  * @param bf the bounded function to integrate
  * @param iterations how many random positions to test
  * @param maxValue the maximum value the function will take over this range
  * @return estimate of the integral of f from a to b
"""
def intergrateRandomFunction(bf, iterations, maxValue):
  numberBelow = 0
  for i in range(0, iterations):
    x = bf.randomX()
    y = random.random() * maxValue
    if y < bf.f(x):
      numberBelow += 1
  area = (bf.upperBounds - bf.lowerBound) * maxValue
  return numberBelow / iterations * area # Leave off return !!!

def integrateTrapazoid(bf, iterations):
  sum = 0.0
  xs = list(bf.rangeInBounds(iterations))
  deltaX = (bf.upperBound - bf.lowerBound) / iterations
  for i in range(0, len(xs) - 1):
    x1 = xs[i]
    x2 = xs[i+1]
  sum += (bf.f(x1) + bf.f(x2)) * deltaX
  return sum

def integrateSimpson(bf, iterations):
  xs = list(bf.rangeInBound(iterations * 2))
  deltaX = (bf.upperBound - bf.lowerBound) / (2 * iterations)
  sum1 = 0.0
  for i in range(1, iterations + 1):
    sum1 += bf.f(xs[2 * i - 1])
  sum2 = 0.0
  for i in range(1, iterations):
    sum2 += bf.f(xs[2 * i])
  return deltaX * (bf.f(xs[0]) + 4 * sum1 + 2 * sum2 + bf.f(xs[-1])) / 3.0

randomIntegrator()