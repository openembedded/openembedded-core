require qemu.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

SRC_URI += "file://configure-fix-Darwin-target-detection.patch \
            file://qemu-enlarge-env-entry-size.patch \
            file://no-valgrind.patch \
            file://pathlimit.patch \
            file://qemu-2.5.0-cflags.patch \
            file://target-ppc-fix-user-mode.patch \
"

SRC_URI =+ "http://wiki.qemu-project.org/download/${BP}.tar.bz2"

SRC_URI[md5sum] = "17940dce063b6ce450a12e719a6c9c43"
SRC_URI[sha256sum] = "dafd5d7f649907b6b617b822692f4c82e60cf29bc0fc58bc2036219b591e5e62"

COMPATIBLE_HOST_mipsarchn32 = "null"
COMPATIBLE_HOST_mipsarchn64 = "null"

do_install_append() {
    # Prevent QA warnings about installed ${localstatedir}/run
    if [ -d ${D}${localstatedir}/run ]; then rmdir ${D}${localstatedir}/run; fi
}
