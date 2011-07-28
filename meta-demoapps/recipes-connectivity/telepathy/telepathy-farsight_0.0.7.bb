SUMMARY = "Telepathy fasrsight"
DESCRIPTION = "Glue library for telepathy media signalling and the media \
streaming capabilities of Farsight2."
HOMEPAGE = "http://telepathy.freedesktop.org/wiki/"
DEPENDS = "glib-2.0 dbus telepathy-glib farsight2"
LICENSE = "LGPLv2"

SRC_URI = "http://telepathy.freedesktop.org/releases/telepathy-farsight/${BPN}-${PV}.tar.gz \
"

inherit autotools

EXTRA_OECONF = "--disable-python"

FILES_${PN} += "${datadir}/telepathy \
		${datadir}/dbus-1"
