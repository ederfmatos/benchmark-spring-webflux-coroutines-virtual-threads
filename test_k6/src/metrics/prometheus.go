package metrics

import (
	"fmt"
	"net/url"
	"strconv"
	"time"
)

type Prometheus struct {
	httpClient      HttpClient
	applicationName string
}

type QueryRangeResponse struct {
	Data QueryRangeData `json:"data"`
}

type QueryRangeData struct {
	Result []QueryRangeResult `json:"result"`
}

type QueryRangeResult struct {
	Values [][]interface{} `json:"values"`
}

func NewPrometheus(httpClient HttpClient, applicationName string) *Prometheus {
	return &Prometheus{httpClient: httpClient, applicationName: applicationName}
}

func (prometheus Prometheus) GetMemory() MetricRange {
	metric := prometheus.getQueryRange("sum(jvm_memory_used_bytes{application=\"%s\"})")
	return MetricRange{
		Minimum: metric.Minimum / 1000000,
		Average: metric.Average / 1000000,
		Maximum: metric.Maximum / 1000000,
	}
}

func (prometheus Prometheus) GetCPU() MetricRange {
	return prometheus.getQueryRange("system_cpu_usage{application=\"%s\"}")
}

func (prometheus Prometheus) GetThreads() MetricRange {
	return prometheus.getQueryRange("jvm_threads_live_threads{application=\"%s\"}")
}

func (prometheus Prometheus) getQueryRange(metric string) MetricRange {
	start := strconv.FormatInt(time.Now().Add(-11*time.Minute).Unix(), 10)
	end := strconv.FormatInt(time.Now().Unix(), 10)
	query := url.QueryEscape(fmt.Sprintf(metric, prometheus.applicationName))
	var response QueryRangeResponse
	rawUrl := fmt.Sprintf("/query_range?step=14&query=%s&start=%s&end=%s", query, start, end)
	prometheus.httpClient.Get(rawUrl, &response)
	var values []float64
	for _, value := range response.Data.Result[0].Values {
		a := value[1]
		metric, _ := strconv.ParseFloat(a.(string), 64)
		values = append(values, metric)
	}
	return MetricRange{
		Minimum: minimum(values),
		Average: average(values),
		Maximum: maximum(values),
	}
}
