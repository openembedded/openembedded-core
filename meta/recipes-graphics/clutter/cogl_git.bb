require cogl.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=4fbd65380cdd255951079008b364516c"

# the 1.8.2 tag
SRCREV = "e398e374e2ff0e88bc1d63577a192f8ca04a1cb5"
PV = "1.8.2+git${SRCPV}"
PR = "r1"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://git.gnome.org/cogl;protocol=git;branch=master \
	   file://build_for_armv4t.patch"
S = "${WORKDIR}/git"

AUTOTOOLS_AUXDIR = "${S}/build"

do_configure_prepend () {
	# Disable DOLT
	sed -i -e 's/^DOLT//' ${S}/configure.ac
}
