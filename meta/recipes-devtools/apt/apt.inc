SUMMARY = "Advanced front-end for dpkg"
LICENSE = "GPLv2.0+"
SECTION = "base"

SRC_URI = "http://snapshot.debian.org/archive/debian/20150428T221633Z/pool/main/a/${BPN}/${BPN}_${PV}.tar.xz \
           file://use-host.patch \
           file://makerace.patch \
           file://no-nls-dpkg.patch \
           file://fix-gcc-4.6-null-not-defined.patch \
           file://truncate-filename.patch \
           file://nodoc.patch \
           file://disable-configure-in-makefile.patch \
           file://disable-test.patch \
           "

inherit autotools gettext

EXTRA_AUTORECONF = "--exclude=autopoint,autoheader"

do_configure_prepend() {
    rm -rf ${S}/buildlib/config.sub
    rm -rf ${S}/buildlib/config.guess
}

FILES_${PN}-dbg += "${libdir}/apt/methods/.debug/"
