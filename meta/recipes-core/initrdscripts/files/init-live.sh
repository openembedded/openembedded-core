#!/bin/sh

PATH=/sbin:/bin:/usr/sbin:/usr/bin

ROOT_MOUNT="/rootfs/"
ROOT_IMAGE="rootfs.img"
MOUNT="/bin/mount"
UMOUNT="/bin/umount"
ISOLINUX=""
UNIONFS="no"

# Copied from initramfs-framework. The core of this script probably should be
# turned into initramfs-framework modules to reduce duplication.
udev_daemon() {
	OPTIONS="/sbin/udev/udevd /sbin/udevd /lib/udev/udevd /sbin/systemd/systemd-udevd /lib/systemd/systemd-udevd"

	for o in $OPTIONS; do
		if [ -x "$o" ]; then
			echo $o
			return 0
		fi
	done

	return 1
}

_UDEV_DAEMON=`udev_daemon`

early_setup() {
    mkdir -p /proc
    mkdir -p /sys
    mount -t proc proc /proc
    mount -t sysfs sysfs /sys
    mount -t devtmpfs none /dev

    # support modular kernel
    modprobe isofs 2> /dev/null

    mkdir -p /run
    mkdir -p /var/run

    $_UDEV_DAEMON --daemon
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
            console=*)
                if [ -z "${console_params}" ]; then
                    console_params=$arg
                else
                    console_params="$console_params $arg"
                fi ;;
            debugshell*)
                if [ -z "$optarg" ]; then
                        shelltimeout=30
                else
                        shelltimeout=$optarg
                fi 
        esac
    done
}

boot_live_root() {
    killall udevd 2>/dev/null

    # Move the mount points of some filesystems over to
    # the corresponding directories under the real root filesystem.
    mount -n --move /proc ${ROOT_MOUNT}/proc
    mount -n --move /sys ${ROOT_MOUNT}/sys
    mount -n --move /dev ${ROOT_MOUNT}/dev
    # Move /media/$i over to the real root filesystem
    mount -n --move /media/$i ${ROOT_MOUNT}/media/realroot

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
C=0
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
  # don't wait for more than $shelltimeout seconds, if it's set
  if [ -n "$shelltimeout" ]; then
      echo -n " " $(( $shelltimeout - $C ))
      if [ $C -ge $shelltimeout ]; then
           echo "..."
	   echo "Mounted filesystems"
           mount | grep media
           echo "Available block devices"
           ls /dev/sd*
           fatal "Cannot find rootfs.img file in /media/* , dropping to a shell "
      fi
      C=$(( C + 1 ))
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
    install|install-efi)
	if [ -f /media/$i/$ISOLINUX/$ROOT_IMAGE ] ; then
	    ./$label.sh $i/$ISOLINUX $ROOT_IMAGE $video_mode $vga_mode $console_params
	else
	    fatal "Could not find $label script"
	fi

	# If we're getting here, we failed...
	fatal "Installation image failed"
	;;
esac
