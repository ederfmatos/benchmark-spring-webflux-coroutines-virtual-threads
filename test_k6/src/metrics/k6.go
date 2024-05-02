package metrics

import (
	"encoding/json"
	"fmt"
	"os"
)

type K6 struct {
	Metric K6Metric
}

type K6MetricResponse struct {
	Metrics K6Metric `json:"metrics"`
}

type K6Metric struct {
	HttpRequestSending            K6MetricWrapper `json:"http_req_sending"`
	ResponseTimeCreateTransaction K6MetricWrapper `json:"response_time_create_transaction"`
	ResponseTimeGetTransaction    K6MetricWrapper `json:"response_time_get_transaction"`
}

type K6MetricWrapper struct {
	Values K6MetricValue `json:"values"`
}

type K6MetricValue struct {
	MetricPercentageRange
	Minimum float64 `json:"min,omitempty"`
	Average float64 `json:"avg,omitempty"`
	Maximum float64 `json:"max,omitempty"`
	Count   int     `json:"count,omitempty"`
}

func NewK6(name string) *K6 {
	bytes, err := os.ReadFile(fmt.Sprintf("reports/%s.json", name))
	checkError(err)
	var metricResponse K6MetricResponse
	err = json.Unmarshal(bytes, &metricResponse)
	checkError(err)
	return &K6{Metric: metricResponse.Metrics}
}

func checkError(err error) {
	if err != nil {
		panic(err)
	}
}

func (k6 K6) GetRequestCount() int {
	return k6.Metric.HttpRequestSending.Values.Count
}

func (k6 K6) GetResponseTimeCreateTransaction() MetricRange {
	return MetricRange{
		Minimum: k6.Metric.ResponseTimeCreateTransaction.Values.Minimum,
		Average: k6.Metric.ResponseTimeCreateTransaction.Values.Average,
		Maximum: k6.Metric.ResponseTimeCreateTransaction.Values.Maximum,
	}
}

func (k6 K6) GetResponseTimeGetTransaction() MetricRange {
	return MetricRange{
		Minimum: k6.Metric.ResponseTimeGetTransaction.Values.Minimum,
		Average: k6.Metric.ResponseTimeGetTransaction.Values.Average,
		Maximum: k6.Metric.ResponseTimeGetTransaction.Values.Maximum,
	}
}

func (k6 K6) GetResponsePercentageGetTransaction() MetricPercentageRange {
	return k6.Metric.ResponseTimeGetTransaction.Values.MetricPercentageRange
}

func (k6 K6) GetResponsePercentageCreateTransaction() MetricPercentageRange {
	return k6.Metric.ResponseTimeCreateTransaction.Values.MetricPercentageRange
}
