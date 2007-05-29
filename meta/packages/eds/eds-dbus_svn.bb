DESCRIPTION = "Evolution database backend server"
HOMEPAGE = "http://projects.o-hand.com/eds"
LICENSE = "LGPL"
DEPENDS = "intltool-native glib-2.0 gtk+ gconf dbus db gnome-common virtual/libiconv zlib"

PV = "1.4.0+svn${SRCDATE}"
PR = "r5"

SRC_URI = "svn://svn.o-hand.com/repos/${PN};module=trunk;proto=http \
           file://no_libdb.patch;patch=1 \
           file://no_iconv_test.patch;patch=1 \
           file://no_libedataserverui.patch;patch=1 \
           file://iconv-detect.h"

S = "${WORKDIR}/trunk"

inherit autotools pkgconfig

# -ldb needs this on some platforms
LDFLAGS += "-lpthread"

EXTRA_OECONF = "--without-openldap --with-dbus --without-bug-buddy --without-soup --with-libdb=${STAGING_DIR}/${HOST_SYS} --disable-smime --disable-nss --disable-nntp --disable-gtk-doc"

acpaths = " -I ${STAGING_DATADIR}/aclocal/gnome-macros "

PACKAGES =+ "libcamel libcamel-dev libebook libebook-dev libecal libecal-dev libedata-book libedata-book-dev libedata-cal libedata-cal-dev libedataserver libedataserver-dev"

FILES_${PN}-dev =+ "${libdir}/pkgconfig/evolution-data-server-*.pc"
FILES_${PN}-dbg =+ "${libdir}/evolution-data-server-*/camel-providers/.debug ${libdir}/evolution-data-server*/extensions/.debug/"

FILES_libcamel = "${libexecdir}/camel-* ${libdir}/libcamel-*.so.* ${libdir}/libcamel-provider-*.so.* ${libdir}/evolution-data-server-*/camel-providers/*.so ${libdir}/evolution-data-server-*/camel-providers/*.urls"
FILES_libcamel-dev = "${libdir}/libcamel-*.so ${libdir}/libcamel-provider-*.so ${libdir}/pkgconfig/camel*pc ${includedir}/evolution-data-server*/camel"

FILES_libebook = "${libdir}/libebook-*.so.*"
FILES_libebook-dev = "${libdir}/libebook-1.2.so ${libdir}/pkgconfig/libebook-*.pc ${includedir}/evolution-data-server*/libebook/*.h"
RRECOMMENDS_libebook = libedata-book

FILES_libecal = "${libdir}/libecal-*.so.* ${datadir}/evolution-data-server-1.4/zoneinfo"
FILES_libecal-dev = "${libdir}/libecal-*.so ${libdir}/pkgconfig/libecal-*.pc ${includedir}/evolution-data-server*/libecal/*.h ${includedir}/evolution-data-server*/libical/*.h"
RRECOMMENDS_libecal = libedata-cal

FILES_libedata-book = "${libexecdir}/e-addressbook-factory ${datadir}/dbus-1/services/*.AddressBook.service ${libdir}/libedata-book-*.so.* ${libdir}/evolution-data-server-*/extensions/libebook*.so"
FILES_libedata-book-dev = "${libdir}/libedata-book-*.so ${libdir}/pkgconfig/libedata-book-*.pc ${includedir}/evolution-data-server-*/libedata-book"

FILES_libedata-cal = "${libexecdir}/e-calendar-factory ${datadir}/dbus-1/services/*.Calendar.service ${libdir}/libedata-cal-*.so.* ${libdir}/evolution-data-server-*/extensions/libecal*.so"
FILES_libedata-cal-dev = "${libdir}/libedata-cal-*.so ${libdir}/pkgconfig/libedata-cal-*.pc ${includedir}/evolution-data-server-*/libedata-cal"

FILES_libedataserver = "${libdir}/libedataserver-*.so.*"
FILES_libedataserver-dev = "${libdir}/libedataserver-*.so ${libdir}/pkgconfig/libedataserver-*.pc ${includedir}/evolution-data-server-*/libedataserver/*.h"

do_configure_append = " cp ${WORKDIR}/iconv-detect.h ${S} "

do_stage () {
        autotools_stage_all
}
