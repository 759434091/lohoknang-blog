#!/usr/bin/env bash

USERNAME=root
HOST=188.119.103.206
PORT=22122
WORK_DIR=/Users/xuenenglu/IdeaProjects/lohoknang-blog/lohoknang-blog-frontend
APP_DIR=/home/blog
DIST_PKG="dist.tar.gz"
KEY_PATH=/Users/xuenenglu/.ssh/id_rsa

cd ${WORK_DIR} || exit

echo "start process"

npm run build
rm ${DIST_PKG}
tar -czf ${DIST_PKG} dist

/usr/bin/expect << EOF
    set timeout -1
    spawn ssh ${USERNAME}@${HOST} -i ${KEY_PATH} -p ${PORT}
    expect "*#"
        send "cd ${APP_DIR}\r"
    expect "*#"
        send "rm -rf dist*\r"
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
