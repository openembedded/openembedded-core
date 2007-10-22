#!/bin/sh
MDEV=/sbin/mdev
DESC="Busybox mdev setup"

# Complain if thing's aren't right
if [ ! -e /proc/filesystems ]; then
  echo "mdev requires a mounted procfs, not started."
  exit 1
fi

if ! grep -q '[[:space:]]tmpfs$' /proc/filesystems; then
  echo "mdev requires tmpfs support, not started."
  exit 1
fi

if [ ! -d /sys/class/ ]; then
  echo "mdev requires a mounted sysfs, not started."
  exit 1
fi

if [ ! -e /proc/sys/kernel/hotplug ]; then
  echo "mdev requires hotplug support, not started."
  exit 1
fi

# We need to unmount /dev/pts/ and remount it later over the tmpfs
if mountpoint -q /dev/pts/; then
  umount -l /dev/pts/
fi

if mountpoint -q /dev/shm/; then
  umount -l /dev/shm/
fi

# Create tmpfs for /dev
echo "Creating tmpfs at /dev"
mount -t tmpfs tmpfs /dev -o size=800k

# Register mdev as hotplug event helper
echo "$MDEV" > /proc/sys/kernel/hotplug

# Populate /dev from /sys info
echo "Populating /dev using mdev"
$MDEV -s

# Touch .udev to inform scripts that /dev needs no further setup
touch /dev/.udev

# Mount devpts
TTYGRP=5
TTYMODE=620
mkdir -m 755 -p /dev/pts
if [ ! -e /dev/ptmx ]; then
    mknod -m 666 /dev/ptmx c 5 2
fi
mount -t devpts devpts /dev/pts -onoexec,nosuid,gid=$TTYGRP,mode=$TTYMODE

# Make shm directory
mkdir -m 755 -p /dev/shm

# Make extraneous links
ln -sf /proc/self/fd /dev/fd
ln -sf /proc/self/fd/0 /dev/stdin
ln -sf /proc/self/fd/1 /dev/stdout
ln -sf /proc/self/fd/2 /dev/stderr
ln -sf /proc/kcore /dev/core
ln -sf /proc/asound/oss/sndstat /dev/sndstat

exit 0
