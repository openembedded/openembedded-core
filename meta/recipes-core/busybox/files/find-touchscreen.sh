#!/bin/sh

if [ `egrep "input:.*-e0.*,3,.*a0,1,.*18,.*" /sys/class/input/$MDEV/device/modalias|wc -l` -gt 0 ]; then
	ln -sf /dev/input/$MDEV /dev/input/touchscreen0
fi

if [ `egrep "ads7846" /sys/class/input/$MDEV/device/modalias|wc -l` -gt 0 ]; then
	ln -sf /dev/input/$MDEV /dev/input/touchscreen0
fi
