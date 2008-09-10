#!/bin/sh

ROOT_MOUNT="/rootfs/"
ROOT_IMAGE=rootfs.img
MOUNT="/bin/mount"
UMOUNT="/bin/umount"

early_setup() {
    mkdir /proc
    mount -t proc proc /proc
    udevd --daemon
}

read_args() {
    [ -z "$CMDLINE" ] && CMDLINE=`cat /proc/cmdline`
    for arg in $CMDLINE; do
        optarg=`expr "x$arg" : 'x[^=]*=\(.*\)'`
        case $arg in
            root=*)
                ROOT_DEVICE=$optarg ;;
            rootfstype=*)
                ROOT_FSTYPE=$optarg ;;
            rootdelay=*)
                rootdelay=$optarg ;;
        esac
    done
}

boot_live_root() {
    killall udevd
    cd $ROOT_MOUNT
    exec switch_root -c /dev/console $ROOT_MOUNT /sbin/init
}

fatal() {
    echo $1 >$CONSOLE
    echo >$CONSOLE
    exec sh
}

echo "Starting initramfs boot..."
early_setup

[ -z "$CONSOLE" ] && CONSOLE="/dev/console"

read_args

echo "Waiting for Live image to show up..."
while true
do
  for i in `ls /media 2>/dev/null`; do
      if [ -f /media/$i/$ROOT_IMAGE ] ; then
	  found="yes"
      fi
  done
  if [ "$found" = "yes" ]; then
      break;
  fi
  sleep 1
done

mkdir $ROOT_MOUNT
mknod /dev/loop0 b 7 0

if ! $MOUNT -o rw,loop,noatime,nodiratime /media/$i/$ROOT_IMAGE $ROOT_MOUNT
then
    fatal "Couldnt mount rootfs image"
else
    boot_live_root
fi
