DESCRIPTION = "GNOME XSLT library"
SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "libxml2 (>=2.6.27)"
LICENSE = "MIT"
PR = "r1"

SRC_URI = "ftp://xmlsoft.org/libxml2/libxslt-${PV}.tar.gz \
           file://pkgconfig_fix.patch;patch=1"
S = "${WORKDIR}/libxslt-${PV}"

inherit autotools pkgconfig

EXTRA_OECONF = "--without-python --without-debug --without-mem-debug --without-crypto"

do_stage () {
	autotools_stage_all
}

PACKAGES = "${PN}-dbg ${PN}-dev ${PN}-utils ${PN} ${PN}-doc ${PN}-locale"

FILES_${PN}-dev += "${bindir}/xslt-config"
FILES_${PN}-utils += "${bindir}"
