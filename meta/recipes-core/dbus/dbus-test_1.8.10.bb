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
           file://dbus-1.init  \
           file://run-ptest \
           file://python-config.patch \
           file://clear-guid_from_server-if-send_negotiate_unix_f.patch \
           "

SRC_URI[md5sum] = "d6f709bbec0a022a1847c7caec9d6068"
SRC_URI[sha256sum] = "5689f7411165adc953f37974e276a3028db94447c76e8dd92efe910c6d3bae08"

S="${WORKDIR}/dbus-${PV}"
FILESEXTRAPATHS =. "${FILE_DIRNAME}/dbus:"

inherit autotools pkgconfig gettext ptest

EXTRA_OECONF_X = "${@bb.utils.contains('DISTRO_FEATURES', 'x11', '--with-x', '--without-x', d)}"
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
                --disable-systemd \
                --without-systemdsystemunitdir \
                --with-dbus-test-dir=${PTEST_PATH} \
                ${EXTRA_OECONF_X}"

do_install() {
}

do_install_ptest() {
	install -d ${D}${PTEST_PATH}/test
	case1="shell printf refs syslog"
	for i in ${case1}; do install ${B}/test/test-$i ${D}${PTEST_PATH}/test; done
	case2="marshal syntax corrupt dbus-daemon dbus-daemon-eavesdrop loopback relay"
	for i in ${case2}; do install ${B}/test/.libs/test-$i ${D}${PTEST_PATH}/test; done
	case3="bus bus-system bus-launch-helper"
	for i in ${case3}; do install ${B}/bus/test-$i ${D}${PTEST_PATH}/test; done
	install ${B}/dbus/test-dbus ${D}${PTEST_PATH}/test
	cp -r ${B}/test/data ${D}${PTEST_PATH}/test
}
RDEPENDS_${PN}-ptest += "bash"
