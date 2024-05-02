package main

import (
	"ederfmatos/test-benckmark/src/chart"
	metric "ederfmatos/test-benckmark/src/metrics"
	"encoding/json"
	"fmt"
	"io/fs"
	"log"
	"os"
	"os/exec"
	"strings"
	"time"
)

type Scenario struct {
	ContainerName string
	Description   string
	K6Scenario    string
}

var scenarios = []Scenario{
	{"java-tomcat", "Java com Tomcat", "JAVA_TOMCAT"},
	{"java-webflux", "Java com Webflux", "JAVA_WEBFLUX"},
	{"kotlin-tomcat", "Kotlin com Tomcat", "KOTLIN_TOMCAT"},
	{"kotlin-coroutines", "Kotlin com Coroutines", "KOTLIN_COROUTINES"},
	{"kotlin-webflux", "Kotlin com Webflux", "KOTLIN_WEBFLUX"},
	{"java-tomcat-virtual", "Java com Tomcat com Threads Virtuais", "JAVA_TOMCAT_VIRTUAL"},
	{"java-webflux-virtual", "Java com Webflux com Threads Virtuais", "JAVA_WEBFLUX_VIRTUAL"},
	{"kotlin-tomcat-virtual", "Kotlin com Tomcat com Threads Virtuais", "KOTLIN_TOMCAT_VIRTUAL"},
	{"kotlin-coroutines-virtual", "Kotlin com Coroutines com Threads Virtuais", "KOTLIN_COROUTINES_VIRTUAL"},
	{"kotlin-webflux-virtual", "Kotlin com Webflux com Threads Virtuais", "KOTLIN_WEBFLUX_VIRTUAL"},
}

func main() {
	var metrics []metric.Metric
	for i := 0; i < 3; i++ {
		metrics = append(metrics, executeScenarios()...)
	}
	saveMetrics(metrics)
	generateCharts()
}

func saveMetrics(metrics []metric.Metric) {
	metricsContent, _ := json.Marshal(metrics)
	_ = os.WriteFile("metrics.json", metricsContent, fs.FileMode(os.O_RDWR))
}

func generateCharts() {
	var metrics []metric.Metric
	metricsContent, _ := os.ReadFile("metrics.json")
	_ = json.Unmarshal(metricsContent, &metrics)
	metrics = groupMetrics(metrics)
	chart.GenerateChartsFromMetrics(metrics)
}

func executeScenarios() []metric.Metric {
	var metrics []metric.Metric

	for _, scenario := range scenarios {
		restartContainers()

		startContainer(scenario.ContainerName)

		fmt.Printf("Iniciando teste para %s\n", scenario.Description)
		execCommand("k6", "run", "-e", "SCENARY="+scenario.K6Scenario, "script.js")

		metrics = append(metrics, metric.GetMetrics(scenario.ContainerName, scenario.Description))

		time.Sleep(5 * time.Second)
		fmt.Printf("Finalizado teste para %s\n", scenario.Description)

		stopContainer(scenario.ContainerName)
	}

	return metrics
}

func execCommand(command string, args ...string) {
	fmt.Printf("Executing command '%s %s'\n", command, strings.Join(args, " "))
	cmd := exec.Command(command, args...)
	cmd.Stdout = os.Stdout
	cmd.Stderr = os.Stderr
	err := cmd.Run()
	if err != nil {
		log.Fatal(err)
	}
}

func restartContainers() {
	execCommand("docker", "restart", "postgres", "currency-service")
	time.Sleep(10 * time.Second)
}

func startContainer(container string) {
	fmt.Printf("Iniciando container %s.\n", container)
	execCommand("docker", "compose", "-f", "../docker-compose.yml", "up", "-d", "--build", "--force-recreate", container)
	time.Sleep(15 * time.Second)
	fmt.Printf("Container %s iniciado.\n", container)
}

func stopContainer(container string) {
	execCommand("docker", "stop", container)
}
