#!/bin/sh

rm -rf springboot-test

git clone git@github.com:sunyuxiang/springboot-test.git

cd springboot-test

mvn clean package

cd target 

java -jar demo-0.0.1-SNAPSHOT.jar


