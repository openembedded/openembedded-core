DESCRIPTION = "setserial is a program designed to set and/or report the configuration information associated with a serial port"
HOMEPAGE = "http://setserial.sourceforge.net"
AUTHOR = "Theodore Ts'o <tytso@mit.edu>"
SECTION = "console/utils"
LICENSE = "GPL"
PR = "r2"

inherit autotools

SRC_URI = "${SOURCEFORGE_MIRROR}/setserial/${PN}-${PV}.tar.gz"

do_install() {
    install -d ${D}${bindir}
    install -d ${D}${mandir}/man8

    install -m 0755 ${S}/setserial   ${D}${bindir}
    install -m 0644 ${S}/setserial.8 ${D}${mandir}/man8
}
