#
# mountall.sh	Mount all filesystems.
#
# Version:	@(#)mountall.sh  2.83-2  01-Nov-2001  miquels@cistron.nl
#
. /etc/default/rcS

#
# Mount local filesystems in /etc/fstab. For some reason, people
# might want to mount "proc" several times, and mount -v complains
# about this. So we mount "proc" filesystems without -v.
#
test "$VERBOSE" != no && echo "Mounting local filesystems..."
mount -at nonfs,nosmbfs,noncpfs 2>/dev/null

#
# We might have mounted something over /dev, see if /dev/initctl is there.
#
if test ! -p /dev/initctl
then
	rm -f /dev/initctl
	mknod -m 600 /dev/initctl p
fi
kill -USR1 1

#
# Execute swapon command again, in case we want to swap to
# a file on a now mounted filesystem.
#
doswap=yes
case "`uname -r`" in
	2.[0123].*)
		if grep -qs resync /proc/mdstat
		then
			doswap=no
		fi
		;;
esac
if test $doswap = yes
then
	swapon -a 2> /dev/null
fi

: exit 0

