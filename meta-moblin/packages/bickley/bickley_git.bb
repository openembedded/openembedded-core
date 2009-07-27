DESCRIPTION = "A meta data manager API and framework (including Media)"
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"
PV = "0.0.1+git${SRCPV}"
PR = "r2"

DEPENDS = "gtk+ dbus-glib clutter-gst libexif taglib gupnp gupnp-av samba"
RDEPENDS = "xdg-user-dirs"

S = "${WORKDIR}/git"

FILES_${PN} =+ "${datadir}/dbus-1/services/"

inherit autotools_stage
