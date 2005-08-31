#!/bin/sh

TSLIB_TSDEVICE=`detect-stylus --device`
TSLIB_CONFFILE=/usr/share/tslib/ts.conf-h3600
QWS_MOUSE_PROTO=TPanel

export TSLIB_TSDEVICE TSLIB_CONFFILE QWS_MOUSE_PROTO

