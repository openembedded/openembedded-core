#
# bootmisc.sh	Miscellaneous things to be done during bootup.
#

. /etc/default/rcS
#
# Put a nologin file in /etc to prevent people from logging in before
# system startup is complete.
#
if test "$DELAYLOGIN" = yes
then
  echo "System bootup in progress - please wait" > /etc/nologin
  cp /etc/nologin /etc/nologin.boot
fi

#
# Set pseudo-terminal access permissions.
#
if ( ! grep -q devfs /proc/mounts ) && test -c /dev/ttyp0
then
	chmod 666 /dev/tty[p-za-e][0-9a-f]
	chown root:tty /dev/tty[p-za-e][0-9a-f]
fi

#
# Apply /proc settings if defined
#
SYSCTL_CONF="/etc/sysctl.conf"
if [ -f "${SYSCTL_CONF}" ]
then
	if [ -x "/sbin/sysctl" ]
	then
		/sbin/sysctl -p "${SYSCTL_CONF}"
	else
		echo "To have ${SYSCTL_CONF} applied during boot, install package <procps>."
	fi
fi

#
# Update /etc/motd.
#
if test "$EDITMOTD" != no
then
	uname -a > /etc/motd.tmp
	sed 1d /etc/motd >> /etc/motd.tmp
	mv /etc/motd.tmp /etc/motd
fi

#
# This is as good a place as any for a sanity check
# /tmp should be a symlink to /var/tmp to cut down on the number
# of mounted ramdisks.
if test ! -L /tmp && test -d /var/tmp
then
	rm -rf /tmp
	ln -sf /var/tmp /tmp
fi

#
# Update dynamic library cache
#
/sbin/ldconfig

# 
# Recover the time, if there is a time file                 
#
if test -e /etc/timestamp
then
	date -s `cat /etc/timestamp`
fi
: exit 0
