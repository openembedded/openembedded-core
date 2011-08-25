DESCRIPTION = "Various benchmarning tests for X"
HOMEPAGE = "http://www.o-hand.com"
SECTION = "devel"
LICENSE = "Zlib"
DEPENDS = "pango libxext libxft virtual/libx11 gtk+"

SRCREV = "218b99d82b56011e3f1b909e6baf22ce25af6334"
PV = "0.1+git${SRCPV}"
PR = "r0"


SRC_URI = "git://git.yoctoproject.org/${BPN};protocol=git \
           file://dso_linking_change_build_fix.patch"

S = "${WORKDIR}/git/tests"

inherit autotools

do_install() {
    install -d ${D}${bindir}
    find . -name "test-*" -type f -perm -755 -exec install -m 0755 {} ${D}${bindir} \;   
}


LIC_FILES_CHKSUM = "file://test-pango-gdk.c;endline=24;md5=1ee74ec851ecda57eb7ac6cc180f7655"
