DESCRIPTION = "NetworkManager"
SECTION = "net/misc"
LICENSE = "GPL"
HOMEPAGE = "http://www.gnome.org"
PRIORITY = "optional"
DEPENDS = "libnl dbus dbus-glib hal gconf-dbus wireless-tools"
RDEPENDS = "wpa-supplicant iproute2 dhcdbd"

PR = "r5"

SRC_URI="svn://svn.gnome.org/svn/NetworkManager/branches;module=NETWORKMANAGER_0_6_0_RELEASE;proto=http \
	file://NetworkManager \
	file://99_networkmanager"

EXTRA_OECONF = " \
		--without-gnome \
		--with-distro=debian \
		--without-gcrypt \
 		--with-wpa_supplicant=/usr/sbin/wpa_supplicant \
		--with-dhcdbd=/sbin/dhcdbd"

S = "${WORKDIR}/NETWORKMANAGER_0_6_0_RELEASE"

inherit autotools

do_staging () {
	autotools_stage_includes
	oe_libinstall -C libnm-util libnm-util ${STAGING_LIBDIR}
	oe_libinstall gnome/libnm_glib libnm_glib ${STAGING_LIBDIR}
}

do_install () {
	oe_libinstall -C libnm-util libnm-util ${D}/usr/lib
	oe_libinstall -C gnome/libnm_glib libnm_glib ${D}/usr/lib

	oe_runmake -C src DESTDIR="${D}" install
	install -d ${D}/etc/default/volatiles
	install -m 0644 ${WORKDIR}/99_networkmanager ${D}/etc/default/volatiles
	install -d ${D}/etc/init.d/
	install -m 0755 ${WORKDIR}/NetworkManager ${D}/etc/init.d/
	install -d ${D}/${datadir}/
}

pkg_postinst () {
	/etc/init.d/populate-volatile.sh update
}

FILES_${PN} += "${datadir} \
		${libdir}/*.so* \
		${libdir}/*.la \
		${sbindir} \
		${bindir} \
		${sysconfdir} \
		${libexecdir}"

FILES_${PN}-dev = "${incdir} \
		   ${libdir}/*.a \
		   ${libdir}/pkgconfig"

# The networkmanager package needs to be split into app/lib/dev packages. For
# now, silence insane.
INSANE_SKIP_${PN} = "1"
