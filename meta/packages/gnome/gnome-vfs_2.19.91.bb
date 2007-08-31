LICENSE = "GPL"
DEPENDS = "libxml2 gconf dbus bzip2 gnome-mime-data zlib"
RRECOMMENDS = "gnome-vfs-plugin-file shared-mime-info"
# Some legacy packages will require gnome-mime-data to be installed, but use of
# it is deprecated.
PR = "r1"

inherit gnome

# This is to provide compatibility with the gnome-vfs DBus fork
RPROVIDES = "gnome-vfs-plugin-dbus"

SRC_URI += "file://gconftool-lossage.patch;patch=1;pnum=1 \
	    file://gnome-vfs-no-kerberos.patch;patch=1;pnum=0"

EXTRA_OECONF = " \
                 --disable-hal \
		 --disable-openssl \
		 --disable-samba \
		 "

FILES_${PN} += " ${libdir}/vfs ${datadir}/dbus-1/services"
FILES_${PN}-dbg += " ${libdir}/gnome-vfs-2.0/modules/.debug"
FILES_${PN}-dev += " ${libdir}/gnome-vfs-2.0/include"
FILES_${PN}-doc += " ${datadir}/gtk-doc"

do_stage () {
autotools_stage_all
}

PACKAGES_DYNAMIC = "gnome-vfs-plugin-*"

python populate_packages_prepend () {
	print bb.data.getVar('FILES_gnome-vfs', d, 1)

	plugindir = bb.data.expand('${libdir}/gnome-vfs-2.0/modules/', d)
	do_split_packages(d, plugindir, '^lib(.*)\.so$', 'gnome-vfs-plugin-%s', 'GNOME VFS plugin for %s')
}
