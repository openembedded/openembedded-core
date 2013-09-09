DESCRIPTION = "D-Bus test package, only used for D-bus functionality test."
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
           "

SRC_URI[md5sum] = "de4970c20629aeb958a12132415b3630"
SRC_URI[sha256sum] = "1f3d6f4a53a7121328f6cd685fc14440d72e6c0006ecb3312cc779c495915e86"

S="${WORKDIR}/dbus-${PV}"
FILESPATH = "${FILE_DIRNAME}/dbus-${PV}"

inherit autotools pkgconfig gettext ptest

EXTRA_OECONF_X = "${@base_contains('DISTRO_FEATURES', 'x11', '--with-x', '--without-x', d)}"
EXTRA_OECONF_X_virtclass-native = "--without-x"

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
                --with-systemdsystemunitdir=${systemd_unitdir}/system/ \
                --with-dbus-test-dir=${PTEST_PATH} \
                ${EXTRA_OECONF_X}"

do_install() {
}

do_install_ptest() {
    find ${D}${PTEST_PATH} -name Makefile | xargs sed -i 's/^Makefile:/_Makefile:/'
}
