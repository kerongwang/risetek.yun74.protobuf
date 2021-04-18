#!/bin/bash
set -e
mvn clean install
git-bash.exe -c "mvn exec:java -pl client" &

