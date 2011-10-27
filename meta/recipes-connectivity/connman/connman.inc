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

DEPENDS  = "libgdbus dbus glib-2.0 hal iptables"

INITSCRIPT_NAME = "connman"
INITSCRIPT_PARAMS = "start 05 5 2 3 . stop 22 0 1 6 ."

USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "--system --no-create-home \
                       --shell /bin/false --groups video,tty,audio \
                       --user-group xuser"

inherit autotools pkgconfig update-rc.d useradd

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

FILES_${PN}-dev += "${libdir}/connman/plugins/*.la"

python populate_packages_prepend() {
	prefix = (d.getVar("MLPREFIX", True) or "")
	depmap = dict( wifi=prefix+"wpa-supplicant", bluetooth=prefix+"bluez4", ofono=prefix+"ofono" )
	packages = []
	hook = lambda file,pkg,b,c,d:packages.append((file,pkg))

	plugin_dir = bb.data.expand('${libdir}/connman/plugins/', d)
	plugin_name = bb.data.expand('${PN}-plugin-%s', d)

	do_split_packages(d, plugin_dir, '^(.*).so$', plugin_name, '${PN} plugin for %s', extra_depends='', hook=hook )

	for (file, package) in packages:
		plugintype = package.split( '-' )[-1]
		if plugintype in depmap:
			bb.note( "Adding rdependency on %s to package %s" % ( depmap[plugintype], package ) )
			bb.data.setVar("RDEPENDS_%s" % package, depmap[plugintype], d)
}
