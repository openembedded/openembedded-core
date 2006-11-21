#!/bin/sh

TSLIB_TSDEVICE=`detect-stylus --device`
TSLIB_CONFFILE=/etc/ts.conf

export TSLIB_TSDEVICE TSLIB_CONFFILE
