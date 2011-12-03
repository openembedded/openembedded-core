DESCRIPTION = "Highly configurable, modular and secure inetd"
HOMEPAGE = "http://www.xinetd.org"

# xinetd is a BSD-like license
# Apple and Gentoo say BSD here.
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=8ad8615198542444f84d28a6cf226dd8"

DEPENDS = ""
PR ="r1"

SRC_URI = "http://www.xinetd.org/xinetd-${PV}.tar.gz \
      file://xinetd.init \
      file://xinetd.conf \
      file://Various-fixes-from-the-previous-maintainer.patch \
      file://Disable-services-from-inetd.conf-if-a-service-with-t.patch \
      file://xinetd-should-be-able-to-listen-on-IPv6-even-in-ine.patch \
      "

SRC_URI[md5sum] = "567382d7972613090215c6c54f9b82d9"
SRC_URI[sha256sum] = "760e0e617c609a0509ef63fc7e9580d2f1d88c6113bb6d63273de7de7cd0bc1f"

inherit autotools update-rc.d

INITSCRIPT_NAME = "xinetd"
INITSCRIPT_PARAMS = "defaults"

EXTRA_OECONF="--disable-nls"

do_configure() {
	# Looks like configure.in is broken, so we are skipping
	# rebuilding configure and are just using the shipped one
	oe_runconf
}

do_install() {
	# Same here, the Makefile does some really stupid things,
	# but since we only want two files why not override
	# do_install from autotools and doing it ourselfs?
	install -d "${D}/usr/sbin"
	install -d "${D}/etc/init.d"
	install -d "${D}/etc/xinetd.d"
	install -m 644 "${WORKDIR}/xinetd.conf" "${D}/etc"
	install -m 755 "${WORKDIR}/xinetd.init" "${D}/etc/init.d/xinetd"
	install -m 755 "${S}/xinetd/xinetd" "${D}/usr/sbin"
	install -m 755 "${S}/xinetd/itox" "${D}/usr/sbin"
}

CONFFILES_${PN} = "${sysconfdir}/xinetd.conf"
