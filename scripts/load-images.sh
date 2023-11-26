#!/usr/bin/env bash

version=${1:-v6.0}

images=(
  "postgres:15"
  "romanowalex/liquibase-container:latest"
  "romanowalex/migration-application:$version"
)

for image in "${images[@]}"; do
  kind load docker-image "$image"
done
