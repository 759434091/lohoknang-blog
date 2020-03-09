import json
import paramiko

file = open("private.json", encoding="utf-8")
properties = json.load(file)
file.close()

hostname = properties["hostname"]
port = properties["port"]
username = properties["username"]
password = properties["password"]
targetJarName = properties["targetJarName"]
targetJarRegex = properties["targetJarRegex"]
appPath = properties["appPath"]
gitPath = properties["gitPath"]


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
        exit(1)
    print("cmd result: " + stdout.read().decode("utf-8"))
    if ssh_client:
        ssh_client.close()


print("Start deploying")

cleaningScript = r"""
#!/usr/bin/env bash

BLOG_PID=""
cd {appPath}
[[ -f blog.pid ]] && BLOG_PID=$(cat blog.pid)
[[ -n ${{BLOG_PID}} ]] && kill ${{BLOG_PID}}
find . -regextype sed -regex "{targetJarRegex}" | xargs rm -f 
rm -f blog.pid
""".format(appPath=appPath, targetJarRegex=targetJarRegex)
cleaningCmd = r"""cd {appPath} \
&& echo '{cleaningScript}' > clean.sh \
&& chmod +x clean.sh \
&& ./clean.sh \
&& rm -f clean.sh""".format(appPath=appPath, cleaningScript=cleaningScript)
exec_command(cleaningCmd)
print("Finish cleaning")

exec_command("cd {0} && ls".format(appPath))

startupScript = r"""
#!/usr/bin/env bash
cd {gitPath}
git pull
cd lohoknang-blog-backend/
mvn clean package -DskipTests
cd target
cp {targetJarName} {appPath}

cd {appPath}
OUTPUT="output-$(date +"%m%d-%H%M").out"
java --version
nohup java -jar {targetJarName} > ${{OUTPUT}} 2>&1 &
echo "$!" > blog.pid
""".format(gitPath=gitPath, targetJarName=targetJarName, appPath=appPath)
startupCmd = "cd {0} && echo '{1}' > startup.sh && chmod +x startup.sh && ./startup.sh && rm -f startup.sh" \
    .format(appPath, startupScript)
exec_command(startupCmd)
print("Finish deploying")

print("Good bye. ")
exit(0)
