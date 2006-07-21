#!/bin/sh

sleep 2
if [ -x /usr/bin/bl ]; then
	bl toggle
fi
MODEL=`cat /proc/cpuinfo | grep ^Hardware | sed "s/.* //"`
test -e /etc/scap.conf && USER=`cat /etc/scap.conf`
RES=`fbset 2>/dev/null | awk "/geometry/ { print \$2 "x" \$3 }"`
(echo "POST /scap/capture.cgi?$MODEL+$USER+$RES HTTP/1.1"
 echo -n Content-length:
 cat /dev/fb0 | wc -c
 echo "Content-Type: image/gif"
 echo "Host: www.handhelds.org"
 echo ""
 cat /dev/fb0) | nc www.handhelds.org 80
if [ -x /usr/bin/bl ]; then
	bl toggle
fi
        
