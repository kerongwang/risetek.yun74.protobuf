#!/bin/bash
##set -e
if [ -z "$1" ]
 then
  echo "No remote docker host provide"
  exit 1
fi

PROJECT_NAME=risetek.engine
BUILD_DIR=temp_docker_ci_$PROJECT_NAME
REMOTE_NAME=$1
mvn clean install
ssh $REMOTE_NAME "mkdir -p $BUILD_DIR"
scp -r ./ci/docker $REMOTE_NAME:$BUILD_DIR
scp client/target/yun74.protobuf-client-1.0-SNAPSHOT.jar $REMOTE_NAME:$BUILD_DIR/docker/engine.jar
ssh $REMOTE_NAME docker build $BUILD_DIR/docker/ -t devops/$PROJECT_NAME
## clean up
ssh $REMOTE_NAME "rm -rf $BUILD_DIR"
ssh $REMOTE_NAME docker stop $PROJECT_NAME
## ssh $REMOTE_NAME docker run -d --rm --name $PROJECT_NAME -v/mnt/wangyc_test/taos/lib:/var/lib/taos -v/mnt/wangyc_test/taos/log:/var/log/taos devops/$PROJECT_NAME
ssh $REMOTE_NAME docker run -d --rm --name $PROJECT_NAME devops/$PROJECT_NAME
