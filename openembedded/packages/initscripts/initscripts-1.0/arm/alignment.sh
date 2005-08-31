#!/bin/sh

if [ -e /proc/cpu/alignment ]; then
   echo "3" > /proc/cpu/alignment
fi

