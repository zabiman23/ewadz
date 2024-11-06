#!/bin/bash

pushd docker
docker compose down
docker compose build
docker compose up -d
popd
