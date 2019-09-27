#!/usr/bin/env bash

USERNAME=""
HOST=""
PORT=""
WORK_DIR=""
APP_DIR=""
DIST_PKG=""
KEY_PATH=""

source private.sh

cd ${WORK_DIR} || exit

echo "start process"

npm run build
rm ${DIST_PKG}
tar -czf ${DIST_PKG} mobile-dist

/usr/bin/expect << EOF
    set timeout -1
    spawn ssh ${USERNAME}@${HOST} -i ${KEY_PATH} -p ${PORT}
    expect "*#"
        send "cd ${APP_DIR}\r"
    expect "*#"
        send "rm -rf mobile-dist*\r"
        send "logout\r"
    expect eof
EOF

echo "finish cleaning"

/usr/bin/expect << EOF
    set timeout -1
    spawn scp -i ${KEY_PATH} -P ${PORT} ${DIST_PKG} ${USERNAME}@${HOST}:${APP_DIR}
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
        send "tar -zxvf ${DIST_PKG}\r"
        send "logout\r"
    expect eof
EOF

echo "finish deploy"
