DESCRIPTION = "A meta data manager API and framework (including Media)"
SRC_URI = "http://download.moblin.org/sources/bickley/0.4/bickley-0.4_20091124.tar.bz2"

DEPENDS = "gtk+ dbus-glib clutter-gst libexif taglib gupnp gupnp-av samba"
RDEPENDS = "xdg-user-dirs"

FILES_${PN} =+ "${datadir}/dbus-1/services/"

inherit autotools_stage
