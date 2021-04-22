#!/bin/bash
set -e
mvn clean install
git-bash.exe -c "mvn exec:java -Dexec.args='debug' -pl client" &

