#!/bin/sh

. /etc/default/rcS

[ "$ROOTFS_READ_ONLY" = "no" ] && exit 0

if [ "$1" = "start" ] ; then
	grep -q "tmpfs /var/volatile" /proc/mounts || mount /var/volatile
	mkdir -p /var/volatile/lib
	cp -a /var/lib/* /var/volatile/lib
	mount --bind /var/volatile/lib /var/lib
fi

