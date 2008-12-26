HOMEPAGE = "http://www.moblin.org/projects/projects_connman.php"
SUMMARY  = "Moblin Connection Manager"
PV       = "0.0+git${SRCREV}"
PR       = "r4"
S        = "${WORKDIR}/git"
LICENSE  = "GPL"

DEPENDS  = "libgdbus dbus glib-2.0 hal"
RDEPENDS = "dhcp-client wpa-supplicant resolvconf"

EXTRA_OECONF += " \
    ac_cv_path_WPASUPPLICANT=/usr/sbin/wpa_supplicant \
    ac_cv_path_DHCLIENT=/sbin/dhclient "

SRC_URI  = "git://moblin.org/repos/projects/connman.git;protocol=http \
            file://connman "

INITSCRIPT_NAME = "connman"
INITSCRIPT_PARAMS = "defaults 22"

inherit autotools_stage pkgconfig update-rc.d

do_install_append() {
    install -m 0755 ${WORKDIR}/connman ${D}${sysconfdir}/init.d/connman
}

PACKAGES_DYNAMIC = "${PN}-plugin-*"
PACKAGES += "${PN}-script-dhclient"
FILES_${PN} = "${bindir}/* ${sbindir}/* ${libexecdir}/* ${libdir}/lib*.so.* \
            ${sysconfdir} ${sharedstatedir} ${localstatedir} \
            ${base_bindir}/* ${base_sbindir}/* ${base_libdir}/*.so* ${datadir}/${PN} \
            ${datadir}/pixmaps ${datadir}/applications \
            ${datadir}/idl ${datadir}/omf ${datadir}/sounds \
            ${libdir}/bonobo/servers \
            ${datadir}/dbus-1/system-services/*"
FILES_${PN}-script-dhclient += "${libdir}/connman/scripts/dhclient*"
FILES_${PN}-dbg += "${libdir}/connman/plugins/.debug \
                    ${libdir}/connman/scripts/.debug"

python populate_packages_prepend() {
	plugin_dir = bb.data.expand('${libdir}/connman/plugins/', d)
	plugin_name = bb.data.expand('${PN}-plugin-%s', d)
	do_split_packages(d, plugin_dir, '^lib(.*).so$', plugin_name, '${PN} plugin for %s', extra_depends='' )
}
