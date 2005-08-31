#! /bin/sh
#
# umountnfs.sh	Unmount all network filesystems.
#
PATH=/sbin:/bin:/usr/sbin:/usr/bin

# Write a reboot record to /var/log/wtmp before unmounting
halt -w

# Ensure /proc is mounted
test -r /proc/mounts || mount -t proc proc /proc

echo "Unmounting remote filesystems..."

#
# Read the list of mounted file systems and -f umount the
# known network file systems.  -f says umount it even if
# the server is unreachable.  Do not attempt to umount
# the root file system.  Unmount in reverse order from
# that given by /proc/mounts (otherwise it may not work).
#
unmount() {
	local dev mp type opts
	if read dev mp type opts
	then
		# recurse - unmount later items
		unmount
		# skip /, /proc and /dev
		case "$mp" in
		/|/proc)return 0;;
		/dev)	return 0;;
		esac
		# then unmount this, if nfs
		case "$type" in
		nfs|smbfs|ncpfs) umount -f "$mp";;
		esac
	fi
}

unmount </proc/mounts
