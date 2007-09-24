DEFAULT_PREFERENCE = "-1"

SECTION = "base"
HOMEPAGE = "http://www.freedesktop.org/Software/dbus"
DESCRIPTION = "message bus system for applications to talk to one another"
LICENSE = "GPL"
DEPENDS = "expat glib-2.0 virtual/libintl"

PR = "r3"

SRC_URI = "http://dbus.freedesktop.org/releases/dbus/dbus-${PV}.tar.gz \
	   file://tmpdir.patch;patch=1 \
	   file://dbus-1.init \
	   file://cross.patch;patch=1 \
	   file://fix-install-daemon.patch;patch=1"

inherit autotools pkgconfig update-rc.d gettext

INITSCRIPT_NAME = "dbus-1"
INITSCRIPT_PARAMS = "defaults"

CONFFILES_${PN} = "${sysconfdir}/dbus-1/system.conf ${sysconfdir}/dbus-1/session.conf"

FILES_${PN} = "${bindir}/dbus-daemon ${bindir}/dbus-launch ${bindir}/dbus-cleanup-sockets ${bindir}/dbus-send ${bindir}/dbus-monitor ${bindir}/dbus-uuidgen ${sysconfdir} ${datadir}/dbus-1/services ${libdir}/lib*.so.*"
FILES_${PN}-dev += "${libdir}/dbus-1.0/include"

pkg_postinst_dbus() {
#!/bin/sh

# can't do adduser stuff offline
if [ "x$D" != "x" ]; then
  exit 1
fi

MESSAGEUSER=messagebus
MESSAGEHOME=/var/run/dbus

mkdir -p $MESSAGEHOME || true
chgrp "$MESSAGEUSER" "$MESSAGEHOME" 2>/dev/null || addgroup "$MESSAGEUSER"
chown "$MESSAGEUSER"."$MESSAGEUSER" "$MESSAGEHOME" 2>/dev/null || adduser --system --home "$MESSAGEHOME" --no-create-home --disabled-password --ingroup "$MESSAGEUSER" "$MESSAGEUSER"
}

EXTRA_OECONF = " --disable-tests --disable-checks --disable-xml-docs \
                 --disable-doxygen-docs --with-xml=expat --without-x"

do_stage () {
	oe_libinstall -so -C dbus libdbus-1 ${STAGING_LIBDIR}

	autotools_stage_includes

	mkdir -p ${STAGING_LIBDIR}/dbus-1.0/include/dbus/
	install -m 0644 dbus/dbus-arch-deps.h ${STAGING_LIBDIR}/dbus-1.0/include/dbus/
}

do_install_append () {
	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/dbus-1.init ${D}${sysconfdir}/init.d/dbus-1
}

python populate_packages_prepend () {
	if (bb.data.getVar('DEBIAN_NAMES', d, 1)):
		bb.data.setVar('PKG_dbus', 'dbus-1', d)
}
