DESCRIPTION = "A dbus service that listens to desktop notification requests and displays them"
HOMEPAGE = "http://www.galago-project.org/"
SECTION = "x11"
LICENSE = "GPL"
DEPENDS = "dbus gtk+ gconf"

SRC_URI = "http://www.galago-project.org/files/releases/source/${PN}/${P}.tar.gz \
        file://no-wnck-sexy.patch;patch=1"

EXTRA_OECONF = "--disable-binreloc"

inherit autotools pkgconfig

FILES_${PN} = "\
  ${libexecdir}/notification-daemon \
  ${datadir}/dbus-1/services/ \
  ${libdir}/notification-daemon-1.0/engines/*.so \
  ${sysconfdir}/gconf/schemas/notification-daemon.schemas \
"

FILES_${PN}-dbg += "${libexecdir}/.debug ${libdir}/notification-daemon-1.0/engines/.debug"
