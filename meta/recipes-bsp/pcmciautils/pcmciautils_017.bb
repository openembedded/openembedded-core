require pcmciautils.inc

PR = "r0"

SRC_URI += "file://makefile_fix.patch \
            file://version_workaround.patch"

SRC_URI[md5sum] = "5245af28eeba57ec0606a874d44d10f7"
SRC_URI[sha256sum] = "2045f0e8f837f44aed72ac91c1a8cf3b899caf098a25d04f47982be6386bd4e1"

FILES_${PN}-dbg += "${libdir}/udev/.debug"
FILES_${PN} += "${libdir}/udev"
