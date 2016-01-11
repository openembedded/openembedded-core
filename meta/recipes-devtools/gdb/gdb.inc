require gdb-common.inc

inherit gettext

SRC_URI += "file://0002-Change-order-of-CFLAGS.patch \
            file://0003-Add-support-for-Renesas-SH-sh4-architecture.patch \
            file://0001-Use-exported-definitions-of-SIGRTMIN.patch \
           "
#LDFLAGS_append = " -s"
#export CFLAGS_append=" -L${STAGING_LIBDIR}"

# cross-canadian must not see this
PACKAGES =+ "gdbserver"
FILES_gdbserver = "${bindir}/gdbserver"
