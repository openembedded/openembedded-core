require cogl.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=7fbc338309ac38fefcd64b04bb903e34"

SRCREV = "0a70a159c65357809740971570b2e301451161b5"
PV = "1.8.0+git${SRCPV}"
PR = "r0"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://git.gnome.org/cogl;protocol=git;branch=master"
S = "${WORKDIR}/git"

AUTOTOOLS_AUXDIR = "${S}/build"

do_configure_prepend () {
	# Disable DOLT
	sed -i -e 's/^DOLT//' ${S}/configure.ac
}
