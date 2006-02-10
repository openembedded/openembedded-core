LICENSE = "LGPL"
DEPENDS = "glib-2.0 gtk+ gconf dbus db gnome-common libglade libiconv"
RDEPENDS = "gconf dbus-1 db libiconv"
MAINTAINER = "Chris Lord <chris@openedhand.com>"
DESCRIPTION = "Evolution database backend server"
PV = "1.4.0+svn${SRCDATE}"
PR = "r15"

SRC_URI = "svn://svn.o-hand.com/repos/${PN};module=trunk;proto=http \
           file://no_libdb.patch;patch=1 \
           file://no_iconv_test.patch;patch=1 \
           file://no_libedataserverui.patch;patch=1 \
           file://disable_orbit.patch;patch=1 \
           file://iconv-detect.h"

S = "${WORKDIR}/trunk"

inherit autotools pkgconfig

EXTRA_OECONF = "--without-openldap --with-dbus --without-bug-buddy --without-soup --with-libdb=${STAGING_DIR}/${HOST_SYS} --disable-smime --disable-nss --disable-camel --disable-nntp --disable-gtk-doc"

acpaths = " -I ${STAGING_DATADIR}/aclocal/gnome-macros "

FILES_${PN} += "${libdir}/evolution-data-server-1.2/extensions/*.so \
                ${libdir}/evolution-data-server-1.2/camel-providers/*.so \
                ${libdir}/evolution-data-server-1.2/camel-providers/*.urls \
                ${datadir}/evolution-data-server-1.4/zoneinfo/zones.tab \
                ${datadir}/evolution-data-server-1.4/zoneinfo/*/*.ics \
                ${datadir}/evolution-data-server-1.4/zoneinfo/*/*/*.ics \
                ${datadir}/dbus-1/services/*.service"
FILES_${PN}-dev += "${libdir}/evolution-data-server-1.2/extensions/*.la \
                    ${libdir}/evolution-data-server-1.2/camel-providers/*.la"


do_configure_append = " cp ${WORKDIR}/iconv-detect.h ${S} "

do_stage () {
	oe_libinstall -so -C addressbook/libebook-dbus libebook-1.2 ${STAGING_LIBDIR}
	oe_libinstall -so -C addressbook/libedata-book-dbus libedata-book-1.2 ${STAGING_LIBDIR}
	oe_libinstall -so -C calendar/libecal-dbus libecal-1.2 ${STAGING_LIBDIR}
	oe_libinstall -so -C calendar/libedata-cal-dbus libedata-cal-1.2 ${STAGING_LIBDIR}
	oe_libinstall -so -C libedataserver libedataserver-1.2 ${STAGING_LIBDIR}

	install -d ${STAGING_INCDIR}/camel ${STAGING_INCDIR}/libebook \
		${STAGING_INCDIR}/libecal ${STAGING_INCDIR}/libedataserver \
		${STAGING_INCDIR}/libical
	install -m 0644 ${S}/camel/*.h ${STAGING_INCDIR}/camel
	install -m 0644 ${S}/addressbook/libebook-dbus/*.h ${STAGING_INCDIR}/libebook
	install -m 0644 ${S}/calendar/libecal-dbus/*.h ${STAGING_INCDIR}/libecal
	install -m 0644 ${S}/libedataserver/*.h ${STAGING_INCDIR}/libedataserver
	install -m 0644 ${S}/calendar/libical/src/libical/*.h ${STAGING_INCDIR}/libical
}

