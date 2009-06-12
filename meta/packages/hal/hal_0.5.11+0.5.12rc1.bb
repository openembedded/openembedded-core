require hal.inc

PR = "r2"

SRC_URI = "http://hal.freedesktop.org/releases/hal-0.5.12rc1.tar.gz \
           file://99_hal \
           file://20hal"

S = "${WORKDIR}/hal-0.5.12rc1"

EXTRA_OECONF += "--with-linux-input-header=${STAGING_INCDIR}/linux/input.h"

PACKAGES =+ "libhal libhal-storage"

FILES_libhal = "${libdir}/libhal.so.*"
FILES_libhal-storage = "${libdir}/libhal-storage.so.*"

FILES_${PN} =+ "${bindir}/hal-disable-polling \
                ${bindir}/hal-setup-keymap"
