package chart

import (
	"ederfmatos/test-benckmark/src/metrics"
)

func GenerateChartsFromMetrics(values []metrics.Metric) {
	GenerateChart(values, "Total de requisições", func(metricValue metrics.Metric) any {
		return metricValue.TotalRequests
	})
	GenerateChart(values, "Média de requisições por segundo", func(metricValue metrics.Metric) any {
		return metricValue.AverageRequestsPerSecond
	})
	GenerateChartSeriesFromMetrics(values, "Consumo de memória (em Mb)", func(metricValue metrics.Metric) metrics.MetricRange {
		return metricValue.Memory
	})
	GenerateChartSeriesFromMetrics(values, "Consumo de CPU (%)", func(metricValue metrics.Metric) metrics.MetricRange {
		return metricValue.CPU
	})
	GenerateChartSeriesFromMetrics(values, "Número de threads", func(metricValue metrics.Metric) metrics.MetricRange {
		return metricValue.Threads
	})
	GenerateChartSeriesFromMetrics(values, "Tempo de resposta POST transactions", func(metricValue metrics.Metric) metrics.MetricRange {
		return metricValue.ResponseTime[0].MetricRange
	})
	GenerateChartSeriesFromMetrics(values, "Tempo de resposta GET transactions", func(metricValue metrics.Metric) metrics.MetricRange {
		return metricValue.ResponseTime[1].MetricRange
	})
	GenerateChartSeries(values, "Percentual de Tempo de resposta POST transactions", []string{"p90", "p95", "p99"}, func(metricValue metrics.Metric) metrics.MetricRange {
		responseTime := metricValue.ResponseTime[0]
		return metrics.MetricRange{Minimum: responseTime.P90, Average: responseTime.P95, Maximum: responseTime.P99}
	})
	GenerateChartSeries(values, "Percentual de Tempo de resposta GET transactions", []string{"p90", "p95", "p99"}, func(metricValue metrics.Metric) metrics.MetricRange {
		responseTime := metricValue.ResponseTime[1]
		return metrics.MetricRange{Minimum: responseTime.P90, Average: responseTime.P95, Maximum: responseTime.P99}
	})
}
