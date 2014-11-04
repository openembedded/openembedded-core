require gdb-common.inc

inherit gettext

SRC_URI += "file://gdbserver-cflags-last.diff;striplevel=0 \
            file://renesas-sh-native-support.patch \
           "
#LDFLAGS_append = " -s"
#export CFLAGS_append=" -L${STAGING_LIBDIR}"

# cross-canadian must not see this
PACKAGES =+ "gdbserver"
FILES_gdbserver = "${bindir}/gdbserver"
