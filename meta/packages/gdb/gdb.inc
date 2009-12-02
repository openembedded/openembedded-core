require gdb-common.inc

inherit gettext

SRC_URI += "file://kill_arm_map_symbols.patch;patch=1 \
            file://gdbserver-cflags-last.diff;patch=1;pnum=0 \
            file://ignore_whitespace_changes.patch;patch=1 "

#LDFLAGS_append = " -s"
#export CFLAGS_append=" -L${STAGING_LIBDIR}"

