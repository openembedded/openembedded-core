require kexec-tools.inc
export LDFLAGS = "-L${STAGING_LIBDIR}"
EXTRA_OECONF = " --with-zlib=yes"

SRC_URI += "file://kexec-tools-Refine-kdump-device_tree-sort.patch"

SRC_URI[md5sum] = "2309ba43981cb6d39d07ac3a9aac30ab"
SRC_URI[sha256sum] = "dde5c38be39882c6c91f0129647349c4e1943b077d3020af1970b481ee954eb0"

PACKAGES =+ "kexec kdump vmcore-dmesg"

ALLOW_EMPTY_${PN} = "1"
RRECOMMENDS_${PN} = "kexec kdump vmcore-dmesg"

FILES_kexec = "${sbindir}/kexec"
FILES_kdump = "${sbindir}/kdump"
FILES_vmcore-dmesg = "${sbindir}/vmcore-dmesg"
