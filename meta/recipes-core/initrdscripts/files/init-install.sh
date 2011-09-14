#!/bin/sh -e
#
# Copyright (C) 2008-2011 Intel
#
# install.sh [device_name] [rootfs_name] [video_mode] [vga_mode]
#

# We need 20 Mb for the boot partition
boot_size=20

# 5% for the swap
swap_ratio=5

found="no"

echo "Searching for a hard drive..."
for device in 'hda' 'hdb' 'sda' 'sdb'
  do
  if [ -e /sys/block/${device}/removable ]; then
      if [ "$(cat /sys/block/${device}/removable)" = "0" ]; then
	  found="yes"

	  while true; do
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
rm -f /etc/udev/scripts/mount*

#
# Unmount anything the automounter had mounted
#
umount /dev/${device} 2> /dev/null || /bin/true
umount /dev/${device}1 2> /dev/null || /bin/true
umount /dev/${device}2 2> /dev/null || /bin/true
umount /dev/${device}3 2> /dev/null || /bin/true
umount /dev/${device}4 2> /dev/null || /bin/true
umount /dev/${device}5 2> /dev/null || /bin/true
umount /dev/${device}6 2> /dev/null || /bin/true

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
cat /proc/mounts > /etc/mtab

disk_size=$(parted /dev/${device} unit mb print | grep Disk | cut -d" " -f 3 | sed -e "s/MB//")

swap_size=$((disk_size*5/100))
rootfs_size=$((disk_size-boot_size-swap_size))

rootfs_start=$((boot_size + 1))
rootfs_end=$((rootfs_start+rootfs_size))
swap_start=$((rootfs_end+1))

bootfs=/dev/${device}1
rootfs=/dev/${device}2
swap=/dev/${device}3

echo "*****************"
echo "Boot partition size:   $boot_size MB (/dev/${device}1)"
echo "Rootfs partition size: $rootfs_size MB (/dev/${device}2)"
echo "Swap partition size:   $swap_size MB (/dev/${device}3)"
echo "*****************"
echo "Deleting partition table on /dev/${device} ..."
dd if=/dev/zero of=/dev/${device} bs=512 count=2

echo "Creating new partition table on /dev/${device} ..."
parted /dev/${device} mklabel msdos

echo "Creating boot partition on /dev/${device}1"
parted /dev/${device} mkpart primary 1 $boot_size

echo "Creating rootfs partition on /dev/${device}2"
parted /dev/${device} mkpart primary $rootfs_start $rootfs_end

echo "Creating swap partition on /dev/${device}3"
parted /dev/${device} mkpart primary $swap_start $disk_size

parted /dev/${device} print

echo "Formatting /dev/${device}1 to ext2..."
mkfs.ext3 $bootfs

echo "Formatting /dev/${device}2 to ext3..."
mkfs.ext3 $rootfs

echo "Formatting swap partition...(/dev/${device}3)"
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

if [ -f /ssd/etc/grub.d/40_custom ] ; then
    echo "Preparing custom grub2 menu..."
    sed -i "s@__ROOTFS__@$rootfs@g" /ssd/etc/grub.d/40_custom
    sed -i "s/__VIDEO_MODE__/$3/g" /ssd/etc/grub.d/40_custom
    sed -i "s/__VGA_MODE__/$4/g" /ssd/etc/grub.d/40_custom
    mount $bootfs /bootmnt
    cp /ssd/etc/grub.d/40_custom /bootmnt/40_custom
    umount /bootmnt
fi

umount /ssd
umount /rootmnt

echo "Preparing boot partition..."
mount $bootfs /ssd
grub-install --root-directory=/ssd /dev/${device}

echo "(hd0) /dev/${device}" > /ssd/boot/grub/device.map

if [ -f /ssd/40_custom ] ; then
    mv /ssd/40_custom /ssd/boot/grub/grub.cfg
    sed -i "/#/d" /ssd/boot/grub/grub.cfg
    sed -i "/exec tail/d" /ssd/boot/grub/grub.cfg
    chmod 0444 /ssd/boot/grub/grub.cfg
else
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
