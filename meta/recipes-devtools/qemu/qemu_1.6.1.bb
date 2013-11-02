require qemu.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

SRC_URI += "file://fxrstorssefix.patch"

SRC_URI_prepend = "http://wiki.qemu.org/download/qemu-${PV}.tar.bz2"
SRC_URI[md5sum] = "3a897d722457c5a895cd6ac79a28fda0"
SRC_URI[sha256sum] = "fc736f44aa10478223c881310a7e40fc8386547e9cadf7d01ca4685951605294"

COMPATIBLE_HOST_class-target_mips64 = "null"

do_install_append() {
    # Prevent QA warnings about installed ${localstatedir}/run
    if [ -d ${D}${localstatedir}/run ]; then rmdir ${D}${localstatedir}/run; fi
}
