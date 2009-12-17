HOMEPAGE = "http://www.moblin.org/projects/projects_connman.php"
SUMMARY  = "Moblin Connection Manager"
LICENSE  = "GPL"

DEPENDS  = "libgdbus dbus glib-2.0 hal gettext"
RDEPENDS_${PN} = "dhcp-client wpa-supplicant resolvconf"

EXTRA_OECONF += " \
    --enable-ethernet=builtin --enable-wifi=builtin --enable-dhclient=builtin \
    --enable-bluetooth=builtin --enable-udev --enable-loopback=builtin \
    --enable-dnsproxy=builtin --enable-threads --enable-resolvconf=builtin \
    --enable-client --enable-fake --with-dhclient=/sbin/dhclient \
    --enable-test \
    ac_cv_path_WPASUPPLICANT=/usr/sbin/wpa_supplicant"

INITSCRIPT_NAME = "connman"
INITSCRIPT_PARAMS = "start 05 5 2 . stop 22 0 1 6 ."

inherit autotools_stage pkgconfig update-rc.d

do_install_append() {
    install -d ${D}${sysconfdir}/init.d/
    install -m 0755 ${WORKDIR}/connman ${D}${sysconfdir}/init.d/connman
}

PACKAGES_DYNAMIC = "${PN}-plugin-*"
FILES_${PN} = "${bindir}/* ${sbindir}/* ${libexecdir}/* ${libdir}/lib*.so.* \
            ${sysconfdir} ${sharedstatedir} ${localstatedir} \
            ${base_bindir}/* ${base_sbindir}/* ${base_libdir}/*.so* ${datadir}/${PN} \
            ${datadir}/pixmaps ${datadir}/applications \
            ${datadir}/idl ${datadir}/omf ${datadir}/sounds \
            ${libdir}/bonobo/servers \
            ${libdir}/${PN}/test/* \
            ${datadir}/dbus-1/system-services/* \
            ${libdir}/connman/scripts/dhclient*"
FILES_${PN}-dbg += "${libdir}/connman/plugins/.debug \
                    ${libdir}/connman/scripts/.debug"

python populate_packages_prepend() {
	plugin_dir = bb.data.expand('${libdir}/connman/plugins/', d)
	plugin_name = bb.data.expand('${PN}-plugin-%s', d)
	do_split_packages(d, plugin_dir, '^lib(.*).so$', plugin_name, '${PN} plugin for %s', extra_depends='' )
}