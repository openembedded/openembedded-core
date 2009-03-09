DESCRIPTION = "Message bus system for applications to talk to one another"
HOMEPAGE = "http://dbus.freedesktop.org"
SECTION = "base"
LICENSE = "GPL"
DEPENDS = "expat glib-2.0 virtual/libintl virtual/libx11 libsm"

SRC_URI = "\
  http://dbus.freedesktop.org/releases/dbus/dbus-${PV}.tar.gz \
  file://cross.patch;patch=1 \
  file://tmpdir.patch;patch=1 \
  file://dbus-1.init \
  file://99_dbus \
"

inherit autotools pkgconfig gettext

INITSCRIPT_NAME = "dbus-1"
INITSCRIPT_PARAMS = "start 02 5 2 . stop 20 0 1 6 ."

CONFFILES_${PN} = "${sysconfdir}/dbus-1/system.conf ${sysconfdir}/dbus-1/session.conf"

DEBIANNAME_${PN} = "dbus-1"

PACKAGES =+ "${PN}-lib"

FILES_${PN} = "\
${bindir}/dbus-daemon* \
${bindir}/dbus-uuidgen \
${bindir}/dbus-launch \
${bindir}/dbus-cleanup-sockets \
${bindir}/dbus-send \
${bindir}/dbus-monitor \
${libexecdir}/dbus* \
${sysconfdir} \
${datadir}/dbus-1/services \
${datadir}/dbus-1/system-services \
"
FILES_${PN}-lib = "${libdir}/lib*.so.*"
RRECOMMENDS_${PN}-lib = "${PN}"
FILES_${PN}-dev += "${libdir}/dbus-1.0/include ${bindir}/dbus-glib-tool ${libdir}/"

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

grep -q netdev: /etc/group || addgroup netdev
chmod u+s /usr/libexec/dbus-daemon-launch-helper
}

EXTRA_OECONF = "\
  --disable-qt \
  --disable-qt3 \
  --disable-gtk \
  --disable-tests \
  --disable-checks \
  --disable-xml-docs \
  --disable-doxygen-docs \
  --with-xml=expat \
  --with-x \
"

do_stage() {
	oe_libinstall -so -C dbus libdbus-1 ${STAGING_LIBDIR}

	autotools_stage_includes

	mkdir -p ${STAGING_LIBDIR}/dbus-1.0/include/dbus/
	install -m 0644 dbus/dbus-arch-deps.h ${STAGING_LIBDIR}/dbus-1.0/include/dbus/
}

do_install() {
	autotools_do_install
	install -d ${D}/etc/default/volatiles
	install -m 0644 ${WORKDIR}/99_dbus ${D}/etc/default/volatiles/

	install -d ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/dbus-1.init ${D}${sysconfdir}/init.d/dbus-1
	# the stock install seems to install the libtool wrapper script, so we have to copy this manually :M:
	if [ -e bus/.libs/dbus-daemon-launch-helper ]; then
		install -m 0755 bus/.libs/dbus-daemon-launch-helper ${D}${libexecdir}/
	fi
}
