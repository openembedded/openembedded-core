DESCRIPTION = "Swabber is a tool that can help with understanding a program's use of host files."
HOMEPAGE = "http://git.yoctoproject.org/cgit/cgit.cgi/swabber"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=393a5ca445f6965873eca0259a17f833"

SRCREV = "a0792390c5d6d5a5bade7ab155c80eef3f30fa52"
PV = "0.0+git${SRCPV}"
PR = "r0"

S = "${WORKDIR}/git"

SRC_URI = "git://git.yoctoproject.org/swabber;protocol=git"

inherit native

do_configure () {
	:
}

do_install() {
  oe_runmake 'DESTDIR=${D}' install
}
