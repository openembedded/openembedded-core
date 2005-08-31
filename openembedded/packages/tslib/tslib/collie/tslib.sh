#!/bin/sh

if (uname -r|grep -q 'embedix'); then
	TSLIB_TSDEVICE=/dev/ts
	TSLIB_TSEVENTTYPE=COLLIE
else
	TSLIB_TSDEVICE=/dev/input/event0
fi

export TSLIB_TSDEVICE TSLIB_TSEVENTTYPE
