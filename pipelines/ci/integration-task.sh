#!/usr/bin/env sh

set -e
cd db-dumper-service

bin/install-binaries
export JAVA_OPTS="${JAVA_OPTS} -Djava.security.egd=file:///dev/urandom"
mvn -B clean integration-test failsafe:verify