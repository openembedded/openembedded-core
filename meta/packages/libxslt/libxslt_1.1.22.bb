DESCRIPTION = "GNOME XSLT library"
SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "libxml2 (>=2.6.27)"
LICENSE = "MIT"
PR = "r2"

SRC_URI = "ftp://xmlsoft.org/libxml2/libxslt-${PV}.tar.gz \
           file://pkgconfig_fix.patch;patch=1"
S = "${WORKDIR}/libxslt-${PV}"

inherit autotools_stage pkgconfig

EXTRA_OECONF = "--without-python --without-debug --without-mem-debug --without-crypto"

PACKAGES = "${PN}-dbg ${PN}-dev ${PN}-utils ${PN} ${PN}-doc ${PN}-locale"

FILES_${PN}-dev += "${bindir}/xslt-config"
FILES_${PN}-utils += "${bindir}"
