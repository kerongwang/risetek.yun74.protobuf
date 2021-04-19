#!/bin/bash
##set -e
if [ -z "$1" ]
 then
  echo "No remote docker host provide"
  exit 1
fi

PROJECT_NAME=risetek.engine.base
BUILD_DIR=temp_basedocker_ci_$PROJECT_NAME
REMOTE_NAME=$1
ssh $REMOTE_NAME "mkdir -p $BUILD_DIR"
scp -r ./ci/basedocker $REMOTE_NAME:$BUILD_DIR
ssh $REMOTE_NAME docker build $BUILD_DIR/basedocker/ -t devops/$PROJECT_NAME
## clean up
## ssh $REMOTE_NAME "rm -rf $BUILD_DIR"
