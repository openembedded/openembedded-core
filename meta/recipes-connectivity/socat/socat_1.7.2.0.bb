DESCRIPTION = "Socat is a relay for bidirectional data \
transfer between two independent data channels."
HOMEPAGE = "http://www.dest-unreach.org/socat/"

SECTION = "console/network"

DEPENDS = "openssl"

LICENSE = "GPL-2.0+-with-OpenSSL-exception"

PR = "r0"
SRC_URI = "http://www.dest-unreach.org/socat/download/socat-${PV}.tar.bz2;name=src \
           file://compile.patch"

LIC_FILES_CHKSUM = "file://COPYING;md5=18810669f13b87348459e611d31ab760 \
                    file://README;beginline=252;endline=282;md5=79246f11a1db0b6ccec54d1fb711c01e"

SRC_URI[src.md5sum] = "eb563dd00b9d39a49fb62a677fc941fe"
SRC_URI[src.sha256sum] = "59b3bde927c14fbc3f9e42c782971710da8a89bbf46f7531f09a681754041802"

EXTRA_OECONF = " --disable-termios "

inherit autotools

do_install_prepend () {
    mkdir -p ${D}${bindir}
    install -d ${D}${bindir} ${D}${mandir}/man1
}
