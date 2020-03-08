import subprocess
import json
import paramiko

file = open("private.json", encoding="utf-8")
properties = json.load(file)
file.close()

hostname = properties["hostname"]
port = properties["port"]
username = properties["username"]
password = properties["password"]
targetDirPath = properties["targetDirPath"]
targetJarName = properties["targetJarName"]
targetJarRegex = properties["targetJarRegex"]
appPath = properties["appPath"]
mvnPath = properties["mvnPath"]

print("Start packaging")

(status, output) = subprocess.getstatusoutput("{0} clean package -DskipTests".format(mvnPath))
print(output)
if status != 0:
    exit(1)

print("Start deploying")

# noinspection PyTypeChecker
transport = paramiko.Transport((hostname, port))
transport.connect(username=username, password=password)
sftpClient = paramiko.SFTPClient.from_transport(transport)

print("successfully get the ssh&sftp session")


def gratefully_shutdown():
    if sftpClient:
        sftpClient.close()


def exec_command(cmd):
    ssh_client = paramiko.SSHClient()
    ssh_client.set_missing_host_key_policy(paramiko.AutoAddPolicy())
    ssh_client.connect(hostname, port=port, username=username, password=password)
    print("command: " + cmd)
    (stdin, stdout, stderr) = ssh_client.exec_command(cmd)
    err = stderr.read().decode("utf-8")
    if err:
        print("catch err: " + err)
        if ssh_client:
            ssh_client.close()
        gratefully_shutdown()
        exit(1)
    print("cmd result: " + stdout.read().decode("utf-8"))
    if ssh_client:
        ssh_client.close()


cleaningScript = r"""
#!/usr/bin/env bash

BLOG_PID=""
cd {0}
[[ -f blog.pid ]] && BLOG_PID=$(cat blog.pid)
[[ -n ${{BLOG_PID}} ]] && kill ${{BLOG_PID}}
find . -regextype sed -regex "{1}" | xargs rm -f 
rm -f blog.pid
""".format(appPath, targetJarRegex)
cleaningCmd = r"""cd {0} && echo '{1}' > clean.sh && chmod +x clean.sh && ./clean.sh && rm -f clean.sh""" \
    .format(appPath, cleaningScript)
exec_command(cleaningCmd)
print("Finish cleaning")

try:
    sftpClient.put("{0}/{1}".format(targetDirPath, targetJarName), "{0}/{1}".format(appPath, targetJarName))
except IOError:
    gratefully_shutdown()
    exit(1)
print("Finish uploading")

exec_command("cd {0} && ls".format(appPath))

startupScript = r"""
#!/usr/bin/env bash

cd {0}
OUTPUT="output-$(date +"%Y%m%d%H%M%S").out"
java --version
nohup java -jar {1} > ${{OUTPUT}} 2>&1 &
echo "$!" > blog.pid
""".format(appPath, targetJarName)
startupCmd = "cd {0} && echo '{1}' > startup.sh && chmod +x startup.sh && ./startup.sh && rm -f startup.sh" \
    .format(appPath, startupScript)
exec_command(startupCmd)
print("Finish deploying")

gratefully_shutdown()
print("Finish closing session. ")
print("Good bye. ")
exit(0)
