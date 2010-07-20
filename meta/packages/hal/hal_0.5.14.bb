require hal.inc

PR = "r0"

EXTRA_OECONF += "--with-linux-input-header=${STAGING_INCDIR}/linux/input.h"
EXTRA_OEMAKE += "-e 'udevrulesdir=$(sysconfdir)/udev/rules.d'"

PACKAGES =+ "libhal libhal-storage"

FILES_libhal = "${libdir}/libhal.so.*"
FILES_libhal-storage = "${libdir}/libhal-storage.so.*"

FILES_${PN} =+ "${bindir}/hal-disable-polling \
                ${bindir}/hal-setup-keymap"
