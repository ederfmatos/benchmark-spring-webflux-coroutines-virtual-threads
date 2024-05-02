package metrics

import (
	"math"
)

func minimum(numbers []float64) float64 {
	minValue := float64(-1)
	for _, num := range numbers {
		if num > 0 && (minValue == -1 || num < minValue) && !math.IsNaN(num) {
			minValue = num
		}
	}
	return minValue
}

func maximum(numbers []float64) float64 {
	maxValue := float64(0)
	for _, num := range numbers {
		if !math.IsNaN(num) && num > maxValue {
			maxValue = num
		}
	}
	return maxValue
}

func average(numbers []float64) float64 {
	sum := float64(0)
	quantity := float64(0)
	for _, num := range numbers {
		if num > 0 {
			sum += num
			quantity++
		}
	}
	average := sum / quantity
	if math.IsNaN(average) {
		return 0.0
	}
	return average
}
