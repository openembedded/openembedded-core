#!/bin/sh

if [ -z "`which chkhinge`" ]; then
   # probably not a clamshell zaurus
   exit 0
fi

chkhinge -e
if [ $? = 12 ]; then
   xrandr -o right
fi

