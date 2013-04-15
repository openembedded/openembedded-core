#!/bin/sh -e
#
# Copyright (C) 2008-2011 Intel
#
# install.sh [device_name] [rootfs_name] [video_mode] [vga_mode]
#

PATH=/sbin:/bin:/usr/sbin:/usr/bin

# We need 20 Mb for the boot partition
boot_size=20

# 5% for the swap
swap_ratio=5

found="no"

echo "Searching for a hard drive..."
for device in 'hda' 'hdb' 'sda' 'sdb' 'mmcblk0' 'mmcblk1'
  do
  if [ -e /sys/block/${device}/removable ]; then
      if [ "$(cat /sys/block/${device}/removable)" = "0" ]; then
	  found="yes"

	  while true; do
	      # Try sleeping here to avoid getting kernel messages
              # obscuring/confusing user
	      sleep 5
	      echo "Found drive at /dev/${device}. Do you want to install this image there ? [y/n]"
	      read answer
	      if [ "$answer" = "y" ] ; then
		  break
	      fi
	  
	      if [ "$answer" = "n" ] ; then
		  found=no
		  break
	      fi

	      echo "Please answer by y or n"
	  done
      fi
  fi

  if [ "$found" = "yes" ]; then
      break;
  fi

done

if [ "$found" = "no" ]; then
      exit 1      
fi

echo "Installing image on /dev/${device}"

#
# The udev automounter can cause pain here, kill it
#
rm -f /etc/udev/rules.d/automount.rules
rm -f /etc/udev/scripts/mount*

#
# Unmount anything the automounter had mounted
#
umount /dev/${device}* 2> /dev/null || /bin/true

if [ ! -b /dev/sda ] ; then
    mknod /dev/sda b 8 0
fi

if [ ! -b /dev/sdb ] ; then
    mknod /dev/sdb b 8 16
fi

if [ ! -b /dev/loop0 ] ; then
    mknod /dev/loop0 b 7 0
fi

mkdir -p /tmp

disk_size=$(parted /dev/${device} unit mb print | grep Disk | cut -d" " -f 3 | sed -e "s/MB//")

swap_size=$((disk_size*swap_ratio/100))
rootfs_size=$((disk_size-boot_size-swap_size))

rootfs_start=$((boot_size))
rootfs_end=$((rootfs_start+rootfs_size))
swap_start=$((rootfs_end))

# MMC devices are special in a couple of ways
# 1) they use a partition prefix character 'p'
# 2) they are detected asynchronously (need rootwait)
rootwait=""
part_prefix=""
if [ ! "${device#mmcblk}" = "${device}" ]; then
	part_prefix="p"
	rootwait="rootwait"
fi
bootfs=/dev/${device}${part_prefix}1
rootfs=/dev/${device}${part_prefix}2
swap=/dev/${device}${part_prefix}3

echo "*****************"
echo "Boot partition size:   $boot_size MB ($bootfs)"
echo "Rootfs partition size: $rootfs_size MB ($rootfs)"
echo "Swap partition size:   $swap_size MB ($swap)"
echo "*****************"
echo "Deleting partition table on /dev/${device} ..."
dd if=/dev/zero of=/dev/${device} bs=512 count=2

echo "Creating new partition table on /dev/${device} ..."
parted /dev/${device} mklabel msdos

echo "Creating boot partition on $bootfs"
parted /dev/${device} mkpart primary 0% $boot_size

echo "Creating rootfs partition on $rootfs"
parted /dev/${device} mkpart primary $rootfs_start $rootfs_end

echo "Creating swap partition on $swap"
parted /dev/${device} mkpart primary $swap_start 100%

parted /dev/${device} print

echo "Formatting $bootfs to ext3..."
mkfs.ext3 $bootfs

echo "Formatting $rootfs to ext3..."
mkfs.ext3 $rootfs

echo "Formatting swap partition...($swap)"
mkswap $swap

mkdir /ssd
mkdir /rootmnt
mkdir /bootmnt

mount $rootfs /ssd
mount -o rw,loop,noatime,nodiratime /media/$1/$2 /rootmnt

echo "Copying rootfs files..."
cp -a /rootmnt/* /ssd

if [ -d /ssd/etc/ ] ; then
    echo "$swap                swap             swap       defaults              0  0" >> /ssd/etc/fstab

    # We dont want udev to mount our root device while we're booting...
    if [ -d /ssd/etc/udev/ ] ; then
	echo "/dev/${device}" >> /ssd/etc/udev/mount.blacklist
    fi
fi

if [ -f /etc/grub.d/40_custom ] ; then
    echo "Preparing custom grub2 menu..."
    GRUBCFG="/bootmnt/boot/grub/grub.cfg"
    mount $bootfs /bootmnt
    mkdir -p $(dirname $GRUBCFG)
    cp /etc/grub.d/40_custom $GRUBCFG
    sed -i "s@__ROOTFS__@$rootfs $rootwait@g" $GRUBCFG
    sed -i "s/__VIDEO_MODE__/$3/g" $GRUBCFG
    sed -i "s/__VGA_MODE__/$4/g" $GRUBCFG
    sed -i "s/__CONSOLE__/$5/g" $GRUBCFG
    sed -i "/#/d" $GRUBCFG
    sed -i "/exec tail/d" $GRUBCFG
    chmod 0444 $GRUBCFG
    umount /bootmnt
fi

umount /ssd
umount /rootmnt

echo "Preparing boot partition..."
mount $bootfs /ssd
grub-install --root-directory=/ssd /dev/${device}

echo "(hd0) /dev/${device}" > /ssd/boot/grub/device.map

# If grub.cfg doesn't exist, assume GRUB 0.97 and create a menu.lst
if [ ! -f /ssd/boot/grub/grub.cfg ] ; then
    echo "Preparing custom grub menu..."
    echo "default 0" > /ssd/boot/grub/menu.lst
    echo "timeout 30" >> /ssd/boot/grub/menu.lst
    echo "title Live Boot/Install-Image" >> /ssd/boot/grub/menu.lst
    echo "root  (hd0,0)" >> /ssd/boot/grub/menu.lst
    echo "kernel /boot/vmlinuz root=$rootfs rw $3 $4 quiet" >> /ssd/boot/grub/menu.lst
fi

cp /media/$1/vmlinuz /ssd/boot/

umount /ssd
sync

echo "Remove your installation media, and press ENTER"

read enter

echo "Rebooting..."
reboot -f
