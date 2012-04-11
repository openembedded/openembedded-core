#!/bin/sh

PATH=/sbin:/bin:/usr/sbin:/usr/bin

ROOT_MOUNT="/rootfs/"
ROOT_IMAGE="rootfs.img"
MOUNT="/bin/mount"
UMOUNT="/bin/umount"
ISOLINUX=""
UNIONFS="no"

early_setup() {
    mkdir /proc
    mkdir /sys
    mount -t proc proc /proc
    mount -t sysfs sysfs /sys

    # support modular kernel
    modprobe isofs 2> /dev/null

    mkdir /run
    udevd --daemon
    udevadm trigger --action=add
}

read_args() {
    [ -z "$CMDLINE" ] && CMDLINE=`cat /proc/cmdline`
    for arg in $CMDLINE; do
        optarg=`expr "x$arg" : 'x[^=]*=\(.*\)'`
        case $arg in
            root=*)
                ROOT_DEVICE=$optarg ;;
            rootfstype=*)
                modprobe $optarg 2> /dev/null ;;
            LABEL=*)
                label=$optarg ;;
            video=*)
                video_mode=$arg ;;
            vga=*)
                vga_mode=$arg ;;
        esac
    done
}

boot_live_root() {
    killall udevd 2>/dev/null

    # use devtmpfs if available
    if grep -q devtmpfs /proc/filesystems; then
        mount -t devtmpfs devtmpfs $ROOT_MOUNT/dev
    fi

    cd $ROOT_MOUNT
    exec switch_root -c /dev/console $ROOT_MOUNT /sbin/init
}

fatal() {
    echo $1 >$CONSOLE
    echo >$CONSOLE
    exec sh
}

early_setup

[ -z "$CONSOLE" ] && CONSOLE="/dev/console"

read_args

echo "Waiting for removable media..."
while true
do
  for i in `ls /media 2>/dev/null`; do
      if [ -f /media/$i/$ROOT_IMAGE ] ; then
		found="yes"
		break
	  elif [ -f /media/$i/isolinux/$ROOT_IMAGE ]; then
		found="yes"
		ISOLINUX="isolinux"
		break	
      fi
  done
  if [ "$found" = "yes" ]; then
      break;
  fi
  sleep 1
done

case $label in
    boot)
	mkdir $ROOT_MOUNT
	mknod /dev/loop0 b 7 0 2>/dev/null

	
	if [ "$UNIONFS" = "yes" ]; then
	    mkdir /rootfs-tmp

	    if ! $MOUNT -o rw,loop,noatime,nodiratime /media/$i/$ISOLINUX/$ROOT_IMAGE /rootfs-tmp ; then
		fatal "Could not mount rootfs image"
	    else
		mkdir /cow
		mount -t tmpfs -o rw,noatime,mode=755 tmpfs /cow
		mount -t unionfs -o dirs=/cow:/rootfs-tmp=ro unionfs $ROOT_MOUNT
		boot_live_root
	    fi
	else
	    if ! $MOUNT -o rw,loop,noatime,nodiratime /media/$i/$ISOLINUX/$ROOT_IMAGE $ROOT_MOUNT ; then
		fatal "Could not mount rootfs image"
	    else
		boot_live_root
	    fi
	fi
	;;
    install)
	if [ -f /media/$i/$ISOLINUX/$ROOT_IMAGE ] ; then
	    ./install.sh $i/$ISOLINUX $ROOT_IMAGE $video_mode $vga_mode
	else
	    fatal "Could not find install script"
	fi

	# If we're getting here, we failed...
	fatal "Installation image failed"
	;;
esac
