#!/bin/bash
# Runs zipkin wiht mysql
cat << EOF > /tmp/zipkin.yml
version: '3'

services:
  storage:
    image: openzipkin/zipkin-mysql
    ports:
      - 3306:3306
  server:
    image: openzipkin/zipkin
    environment:
      - STORAGE_TYPE=mysql
      - MYSQL_HOST=mysql
    ports:
      - 9411:9411
    depends_on:
      - storage
EOF

docker-compose -f /tmp/zipkin.yml up -d
