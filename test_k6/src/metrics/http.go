package metrics

import (
	"encoding/json"
	"fmt"
	"net/http"
)

type HttpClient struct {
	baseUrl string
}

func NewHttpClient(baseUrl string) HttpClient {
	return HttpClient{baseUrl: baseUrl}
}

func (httpClient *HttpClient) Get(path string, responseData any) {
	rawUrl := httpClient.baseUrl + path
	response, err := http.Get(rawUrl)
	if err != nil {
		fmt.Printf("Erro ao fazer a requisição: %v", err)
		panic(err)
	}

	defer response.Body.Close()

	if err := json.NewDecoder(response.Body).Decode(&responseData); err != nil {
		fmt.Printf("Erro ao decodificar a resposta JSON: %v", err)
		panic(err)
	}
}
