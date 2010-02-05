DESCRIPTION = "Media deamon and play queue manager"
SRC_URI = "http://download.moblin.org/sources/bognor-regis/0.5/bognor-regis-0.5.0_20091116.tar.bz2"
LICENSE = "GPLv2"

DEPENDS = "glib-2.0 dbus-glib gtk+ libnotify gupnp gupnp-av bickley"

FILES_${PN} += "${datadir}/dbus-1/services"

inherit autotools_stage
