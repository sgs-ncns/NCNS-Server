#!/bin/bash

SERVICE_NAME=$1-service

echo ">> kill previous process"
kill -9 $(jps | grep $SERVICE_NAME | awk '{print $1}')

echo ">> git pull origin develop"
cd ../
git pull origin develop

echo ">> ###### $SERVICE_NAME ######"
cd $SERVICE_NAME/

echo ">> remove old jar file"
rm -rf build/

echo ">> build new jar file"
cd ../
./gradlew clean
./gradlew $SERVICE_NAME:bootJar

echo ">> run server"
cd $SERVICE_NAME/build/libs/
nohup java -jar -Dspring.profiles.active=prod $SERVICE_NAME-0.0.1.jar &
