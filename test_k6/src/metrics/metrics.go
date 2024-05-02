package metrics

import (
	"fmt"
)

type Metric struct {
	Scenario                 string         `json:"scenario"`
	TotalRequests            int            `json:"totalRequests"`
	AverageRequestsPerSecond float64        `json:"averageRequestsPerSecond"`
	Memory                   MetricRange    `json:"memory"`
	CPU                      MetricRange    `json:"cpu"`
	Threads                  MetricRange    `json:"threads"`
	ResponseTime             []ResponseTime `json:"responseTime"`
}

type MetricRange struct {
	Minimum float64 `json:"minimum,omitempty"`
	Average float64 `json:"average,omitempty"`
	Maximum float64 `json:"maximum,omitempty"`
}

type MetricPercentageRange struct {
	P90 float64 `json:"p(90),omitempty"`
	P95 float64 `json:"p(95),omitempty"`
	P99 float64 `json:"p(99),omitempty"`
}

type ResponseTime struct {
	MetricRange
	MetricPercentageRange
	Path string `json:"path"`
}

func GetMetrics(containerName, description string) Metric {
	fmt.Printf("Reporting metrics for %s\n", description)

	k6 := NewK6(containerName)
	httpClient := NewHttpClient("http://localhost:9090/api/v1")
	prometheus := NewPrometheus(httpClient, containerName)
	memory := prometheus.GetMemory()
	cpu := prometheus.GetCPU()
	threads := prometheus.GetThreads()

	requestPercentageCreateTransaction := k6.GetResponsePercentageCreateTransaction()
	requestPercentageGetTransaction := k6.GetResponsePercentageGetTransaction()
	responseTimeCreateTransaction := k6.GetResponseTimeCreateTransaction()
	responseTimeCreateTransactionGetTransaction := k6.GetResponseTimeGetTransaction()
	requestCount := k6.GetRequestCount()
	averageRequestsPerSecond := float64(requestCount / 300)
	return Metric{
		Scenario:                 description,
		TotalRequests:            requestCount,
		AverageRequestsPerSecond: averageRequestsPerSecond,
		Memory:                   memory,
		CPU:                      cpu,
		Threads:                  threads,
		ResponseTime: []ResponseTime{
			{
				Path:                  "POST /transactions",
				MetricRange:           responseTimeCreateTransaction,
				MetricPercentageRange: requestPercentageCreateTransaction,
			},
			{
				Path:                  "GET /transactions/{id}",
				MetricRange:           responseTimeCreateTransactionGetTransaction,
				MetricPercentageRange: requestPercentageGetTransaction,
			},
		},
	}
}
