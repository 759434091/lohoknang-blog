#!/usr/bin/env bash

USERNAME=root
HOST=188.119.103.206
PORT=22122
WORK_DIR="/Users/xuenenglu/IdeaProjects/lohoknang-blog/lohoknang-blog-backend"
TARGET_PATH="/Users/xuenenglu/IdeaProjects/lohoknang-blog/lohoknang-blog-backend/target/lohoknang-blog-backend-0.0.1-SNAPSHOT.jar"
APP_DIR="/home/blog/blog-backend"
KEY_PATH="/Users/xuenenglu/.ssh/id_rsa"
MVN_BIN="/Users/xuenenglu/IdeaProjects/lohoknang-blog/lohoknang-blog-backend/mvnw"


cd ${WORK_DIR} || exit

echo "start process"

${MVN_BIN} clean package -DskipTests

/usr/bin/expect << EOF
    set timeout -1
    spawn ssh ${USERNAME}@${HOST} -i ${KEY_PATH} -p ${PORT}
    expect "*#"
        send "cd ${APP_DIR}\r"
    expect "*#"
        send "./kill.sh\r"
    expect "*#"
        send "rm lohoknang-blog-backend-0.0.1-SNAPSHOT.jar\r"
    expect "*#"
        send "logout\r"
    expect eof
EOF

echo "finish cleaning"

/usr/bin/expect << EOF
    set timeout -1
    spawn scp -i ${KEY_PATH} -P ${PORT} ${TARGET_PATH} ${USERNAME}@${HOST}:${APP_DIR}
    expect "ETA" {
        exp_continue;
    }
EOF

echo "finish upload"

/usr/bin/expect << EOF
    set timeout -1
    spawn ssh ${USERNAME}@${HOST} -i ${KEY_PATH} -p ${PORT}
    expect "*#"
        send "cd ${APP_DIR}\r"
    expect "*#"
        send "./start.sh\r"
    expect "*#"
        send "logout\r"
    expect eof
EOF

echo "finish deploy"
