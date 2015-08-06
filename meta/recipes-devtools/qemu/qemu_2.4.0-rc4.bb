require qemu.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

SRC_URI += "file://configure-fix-Darwin-target-detection.patch \
            file://qemu-enlarge-env-entry-size.patch \
            file://Qemu-Arm-versatilepb-Add-memory-size-checking.patch \
           "
SRC_URI_prepend = "http://wiki.qemu-project.org/download/${BPN}-${REALPV}.tar.bz2"
SRC_URI[md5sum] = "0e9c00b15d383043ce888f7bf17308c9"
SRC_URI[sha256sum] = "724bc17d29babe2b55107c76af7cd67e162857958a3bfe2f34bb9776f1f5f4b3"

S="${WORKDIR}/${BPN}-${REALPV}"
REALPV = "2.4.0-rc4"
PV = "2.3.99+${REALPV}"

COMPATIBLE_HOST_class-target_mips64 = "null"

do_install_append() {
    # Prevent QA warnings about installed ${localstatedir}/run
    if [ -d ${D}${localstatedir}/run ]; then rmdir ${D}${localstatedir}/run; fi
}
