DESCRIPTION = "GPS navigation/map display software"
LICENSE = "GPL"
DEPENDS = "sqlite3 gtk+ gnome-vfs dbus bluez-libs"
RDEPENDS = "bluez-utils"
PV = "1.2.4+svn${SRCDATE}"
PR = "r1"

# Only works with SRCDATE_maemo-mapper-nohildon = "20061114"
SRC_URI = "svn://garage.maemo.org/svn/maemo-mapper;proto=https;module=trunk \
           http://home.tal.org/%7Emilang/n770/maemo-mapper-desktop-20061114-001.patch;patch=1;pnum=0 \
	   file://fix_segfault.patch;patch=1"

S = "${WORKDIR}/trunk"

inherit autotools pkgconfig

do_install_append () {
	install -d ${D}${datadir}/applications/
	mv ${D}/maemo-mapper.desktop ${D}${datadir}/applications/
}

#FILES_${PN} += "${datadir}/icons"
