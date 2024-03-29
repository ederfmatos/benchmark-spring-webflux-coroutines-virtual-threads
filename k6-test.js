import http from "k6/http";
import { check, sleep } from "k6";
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";

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

const currentScenary = scenaries.JAVA_TOMCAT;

export let options = {
  vus: 50,
  duration: "60s",
};

export function handleSummary(data) {
  const key = `reports/${currentScenary.reportName}.html`;
  return {
    [key]: htmlReport(data),
  };
}

export default function () {
  const port = currentScenary.port;
  const endpoint = `http://localhost:${port}/transactions`;
  const response = http.post(
    endpoint,
    JSON.stringify({
      description: `Transaction ${
        Math.floor(Math.random() * (10000000 - 1 + 1)) + 1
      }`,
      currency: "EURO",
      amount: 197.5,
    }),
    {
      headers: {
        "Content-Type": "application/json",
      },
    }
  );

  check(response, {
    "status is 201": (r) => r.status === 201,
  });

  sleep(0.1);
}
