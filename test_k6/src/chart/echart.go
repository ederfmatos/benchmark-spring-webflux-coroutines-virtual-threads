package chart

import (
	"ederfmatos/test-benckmark/src/metrics"
	"fmt"
	"log"
	"os"
	"sort"
	"strconv"

	"github.com/go-echarts/go-echarts/v2/charts"
	"github.com/go-echarts/go-echarts/v2/opts"
	"github.com/go-echarts/go-echarts/v2/types"
)

func GenerateChartSeriesFromMetrics(scenarios []metrics.Metric, title string, getMetricRange func(metric metrics.Metric) metrics.MetricRange) {
	GenerateChartSeries(scenarios, title, []string{"Mínimo", "Médio", "Máximo"}, getMetricRange)
}

func GenerateChartSeries(scenarios []metrics.Metric, title string, labels []string, getMetricRange func(metric metrics.Metric) metrics.MetricRange) {
	bar := charts.NewBar()
	bar.SetGlobalOptions(
		charts.WithInitializationOpts(opts.Initialization{
			BackgroundColor: "white",
			Theme:           types.ThemeWalden,
		}),
		charts.WithTitleOpts(opts.Title{Title: title}),
		charts.WithGridOpts(opts.Grid{ContainLabel: true, Left: "1%", Top: "5%", Bottom: "1%"}),
		charts.WithLegendOpts(opts.Legend{Show: true, Right: "1%"}),
	)
	sort.Slice(scenarios, func(i, j int) bool {
		return getMetricRange(scenarios[i]).Maximum < getMetricRange(scenarios[j]).Maximum
	})

	xAxis := make([]string, len(scenarios))
	dataMinimum := make([]opts.BarData, len(scenarios))
	dataAverage := make([]opts.BarData, len(scenarios))
	dataMaximum := make([]opts.BarData, len(scenarios))
	label := opts.Label{Show: true, FontSize: 8}
	for i, scenario := range scenarios {
		xAxis[i] = scenario.Scenario
		metricRange := getMetricRange(scenario)
		dataMinimum[i] = opts.BarData{Value: GetValueFormatted(metricRange.Minimum), Name: scenario.Scenario, Label: &label}
		dataAverage[i] = opts.BarData{Value: GetValueFormatted(metricRange.Average), Name: scenario.Scenario, Label: &label}
		dataMaximum[i] = opts.BarData{Value: GetValueFormatted(metricRange.Maximum), Name: scenario.Scenario, Label: &label}
	}
	labelOptions := charts.WithLabelOpts(opts.Label{Show: true, Position: "right"})
	bar.AddSeries(labels[0], dataMinimum, labelOptions)
	bar.AddSeries(labels[1], dataAverage, labelOptions)
	bar.AddSeries(labels[2], dataMaximum, labelOptions)

	bar.SetXAxis(xAxis)
	bar.XYReversal()

	file, _ := os.Create(fmt.Sprintf("graficos/%s.html", title))
	err := bar.Render(file)
	checkError(err)

	GenerateChart(scenarios, fmt.Sprintf("%s %s", title, labels[0]), func(value metrics.Metric) any {
		return getMetricRange(value).Minimum
	})

	GenerateChart(scenarios, fmt.Sprintf("%s %s", title, labels[1]), func(value metrics.Metric) any {
		return getMetricRange(value).Average
	})

	GenerateChart(scenarios, fmt.Sprintf("%s %s", title, labels[2]), func(value metrics.Metric) any {
		return getMetricRange(value).Maximum
	})
}

func GenerateChart(scenarios []metrics.Metric, title string, getValue func(value metrics.Metric) any) {
	bar := charts.NewBar()
	bar.SetGlobalOptions(
		charts.WithInitializationOpts(opts.Initialization{
			BackgroundColor: "white",
			Theme:           types.ThemeWalden,
		}),
		charts.WithTitleOpts(opts.Title{Title: title}),
		charts.WithGridOpts(opts.Grid{ContainLabel: true, Left: "1%", Top: "5%", Bottom: "1%"}),
		charts.WithLegendOpts(opts.Legend{Show: false}),
	)

	type Item struct {
		text  string
		value any
	}

	items := make([]Item, len(scenarios))
	for i, scenario := range scenarios {
		items[i] = Item{scenario.Scenario, getValue(scenario)}
	}
	sort.Slice(items, func(i, j int) bool {
		if _, ok := items[i].value.(int); ok {
			return items[i].value.(int) < items[j].value.(int)
		}
		return items[i].value.(float64) < items[j].value.(float64)
	})

	xAxis := make([]string, len(items))
	series := make([]opts.BarData, len(items))

	for i, item := range items {
		xAxis[i] = item.text
		series[i] = opts.BarData{
			Value: GetValueFormatted(item.value),
			Name:  item.text,
		}
	}
	bar.AddSeries(title, series, charts.WithLabelOpts(opts.Label{Show: true, Position: "right"}))
	bar.SetXAxis(xAxis)
	bar.XYReversal()

	file, err := os.Create(fmt.Sprintf("graficos/%s.html", title))
	checkError(err)
	err = bar.Render(file)
	checkError(err)
}

func GetValueFormatted(value any) any {
	if intValue, ok := value.(int); ok {
		return strconv.Itoa(intValue)
	}
	if floatValue, ok := value.(float64); ok {
		return fmt.Sprintf("%.2f", floatValue)
	}
	return value
}

func checkError(err error) {
	if err != nil {
		log.Fatalf("ocorreu um erro %s", err)
	}
}
