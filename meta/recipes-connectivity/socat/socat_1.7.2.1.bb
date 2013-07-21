DESCRIPTION = "Socat is a relay for bidirectional data \
transfer between two independent data channels."
HOMEPAGE = "http://www.dest-unreach.org/socat/"

SECTION = "console/network"

DEPENDS = "openssl readline"

LICENSE = "GPL-2.0+-with-OpenSSL-exception"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://README;beginline=252;endline=282;md5=79246f11a1db0b6ccec54d1fb711c01e"


PR = "r0"
SRC_URI = "http://www.dest-unreach.org/socat/download/socat-${PV}.tar.bz2;name=src \
           file://compile.patch"

SRC_URI[src.md5sum] = "7ddfea7e9e85f868670f94d3ea08358b"
SRC_URI[src.sha256sum] = "faea2ed6c63bb97a59237fd43b7c35ad248317297e8bfeb2e6f2ec1e6bc58277"

PACKAGECONFIG ??= "tcp-wrappers"
PACKAGECONFIG[tcp-wrappers] = "--enable-libwrap,--disable-libwrap,tcp-wrappers"

EXTRA_OECONF = " --disable-termios "

inherit autotools

do_install_prepend () {
    mkdir -p ${D}${bindir}
    install -d ${D}${bindir} ${D}${mandir}/man1
}
