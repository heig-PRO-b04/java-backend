import re
import os
import sys
import subprocess

print("Running integration tests")

path = r"."
for f in os.listdir(path):
    if (re.match('^test(.)+\.py$', f)):
        command = "python3 " + path + "/" + f
        p = subprocess.Popen(command, stdout=subprocess.PIPE, shell=True)
        print("Running " + command)
        status = p.wait()

        if (status != 0):
            print ("Error in file: " + f)
            sys.exit(1)

