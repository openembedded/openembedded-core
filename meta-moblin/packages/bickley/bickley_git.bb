
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"
PV = "0.0+git${SRCREV}"
PR = "r1"

DEPENDS = "redland gtk+ dbus-glib clutter-gst-0.8 libexif taglib gupnp gupnp-av"

S = "${WORKDIR}/git"

inherit autotools_stage
