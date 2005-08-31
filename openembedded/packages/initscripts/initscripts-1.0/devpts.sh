#! /bin/sh

. /etc/default/devpts

test "`uname -s`" = "Linux" || exit 0

#
#	First find out if devpts is available. Also check if devfs
#	is already mounted - in that case we don't want to use devpts.
#
if test ! -e /dev/.devfsd && ( grep -q devpts /proc/filesystems )
then
	#
	#	Create multiplexor device.
	#
	test -c /dev/ptmx || mknod -m 666 /dev/ptmx c 5 2

	#
	#	Mount /dev/pts if needed.
	#
	if ( ! grep -q devpts /proc/mounts )
	then
		mkdir -p /dev/pts
		mount -t devpts devpts /dev/pts -ogid=${TTYGRP},mode=${TTYMODE}
	fi
fi

exit 0
