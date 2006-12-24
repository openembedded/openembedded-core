LICENSE = "GPL"
PR = "r1"

inherit gnome

DEPENDS = "libxml2 gconf dbus bzip2 gnome-mime-data zlib"
RRECOMMENDS = "gnome-vfs-plugin-file gnome-mime-data shared-mime-info"

# This is to provide compatibility with the gnome-vfs DBus fork
PROVIDES = "gnome-vfs-plugin-dbus"

SRC_URI += "file://gconftool-lossage.patch;patch=1;pnum=1"

EXTRA_OECONF = "--disable-openssl"
#export KRB5_CONFIG="none"

FILES_${PN} += " ${libdir}/vfs"
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
