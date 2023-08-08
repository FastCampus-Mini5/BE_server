#!/bin/bash

#GitHub Repository 이름?
PROJECT_NAME="BE_server"
DEPLOY_PATH=/home/ubuntu/$PROJECT_NAME/
DEPLOY_LOG_PATH="/home/ubuntu/$PROJECT_NAME/deploy.log"
DEPLOY_ERR_LOG_PATH="/home/ubuntu/$PROJECT_NAME/deploy_err.log"

ADMIN_LOG_PATH="/home/ubuntu/$PROJECT_NAME/admin.log"
APPLICATION_LOG_PATH="/home/ubuntu/$PROJECT_NAME/application.log"

ADMIN_JAR_PATH="/home/ubuntu/$PROJECT_NAME/admin/build/libs/*.jar"
APPLICATION_JAR_PATH="/home/ubuntu/$PROJECT_NAME/application/build/libs/*.jar"

ADMIN_BUILD_JAR=$(ls $ADMIN_JAR_PATH)
APPLICATION_BUILD_JAR=$(ls $APPLICATION_JAR_PATH)

ADMIN_JAR_NAME=$(basename $ADMIN_BUILD_JAR)
APPLICATION_JAR_NAME=$(basename $APPLICATION_BUILD_JAR)

echo "==== 배포 시작 : $(date +%c) ====" >> $DEPLOY_LOG_PATH

echo "> admin build 파일명 : $ADMIN_JAR_NAME" >> $DEPLOY_LOG_PATH
echo "> application build 파일명 : $APPLICATION_JAR_NAME" >> $DEPLOY_LOG_PATH

echo "> build 파일 복사" >> $DEPLOY_LOG_PATH
cp $ADMIN_BUILD_JAR $DEPLOY_PATH
cp $APPLICATION_BUILD_JAR $DEPLOY_PATH

echo "> 현재 동작중인 애플리케이션 pid 체크" >> $DEPLOY_LOG_PATH
ADMIN_CURRENT_PID=$(pgrep -f $ADMIN_JAR_NAME)
APPLICATION_CURRENT_PID=$(pgrep -f $APPLICATION_JAR_NAME)

if [ -z $ADMIN_CURRENT_PID ]
then
  echo "> 현재 동작중인 ADMIN 존재 X" >> $DEPLOY_LOG_PATH
else
  echo "> 현재 동작중인 ADMIN 존재 O" >> $DEPLOY_LOG_PATH
  echo "> 현재 동작중인 ADMIN 강제 종료 진행" >> $DEPLOY_LOG_PATH
  echo "> kill -9 $ADMIN_CURRENT_PID" >> $DEPLOY_LOG_PATH
  kill -9 $ADMIN_CURRENT_PID
fi

if [ -z $APPLICATION_CURRENT_PID ]
then
  echo "> 현재 동작중인 APPLICATION 존재 X" >> $DEPLOY_LOG_PATH
else
  echo "> 현재 동작중인 APPLICATION 존재 O" >> $DEPLOY_LOG_PATH
  echo "> 현재 동작중인 APPLICATION 강제 종료 진행" >> $DEPLOY_LOG_PATH
  echo "> kill -9 $APPLICATION_CURRENT_PID" >> $DEPLOY_LOG_PATH
  kill -9 $APPLICATION_CURRENT_PID
fi

ADMIN_DEPLOY_JAR=$DEPLOY_PATH$ADMIN_JAR_NAME
APPLICATION_DEPLOY_JAR=$DEPLOY_PATH$APPLICATION_JAR_NAME

echo "> ADMIN_DEPLOY_JAR 배포" >> $ADMIN_LOG_PATH
nohup java -Xms128m -Xmx196m -jar -Dspring.profiles.active=prod $ADMIN_DEPLOY_JAR >> $ADMIN_LOG_PATH 2> $DEPLOY_ERR_LOG_PATH &

echo "> APPLICATION_DEPLOY_JAR 배포" >> $APPLICATION_LOG_PATH
nohup java -Xms128m -Xmx196m -jar -Dspring.profiles.active=prod $APPLICATION_DEPLOY_JAR >> $APPLICATION_LOG_PATH 2> $DEPLOY_ERR_LOG_PATH &

sleep 3

echo "> 배포 종료 : $(date +%c)" >> $DEPLOY_LOG_PATH