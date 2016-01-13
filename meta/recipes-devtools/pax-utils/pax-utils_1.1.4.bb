SUMMARY = "Security-focused ELF files checking tool"
DESCRIPTION = "This is a small set of various PaX aware and related \
utilities for ELF binaries. It can check ELF binary files and running \
processes for issues that might be relevant when using ELF binaries \
along with PaX, such as non-PIC code or executable stack and heap."
HOMEPAGE = "http://www.gentoo.org/proj/en/hardened/pax-utils.xml"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=eb723b61539feef013de476e68b5c50a"

SRC_URI = "http://gentoo.osuosl.org/distfiles/pax-utils-${PV}.tar.xz \
           file://fix-configure-failure.patch \
"

SRC_URI[md5sum] = "1bfbb5237881f0c5e476b2f7ffcc00d5"
SRC_URI[sha256sum] = "796860fbd48c5f811e699e8492dc1110459bad0a62efa8d346b74a4f6f556414"

RDEPENDS_${PN} += "bash"

do_configure_prepend() {
    touch ${S}/NEWS ${S}/AUTHORS ${S}/ChangeLog
}

do_install() {
    oe_runmake PREFIX=${D}${prefix} DESTDIR=${D} install
}

BBCLASSEXTEND = "native"

inherit autotools pkgconfig

PACKAGECONFIG ??= "${@bb.utils.contains('DISTRO_FEATURES', 'largefile', 'largefile', '', d)} \
"
PACKAGECONFIG[libcap] = "--with-caps, --without-caps, libcap"
PACKAGECONFIG[libseccomp] = "--with-seccomp, --without-seccomp, libseccomp"
PACKAGECONFIG[largefile] = "--enable-largefile,--disable-largefile,,"
PACKAGECONFIG[pyelftools] = "--with-python, --without-python,, pyelftools"
