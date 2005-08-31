#!/bin/sh

case `uname -r` in
2.4*)
	TSLIB_TSDEVICE=/dev/touchscreen/0raw
	TSLIB_CONFFILE=/usr/share/tslib/ts.conf-h3600-2.4
	;;
*)
	TSLIB_TSDEVICE=`detect-stylus --device`
	TSLIB_CONFFILE=/usr/share/tslib/ts.conf-h3600
	;;
esac

export TSLIB_TSDEVICE TSLIB_CONFFILE

