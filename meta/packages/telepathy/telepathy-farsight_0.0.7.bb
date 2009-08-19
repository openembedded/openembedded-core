DESCRIPTION = "Telepathy fasrsight"
HOMEPAGE = "http://telepathy.freedesktop.org/wiki/"
DEPENDS = "glib-2.0 dbus telepathy-glib farsight2"
LICENSE = "LGPLv2"

SRC_URI = "http://telepathy.freedesktop.org/releases/telepathy-farsight/${P}.tar.gz \
"

inherit autotools_stage

EXTRA_OECONF = "--disable-python"

AUTOTOOLS_STAGE_PKGCONFIG = "1"

FILES_${PN} += "${datadir}/telepathy \
		${datadir}/dbus-1"
