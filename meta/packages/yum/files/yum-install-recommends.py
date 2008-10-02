#!/usr/bin/env python
import os, sys

root = sys.argv[1]
installcmd = sys.argv[2]

#
# Take an rpm image and look through for Recommends:. For each recommends 
# found, try and install any matching packages including any Recommends for
# packages installed by us.
#


def get_recommends():
    deps = []
    output = os.popen("rpm --root %s -aq --recommends" % (root))
    lines = output.readlines()
    for line in lines:
        line = line.replace("(none)","")
        if line:
            deps.append(line.split()[0])
    return deps

processed = []

while True:
    toinstall = []
    recommends = set(get_recommends())
    for item in recommends:
        if item not in processed:
            toinstall.append(item)
    if len(toinstall) != 0:
        print "Installing %s" % " ".join(toinstall)
        os.system("%s %s" % (installcmd, " ".join(toinstall)))
    else:
        break
    processed.extend(toinstall)

    
