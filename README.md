# Overview

This is an **Java 11** RESTful web service built with **Spring Boot framework** + **H2 in file database** using **Gradle**.

# Getting started

You should have **Java 11** installed. 

To build and run:

    ./gradlew bootRun

To test that it works, open a browser tab at http://localhost:8080.  

Alternatively, you can run

    curl http://localhost:8080

# Build & Test

Build the jar package and run the JUnit test cases that cover both RESTFul APIs and DB access:

    ./gradlew build

# RESTful APIs

### API - Get balance of all accounts

| URI         | Verb    | HTTP response code           | Description                 |
| ----------- | ------- | ---------------------------- | --------------------------- |
| `/accounts` | **GET** | Success: 200<br/>Failed: 400 | get details of all accounts |


Example:

    curl http://localhost:8080/accounts

### API - Transfer amount between bank accounts

| URI         | Verb | Request Body                                                 | HTTP response code                 | Description                                                |
| ----------- | ---- | ------------------------------------------------------------ | ---------------------------------- | ---------------------------------------------------------- |
| `/transfer` | POST | `{<br/>"srcAccId": <source account id>, <br/>"destAccId": <destination account id>, <br/>"amount": <amount to transfer><br/>}` | Success: 200<br/>Failed: 400 / 500 | Transfer amount from source account to destination account |


Example:

    curl -X POST -H "Content-type: application/json" -d "{\"srcAccId\" : \"12345678\", \"destAccId\" : \"88888888\", \"amount\" : 100}" "http://localhost:8080/transfer"

### Note: HTTP response code for failed request

There are 2 different response errors according to source of the problem:

* **400 (Client side exception)**: When client sent an invalid request. This can be fixed by client.
* **500 (Server side exception**): Server failed to fulfill a valid request due to an error with server, for example DB access problem.