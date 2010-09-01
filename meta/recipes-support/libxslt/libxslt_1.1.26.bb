DESCRIPTION = "GNOME XSLT library"
HOMEPAGE = "http://xmlsoft.org/XSLT/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=0cd9a07afbeb24026c9b03aecfeba458"

SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "libxml2 (>=2.6.27)"
PR = "r0"

SRC_URI = "ftp://xmlsoft.org/libxslt//libxslt-${PV}.tar.gz \
           file://pkgconfig_fix.patch;patch=1"
S = "${WORKDIR}/libxslt-${PV}"

inherit autotools pkgconfig

EXTRA_OECONF = "--without-python --without-debug --without-mem-debug --without-crypto"

PACKAGES = "${PN}-dbg ${PN}-dev ${PN}-utils ${PN} ${PN}-doc ${PN}-locale"

FILES_${PN}-dev += "${bindir}/xslt-config"
FILES_${PN}-utils += "${bindir}"

BBCLASSEXTEND = "native"
