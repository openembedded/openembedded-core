#!/bin/sh

TSLIB_TSDEVICE=`detect-stylus --device`
TSLIB_CONFFILE=/usr/share/tslib/ts-2.6.conf

export TSLIB_TSDEVICE TSLIB_CONFFILE
