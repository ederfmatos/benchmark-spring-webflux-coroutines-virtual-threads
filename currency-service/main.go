package main

import (
	"encoding/json"
	"log"
	"net/http"
	"time"
)

type Currency struct {
	Dollar float64 `json:"DOLLAR"`
	Euro   float64 `json:"EURO"`
	Brl    float64 `json:"BRL"`
}

func main() {
	currency := Currency{
		Dollar: 5.02,
		Euro:   5.42,
		Brl:    1.0,
	}
	jsonBytes, _ := json.Marshal(currency)

	http.HandleFunc("GET /currencies", func(w http.ResponseWriter, r *http.Request) {
		time.Sleep(40 * time.Millisecond)
		log.Println("Request received: ", r.Header.Get("X-Request-ID"))
		w.Header().Set("Content-Type", "application/json")
		w.Write(jsonBytes)
	})

	log.Println("Servidor iniciado em http://localhost:8090")
	log.Fatal(http.ListenAndServe(":8090", nil))
}
