HOMEPAGE = "http://www.moblin.org/projects/projects_connman.php"
SUMMARY  = "Moblin Connection Manager"
PV       = "0.19+git${SRCREV}"
PR       = "r15"
S        = "${WORKDIR}/git"
LICENSE  = "GPL"

DEPENDS  = "libgdbus dbus glib-2.0 hal"
RDEPENDS_${PN} = "dhcp-client wpa-supplicant resolvconf"

EXTRA_OECONF += " \
    --enable-ethernet=builtin --enable-wifi=builtin --enable-dhclient=builtin \
    --enable-bluetooth=builtin --enable-udev --enable-loopback=builtin \
    --enable-dnsproxy=builtin --enable-threads --enable-resolvconf=builtin \
    --enable-client --enable-fake --with-dhclient=/sbin/dhclient \
    ac_cv_path_WPASUPPLICANT=/usr/sbin/wpa_supplicant"

SRC_URI  = "git://git.kernel.org/pub/scm/network/connman/connman.git;protocol=git \
            file://connman-install-tests.patch;patch=1 \
            file://udevfix.patch;patch=1 \
            file://dbusperms.patch;patch=1 \
            file://connman "

INITSCRIPT_NAME = "connman"
INITSCRIPT_PARAMS = "start 05 5 2 . stop 22 0 1 6 ."

inherit autotools_stage pkgconfig update-rc.d

do_install_append() {
    install -m 0755 ${WORKDIR}/connman ${D}${sysconfdir}/init.d/connman
}

PACKAGES_DYNAMIC = "${PN}-plugin-*"
FILES_${PN} = "${bindir}/* ${sbindir}/* ${libexecdir}/* ${libdir}/lib*.so.* \
            ${sysconfdir} ${sharedstatedir} ${localstatedir} \
            ${base_bindir}/* ${base_sbindir}/* ${base_libdir}/*.so* ${datadir}/${PN} \
            ${datadir}/pixmaps ${datadir}/applications \
            ${datadir}/idl ${datadir}/omf ${datadir}/sounds \
            ${libdir}/bonobo/servers \
            ${datadir}/dbus-1/system-services/* \
            ${libdir}/connman/scripts/dhclient*"
FILES_${PN}-dbg += "${libdir}/connman/plugins/.debug \
                    ${libdir}/connman/scripts/.debug"

python populate_packages_prepend() {
	plugin_dir = bb.data.expand('${libdir}/connman/plugins/', d)
	plugin_name = bb.data.expand('${PN}-plugin-%s', d)
	do_split_packages(d, plugin_dir, '^lib(.*).so$', plugin_name, '${PN} plugin for %s', extra_depends='' )
}
