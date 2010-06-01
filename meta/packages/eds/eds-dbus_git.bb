DESCRIPTION = "Evolution database backend server"
LICENSE = "LGPL"
DEPENDS = "intltool-native glib-2.0 gtk+ gconf dbus db gnome-common virtual/libiconv zlib libsoup-2.4 libglade libical gnome-keyring"

PV = "2.29+git${SRCPV}"
PR = "r5"

SRC_URI = "git://git.gnome.org/evolution-data-server;protocol=git \
           file://oh-contact.patch;patch=1;pnum=0 \
           file://nossl.patch;patch=1 \
           file://parallelmake.patch;patch=1 \
           file://iconv-detect.h"

S = "${WORKDIR}/git"

inherit autotools pkgconfig

# -ldb needs this on some platforms
LDFLAGS += "-lpthread"

# Parallel make shows many issues with this source code.
# Current problems seem to be duplicate execution of the calander/backends
# directories by make resulting in truncated/corrupt .la files
PARALLEL_MAKE = ""

do_configure_prepend () {
        echo "EXTRA_DIST = " > ${S}/gtk-doc.make
}

do_configure_append () {
        cp ${WORKDIR}/iconv-detect.h ${S}
}

EXTRA_OECONF = "--without-openldap --with-dbus --without-bug-buddy \
                --with-soup --with-libdb=${STAGING_DIR_HOST}${prefix} \
                --disable-smime --disable-ssl --disable-nntp --disable-gtk-doc --without-weather"

PACKAGES =+ "libcamel libcamel-dev libebook libebook-dev libecal libecal-dev \
             libedata-book libedata-book-dev libedata-cal libedata-cal-dev \
             libedataserver libedataserver-dev \
             libedataserverui libedataserverui-dev"

FILES_${PN}-dev =+ "${libdir}/pkgconfig/evolution-data-server-*.pc"
FILES_${PN}-dbg =+ "${libdir}/evolution-data-server-*/camel-providers/.debug \
                    ${libdir}/evolution-data-server*/extensions/.debug/"
RRECOMMENDS_${PN}-dev += "libecal-dev libebook-dev"

FILES_libcamel = "${libexecdir}/camel-* ${libdir}/libcamel-*.so.* \
                  ${libdir}/libcamel-provider-*.so.* \
                  ${libdir}/evolution-data-server-*/camel-providers/*.so \
                  ${libdir}/evolution-data-server-*/camel-providers/*.urls"
FILES_libcamel-dev = "${libdir}/libcamel-*.so ${libdir}/libcamel-provider-*.so \
                      ${libdir}/pkgconfig/camel*pc \
                      ${includedir}/evolution-data-server*/camel"

FILES_libebook = "${libdir}/libebook-*.so.*"
FILES_libebook-dev = "${libdir}/libebook-1.2.so \
                      ${libdir}/pkgconfig/libebook-*.pc \
                      ${includedir}/evolution-data-server*/libebook/*.h"
RRECOMMENDS_libebook = "libedata-book"

FILES_libecal = "${libdir}/libecal-*.so.* \
                 ${datadir}/evolution-data-server-1.4/zoneinfo"
FILES_libecal-dev = "${libdir}/libecal-*.so ${libdir}/pkgconfig/libecal-*.pc \
                     ${includedir}/evolution-data-server*/libecal/*.h \
                     ${includedir}/evolution-data-server*/libical/*.h"
RRECOMMENDS_libecal = "libedata-cal tzdata"

FILES_libedata-book = "${libexecdir}/e-addressbook-factory \
                       ${datadir}/dbus-1/services/*.AddressBook.service \
                       ${libdir}/libedata-book-*.so.* \
                       ${libdir}/evolution-data-server-*/extensions/libebook*.so"
FILES_libedata-book-dev = "${libdir}/libedata-book-*.so \
                           ${libdir}/pkgconfig/libedata-book-*.pc \
                           ${includedir}/evolution-data-server-*/libedata-book"

FILES_libedata-cal = "${libexecdir}/e-calendar-factory \
                      ${datadir}/dbus-1/services/*.Calendar.service \
                      ${libdir}/libedata-cal-*.so.* \
                      ${libdir}/evolution-data-server-*/extensions/libecal*.so"
FILES_libedata-cal-dev = "${libdir}/libedata-cal-*.so \
                          ${libdir}/pkgconfig/libedata-cal-*.pc \
                          ${includedir}/evolution-data-server-*/libedata-cal"

FILES_libedataserver = "${libdir}/libedataserver-*.so.*"
FILES_libedataserver-dev = "${libdir}/libedataserver-*.so \
                            ${libdir}/pkgconfig/libedataserver-*.pc \
                            ${includedir}/evolution-data-server-*/libedataserver/*.h"

FILES_libedataserverui = "${libdir}/libedataserverui-*.so.* ${datadir}/evolution-data-server-1.4/glade/*.glade"
FILES_libedataserverui-dev = "${libdir}/libedataserverui-*.so \
                              ${libdir}/pkgconfig/libedataserverui-*.pc \
                              ${includedir}/evolution-data-server-*/libedataserverui/*.h"

