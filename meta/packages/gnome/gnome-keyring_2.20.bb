LICENSE = "GPL"
SECTION = "x11/gnome"

PR = "r1"

inherit autotools gnome pkgconfig

DEPENDS = "gtk+ libgcrypt"

EXTRA_OECONF = "--disable-gtk-doc"

SRC_URI += "file://org.gnome.keyring.service"

do_install_append () {
	install -d ${D}${datadir}/dbus-1/services
	install -m 0644 ${WORKDIR}/org.gnome.keyring.service ${D}${datadir}/dbus-1/services
}

FILES_${PN} += "${datadir}/dbus-1/services"

do_stage() {
        autotools_stage_all
}
