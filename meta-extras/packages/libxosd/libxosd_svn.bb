DESCRIPTION = "A library for displaying a TV-like on-screen display in X."
SECTION = "libs/x11"
DEPENDS = "virtual/libx11 libxext"
LICENSE = "LGPL"
PV = "2.2.15+svnr${SRCREV}"
PR = "r5"

SRC_URI = "svn://libxosd.svn.sourceforge.net/svnroot/libxosd/source;module=current;proto=https \
           file://autofoo.patch;patch=1 \
           file://use-sane-default-font.patch;patch=1"

S = "${WORKDIR}/current"

inherit autotools binconfig

do_stage() {
	autotools_stage_all
}

PACKAGES =+ "${PN}-examples-dbg ${PN}-examples"
FILES_${PN}-examples = "${bindir}/osd_cat"
FILES_${PN}-examples-dbg += "${bindir}/.debug/"
FILES_${PN}-dev += "${bindir}/xosd-config"
FILES_${PN} = "${libdir}/libxosd.so.*"
