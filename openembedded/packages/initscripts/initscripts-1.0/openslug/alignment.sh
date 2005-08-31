#!/bin/sh
#
# How to handle alignment faults on the ARM
#
# 0 - ignore, the value will probably be rotated
# 1 - warn, a log message will be output
# 2 - fixup, the kernel will do an expensive aligned read
# 3 - fixup+warn
# 4 - signal, the process will get an illegal instruction fault
# 5 - signal+warn
# 6 - invalid (has no effect)
# 7 - invalid (has no effect)
#
# Set ALIGN in /etc/default/rcS to override (do not edit this
# file!)  Set ALIGN to empty to avoid changing the kernel
# default (currently '0').
ALIGN=1
. /etc/default/rcS
test -e /proc/cpu/alignment -a -n "$ALIGN" -a "$ALIGN" -ge 0 -a "$ALIGN" -lt 6 &&
	echo "$ALIGN" >/proc/cpu/alignment
