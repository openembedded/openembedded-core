#!/bin/sh

LOAD_MODULE=modprobe
[ -f /proc/modules ] || exit 0
[ -f /etc/modules ] || exit 0
[ -e /sbin/modprobe ] || LOAD_MODULE=insmod

if [ ! -e /sbin/depmod ]; then
	[ -f /lib/modules/`uname -r`/modules.dep ] || LOAD_MODULE=insmod
else 
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
