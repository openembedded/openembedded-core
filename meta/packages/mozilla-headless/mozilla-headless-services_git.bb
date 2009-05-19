SRC_URI = "git://git.moblin.org/mozilla-headless-services.git;protocol=git \
           file://confighack.patch;patch=1"
PV = "0.0+git${SRCREV}"
PR = "r1"

DEPENDS = "glib-2.0 dbus mozilla-headless"

EXTRA_OECONF = "--enable-gnome-proxy"

S = "${WORKDIR}/git"

FILES_${PN} += "${datadir}/dbus-1/services"

inherit autotools_stage
