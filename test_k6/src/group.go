package main

import metric "ederfmatos/test-benckmark/src/metrics"

func groupMetrics(values []metric.Metric) []metric.Metric {
	metricMap := make(map[string][]metric.Metric)
	for _, item := range values {
		metricMap[item.Scenario] = append(metricMap[item.Scenario], item)
	}
	var averagedMetrics []metric.Metric

	for scenario, metrics := range metricMap {
		avgMetric := metric.Metric{
			Scenario: scenario,
		}
		avgMetric.TotalRequests = calculateAverage(metrics, func(item metric.Metric) int {
			return item.TotalRequests
		})
		avgMetric.AverageRequestsPerSecond = calculateAverageFloat(metrics, func(item metric.Metric) float64 {
			return item.AverageRequestsPerSecond
		})
		avgMetric.Memory = calculateAverageFromRange(metrics, func(item metric.Metric) metric.MetricRange {
			return item.Memory
		})
		avgMetric.CPU = calculateAverageFromRange(metrics, func(item metric.Metric) metric.MetricRange {
			return item.CPU
		})
		avgMetric.Threads = calculateAverageFromRange(metrics, func(item metric.Metric) metric.MetricRange {
			return item.Threads
		})
		avgMetric.ResponseTime = calculateAverageFromResponseTime(metrics)
		averagedMetrics = append(averagedMetrics, avgMetric)
	}
	return averagedMetrics
}

func calculateAverageFromResponseTime(metrics []metric.Metric) []metric.ResponseTime {
	minimumGetTransaction := float64(0)
	averageGetTransaction := float64(0)
	maximumGetTransaction := float64(0)
	p90GetTransaction := float64(0)
	p95GetTransaction := float64(0)
	p99GetTransaction := float64(0)

	minimumCreateTransaction := float64(0)
	averageCreateTransaction := float64(0)
	maximumCreateTransaction := float64(0)
	p90CreateTransaction := float64(0)
	p95CreateTransaction := float64(0)
	p99CreateTransaction := float64(0)

	for _, metricItem := range metrics {
		minimumCreateTransaction += metricItem.ResponseTime[0].Minimum
		averageCreateTransaction += metricItem.ResponseTime[0].Average
		maximumCreateTransaction += metricItem.ResponseTime[0].Maximum
		p90CreateTransaction += metricItem.ResponseTime[0].P90
		p95CreateTransaction += metricItem.ResponseTime[0].P95
		p99CreateTransaction += metricItem.ResponseTime[0].P99

		minimumGetTransaction += metricItem.ResponseTime[1].Minimum
		averageGetTransaction += metricItem.ResponseTime[1].Average
		maximumGetTransaction += metricItem.ResponseTime[1].Maximum
		p90GetTransaction += metricItem.ResponseTime[1].P90
		p95GetTransaction += metricItem.ResponseTime[1].P95
		p99GetTransaction += metricItem.ResponseTime[1].P99
	}

	totalMetrics := float64(len(metrics))
	return []metric.ResponseTime{
		{
			MetricRange: metric.MetricRange{
				Minimum: minimumCreateTransaction / totalMetrics,
				Average: averageCreateTransaction / totalMetrics,
				Maximum: maximumCreateTransaction / totalMetrics,
			},
			MetricPercentageRange: metric.MetricPercentageRange{
				P90: p90CreateTransaction / totalMetrics,
				P95: p95CreateTransaction / totalMetrics,
				P99: p99CreateTransaction / totalMetrics,
			},
			Path: "POST /transactions",
		},
		{
			MetricRange: metric.MetricRange{
				Minimum: minimumGetTransaction / totalMetrics,
				Average: averageGetTransaction / totalMetrics,
				Maximum: maximumGetTransaction / totalMetrics,
			},
			MetricPercentageRange: metric.MetricPercentageRange{
				P90: p90GetTransaction / totalMetrics,
				P95: p95GetTransaction / totalMetrics,
				P99: p99GetTransaction / totalMetrics,
			},
			Path: "GET /transactions/{id}",
		},
	}
}

func calculateAverageFromRange(metrics []metric.Metric, getRange func(item metric.Metric) metric.MetricRange) metric.MetricRange {
	return metric.MetricRange{
		Minimum: calculateAverageFloat(metrics, func(item metric.Metric) float64 {
			return getRange(item).Minimum
		}),
		Average: calculateAverageFloat(metrics, func(item metric.Metric) float64 {
			return getRange(item).Average
		}),
		Maximum: calculateAverageFloat(metrics, func(item metric.Metric) float64 {
			return getRange(item).Maximum
		}),
	}
}

func calculateAverageFloat(metrics []metric.Metric, getValue func(item metric.Metric) float64) float64 {
	total := float64(0)
	for _, item := range metrics {
		total += getValue(item)
	}
	return total / float64(len(metrics))
}

func calculateAverage(metrics []metric.Metric, getValue func(item metric.Metric) int) int {
	total := 0
	for _, item := range metrics {
		total += getValue(item)
	}
	return total / len(metrics)
}
