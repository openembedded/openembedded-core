#!/bin/sh

. /etc/default/rcS

[ "$ROOTFS_READ_ONLY" = "no" ] && exit 0

# Make sure unionfs is in /proc/filesystems
if ! grep -q unionfs /proc/filesystems; then
    echo "ERROR: unionfs not supported by kernel!"
    exit 1
fi

mkdir -p /var/volatile/lib
mount -t unionfs -o dirs=/var/volatile/lib:/var/lib=ro none /var/lib

if [ $? != 0 ]; then
    echo "ERROR: Union mount failed!"
    exit 1
fi
