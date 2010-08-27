#!/bin/sh

. /etc/formfactor/config

if [ "$HAVE_TOUCHSCREEN" = "1" ]; then
	while [ ! -z $TSLIB_TSDEVICE ] && [ ! -f /etc/pointercal ]
	do
	   /usr/bin/xtscal
	done
fi
