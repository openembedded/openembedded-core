SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"
PV = "0.1+git${SRCPV}"
PR = "r5"

DEPENDS = "libsoup-2.4 gconf-dbus librest glib-2.0 twitter-glib gnome-keyring"
RDEPENDS_${PN} = "connman"

S = "${WORKDIR}/git"

inherit autotools_stage

FILES_${PN} += "${datadir}/dbus-1/services"
FILES_${PN}-dbg += "${libdir}/mojito/sources/.debug/* ${libdir}/mojito/services/.debug/"

PARALLEL_MAKE = ""

EXTRA_OECONF = "--with-online=connman --disable-shave"
