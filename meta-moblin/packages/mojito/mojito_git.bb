
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"
PV = "0.0+git${SRCREV}"
PR = "r4"

DEPENDS = "libsoup-2.4 gconf-dbus librest glib-2.0 twitter-glib gnome-keyring"

S = "${WORKDIR}/git"

inherit autotools_stage

FILES_${PN} += "${datadir}/dbus-1/services"
FILES_${PN}-dbg += "${libdir}/mojito/sources/.debug/* ${libdir}/mojito/services/.debug/"

PARALLEL_MAKE = ""

