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
                    file://src/main.c;beginline=1;endline=20;md5=4b55b550fa6b33cc2055ef30dd262b3e"

# we need to define the depends here, the dynamic stuff is too late
DEPENDS  = "dbus glib-2.0 ppp iptables gnutls \
            ${@base_contains('DISTRO_FEATURES', 'bluetooth','bluez4', '', d)} \
            ${@base_contains('DISTRO_FEATURES', 'wifi','wpa-supplicant', '', d)} \
            ofono \
            "

EXTRA_OECONF += "\
    ac_cv_path_WPASUPPLICANT=${sbindir}/wpa_supplicant \
    ac_cv_path_PPPD=${sbindir}/pppd \
    --disable-gtk-doc \
    --enable-debug \
    --enable-threads \
    --enable-loopback \
    --enable-ethernet \
    ${@base_contains('DISTRO_FEATURES', 'wifi','--enable-wifi', '--disable-wifi', d)} \
    ${@base_contains('DISTRO_FEATURES', 'bluetooth','--enable-bluetooth', '--disable-bluetooth', d)} \
    --enable-dnsproxy \
    --enable-ofono \
    --enable-tools \
    --enable-test \
    --disable-polkit \
    --enable-client \
    --enable-fake \
    --disable-ntpd \
"

INITSCRIPT_NAME = "connman"
INITSCRIPT_PARAMS = "start 05 5 2 3 . stop 22 0 1 6 ."

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "--system --no-create-home \
                       --shell /bin/false --groups video,tty,audio \
                       --user-group xuser"

inherit autotools pkgconfig update-rc.d useradd

do_compile_append() {
	sed -i -e s:deny:allow:g src/connman-dbus.conf
}

do_install_append() {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/connman ${D}${sysconfdir}/init.d/connman

	install -d ${D}${bindir}
	install -m 0755 ${S}/tools/*-test ${D}${bindir}
	install -m 0755 ${S}/tools/wispr ${D}${bindir}

	# We don't need to package an empty directory
	rmdir ${D}${libdir}/connman/scripts
}

PACKAGES_DYNAMIC = "${PN}-plugin-*"

python populate_packages_prepend() {
	depmap = dict( pppd="ppp",
	               wifi="wpa-supplicant",
	               bluetooth="bluez4",
	               ofono="ofono" )
	packages = []
	multilib_prefix = (d.getVar("MLPREFIX", True) or "")
	hook = lambda file,pkg,b,c,d:packages.append((file,pkg))
	plugin_dir = d.expand('${libdir}/connman/plugins/')
	plugin_name = d.expand('${PN}-plugin-%s')
	do_split_packages(d, plugin_dir, '^(.*).so$', plugin_name, '${PN} plugin for %s', extra_depends='', hook=hook )
	for (file, package) in packages:
		plugintype = package.split( '-' )[-1]
		if plugintype in depmap:
			rdepends = map(lambda x: multilib_prefix + x,  depmap[plugintype].split())
			bb.note( "Adding rdependency on %s to %s" % ( rdepends, package ) )
			d.setVar("RDEPENDS_%s" % package, " ".join(rdepends))
}

PACKAGES =+ "${PN}-tools ${PN}-tests"

FILES_${PN}-tools = "${bindir}/wispr"

FILES_${PN}-tests = "${bindir}/*-test ${libdir}/${BPN}/test/*"
RDEPENDS_${PN}-tests = "python-dbus"

FILES_${PN} = "${bindir}/* ${sbindir}/* ${libexecdir}/* ${libdir}/lib*.so.* \
            ${sysconfdir} ${sharedstatedir} ${localstatedir} \
            ${base_bindir}/* ${base_sbindir}/* ${base_libdir}/*.so* ${datadir}/${PN} \
            ${datadir}/pixmaps ${datadir}/applications \
            ${datadir}/idl ${datadir}/omf ${datadir}/sounds \
            ${libdir}/bonobo/servers \
            ${datadir}/dbus-1/system-services/*"

FILES_${PN}-dbg += "${libdir}/connman/*/.debug"

FILES_${PN}-dev += "${libdir}/connman/*/*.la"
