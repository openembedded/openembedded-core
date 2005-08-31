#!/bin/sh

module_id() {
    awk 'BEGIN { FS=": " } /Hardware/ { print $2 } ' </proc/cpuinfo
}

case `uname -r` in
2.4*)
	TSLIB_TSDEVICE=/dev/touchscreen/0raw
	TSLIB_CONFFILE=/usr/share/tslib/ts.conf-h3600-2.4
	;;
*)
	TSLIB_TSDEVICE=`detect-stylus --device`
	case `module_id` in
	"HP iPAQ H2200")
		# h2200 needs special linearizing thing
		TSLIB_CONFFILE=/usr/share/tslib/ts.conf-h2200
		;;
	*)
		TSLIB_CONFFILE=/usr/share/tslib/ts.conf-h3600
		;;
	esac
	;;
esac

export TSLIB_TSDEVICE TSLIB_CONFFILE

