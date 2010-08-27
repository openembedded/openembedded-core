#!/bin/sh
### BEGIN INIT INFO
# Provides:          module-init-tools
# Required-Start:    
# Required-Stop:     
# Should-Start:      checkroot
# Should-stop:
# Default-Start:     S
# Default-Stop:
# Short-Description: Process /etc/modules.
# Description:       Load the modules listed in /etc/modules.
### END INIT INFO

LOAD_MODULE=modprobe
[ -f /proc/modules ] || exit 0
[ -f /etc/modules ] || exit 0
[ -e /sbin/modprobe ] || LOAD_MODULE=insmod

if [ ! -f /lib/modules/`uname -r`/modules.dep ]; then
	[ "$VERBOSE" != no ] && echo "Calculating module dependencies ..."
	depmod -Ae
fi

[ "$VERBOSE" != no ] && echo -n "Loading modules: "
(cat /etc/modules; echo; ) |
while read module args
do
	case "$module" in
		\#*|"") continue ;;
	esac
	[ "$VERBOSE" != no ] && echo -n "$module "
	eval "$LOAD_MODULE $module $args >/dev/null 2>&1"
done
[ "$VERBOSE" != no ] && echo

exit 0
