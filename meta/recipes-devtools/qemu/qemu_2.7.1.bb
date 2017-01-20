require qemu.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

SRC_URI += "file://configure-fix-Darwin-target-detection.patch \
            file://qemu-enlarge-env-entry-size.patch \
            file://Qemu-Arm-versatilepb-Add-memory-size-checking.patch \
            file://no-valgrind.patch \
            file://pathlimit.patch \
            file://qemu-2.5.0-cflags.patch \
            file://0003-fix-CVE-2016-7908.patch \
            file://0004-fix-CVE-2016-7909.patch \
"

SRC_URI =+ "http://wiki.qemu-project.org/download/${BP}.tar.bz2"

SRC_URI[md5sum] = "a315bc51ed443a08d2cf1416d76b9ab4"
SRC_URI[sha256sum] = "68636788eb69bcb0b44ba220b32b50495d6bd5712a934c282217831c4822958f"

COMPATIBLE_HOST_mipsarchn32 = "null"
COMPATIBLE_HOST_mipsarchn64 = "null"

do_install_append() {
    # Prevent QA warnings about installed ${localstatedir}/run
    if [ -d ${D}${localstatedir}/run ]; then rmdir ${D}${localstatedir}/run; fi
}
