require qemu.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=441c28d2cf86e15a37fa47e15a72fbac \
                    file://COPYING.LIB;endline=24;md5=c04def7ae38850e7d3ef548588159913"

SRC_URI += "file://configure-fix-Darwin-target-detection.patch \
            file://qemu-enlarge-env-entry-size.patch \
            file://Qemu-Arm-versatilepb-Add-memory-size-checking.patch \
            file://0001-Back-porting-security-fix-CVE-2014-5388.patch \
            file://qemu-CVE-2015-3456.patch \
            file://CVE-2014-7840.patch \
            file://vnc-CVE-2014-7815.patch \
            file://slirp-CVE-2014-3640.patch \
            "
SRC_URI_prepend = "http://wiki.qemu-project.org/download/${BP}.tar.bz2"
SRC_URI[md5sum] = "6726977292b448cbc7f89998fac6983b"
SRC_URI[sha256sum] = "397e23184f4bf613589a8fe0c6542461dc2afdf17ed337e97e6fd2f31e8f8802"

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
