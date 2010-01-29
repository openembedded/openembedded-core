DESCRIPTION = "GNOME frontend for packagekit"
LICENSE = "GPL"

DEPENDS = "packagekit libsexy gconf-dbus polkit polkit-gnome libunique gnome-menus devicekit-power"

inherit gnome

SRC_URI = "http://www.packagekit.org/releases/gnome-packagekit-${PV}.tar.gz"

EXTRA_OECONF = " --enable-compile-warnings=no  --disable-scrollkeeper "

do_configure_prepend() {
	sed -i -e s/help/docs/ Makefile.am 
	sed -i -e s:-Werror::g configure.ac
}

FILES_${PN} += "${datadir}/icons ${datadir}/gnome"
