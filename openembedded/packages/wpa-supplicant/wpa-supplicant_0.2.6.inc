SECTION = "kernel/userland"
HOMEPAGE = "http://hostap.epitest.fi/wpa_supplicant/"
PRIORITY = "optional"
MAINTAINER = "Michael 'Mickey' Lauer <mickey@Vanille.de>"
LICENSE = "GPL"

SRC_URI = "http://hostap.epitest.fi/releases/wpa_supplicant-${PV}.tar.gz \
	   file://wpa-defconfig \
	   file://wpa_supplicant_default.conf"

RREPLACES_${PN} = "hostap-${PN}"
RPROVIDES_${PN} = "hostap-${PN}"
RCONFLICTS_${PN} = "hostap-${PN}"

S = "${WORKDIR}/wpa_supplicant-${PV}"

do_compile() {
	unset CFLAGS
	base_do_compile
}

do_configure() {
	install -m 0644 ${WORKDIR}/wpa-defconfig .config
}

do_install() {
	install -d ${D}${sysconfdir} ${D}${bindir}
	install -m 0644 wpa_supplicant.conf ${D}${sysconfdir}/wpa_supplicant.conf.sample
	install -m 0644 ../wpa_supplicant_default.conf ${D}${sysconfdir}/wpa_supplicant.conf
	install -m 0755 wpa_passphrase ${D}${bindir}
	install -m 0755 wpa_cli        ${D}${bindir}
	install -m 0755 wpa_supplicant ${D}${bindir}
}

