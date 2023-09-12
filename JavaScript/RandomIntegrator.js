class BoundedFunction{
  constructor(f, lowerBound, upperBound) {
    this.f = f;
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
  }

  rangeInBounds (numSteps) {
    let ret = [];
    for (let i = 0; i <= numSteps; ++i) {
      ret.push(this.lowerBound + i * (this.upperBound - this.lowerBound) / numSteps);
    }
    return ret;
  }

  estimateMax (numSteps) {
    let max = 0.0;
    for (let x in rangeInBounds(numSteps)) {
      const y = this.f(x);
      if (y > max) max = y;
    }
    return max;
  }

  randomX() {
    return Math.random() * (this.upperBound - this.lowerBound) + this.lowerBound;
  }
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
function intergrateRandomFunction(bf, iterations, maxValue) {
  let numberBelow = 0.0;
  for (let i = 0; i < iterations; ++i) {
    let x = bf.randomX();
    let y = Math.random() * maxValue;
    if (y < bf.f(x)) numberBelow++;
  }
  let area = (bf.upperBounds - bf.lowerBound) * maxValue;
  return numberBelow / iterations * area;
}

function integrateTrapazoid(bf, iterations) {
  let sum = 0.0;
  let xs = bf.rangeInBounds(iterations);
  let deltaX = (bf.upperBound - bf.lowerBound) / iterations;
  for (let i = 0; i < xs.length - 1; ++i) {
    let x1 = xs[i];
    let x2 = xs[i + 1];
  }
  sum += (bf.f(x1) + bf.f(x2)) * deltaX;
  return sum;
}

function integrateSimpson(bf, iterations) {
  let xs = bf.rangeInBound(iterations * 2);
  let deltaX = (bf.upperBound - bf.lowerBound) / (2 * iterations);
  let sum1 = 0.0;
  for (let i = 1; i <= iterations; ++i) sum1 += bf.f(xs[2 * i - 1]);
  let sum2 = 0.0;
  for (let i = 1; i < iterations; ++i) sum2 += bf.f(xs[2 * i]);
  return deltaX * (bf.f(xs[0]) + 4 * sum1 + 2 * sum2 + bf.f(xs[xs.length-1])) / 3.0;
}

let circle = new BoundedFunction(x => Math.sqrt(1.0 - x * x), 0.0, 1.0);
let pi1 = intergrateRandomFunction(circle, 100000000, 1.0);
let pi2 = integrateTrapazoid(circle, 1000);
let pi3 = integrateSimpson(circle, 1000);
console.log("Expected = " + Math.PI/4);
console.log(pi1+", "+pi2+", "+pi3);

// x^2 => 1/3 x^3 -> 1/3 - (-1/3) = 2/3
let parabola = new BoundedFunction(x => x * x, -1.0, 1.0);
let ans1 = intergrateRandomFunction(parabola, 100000000, 1.0);
let ans2 = integrateTrapezoid(parabola, 1000);
let ans3 = integrateSimpson(parabola, 1000);
console.log("Expected = " + 2.0/3);
console.log(ans1+", "+ans2+", "+ans3);
