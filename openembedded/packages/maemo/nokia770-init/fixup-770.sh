#! /bin/sh
case "$1" in
  start)
        echo -n "Unmounting virtual fs from initrd"
        umount /mnt/initfs/sys
        umount /mnt/initfs/proc
        ln -s /dev/vc/0 /dev/tty0
        ln -s /dev/vc/1 /dev/tty1
        ln -s /dev/vc/2 /dev/tty2
        ln -s /dev/vc/3 /dev/tty3
        ln -s /dev/vc/4 /dev/tty4
        ln -s /dev/vc/5 /dev/tty5
        ln -s /dev/vc/6 /dev/tty6
        ln -s /dev/vc/7 /dev/tty7
        ;;
  stop)
        ;;
  *)
        echo "Usage: $SCRIPTNAME {start|stop}" >&2
        exit 1
        ;;
esac

exit 0 
                                                                   