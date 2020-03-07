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
workPath = properties["workPath"]
appPath = properties["appPath"]
distPkg = properties["distPkg"]

print("Start packaging")

(status, output) = subprocess.getstatusoutput(
    "cd {0} && npm run build && rm {1} && tar -czf {1} dist".format(workPath, distPkg, distPkg))
print(output)
if status != 0:
    exit(1)

print("Start deploying")

sshClient = paramiko.SSHClient()
sshClient.set_missing_host_key_policy(paramiko.AutoAddPolicy())
sshClient.connect(hostname, port=port, username=username, password=password)

# noinspection PyTypeChecker
transport = paramiko.Transport((hostname, port))
transport.connect(username=username, password=password)
sftpClient = paramiko.SFTPClient.from_transport(transport)

print("successfully get the ssh&sftp session")


def gratefully_shutdown():
    if sshClient:
        sshClient.close()
    if sftpClient:
        sftpClient.close()


def exec_command(cmd):
    print("command: " + cmd)
    (stdin, stdout, stderr) = sshClient.exec_command(cmd)
    err = stderr.read().decode("utf-8")
    if err:
        print("catch err: " + err)
        gratefully_shutdown()
        exit(1)
    print("cmd result: " + stdout.read().decode("utf-8"))


exec_command(r"cd {0} && rm -rf dist*".format(appPath))
print("Finish cleaning")

try:
    sftpClient.put("{0}/{1}".format(workPath, distPkg), "{0}/{1}".format(appPath, distPkg))
except IOError:
    gratefully_shutdown()
    exit(1)
print("Finish uploading")

startupScript = r"""
#!/usr/bin/env bash

cd {0}
tar -zxvf {1}
""".format(appPath, distPkg)
startupCmd = "cd {0} && echo '{1}' > startup.sh && chmod +x startup.sh && ./startup.sh && rm -f startup.sh" \
    .format(appPath, startupScript)
exec_command(startupCmd)
print("Finish deploying")

gratefully_shutdown()
print("Finish closing session. ")
print("Good bye. ")
exit(0)
