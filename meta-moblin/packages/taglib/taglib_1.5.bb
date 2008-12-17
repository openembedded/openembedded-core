DESCRIPTION = "TagLib is a library for reading and editing the meta-data of several popular audio formats"
SECTION = "libs/multimedia"
HOMEPAGE = "http://developer.kde.org/~wheeler/taglib.html"
LICENSE = "LGPL"
PR = "r2"

SRC_URI = "http://developer.kde.org/~wheeler/files/src/taglib-${PV}.tar.gz \
           file://nolibtool.patch;patch=1 \
           file://fix_gcc4.3_ftbfs.diff.diff;patch=1 \
	   file://fix_includes_r583305.diff.diff;patch=1 \
           file://fix_includes_r583286.diff.diff;patch=1 \
           file://fix_vbr_length_r515068_r579077.diff.diff;patch=1 \
           file://reopen_readonly_r633092.diff.diff;patch=1"

S = "${WORKDIR}/taglib-${PV}"

inherit autotools pkgconfig binconfig

do_stage() {
	autotools_stage_all
}

PACKAGES =+ "${PN}-c"
FILES_${PN}-dbg += "${bindir}/taglib-config"
FILES_${PN}-c = "${libdir}/libtag_c.so.*"
