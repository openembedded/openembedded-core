DESCRIPTION = "LIRC is a package that allows you to decode and send infra-red signals of many (but not all) commonly used remote controls."
PRIORITY = "optional"
SECTION = "console/network"
LICENSE = "GPLv2+"
DEPENDS = "virtual/kernel fakeroot-native setserial"
PR = "r1"
HOMEPAGE = "http://www.lirc.org/"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"
inherit autotools module-base
SRC_URI = "http://downloads.sourceforge.net/project/lirc/LIRC/${PV}/lirc-${PV}.tar.bz2 \
           file://${PN}-${PV}-remove-systemd-and-add-usb_irtoy.patch; \
"
SRC_URI[md5sum] = "3b78c3cc872d5e2fa78b796c2efd46db"
SRC_URI[sha256sum] = "465e65abc893c305ec347b771e2b98bbc3465ca9ba8c1dcf4cd243107ac2536d"
S = "${WORKDIR}/lirc-${PV}"
EXTRA_OECONF = "--with-kerneldir=${STAGING_KERNEL_DIR} --with-driver=userspace"

do_configure_prepend() {
        rm -Rf ${S}/systemd
		sed 's/\/var\/run\/lirc/\/var\/run/' ${S}/lirc_options.conf > ${S}/lirc_options.conf.new
		mv ${S}/lirc_options.conf.new ${S}/lirc_options.conf
}
