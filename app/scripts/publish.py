"""
Prepare secret.json at the root of the project.
{
    "keystorepath": "E:\\Path\\To\\Keystore\\vault.jks",
    "keystorepass": "password",
    "keystorealias": "alias",
    "keystorealiaspass": "password"
}
"""

import os
import json
import shutil

with open("secret.json") as f:
    SECRET = json.load(f)

cmd = "npx cap build android --androidreleasetype=APK"
cmd += f' --keystorealias="{SECRET["keystorealias"]}"'
cmd += f' --keystorealiaspass="{SECRET["keystorealiaspass"]}"'
cmd += f' --keystorepass="{SECRET["keystorepass"]}"'
cmd += f' --keystorepath="{SECRET["keystorepath"]}"'

ret = os.system(cmd)
if ret != 0:
    print("Failed to build android app")
    exit(1)

with open("package.json", "r") as f:
    VERSION = json.load(f)["version"]

ARTIFACT = "android\\app\\build\\outputs\\apk\\release\\app-release-signed.apk"
PUBLISH = f"publish\\spendit-{VERSION}.apk"

os.makedirs("publish", exist_ok=True)
shutil.copyfile(ARTIFACT, PUBLISH)

print(f"Published {PUBLISH}")
