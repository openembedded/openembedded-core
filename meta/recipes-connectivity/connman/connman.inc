SUMMARY = "A daemon for managing internet connections within embedded devices"
DESCRIPTION = "The ConnMan project provides a daemon for managing \
internet connections within embedded devices running the Linux \
operating system.  The Connection Manager is designed to be slim and \
to use as few resources as possible, so it can be easily integrated. \
It is a fully modular system that can be extended, through plug-ins, \
to support all kinds of wired or wireless technologies. Also, \
configuration methods, like DHCP and domain name resolving, are \
implemented using plug-ins."
HOMEPAGE = "http://connman.net/"
BUGTRACKER = "http://bugs.meego.com/buglist.cgi?quicksearch=connman"
LICENSE  = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e \
                    file://src/main.c;beginline=1;endline=20;md5=d6a1ac98a6791c5294e8a7f176ecd66d"

# we need to define the depends here, the dynamic stuff is too late
DEPENDS  = "dbus glib-2.0 ppp iptables gnutls \
            ${@base_contains('DISTRO_FEATURES', 'bluetooth','bluez4', '', d)} \
            ${@base_contains('DISTRO_FEATURES', 'wifi','wpa-supplicant', '', d)} \
            ${@base_contains('DISTRO_FEATURES', '3g','ofono', '', d)} \
            "

INC_PR = "r19"

TIST = "--enable-tist"
TIST_powerpc = ""

EXTRA_OECONF += "\
    ac_cv_path_WPASUPPLICANT=${sbindir}/wpa_supplicant \
    ac_cv_path_PPPD=${sbindir}/pppd \
    --enable-debug \
    --enable-threads \
    --enable-loopback \
    --enable-ethernet \
    ${@base_contains('DISTRO_FEATURES', 'wifi','--enable-wifi ${TIST}', '--disable-wifi', d)} \
    ${@base_contains('DISTRO_FEATURES', 'bluetooth','--enable-bluetooth ${TIST}', '--disable-bluetooth', d)} \
    ${@base_contains('DISTRO_FEATURES', '3g', '--enable-ofono', '--disable-ofono', d)} \
    --enable-tools \
    --enable-test \
    --disable-polkit \
    --enable-client \
    --enable-fake \
    ${@base_contains('DISTRO_FEATURES', 'systemd', '--with-systemdunitdir=${systemd_unitdir}/system/', '--with-systemdunitdir=', d)} \
"

INITSCRIPT_NAME = "connman"
INITSCRIPT_PARAMS = "start 05 5 2 3 . stop 22 0 1 6 ."

SYSTEMD_SERVICE_${PN} = "connman.service"
SYSTEMD_WIRED_SETUP = "ExecStartPre=-${libdir}/connman/wired-setup"

# IMPORTANT: because xuser is shared with rootless X, please make sure the
# USERADD_PARAM is in sync with the one in xserver-nodm-init.bb
USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "--create-home \
                       --groups video,tty,audio \
                       --user-group xuser"

inherit autotools gtk-doc pkgconfig systemd update-rc.d useradd

do_configure_append () {
	sed -i "s#ExecStart=#${SYSTEMD_WIRED_SETUP}\nExecStart=#" ${S}/src/connman.service

}

# This allows *everyone* to access ConnMan over DBus, without any access
# control.  Really the at_console flag should work, which would mean that
# both this and the xuser patch can be dropped.
do_compile_append() {
	sed -i -e s:deny:allow:g src/connman-dbus.conf
}

do_install_append() {
	if ${@base_contains('DISTRO_FEATURES','sysvinit','true','false',d)}; then
		install -d ${D}${sysconfdir}/init.d
		install -m 0755 ${WORKDIR}/connman ${D}${sysconfdir}/init.d/connman
		sed -i s%@LIBDIR@%${libdir}% ${D}${sysconfdir}/init.d/connman
	fi

	install -d ${D}${bindir}
	install -m 0755 ${S}/tools/*-test ${D}${bindir}
	install -m 0755 ${S}/tools/wispr ${D}${bindir}
	install -m 0755 ${B}/client/connmanctl ${D}${bindir}

	# We don't need to package an empty directory
	rmdir ${D}${libdir}/connman/scripts

	# Automake 1.12 won't install empty directories, but we need the
	# plugins directory to be present for ownership
	mkdir -p ${D}${libdir}/connman/plugins
}

# These used to be plugins, but now they are core
RPROVIDES_${PN} = "\
	connman-plugin-loopback \
	connman-plugin-ethernet \
	${@base_contains('DISTRO_FEATURES', 'bluetooth','connman-plugin-bluetooth', '', d)} \
	${@base_contains('DISTRO_FEATURES', 'wifi','connman-plugin-wifi', '', d)} \
	${@base_contains('DISTRO_FEATURES', '3g','connman-plugin-ofono', '', d)} \
	"

RDEPENDS_${PN} = "\
	dbus \
	${@base_contains('DISTRO_FEATURES', 'bluetooth', 'bluez4', '', d)} \
	${@base_contains('DISTRO_FEATURES', 'wifi','wpa-supplicant', '', d)} \
	${@base_contains('DISTRO_FEATURES', '3g','ofono', '', d)} \
	"

PACKAGES_DYNAMIC += "^${PN}-plugin-.*"

python populate_packages_prepend() {
    depmap = dict(pppd="ppp")
    packages = []
    multilib_prefix = (d.getVar("MLPREFIX", True) or "")
    hook = lambda file,pkg,b,c,d:packages.append((file,pkg))
    plugin_dir = d.expand('${libdir}/connman/plugins/')
    plugin_name = d.expand('${PN}-plugin-%s')
    do_split_packages(d, plugin_dir, '^(.*).so$', plugin_name, '${PN} plugin for %s', extra_depends='', hook=hook, prepend=True )
    for (file, package) in packages:
        plugintype = package.split( '-' )[-1]
        if plugintype in depmap:
            rdepends = map(lambda x: multilib_prefix + x,  depmap[plugintype].split())
            bb.note( "Adding rdependency on %s to %s" % ( rdepends, package ) )
            d.setVar("RDEPENDS_%s" % package, " ".join(rdepends))
}

PACKAGES =+ "${PN}-tools ${PN}-tests ${PN}-client"

FILES_${PN}-tools = "${bindir}/wispr"

FILES_${PN}-tests = "${bindir}/*-test ${libdir}/${BPN}/test/*"
RDEPENDS_${PN}-tests = "python-dbus python-pygobject python-textutils python-subprocess python-fcntl python-netclient"

FILES_${PN}-client = "${bindir}/connmanctl"

FILES_${PN} = "${bindir}/* ${sbindir}/* ${libexecdir}/* ${libdir}/lib*.so.* \
            ${libdir}/connman/plugins \
            ${sysconfdir} ${sharedstatedir} ${localstatedir} \
            ${base_bindir}/* ${base_sbindir}/* ${base_libdir}/*.so* ${datadir}/${PN} \
            ${datadir}/dbus-1/system-services/*"

FILES_${PN}-dbg += "${libdir}/connman/*/.debug"

FILES_${PN}-dev += "${libdir}/connman/*/*.la"
