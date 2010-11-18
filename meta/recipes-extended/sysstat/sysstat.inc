SUMMARY = "System performance tools"
DESCRIPTION = "The sysstat utilities are a collection of performance monitoring tools for Linux."
HOMEPAGE = "http://sebastien.godard.pagesperso-orange.fr/"
LICENSE = "GPLv2+"
SECTION = "console/utils"

DEPENDS = "virtual/libintl"

SRC_URI = "http://pagesperso-orange.fr/sebastien.godard/sysstat-${PV}.tar.gz \
		   file://99_sysstat"

inherit autotools

do_install() {
	autotools_do_install

	# don't install /var/log/sa when populating rootfs. Do it through volatile

	rm -rf ${D}/var
	install -d ${D}/etc/default/volatiles
	install -m 0644 ${WORKDIR}/99_sysstat ${D}/etc/default/volatiles
}

pkg_postinst_${PN} () {
        /etc/init.d/populate-volatile.sh update
}


FILES_${PN} += "${libdir}/sa"

TARGET_CC_ARCH += "${LDFLAGS}"
LDFLAGS_append_linux-uclibc = " -lintl"
LDFLAGS_append_linux-uclibceabi = " -lintl"
LDFLAGS_append_linux-uclibcspe = " -lintl"
