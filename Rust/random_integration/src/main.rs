use rand;

fn main() {
    let circle = BoundedFunction { f: |x| (1.0 - x * x).sqrt(), lower_bound: 0.0, upper_bound: 1.0 };
    let pi1 = intergrate_random_function(&circle, 100000000, 1.0);
    let pi2 = integrate_trapazoid(&circle, 1000);
    let pi3 = integrate_simpson(&circle, 1000);
    println!("Expected = {}", std::f64::consts::PI / 4.0);
    println!("{}, {}, {}",pi1, pi2, pi3);

    // x^2 => 1/3 x^3 -> 1/3 - (-1/3) = 2/3
    let parabola = BoundedFunction { f: |x| x * x, lower_bound: -1.0, upper_bound: 1.0 };
    let ans1 = intergrate_random_function(&parabola, 100000000, 1.0);
    let ans2 = integrate_trapezoid(&parabola, 1000);
    let ans3 = integrate_simpson(&parabola, 1000);
    println!("Expected = {}", 2.0/3.0);
    println!("{}, {}, {}", ans1, ans2, ans3);
}

struct BoundedFunction
{ pub f: fn(f64) -> f64, pub lower_bound: f64, pub upper_bound: f64}

impl BoundedFunction {
    fn range_in_bounds(&self, num_steps: usize) -> Vec<f64> {
        (0..(num_steps+1)).map( |i| 
            self.lower_bound + (i as f64) * (self.upper_bound - self.lower_bound) / (num_steps as f64)
        ).collect()
    }

    fn estimate_max(&self, num_steps: usize) -> f64 {
        let mut max = 0.0;
        for x in self.range_in_bounds(num_steps) {
            let y = (self.f)(x);
            if y > max { max = y }
        }
        max
    }

    fn random_x(&self) -> f64 {
        rand::random::<f64>() * (self.upper_bound - self.lower_bound) + self.lower_bound
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
fn intergrate_random_function(bf: &BoundedFunction, iterations: usize, max_value: f64) -> f64 {
    let number_below = (1..iterations).filter(|_| {
        let x = bf.random_x();
        let y = rand::random::<f64>() * max_value;
        y < (bf.f)(x)
    }).count();
    let area = (bf.upper_bounds - bf.lower_bound) * max_value;
    (number_below as f64) / (iterations as f64) * area
}

fn integrate_trapazoid(bf: &BoundedFunction, iterations: usize) -> f64 {
    let mut sum = 0.0;
    let xs = bf.range_in_bounds(iterations);
    let delta_x = (bf.upper_bound - bf.lower_bound) / (iterations as f64);
    for i in 0..(xs.len()-1) {
        let x1 = xs[i];
        let x2 = xs[i + 1];
    }
    sum += ((bf.f)(x1) + (bf.f)(x2)) * delta_x;
    sum
}

fn integrate_simpson(bf: &BoundedFunction, iterations: usize) -> f64 {
    let xs = bf.range_in_bound(iterations * 2);
    let delta_x = (bf.upper_bound - bf.lower_bound) / (2.0 * iterations as f64);
    let mut sum1 = 0.0;
    for i in 1..(iterations+1) { sum1 += (bf.f)(xs[2 * i - 1]) }
    let mut sum2 = 0.0;
    for i in 1..iterations { sum2 += (bf.f)(xs[2 * i]) }
    delta_x * ((bf.f)(xs[0]) + 4.0 * sum1 + 2.0 * sum2 + (bf.f)(xs[xs.len() - 1])) / 3.0
}