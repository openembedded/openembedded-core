SUMMARY = "D-Bus test package (for D-bus functionality testing only)"
HOMEPAGE = "http://dbus.freedesktop.org"
SECTION = "base"
LICENSE = "AFL-2 | GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=10dded3b58148f3f1fd804b26354af3e \
                    file://dbus/dbus.h;beginline=6;endline=20;md5=7755c9d7abccd5dbd25a6a974538bb3c"

DEPENDS = "python-pygobject dbus dbus-glib"

RDEPENDS_${PN} += "make"
RDEPENDS_${PN}-dev = ""

SRC_URI = "http://dbus.freedesktop.org/releases/dbus/dbus-${PV}.tar.gz \
           file://tmpdir.patch \
           file://ptest.patch \
           file://dbus-1.init  \
           file://run-ptest \
           file://python-config.patch \
           file://clear-guid_from_server-if-send_negotiate_unix_f.patch \
           "

SRC_URI[md5sum] = "b02e9c95027a416987b81f9893831061"
SRC_URI[sha256sum] = "7085a0895a9eb11a952394cdbea6d8b4358e17cb991fed0e8fb85e2b9e686dcd"

S="${WORKDIR}/dbus-${PV}"
FILESPATH = "${FILE_DIRNAME}/dbus"

inherit autotools pkgconfig gettext ptest

EXTRA_OECONF_X = "${@base_contains('DISTRO_FEATURES', 'x11', '--with-x', '--without-x', d)}"
EXTRA_OECONF_X_class-native = "--without-x"

EXTRA_OECONF = "--enable-tests \
                --enable-modular-tests \
                --enable-installed-tests \
                --enable-checks \
                --enable-asserts \
                --enable-verbose-mode \
                --disable-xml-docs \
                --disable-doxygen-docs \
                --disable-libaudit \
                --with-xml=expat \
                --disable-systemd \
                --without-systemdsystemunitdir \
                --with-dbus-test-dir=${PTEST_PATH} \
                ${EXTRA_OECONF_X}"

do_install() {
}

do_install_ptest() {
    find ${D}${PTEST_PATH} -name Makefile | xargs sed -i 's/^Makefile:/_Makefile:/'
}
