import http from "k6/http";
import { check, sleep } from "k6";
import { Trend } from 'k6/metrics';
import { textSummary } from 'https://jslib.k6.io/k6-summary/0.0.2/index.js';

const scenaries = {
  JAVA_TOMCAT: { port: 8083, reportName: "java-tomcat" },
  JAVA_TOMCAT_VIRTUAL: { port: 8083, reportName: "java-tomcat-virtual" },
  JAVA_WEBFLUX: { port: 8084, reportName: "java-webflux" },
  JAVA_WEBFLUX_VIRTUAL: { port: 8084, reportName: "java-webflux-virtual" },
  KOTLIN_TOMCAT: { port: 8080, reportName: "kotlin-tomcat" },
  KOTLIN_TOMCAT_VIRTUAL: { port: 8080, reportName: "kotlin-tomcat-virtual" },
  KOTLIN_COROUTINES: { port: 8081, reportName: "kotlin-coroutines" },
  KOTLIN_COROUTINES_VIRTUAL: {
    port: 8081,
    reportName: "kotlin-coroutines-virtual",
  },
  KOTLIN_WEBFLUX: { port: 8082, reportName: "kotlin-webflux" },
  KOTLIN_WEBFLUX_VIRTUAL: { port: 8082, reportName: "kotlin-webflux-virtual" },
};

const currentScenary = scenaries[__ENV.SCENARY];
const responseTimeCreateTransaction = new Trend('response_time_create_transaction');
const responseTimeGetTransaction = new Trend('response_time_get_transaction');

export let options = {
  stages: [
    { duration: "15s", target: 50 },
    { duration: "30s", target: 50 },
    { duration: "15s", target: 100 },
    { duration: "30s", target: 100 },
    { duration: "15s", target: 200 },
    { duration: "30s", target: 200 },
    { duration: "15s", target: 300 },
    { duration: "30s", target: 300 },
    { duration: "15s", target: 400 },
    { duration: "30s", target: 400 },
    { duration: "15s", target: 500 },
    { duration: "30s", target: 500 },
    { duration: "30s", target: 0 },
  ],
  systemTags: ['status', 'method', 'url', 'check'],
  summaryTrendStats: ['avg', 'min', 'med', 'max', 'p(90)', 'p(95)', 'p(99)', 'p(99.99)', 'count'],
};

export function handleSummary(data) {
  const key = `reports/${currentScenary.reportName}.json`;
  return {
    [key]: JSON.stringify(data, null, 2),
    stdout: textSummary(data, { indent: '→', enableColors: true }),
  };
}

function createTransaction() {
  const endpoint = `http://localhost:${currentScenary.port}/transactions`;
  const response = http.post(
    endpoint,
    JSON.stringify({
      description: `Transaction ${Math.random()}`,
      currency: "EURO",
      amount: 197.5,
    }),
    {
      headers: {
        "Content-Type": "application/json",
      },
    }
  );
  responseTimeCreateTransaction.add(response.timings.duration)

  check(
      response,
      { "status is 201": (r) => r.status === 201 },
  );
  return JSON.parse(response.body);
}

function getTransaction(transactionId) {
  const endpoint = `http://localhost:${currentScenary.port}/transactions/${transactionId}`;
  const response = http.get(endpoint, {
    headers: {
      "Content-Type": "application/json",
    },
  });
  responseTimeGetTransaction.add(response.timings.duration)

  check(
      response,
      { "status is 200": (r) => r.status === 200 },
  );
}

export default function () {
  const transaction = createTransaction();
  getTransaction(transaction.id);
  sleep(0.1);
}
