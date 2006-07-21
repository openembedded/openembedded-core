#! /bin/sh
#
# umountnfs.sh	Unmount all network filesystems.
#

PATH=/sbin:/bin:/usr/sbin:/usr/bin

# Write a reboot record to /var/log/wtmp before unmounting
halt -w

echo "Unmounting remote filesystems..."

test -f /etc/fstab && (

#
#	Read through fstab line by line and unount network file systems
#
while read device mountpt fstype options
do
	if test "$fstype" = nfs ||  test "$fstype" = smbfs ||  test "$fstype" = ncpfs
	then
		umount -f $mountpt
	fi
done
) < /etc/fstab

: exit 0

