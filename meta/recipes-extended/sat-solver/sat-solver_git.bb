DESCRIPTION  = "Sat Solver"
HOMEPAGE = "http://en.opensue.org/Portal:Libzypp"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE.BSD;md5=62272bd11c97396d4aaf1c41bc11f7d8"

DEPENDS = "libcheck rpm zlib expat"

PV = "0.0-git${SRCPV}"
PR = "r0"

SRCREV = "aa799f7bae0ec055e0e527203635001bb7346dbc"

SRC_URI = "git://gitorious.org/opensuse/sat-solver.git;protocol=git \
           file://cmake.patch \
           file://rpm5.patch"

S = "${WORKDIR}/git"

inherit cmake pkgconfig
