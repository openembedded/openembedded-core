DESCRIPTION = "Password and keyring managing daemon"
HOMEPAGE = "http://www.gnome.org/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "GPLv2+ & LGPLv2+ & LGPLv2.1+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://gcr/gcr.h;endline=22;md5=a272df1e633e27ecf35e74fb5576250e \
                    file://egg/egg-dbus.h;endline=25;md5=eb6f531af37165dc53420c073d774e61 \
                    file://gp11/gp11.h;endline=24;md5=bd8c7a8a21d6c28d40536d96a35e3469 \
                    file://pkcs11/pkcs11i.h;endline=24;md5=e72cfbb718389b76a4dae838d1c1f439"

SECTION = "x11/gnome"

PR = "r1"

inherit autotools gnome pkgconfig

DEPENDS = "gtk+ libgcrypt libtasn1 libtasn1-native gconf"
RDEPENDS_${PN} = "libgnome-keyring"

EXTRA_OECONF = "--disable-gtk-doc"

SRC_URI += "file://org.gnome.keyring.service"

SRC_URI[archive.md5sum] = "9a8aa74e03361676f29d6e73155786fc"
SRC_URI[archive.sha256sum] = "31fecec1430a97f59a6159a5a2ea8d6a1b44287f1e9e595b3594df46bf7f18f9"

do_install_append () {
	install -d ${D}${datadir}/dbus-1/services
	install -m 0644 ${WORKDIR}/org.gnome.keyring.service ${D}${datadir}/dbus-1/services
}

FILES_${PN} += "${datadir}/dbus-1/services"
FILES_${PN}-dbg += "${libdir}/gnome-keyring/standalone/.debug/"
FILES_${PN}-dbg += "${libdir}/gnome-keyring/devel/.debug/"
