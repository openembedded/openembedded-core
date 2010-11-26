SUMMARY = "Controls the configuration of serial ports"
DESCRIPTION = "setserial is a program designed to set and/or report the configuration information associated with a serial port"
HOMEPAGE = "http://setserial.sourceforge.net"
AUTHOR = "Theodore Ts'o <tytso@mit.edu>"
SECTION = "console/utils"

# general GPL, no specific version
LICENSE = "GPL"
LIC_FILES_CHKSUM = "file://version.h;beginline=1;endline=6;md5=2e7c59cb9e57e356ae81f50f4e4dfd99"
PR = "r3"

inherit autotools

SRC_URI = "${SOURCEFORGE_MIRROR}/setserial/${PN}-${PV}.tar.gz \
           file://add_stdlib.patch \
          "

do_install() {
    install -d ${D}${bindir}
    install -d ${D}${mandir}/man8

    install -m 0755 ${S}/setserial   ${D}${bindir}
    install -m 0644 ${S}/setserial.8 ${D}${mandir}/man8
}
