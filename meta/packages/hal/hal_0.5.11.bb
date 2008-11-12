require hal.inc

PR = "r1"

SRC_URI += " file://hal-right-input-h.patch;patch=1 \
             file://fix-configure.diff;patch=1"

EXTRA_OECONF += "--with-linux-input-header=${STAGING_INCDIR}/linux/input.h"

PACKAGES =+ "libhal libhal-storage"

FILES_libhal = "${libdir}/libhal.so.*"
FILES_libhal-storage = "${libdir}/libhal-storage.so.*"

FILES_${PN} =+ "${bindir}/hal-disable-polling \
                ${bindir}/hal-setup-keymap"
