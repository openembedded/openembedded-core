require hal.inc

PR = "r5"

EXTRA_OECONF += "--with-linux-input-header=${STAGING_INCDIR}/linux/input.h"
EXTRA_OEMAKE += "-e 'udevrulesdir=$(sysconfdir)/udev/rules.d'"

PACKAGES =+ "libhal libhal-storage"

FILES_libhal = "${libdir}/libhal.so.*"
FILES_libhal-storage = "${libdir}/libhal-storage.so.*"

FILES_${PN} =+ "${bindir}/hal-disable-polling \
                ${bindir}/hal-setup-keymap"

SRC_URI[md5sum] = "e9163df591a6f38f59fdbfe33e73bf20"
SRC_URI[sha256sum] = "323aacfa52f12def3b0d1e76456e34f027c345adc344aad19a8cc0c59c1a8d02"

SRC_URI += "file://probe-video4linux.c.patch"
