#!/bin/sh

TSLIB_TSDEVICE=/dev/input/touchscreen0
TSLIB_CONFFILE=/etc/ts.conf

case `uname -r` in
2.4*)
	TSLIB_TSDEVICE=/dev/touchscreen/0raw
	TSLIB_CONFFILE=/usr/share/tslib/ts.conf-h3600-2.4
	;;
esac

export TSLIB_TSDEVICE TSLIB_CONFFILE

