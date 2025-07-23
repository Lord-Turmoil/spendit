"""
This script is used to convert absolute path to relative path
for GitHub action build.
"""

import os

HTML_FILE = "dist/index.html"
CSS_FILE = None
JS_FILE = None
for file in os.listdir("dist/assets"):
    if file.endswith(".css"):
        CSS_FILE = os.path.join("dist/assets", file)
    if file.endswith(".js") and file.startswith("index"):
        JS_FILE = os.path.join("dist/assets", file)

assert CSS_FILE is not None
assert JS_FILE is not None

print("Converting absolute path to relative path...")

with open(HTML_FILE, "r", encoding="utf-8") as f:
    content = f.read()
content = content.replace("/assets", "assets")
with open(HTML_FILE, "w", encoding="utf-8") as f:
    f.write(content)

with open(CSS_FILE, "r", encoding="utf-8") as f:
    content = f.read()
content = content.replace("/assets/", "")
with open(CSS_FILE, "w", encoding="utf-8") as f:
    f.write(content)

print("Converting Object.hasOwn to Object.prototype.hasOwnProperty...")
with open(JS_FILE, "r", encoding="utf-8") as f:
    content = f.read()
content = content.replace("Object.hasOwn", "Object.prototype.hasOwnProperty")
with open(JS_FILE, "w", encoding="utf-8") as f:
    f.write(content)

print("Done.")
