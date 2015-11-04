require qemu.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

SRC_URI += "file://configure-fix-Darwin-target-detection.patch \
            file://qemu-enlarge-env-entry-size.patch \
            file://Qemu-Arm-versatilepb-Add-memory-size-checking.patch \
            file://smc91c111_fix1.patch \
            file://smc91c111_fix2.patch \
            file://smc91c111_fix3.patch \
            file://no-valgrind.patch \
           "
SRC_URI_prepend = "http://wiki.qemu-project.org/download/${BP}.tar.bz2"
SRC_URI[md5sum] = "c99445164e77184a9ba2e7dbf7ed5c29"
SRC_URI[sha256sum] = "ecfe8b88037e41e817d72c460c56c6a0b573d540d6ba38b162d0de4fd22d1bdb"

COMPATIBLE_HOST_class-target_mips64 = "null"

do_install_append() {
    # Prevent QA warnings about installed ${localstatedir}/run
    if [ -d ${D}${localstatedir}/run ]; then rmdir ${D}${localstatedir}/run; fi
}
