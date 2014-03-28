require qemu.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

SRC_URI += "file://fxrstorssefix.patch \
            file://qemu-enlarge-env-entry-size.patch \
            file://Qemu-Arm-versatilepb-Add-memory-size-checking.patch"

SRC_URI_prepend = "http://wiki.qemu.org/download/qemu-${PV}.tar.bz2"
SRC_URI[md5sum] = "32893941d40d052a5e649efcf06aca06"
SRC_URI[sha256sum] = "31f333a85f2d14c605a77679904a9668eaeb1b6dc7da53a1665230f46bc21314"

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
