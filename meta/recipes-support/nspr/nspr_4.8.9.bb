DESCRIPTION = "Netscape Portable Runtime Library"
HOMEPAGE =  "http://www.mozilla.org/projects/nspr/"
LICENSE = "GPL-2.0 | MPL-1.1 | LGPL-2.1"
LIC_FILES_CHKSUM = "file://configure.in;beginline=3;endline=40;md5=99d4d7d68bbc4bc60ebf8c15ed295f28 \
                    file://Makefile.in;beginline=4;endline=38;md5=c2b512182a334e1bfa1edc4d1c84a298 "
SECTION = "libs/network"

PR = "r0"

SRC_URI = "ftp://ftp.mozilla.org/pub/mozilla.org/nspr/releases/v${PV}/src/nspr-${PV}.tar.gz"

SRC_URI += "file://nspr.pc.in "

SRC_URI[md5sum] = "60770d45dc08c0f181b22cdfce5be3e8"
SRC_URI[sha256sum] = "ff43c7c819e72f03bb908e7652c5d5f59a5d31ee86c333e692650207103d1cce"

S = "${WORKDIR}/nspr-${PV}/mozilla/nsprpub"

inherit autotools

do_configure() {
	oe_runconf
}

do_compile_prepend() {
	oe_runmake CROSS_COMPILE=1 CFLAGS="-DXP_UNIX" LDFLAGS="" CC=gcc -C config export
}

do_install_append() {
    install -D ${WORKDIR}/nspr.pc.in ${D}${libdir}/pkgconfig/nspr.pc
    sed -i s:OEPREFIX:${prefix}:g ${D}${libdir}/pkgconfig/nspr.pc
    sed -i s:OELIBDIR:${libdir}:g ${D}${libdir}/pkgconfig/nspr.pc
    sed -i s:OEINCDIR:${includedir}:g ${D}${libdir}/pkgconfig/nspr.pc
    sed -i s:OEEXECPREFIX:${exec_prefix}:g ${D}${libdir}/pkgconfig/nspr.pc
}


