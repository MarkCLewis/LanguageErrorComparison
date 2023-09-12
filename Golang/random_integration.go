package main

import (
	"fmt"
	"math"
	"math/rand"
)

func main() {
	circle := BoundedFunction{func(x float64) float64 { return math.Sqrt(1.0 - x*x) }, 0.0, 1.0}
	pi1 := intergrateRandomFunction(circle, 100000000, 1.0)
	pi2 := integrateTrapazoid(circle, 1000)
	pi3 := integrateSimpson(circle, 1000)
	fmt.Printf("Expected = %f\n", math.Pi/4)
	fmt.Printf("%f, %f, %f\n", pi1, pi2, pi3)

	// x^2 => 1/3 x^3 -> 1/3 - (-1/3) = 2/3
	parabola := BoundedFunction{func(x float64) float64 { return x * x }, -1.0, 1.0}
	ans1 := intergrateRandomFunction(parabola, 100000000, 1.0)
	ans2 := integrateTrapezoid(parabola, 1000)
	ans3 := integrateSimpson(parabola, 1000)
	fmt.Printf("Expected = %f", 2.0/3)
	fmt.Printf("%f, %f, %f\n", ans1, ans2, ans3)
}

type BoundedFunction struct {
	f          func(float64) float64
	lowerBound float64
	upperBound float64
}

func (bf *BoundedFunction) rangeInBounds(numSteps int) []float64 {
	var ret []float64
	for i := 0; i <= numSteps; i++ {
		ret = append(ret, bf.lowerBound+float64(i)*(bf.upperBound-bf.lowerBound)/float64(numSteps))
	}
	return ret
}

func (bf *BoundedFunction) estimateMax(numSteps int) float64 {
	max := 0.0
	for _, x := range bf.rangeInBounds(numSteps) {
		y := bf.f(x)
		if y > max {
			max = y
		}
	}
	return max
}

func (bf *BoundedFunction) randomX() float64 {
	return rand.Float64()*(bf.upperBound-bf.lowerBound) + bf.lowerBound
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
func intergrateRandomFunction(bf BoundedFunction, iterations int64, maxValue float64) float64 {
	numberBelow := 0
	for i := int64(0); i <= iterations; i++ {
		x := bf.randomX()
		y := rand.Float64() * maxValue
		if y < bf.f(x) {
			numberBelow++
		}
	}
	area := (bf.upperBounds - bf.lowerBound) * maxValue
	return area * float64(numberBelow) / float64(iterations)
}

func integrateTrapazoid(bf BoundedFunction, iterations int) float64 {
	sum := 0.0
	xs := bf.rangeInBounds(iterations)
	deltaX := (bf.upperBound - bf.lowerBound) / float64(iterations)
	for i := 0; i < len(xs)-1; i++ {
		x1 := xs[i]
		x2 := xs[i+1]
	}
	sum += (bf.f(x1) + bf.f(x2)) * deltaX
	return sum
}

func integrateSimpson(bf BoundedFunction, iterations int) float64 {
	xs := bf.rangeInBound(iterations * 2)
	deltaX := (bf.upperBound - bf.lowerBound) / float64(2*iterations)
	sum1 := 0.0
	for i := 1; i <= iterations; i++ {
		sum1 += bf.f(xs[2*i-1])
	}
	sum2 := 0.0
	for i := 1; i < iterations; i++ {
		sum2 += bf.f(xs[2*i])
	}
	return deltaX * (bf.f(xs[0]) + 4*sum1 + 2*sum2 + bf.f(xs[len(xs)-1])) / 3.0
}
