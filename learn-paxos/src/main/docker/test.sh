#!/bin/bash

curl -X POST 'http://localhost:8081/api/v1/put?key=hello&value=no1' &
curl -X POST 'http://localhost:8081/api/v1/put?key=hello&value=no2' &
curl -X POST 'http://localhost:8081/api/v1/put?key=hello&value=no3' &
