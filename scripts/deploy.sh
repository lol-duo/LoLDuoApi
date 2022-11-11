#!/bin/bash

REPOSITORY=/home/build
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)
CURRENT_PID=$(pgrep -fla $JAR_NAME | awk '{print $1}')
IP=$(hostname -I)
echo "> 현재 구동 중인 애플리케이션 pid 확인"
echo "현재 구동 중인 애플리케이션 pid: $CURRENT_PID"
if [ -z "$CURRENT_PID" ]; then
  echo "현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -9 $CURRENT_PID"
  kill -9 $CURRENT_PID
  sleep 5
fi
echo "> 새 애플리케이션 배포"


echo "> JAR NAME: $JAR_NAME"
echo "> $JAR_NAME 에 실행권한 추가"
chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"
nohup /opt/jdk-17/bin/java -jar -javaagent:pinpoint-agent-2.2.2/pinpoint-bootstrap-2.2.2.jar -Dpinpoint.agentId=$IP -Dpinpoint.applicationName=API-SERVER -Dpinpoint.config=pinpoint-agent-2.2.2/pinpoint-root.config -Dspring.profiles.active=server $JAR_NAME > /home/build/nohup.out 2>&1 &