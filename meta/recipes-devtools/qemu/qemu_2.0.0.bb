require qemu.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

SRC_URI += "file://qemu-enlarge-env-entry-size.patch \
            file://Qemu-Arm-versatilepb-Add-memory-size-checking.patch"

SRC_URI_prepend = "http://wiki.qemu-project.org/download/${BP}.tar.bz2"
SRC_URI[md5sum] = "2790f44fd76da5de5024b4aafeb594c2"
SRC_URI[sha256sum] = "60cc1aa0cad39cec891f970bed60ca8a484f071adad4943123599ac223543a3b"

COMPATIBLE_HOST_class-target_mips64 = "null"

do_sanitize_sources() {
    # These .git files point to a nonexistent path "../.git/modules" and will confuse git
    # if it tries to recurse into those directories.
    rm -f ${S}/dtc/.git ${S}/pixman/.git
}

addtask sanitize_sources after do_unpack before do_patch

do_install_append() {
    # Prevent QA warnings about installed ${localstatedir}/run
    if [ -d ${D}${localstatedir}/run ]; then rmdir ${D}${localstatedir}/run; fi
}
