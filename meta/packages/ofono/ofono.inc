HOMEPAGE = "http://www.ofono.org"
SUMMARY  = "open source telephony"
LICENSE  = "GPL"

inherit autotools pkgconfig update-rc.d

DEPENDS  = "dbus glib-2.0"

INITSCRIPT_NAME = "ofono"
INITSCRIPT_PARAMS = "defaults 22"

do_install_append() {
  install -d ${D}${sysconfdir}/init.d/
  install -m 0755 ${WORKDIR}/ofono ${D}${sysconfdir}/init.d/ofono
}

