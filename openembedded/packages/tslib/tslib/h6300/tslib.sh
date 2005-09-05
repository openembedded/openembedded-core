#!/bin/sh

TSLIB_TSDEVICE=`detect-stylus --device`
TSLIB_CONFFILE=/usr/share/tslib/ts.conf-h6300

export TSLIB_TSDEVICE TSLIB_CONFFILE

