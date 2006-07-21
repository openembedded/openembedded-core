DESCRIPTION = "LIRC is a package that allows you to decode and send infra-red signals of many commonly used remote controls."
SECTION = "base"
PRIORITY = "optional"
MAINTAINER = "Michael 'Mickey' Lauer <mickey@Vanille.de>"
LICENSE = "GPL"
DEPENDS = "virtual/kernel fakeroot-native"
PR = "r2"

SRC_URI = "${SOURCEFORGE_MIRROR}/lirc/lirc-${PV}.tar.gz \
           file://lirc_sir-sa1100.patch;patch=1"
S = "${WORKDIR}/lirc-${PV}"

inherit autotools module-base

include lirc-config.inc

do_compile() {
	# ${KERNEL_LD} doesn't understand the LDFLAGS, so suppress them
	cd drivers && oe_runmake CC="${KERNEL_CC}" LD="${KERNEL_LD}" LDFLAGS=""
}

fakeroot do_install() {
	oe_runmake -C drivers DESTDIR="${D}" moduledir="/lib/modules/${KERNEL_VERSION}/lirc" install
	rm -rf ${D}/dev
}

pkg_postinst() {
#!/bin/sh
set -e
if [ ! -c $D/dev/lirc ]; then mknod $D/dev/lirc c 61 0; fi
exit 0
}

FILES_${PN} = "/lib/modules"
