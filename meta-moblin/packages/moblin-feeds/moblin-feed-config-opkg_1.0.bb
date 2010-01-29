DESCRIPTION = "Moblin feed configuration files (Online package repositories)"

PR = "r2"
PACKAGE_ARCH = "${MACHINE_ARCH}"

FEEDNAMEPREFIX ?= "INVALID"
FEEDURIPREFIX ?= "INVALID"

do_compile() {
	mkdir -p ${S}/${sysconfdir}/opkg/

	ipkgarchs="${PACKAGE_ARCHS}"

	basefeedconf=${S}/${sysconfdir}/opkg/base-feeds.conf

	rm -f $basefeedconf

	for arch in $ipkgarchs; do
	        echo "src/gz ${FEEDNAMEPREFIX}-$arch http://pokylinux.org/${FEEDURIPREFIX}$arch" >> $basefeedconf
	done
}


do_install () {
	install -d ${D}${sysconfdir}/opkg
	install -m 0644  ${S}/${sysconfdir}/opkg/* ${D}${sysconfdir}/opkg/
}

FILES_${PN} = "${sysconfdir}/opkg/ "

CONFFILES_${PN} += "${sysconfdir}/opkg/base-feeds.conf"
