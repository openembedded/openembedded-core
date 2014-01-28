SUMMARY = "Client for Wi-Fi Protected Access (WPA)"
HOMEPAGE = "http://hostap.epitest.fi/wpa_supplicant/"
BUGTRACKER = "http://hostap.epitest.fi/bugz/"
SECTION = "network"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://COPYING;md5=ab87f20cd7e8c0d0a6539b34d3791d0e \
                    file://README;md5=5cb758942d25f6f61fd4ac388fd446fa \
                    file://wpa_supplicant/wpa_supplicant.c;beginline=1;endline=12;md5=cba4fa09fa364da845ca546f21008909"
DEPENDS = "gnutls dbus libnl openssl"
RRECOMMENDS_${PN} = "wpa-supplicant-passphrase wpa-supplicant-cli"

inherit systemd

SYSTEMD_SERVICE_${PN} = "wpa_supplicant.service wpa_supplicant-nl80211@.service wpa_supplicant-wired@.service"
SYSTEMD_AUTO_ENABLE = "disable"

SRC_URI = "http://hostap.epitest.fi/releases/wpa_supplicant-${PV}.tar.gz \
           file://defconfig-gnutls \
           file://wpa-supplicant.sh \
           file://wpa_supplicant.conf \
           file://wpa_supplicant.conf-sane \
           file://99_wpa_supplicant \
           file://fix-libnl3-host-contamination.patch \
           file://register-autoscan-correctly.patch"

S = "${WORKDIR}/wpa_supplicant-${PV}"

PACKAGES_prepend = "wpa-supplicant-passphrase wpa-supplicant-cli "
FILES_wpa-supplicant-passphrase = "${bindir}/wpa_passphrase"
FILES_wpa-supplicant-cli = "${sbindir}/wpa_cli"
FILES_${PN} += "${datadir}/dbus-1/system-services/*"
CONFFILES_${PN} += "${sysconfdir}/wpa_supplicant.conf"

do_configure () {
	install -m 0755 ${WORKDIR}/defconfig-gnutls wpa_supplicant/.config
	echo "CFLAGS +=\"-I${STAGING_INCDIR}/libnl3\"" >> wpa_supplicant/.config
}

export EXTRA_CFLAGS = "${CFLAGS}"
export BINDIR = "${sbindir}"

do_compile () {
	unset CFLAGS CPPFLAGS CXXFLAGS
	sed -e "s:CFLAGS\ =.*:& \$(EXTRA_CFLAGS):g" -i ${S}/src/lib.rules
	oe_runmake -C wpa_supplicant
}

do_install () {
	install -d ${D}${sbindir}
	install -m 755 wpa_supplicant/wpa_supplicant ${D}${sbindir}
	install -m 755 wpa_supplicant/wpa_cli        ${D}${sbindir}

	install -d ${D}${bindir}
	install -m 755 wpa_supplicant/wpa_passphrase ${D}${bindir}

	install -d ${D}${docdir}/wpa_supplicant
	install -m 644 wpa_supplicant/README ${WORKDIR}/wpa_supplicant.conf ${D}${docdir}/wpa_supplicant

	install -d ${D}${sysconfdir}
	install -m 600 ${WORKDIR}/wpa_supplicant.conf-sane ${D}${sysconfdir}/wpa_supplicant.conf

	install -d ${D}${sysconfdir}/network/if-pre-up.d/
	install -d ${D}${sysconfdir}/network/if-post-down.d/
	install -d ${D}${sysconfdir}/network/if-down.d/
	install -m 755 ${WORKDIR}/wpa-supplicant.sh ${D}${sysconfdir}/network/if-pre-up.d/wpa-supplicant
	cd ${D}${sysconfdir}/network/ && \
	ln -sf ../if-pre-up.d/wpa-supplicant if-post-down.d/wpa-supplicant

	install -d ${D}/${sysconfdir}/dbus-1/system.d
	install -m 644 ${S}/wpa_supplicant/dbus/dbus-wpa_supplicant.conf ${D}/${sysconfdir}/dbus-1/system.d
	install -d ${D}/${datadir}/dbus-1/system-services
	install -m 644 ${S}/wpa_supplicant/dbus/*.service ${D}/${datadir}/dbus-1/system-services

	if ${@base_contains('DISTRO_FEATURES','systemd','true','false',d)}; then
		install -d ${D}/${systemd_unitdir}/system
		install -m 644 ${S}/wpa_supplicant/systemd/*.service ${D}/${systemd_unitdir}/system
	fi

	install -d ${D}/etc/default/volatiles
	install -m 0644 ${WORKDIR}/99_wpa_supplicant ${D}/etc/default/volatiles
}

pkg_postinst_wpa-supplicant () {
	# If we're offline, we don't need to do this.
	if [ "x$D" = "x" ]; then
		killall -q -HUP dbus-daemon || true
	fi

}
