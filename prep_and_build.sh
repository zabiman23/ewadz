#!/bin/bash

VERSION=$1

#./mvnw clean install -Dartifact.version=$VERSION | tee mvn_result.txt
#if grep 'BUILD FAILURE' mvn_result.txt; then exit 1; fi;
#cp -R src/main/resources/config target/

# Move gradle build files to Docker folder
cp build.gradle docker/
cp settings.gradle docker
cp gradlew docker/
cp -r gradle docker/

pushd docker
./build_image.sh $VERSION
popd
