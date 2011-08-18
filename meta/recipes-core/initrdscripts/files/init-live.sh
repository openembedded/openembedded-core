#!/bin/sh

ROOT_MOUNT="/rootfs/"
ROOT_IMAGE="isolinux/rootfs.img"
MOUNT="/bin/mount"
UMOUNT="/bin/umount"

early_setup() {
    mkdir /proc
    mkdir /sys
    mount -t proc proc /proc
    mount -t sysfs sysfs /sys
    udevd --daemon
    /sbin/udevadm trigger --action=add
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
    killall udevd
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
	mknod /dev/loop0 b 7 0

	if ! $MOUNT -o rw,loop,noatime,nodiratime /media/$i/$ROOT_IMAGE $ROOT_MOUNT ; then
	    fatal "Couldnt mount rootfs image"
	else
	    boot_live_root
	fi
	;;
    install)
	if [ -f /media/$i/$ROOT_IMAGE ] ; then
	    ./install.sh $i $ROOT_IMAGE $video_mode $vga_mode
	else
	    fatal "Couldnt find install script"
	fi

	# If we're getting here, we failed...
	fatal "Installation image failed"
	;;
esac

