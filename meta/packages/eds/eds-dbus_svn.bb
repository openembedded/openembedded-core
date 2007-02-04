DESCRIPTION = "Evolution database backend server"
HOMEPAGE = "http://projects.o-hand.com/eds"
LICENSE = "LGPL"
DEPENDS = "intltool-native glib-2.0 gtk+ gconf dbus db gnome-common libglade virtual/libiconv zlib intltool"

PV = "1.4.0+svn${SRCDATE}"
PR = "r1"

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

PACKAGES =+ "libcamel-provider libcamel libebook libecal libedata-book libedata-cal libedataserver"

FILES_libcamel-provider = "${libdir}/libcamel-provider-1.2.so.*"
FILES_libcamel = "${libdir}/libcamel-1.2.so.*"
FILES_libebook = "${libdir}/libebook-1.2*"
FILES_libecal = "${libdir}/libecal-1.2.so.*"
FILES_libedata-book = "${libdir}/libedata-book-1.2.so.*"
FILES_libedata-cal = "${libdir}/libedata-cal-1.2.so.*"
FILES_libedataserver = "${libdir}/libedataserver-1.2.so.*"

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
        autotools_stage_all
}

