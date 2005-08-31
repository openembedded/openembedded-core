DESCRIPTION = "Client for Wi-Fi Protected Access (WPA)."
SECTION = "network"
LICENSE = "GPL"
MAINTAINER = "Holger Schurig"
HOMEPAGE = "http://hostap.epitest.fi/wpa_supplicant/"
DEPENDS = "openssl"
PV = "0.0cvs${CVSDATE}"
PR = "r2"

SRC_URI = "cvs://anonymous@hostap.epitest.fi/cvs;module=hostap \
	file://use-channel.patch;patch=1 \
	file://driver-hermes.patch;patch=1 \
	file://defconfig \
	file://defaults \
	file://init.sh \
	file://wpa_supplicant.conf"
S = "${WORKDIR}/hostap/wpa_supplicant"


PACKAGES_prepend = "wpa-supplicant-passphrase wpa-supplicant-cli "
FILES_wpa-supplicant-passphrase = "/usr/sbin/wpa_passphrase"
FILES_wpa-supplicant-cli = "/usr/sbin/wpa_cli"

RRECOMMENDS_${PN} = "wpa-supplicant-passphrase wpa-supplicant-cli"


INITSCRIPT_NAME = "wpa"
INITSCRIPT_PARAMS = "defaults 10"
inherit update-rc.d


do_configure () {
	install -m 0755 ${WORKDIR}/defconfig  .config
}


do_compile () {
	make
}


do_install () {
	set -x
	install -d ${D}${sbindir}
	install -m755 wpa_supplicant ${D}${sbindir}
	install -m755 wpa_passphrase ${D}${sbindir}
	install -m755 wpa_cli        ${D}${sbindir}

	install -d ${D}${localstatedir}/run/wpa_supplicant

	install -d ${D}${docdir}/wpa_supplicant
	install -m644 README ${WORKDIR}/wpa_supplicant.conf ${D}${docdir}/wpa_supplicant

	install -d ${D}${sysconfdir}/init.d
	install -m700 ${WORKDIR}/init.sh ${D}${sysconfdir}/init.d/wpa

	install -d ${D}${sysconfdir}/default
	install -m600 ${WORKDIR}/defaults ${D}${sysconfdir}/default/wpa
}
