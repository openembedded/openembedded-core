require pcmciautils.inc

PR = "r0"

SRC_URI += "file://makefile_fix.patch \
            file://version_workaround.patch"

FILES_${PN}-dbg += "${libdir}/udev/.debug"
FILES_${PN} += "${libdir}/udev"
